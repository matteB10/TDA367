package com.masthuggis.boki.backend;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.masthuggis.boki.backend.callbacks.FailureCallback;
import com.masthuggis.boki.backend.callbacks.SuccessCallback;
import com.masthuggis.boki.backend.callbacks.stringCallback;
import com.masthuggis.boki.model.observers.BackendObserver;
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

import static androidx.constraintlayout.widget.Constraints.TAG;


class BackendWriter {
    private FirebaseStorage storage = FirebaseStorage.getInstance(); //Reference to Cloud Storage that holds images
    private StorageReference mainRef = storage.getReference();
    private StorageReference imagesRef = mainRef.child("images"); //Reference to storage location of images
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private final List<BackendObserver> backendObservers;
    private CollectionReference advertPath;
    private CollectionReference chatPath;
    private CollectionReference userPath;


    private boolean isWritingImageToDatabase = false;
    private boolean isWritingAdvertToDatabase = false;


    BackendWriter(List<BackendObserver> backendObservers) {
        //Reference to database that holds users and adverts
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        advertPath = db.collection("market");
        chatPath = db.collection("chats");
        userPath = db.collection("users");
        this.backendObservers = backendObservers;
    }

    private void notifyChatObservers() {
        for (BackendObserver backendObserver : backendObservers) {
            backendObserver.onChatsChanged();
        }
    }

    private void notifyMessagesObserver() {
        for (BackendObserver backendObserver : backendObservers) {
            backendObserver.onMessagesChanged();
        }
    }












    void setUsername(String username, SuccessCallback successCallback) {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
            user.updateProfile(profileUpdates).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Username set.");
                    successCallback.onSuccess();
                } else {
                    //TODO FAILURECALLBACK
                }
            });
        }
    }




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

    void userSignIn(String email, String password, SuccessCallback successCallback, FailureCallback failureCallback) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                successCallback.onSuccess();
            } else {
                failureCallback.onFailure(task.getException().getMessage());
            }
        });
    }
    void signOut() {
        auth.signOut();
    }




    void writeAdvertToFirebase(File imageFile, Map<String, Object> data) {
        uploadImageToFirebase(imageFile, (String) data.get("uniqueAdID"), () -> writeToDatabase(data));

    }

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
    private void writeToDatabase(Map<String, Object> data) {
        isWritingAdvertToDatabase = true;
        advertPath.document((String) data.get("uniqueAdID"))
                .set(data).addOnSuccessListener(aVoid -> {
            Log.d(TAG, "DocumentSnapshot successfully written!");
            isWritingAdvertToDatabase = false;


        })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error writing document", e);
                    isWritingAdvertToDatabase = false;
                });
    }

    private void uploadImageToFirebase(File imageFile, String uniqueAdID, SuccessCallback successCallback) {
        isWritingImageToDatabase = true;
        try {
            InputStream uploadStream = new FileInputStream(imageFile);
            UploadTask uploadTask = imagesRef.child(uniqueAdID).putStream(uploadStream);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                isWritingImageToDatabase = false;
                successCallback.onSuccess();

            }).addOnFailureListener(e -> {
                isWritingImageToDatabase = false;
                //Handle errors here
                e.printStackTrace();
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }




    void removeAdFromFavourites(String adID, String userID) {
        userPath.document(userID).get().addOnCompleteListener(task -> {
            List<String> favourites = (List<String>) task.getResult().getData().get("favourites");
            favourites.remove(adID);
            userPath.document(userID).update("favourites", favourites);
        });
    }


    void addAdToFavourites(String adID, String userID) {
        userPath.document(userID).get().addOnCompleteListener(task -> {
            try {
                List<String> favourites = (List<String>) task.getResult().getData().get("favourites");
                favourites.add(adID);
                userPath.document(userID).update("favourites", favourites); //Should write updated favourites to firebase
            } catch (NullPointerException e) { //Is thrown if users favourite-array is currently empty
                List<String> favourites = new ArrayList<>();
                favourites.add(adID);
                userPath.document(userID).update("favourites", favourites); //Adds initial ad as user favourite
            }
        });
    }
    void deleteIDFromFavourites(String userID, String favouriteID) {
        userPath.document(userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                deleteID(favouriteID, task);
            } else {
                //TODO failurecallback
            }
        });
    }
    private void deleteID(String favouriteID, Task<DocumentSnapshot> task) {
        DocumentSnapshot userData = task.getResult();
        String userID = (String) userData.get("userID");
        List<String> favourites = (List<String>) userData.get("favourites");
        favourites.remove(favouriteID);
        userPath.document(userID).update("favourites", favourites);
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
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private Map<String, Object> prepareChatMap(String uniqueChatID) {
        Map<String, Object> chatMap = new HashMap<>();
        chatMap.put("uniqueChatID", uniqueChatID);
        chatMap.put("isActive", true);
        return chatMap;
    }

    private Map<String, Object> prepareMessagesMap(String adOwnerID, String otherUserID, String advertID, String imageURL) {
        Map<String, Object> messagesMap = new HashMap<>();
        messagesMap.put("userOneID", otherUserID);
        messagesMap.put("userTwoID", adOwnerID);
        messagesMap.put("advertID", advertID);
        messagesMap.put("imageURL", imageURL);
        return messagesMap;
    }

    void createNewChat(String adOwnerID, String otherUserID, String advertID, String imageURL, stringCallback stringCallback) {
        String uniqueChatID = UniqueIdCreator.getUniqueID();
        Map<String, Object> chatMap = prepareChatMap(uniqueChatID);
        Map<String, Object> messagesMap = prepareMessagesMap(adOwnerID, otherUserID, advertID, imageURL);
        writeChatAndMessageData(adOwnerID, otherUserID, stringCallback, uniqueChatID, chatMap, messagesMap);
    }


    void writeMessage(String uniqueChatID, Map<String, Object> messageMap) {
        chatPath.document(uniqueChatID).collection("messages").document().set(messageMap).addOnSuccessListener(aVoid -> {

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }


    private void deleteChat(List<Map<String, String>> mapList, String adID, String userID) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("isActive", false);
        for (Map<String, String> map : mapList) {
            userPath.document(Objects.requireNonNull(map.get("receiverID"))).collection("myConversations").document(Objects.requireNonNull(map.get("chatID"))).update(updates);
            userPath.document(userID).collection("myConversations").document(Objects.requireNonNull(map.get("chatID"))).delete();

        }
        notifyChatObservers();
    }

    void removeChat(String userID, String chatID) {
        userPath.document(userID).collection("myConversations").document(chatID).delete();

    }

    /**
     * Deleting an ad with the specific adID from the database
     */
    void deleteAd(List<Map<String, String>> chatReceiverAndUserIDMap, Map<String, String> adIDAndUserID) {
        String adID = adIDAndUserID.get("adID");
        String userID = adIDAndUserID.get("userID");
        advertPath.document((adID)).delete();
        deleteChat(chatReceiverAndUserIDMap, adID, userID);

    }
}
