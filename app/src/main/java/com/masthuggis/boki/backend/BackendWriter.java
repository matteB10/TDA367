package com.masthuggis.boki.backend;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.masthuggis.boki.model.callbacks.FailureCallback;
import com.masthuggis.boki.model.callbacks.SuccessCallback;
import com.masthuggis.boki.model.callbacks.stringCallback;
import com.masthuggis.boki.model.observers.ChatObserver;
import com.masthuggis.boki.utils.UniqueIdCreator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnull;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * BackendWriter is a Java class which communicates with the backend database through writing only.
 * The database used currently is Googles Firebase Firestore.
 * Used by BackendFactory and BackendDataHandler.
 * Written by masthuggis
 */
class BackendWriter {
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference mainRef = storage.getReference();
    private StorageReference imagesRef = mainRef.child("images");
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private final List<ChatObserver> chatObservers;
    private CollectionReference advertPath;
    private CollectionReference chatPath;
    private CollectionReference userPath;

    /**
     * The single constructor of the class which needs a list of chatObservers and messagesObservers to make sure
     * it updates the current observers correctly.
     */

    BackendWriter(List<ChatObserver> chatObservers) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        advertPath = db.collection("market");
        chatPath = db.collection("chats");
        userPath = db.collection("users");
        this.chatObservers = chatObservers;
    }

    private void notifyChatObservers() {
        for (ChatObserver chatObserver : chatObservers) {
            chatObserver.onChatUpdated();
        }
    }

    /**
     * Tries to create a new user depending on the parameters entered by the user using Firebases Authorization function. Depending on the success or failure of the database call sends
     * back a success -or -failurecallback. If successful user data is stored in Firebase Firestore.
     */
    void userSignUpAndSignIn(String email, String password, String username, SuccessCallback successCallback, FailureCallback failureCallback) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", email);
        userMap.put("username", username);
        try {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> {
                                String uniqueUserID = auth.getCurrentUser().getUid();
                                userMap.put("userID", uniqueUserID);
                                userPath.document(uniqueUserID).set(userMap);
                                successCallback.onSuccess();
                            });

                        } else {
                            failureCallback.onFailure(task.getException().getMessage());
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Signs in user through the authorization function offered by Firebase. Depending on its success sends a callback to tell the app to act accordingly.
     */
    void userSignIn(String email, String password, SuccessCallback successCallback, FailureCallback failureCallback) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                successCallback.onSuccess();
            } else {
                failureCallback.onFailure(task.getException().getMessage());
            }
        });
    }

    /**
     * Signs out from the application through the authorization function offered by Firebase.
     */
    void signOut() {
        auth.signOut();
    }

    /**
     * Writes an advertisement to Firebase Firestore.
     *
     * @param imageFile Needs a file which is an image to upload to Firebase Storage.
     * @param data      Uploads a datamap to Firebase Firestore which contains all needed information to
     *                  create a new advertisement when the data is later fetched.
     */
    void writeAdvertToFirebase(File imageFile, Map<String, Object> data) {
        uploadImageToFirebase(imageFile, (String) data.get("uniqueAdID"), () -> writeToDatabase(data));

    }


    /**
     * Private method which writes a datamap to Firebase Firestore containing all information needed to
     * create an advertisement when fetched.
     *
     * @param data
     */
    private void writeToDatabase(Map<String, Object> data) {
        advertPath.document((String) data.get("uniqueAdID"))
                .set(data).addOnSuccessListener(aVoid -> {
            Log.d(TAG, "DocumentSnapshot successfully written!");
        })
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
    }

    /**
     * Updates the information of an advertisement stored in Firebase Firestore and Storage. Takes the uniqueAdvertID from the map supplied
     * by the method call to find the pathway to where the data of this specific advertisement is stored.
     *
     * @param imageFile Updates the image stored in Firebase Storage.
     * @param dataMap   Updates the datamap needed to create an advertisement when the data is later fetched.
     */

    void updateAdToFirebase(File imageFile, Map<String, Object> dataMap) {
        String adID = (String) dataMap.get("uniqueAdID");
        advertPath.whereEqualTo("uniqueAdID", adID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            advertPath.document(documentSnapshot.getId())
                                    .update("title", dataMap.get("title"));
                            advertPath.document(documentSnapshot.getId())
                                    .update("price", dataMap.get("price"));
                            advertPath.document(documentSnapshot.getId())
                                    .update("description", dataMap.get("description"));
                            advertPath.document(documentSnapshot.getId())
                                    .update("condition", dataMap.get("condition"));
                            advertPath.document(documentSnapshot.getId())
                                    .update("tags", dataMap.get("tags"));
                        }
                        if (imageFile != null) {
                            uploadImageToFirebase(imageFile, dataMap.get("uniqueAdID").toString(), () -> {

                            });
                        }
                    } else {
                        //TODO FAILURE CALLBACK
                    }
                });
    }

    /**
     * Helper method which uploads given image to Firebase Storage.
     *
     * @param imageFile
     * @param uniqueAdID
     * @param successCallback
     */

    //TODO ARVID KOMMENTARER HÄR PLS.
    private void uploadImageToFirebase(File imageFile, String uniqueAdID, SuccessCallback successCallback) {
        try {
            InputStream uploadStream = new FileInputStream(imageFile);
            UploadTask uploadTask = imagesRef.child(uniqueAdID).putStream(uploadStream);
            //Handle errors here
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                successCallback.onSuccess();

            }).addOnFailureListener(Throwable::printStackTrace).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@Nonnull Exception e) {

                }
            });
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Removes an advertisement from the user storage in Firebase Firestore through using the userID and advertID
     */
    void removeAdFromFavourites(String adID, String userID) {
        userPath.document(userID).get().addOnCompleteListener(task -> {
            List<String> favourites = (List<String>) task.getResult().getData().get("favourites");
            favourites.remove(adID);
            userPath.document(userID).update("favourites", favourites);
        });
    }

    /**
     * Adds an advertisement to the user storage in Firebase Firestore through using the userID and advertID.
     * First tries to update an already existing list of adverts in the users favourites directory. Throws a NPEx
     * if it doesn't already exists. Catch block then creates a new list and then adds the initial ad as a favourite.
     */
    void addAdToFavourites(String adID, String userID) {
        userPath.document(userID).get().addOnCompleteListener(task -> {
            try {
                List<String> favourites = (List<String>) task.getResult().getData().get("favourites");
                favourites.add(adID);
                userPath.document(userID).update("favourites", favourites);
            } catch (NullPointerException e) {
                List<String> favourites = new ArrayList<>();
                favourites.add(adID);
                userPath.document(userID).update("favourites", favourites);
            }
        });
    }

    /**
     * Deletes advertisement from the users favourite directory in Firebase Firestore using the current userID
     * and id of the advertisement.
     */
    void deleteIDFromFavourites(String userID, String favouriteID) {
        userPath.document(userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                deleteID(favouriteID, task);
            } else {
                if (task.getException() != null) {
                    task.getException().printStackTrace();
                }
            }
        });
    }

    /**
     * Helper method for removing favourite from users favourite directory in Firebase Firestore.
     *
     * @param favouriteID
     * @param task
     */
    private void deleteID(String favouriteID, Task<DocumentSnapshot> task) {
        DocumentSnapshot userData = task.getResult();
        String userID = (String) userData.get("userID");
        List<String> favourites = (List<String>) userData.get("favourites");
        favourites.remove(favouriteID);
        userPath.document(userID).update("favourites", favourites);
    }


    /**
     * Creates a new chat between two users by writing a chatID to each users myConversations directory in Firebase
     * Firestore. Then creates a chat in the chats directory in Firestore which contains the information needed
     * to display current sender/receiver and communicating between users through knowing the user id.
     *
     * @param adOwnerID      ID of the owner of advertisement.
     * @param otherUserID    ID of the current user.
     * @param advertID       ID of the advertisement which is the topic of the chat.
     * @param imageURL       URL of the image belonging to the discussed advertisement.
     * @param stringCallback Returns a string as a callback, which in this case is the unique ID of the chat.
     */

    void createNewChat(String adOwnerID, String otherUserID, String advertID,String adOwnerName,String otherUsername, String imageURL, stringCallback stringCallback) {
        String uniqueChatID = UniqueIdCreator.getUniqueID();
        Map<String, Object> chatMap = prepareChatMap(uniqueChatID);
        Map<String, Object> messagesMap = prepareMessagesMap(adOwnerID,otherUserID,adOwnerName,otherUsername, advertID, imageURL);
        writeChatAndMessageData(adOwnerID, otherUserID, stringCallback, uniqueChatID, chatMap, messagesMap);
    }

    private void writeChatAndMessageData(String adOwnerID, String otherUserID, stringCallback stringCallback, String uniqueChatID, Map<String, Object> chatMap, Map<String, Object> messagesMap) {
        DocumentReference dfSender = userPath.document(otherUserID).collection("myConversations").document(uniqueChatID);
        DocumentReference dfReceiver = userPath.document(adOwnerID).collection("myConversations").document(uniqueChatID);
        DocumentReference dfUniqueChat = chatPath.document(uniqueChatID);
        dfUniqueChat.set(messagesMap).addOnSuccessListener(aVoid -> {
            dfSender.set(chatMap).addOnSuccessListener(bVoid -> {
                dfReceiver.set(chatMap).addOnSuccessListener(cVoid -> {
                    stringCallback.onCallback(uniqueChatID);
                });
            });
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@Nonnull Exception e) {

            }
        });
    }

    /**
     * Prepares a map which is then written to Firebase Firestore containing the information needed to later
     * create a chat when the information is fetched.
     *
     * @returns said map.
     */
    private Map<String, Object> prepareChatMap(String uniqueChatID) {
        Map<String, Object> chatMap = new HashMap<>();
        chatMap.put("uniqueChatID", uniqueChatID);
        chatMap.put("isActive", true);
        return chatMap;
    }

    /**
     * Prepares a map which is written to Firebase Firestore containing information needed to later
     * create messages between two users contained in a chat. The information is later used to create
     * messages when fetched.
     *
     * @param adOwnerID
     * @param otherUserID
     * @param advertID
     * @param imageURL
     * @return
     */
    private Map<String, Object> prepareMessagesMap(String adOwnerID, String otherUserID,String userOneUsername,String userTwoUsername, String advertID, String imageURL) {
        Map<String, Object> messagesMap = new HashMap<>();
        messagesMap.put("userOneID", otherUserID);
        messagesMap.put("userTwoID", adOwnerID);
        messagesMap.put("advertID", advertID);
        messagesMap.put("imageURL", imageURL);
        messagesMap.put("userOneUsername",userOneUsername);
        messagesMap.put("userTwoUsername",userTwoUsername);
        return messagesMap;
    }

    /**
     * Writes a new message to the folder a specific chat(supplied by the parameter uniqueChatID)
     * containing needed information to later create and display said message when the information is
     * fetched.
     *
     * @param uniqueChatID Used to find the correct pathway to said chat.
     * @param messageMap   Map containing necessary information.
     */
    void writeMessage(String uniqueChatID, Map<String, Object> messageMap) {
        chatPath.document(uniqueChatID).collection("messages").document().set(messageMap).addOnSuccessListener(aVoid -> {

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@Nonnull Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Deleting an ad with the specific adID from the database. At the same time deletes all chats
     * considering said advertisement for all users.
     */
    void deleteAd(List<Map<String, String>> chatReceiverAndUserIDMap, Map<String, String> adIDAndUserID) {
        String adID = adIDAndUserID.get("adID");
        String userID = adIDAndUserID.get("userID");
        if (adID != null) {
            advertPath.document((adID)).delete();
            makeReceiverChatInactive(chatReceiverAndUserIDMap, userID);
        }
    }

    /**
     * Sets the chats of all receivers to inactive so that the receivers are notified
     * of the removal of the advertisement.
     */

    private void makeReceiverChatInactive(List<Map<String, String>> mapList, String userID) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("isActive", false);
        for (Map<String, String> map : mapList) {
            userPath.document(Objects.requireNonNull(map.get("receiverID"))).collection("myConversations").document(Objects.requireNonNull(map.get("chatID"))).update(updates);
            userPath.document(userID).collection("myConversations").document(Objects.requireNonNull(map.get("chatID"))).delete();

        }
        notifyChatObservers();
    }

    /**
     * Deletes the reference to the chat from the users myConversations directory
     *
     * @param userID
     * @param chatID
     */
    void removeChat(String userID, String chatID) {
        userPath.document(userID).collection("myConversations").document(chatID).delete();

    }


}
