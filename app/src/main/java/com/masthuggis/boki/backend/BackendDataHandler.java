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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.masthuggis.boki.backend.callbacks.DBCallback;
import com.masthuggis.boki.backend.callbacks.DBMapCallback;
import com.masthuggis.boki.backend.callbacks.FailureCallback;
import com.masthuggis.boki.backend.callbacks.SuccessCallback;
import com.masthuggis.boki.backend.callbacks.stringCallback;
import com.masthuggis.boki.model.DataModel;
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

/**
 * Utility class with functionality for Fetching data from Backend in form of a .json-file.
 * The .json-file must be placed in the assets-folder of the application
 */
public class BackendDataHandler implements iBackend {

    /**
     * Singleton that handles all data-fetching from firebase
     * Called from repositories
     */
    private FirebaseFirestore db = FirebaseFirestore.getInstance(); //Reference to database that holds users and adverts
    private FirebaseStorage storage = FirebaseStorage.getInstance(); //Reference to Cloud Storage that holds images
    private StorageReference mainRef = storage.getReference();
    private StorageReference imagesRef = mainRef.child("images"); //Reference to storage location of images
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private final List<BackendObserver> backendObservers = new ArrayList<>();
    private CollectionReference advertPath;
    private CollectionReference chatPath;
    private CollectionReference userPath;


    private boolean isWritingImageToDatabase = false;
    private boolean isWritingAdvertToDatabase = false;
    private final BackendReader backendReader = new BackendReader();
    private final BackendWriter backendWriter = new BackendWriter();


    BackendDataHandler() {
        advertPath = db.collection("market");
        chatPath = db.collection("chats");
        userPath = db.collection("users");
    }

    public void addBackendObserver(BackendObserver backendObserver) {
        this.backendObservers.add(backendObserver);
    }

    public void removeBackendObserver(BackendObserver backendObserver) {
        this.backendObservers.remove(backendObserver);
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

    private void notifyAdvertObservers() {
        for (BackendObserver backendObserver : backendObservers) {
            backendObserver.onAdvertisementsChanged();
        }
    }

    //Also somehow needs to give the application the userID/AdvertID generated by firebase
    public void writeAdvertToFirebase(File imageFile, Map<String, Object> data) {
        uploadImageToFirebase(imageFile, (String) data.get("uniqueAdID"), () -> writeToDatabase(data));

    }

    //Gets a list of the ids of the adverts the current user has marked as favourites
    public void getFavouriteIDs(DBMapCallback dbMapCallback) {
        String userID = DataModel.getInstance().getUserID();
        userPath.document(userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot user = task.getResult();
                dbMapCallback.onCallBack(user.getData());
            }
        });
    }

    public void deleteIDFromFavourites(String favouriteID) {
        String userID = DataModel.getInstance().getUserID();
        userPath.document(userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                deleteID(favouriteID, task);
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


    private void writeToDatabase(Map<String, Object> data) {
        isWritingAdvertToDatabase = true;
        advertPath.document((String) data.get("uniqueAdID"))
                .set(data).addOnSuccessListener(aVoid -> {
            Log.d(TAG, "DocumentSnapshot successfully written!");
            isWritingAdvertToDatabase = false;
            notifyAdvertObservers();

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

    @Override
    public void initialAdvertFetch(DBCallback dbCallback) {
        advertPath.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Map<String, Object>> advertsData = new ArrayList<>();
                for (DocumentSnapshot advertData : task.getResult().getDocuments()) {
                    advertsData.add(advertData.getData());
                }
                updateAdvertsDataListWithImgUrl(advertsData, dbCallback::onCallBack);
            }
        });
    }


    public void attachMarketListener(DBCallback DBCallback) {
        advertPath.addSnapshotListener((queryDocumentSnapshots, e) -> { //Runs every time change happens i market
            if (e != null) { //Can't attach listener, is path correct to firebase?
                Log.w(TAG, "Listen failed", e);
                return;
            }
            if (queryDocumentSnapshots.size() == 0) {
                List<Map<String, Object>> newList = new ArrayList<>();
                DBCallback.onCallBack(newList);
                return;
            }
            List<DocumentSnapshot> adverts = queryDocumentSnapshots.getDocuments();
            List<Map<String, Object>> advertDataList = new ArrayList<>();
            for (DocumentSnapshot snapshot : adverts) {
                Map<String, Object> toBeAdded = snapshot.getData();
                advertDataList.add(toBeAdded);
            }
            updateAdvertsDataListWithImgUrl(advertDataList, DBCallback::onCallBack);
        });
    }

    private void updateAdvertsDataListWithImgUrl(List<Map<String, Object>> adverts, DBCallback dbCallback) {
        List<Map<String, Object>> advertsWithImages = new ArrayList<>();
        for (Map<String, Object> map : adverts) {
            getFirebaseURL((String) map.get("uniqueAdID"), url -> {
                map.put("imgUrl", url);
                advertsWithImages.add(map);
                if (advertsWithImages.size() == adverts.size()) {
                    dbCallback.onCallBack(adverts);
                }
            });
        }
    }


    private void getFirebaseURL(String uniqueID, stringCallback stringCallback) {
        imagesRef.child(uniqueID).getDownloadUrl().addOnSuccessListener(uri -> {
            //Got the download URL
            stringCallback.onCallback(uri.toString());
        }).addOnFailureListener(e -> e.printStackTrace());
    }


    public void getUserChats(String userID, DBCallback DBCallback, FailureCallback failureCallback) {
        userPath.document(userID).collection("myConversations").addSnapshotListener((queryDocumentSnapshots, e) -> {
            List<Map<String, Object>> chatDataList = new ArrayList<>();
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }
            if (queryDocumentSnapshots == null || queryDocumentSnapshots.size() <= 0) {
                failureCallback.onFailure("User has no chats.");
                notifyChatObservers();
                return;
            }
            BackendDataHandler.this.createChats(queryDocumentSnapshots, chatDataList, () -> {
                notifyChatObservers();
                DBCallback.onCallBack(chatDataList);
            });
        });
    }

    private void createChats(QuerySnapshot queryDocumentSnapshots, List<Map<String, Object>> chatDataList, SuccessCallback successCallback) {
        for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
            String uniqueChatID = (q.getData().get("uniqueChatID").toString());
            boolean isActive = ((boolean) q.getData().get("isActive"));
            db.collection("chats").document(uniqueChatID).get().addOnSuccessListener(documentSnapshot -> {
                Map<String, Object> data = documentSnapshot.getData();
                if (data == null) {
                    return;
                }
                data.put("uniqueChatID", uniqueChatID);
                data.put("isActive", isActive);
                chatDataList.add(data);
                if (chatDataList.size() == queryDocumentSnapshots.size()) {
                    successCallback.onSuccess();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }

    }


    public void createNewChat(String adOwnerID, String otherUserID, String advertID, String imageURL, stringCallback stringCallback) {

        //Här stoppar vi in uniquechatID i en map för att kunna sätta i båda användarnas firebase så de båda hittar sin chatt.

        String uniqueChatID = UniqueIdCreator.getUniqueID();
        Map<String, Object> chatMap = new HashMap<>();
        chatMap.put("uniqueChatID", uniqueChatID);
        chatMap.put("isActive", true);


        Map<String, Object> messagesMap = new HashMap<>();
        messagesMap.put("userOneID", otherUserID);
        messagesMap.put("userTwoID", adOwnerID);
        messagesMap.put("advertID", advertID);
        messagesMap.put("imageURL", imageURL);

        DocumentReference dfSender = userPath.document(otherUserID).collection("myConversations").document(uniqueChatID);
        DocumentReference dfReceiver = userPath.document(adOwnerID).collection("myConversations").document(uniqueChatID);
        DocumentReference dfUniqueChat = db.collection("chats").document(uniqueChatID);
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

    @Override
    public void addAdToFavourites(String adID, String userID) {
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

    @Override
    public void removeAdFromFavourites(String adID, String userID) {
        userPath.document(userID).get().addOnCompleteListener(task -> {
            List<String> favourites = (List<String>) task.getResult().getData().get("favourites");
            favourites.remove(adID);
            userPath.document(userID).update("favourites", favourites);
        });
    }

    public void userSignIn(String email, String password, SuccessCallback successCallback, FailureCallback failureCallback) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                successCallback.onSuccess();
            } else {
                failureCallback.onFailure(task.getException().getMessage());
            }
        });
    }

    public boolean isUserSignedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null;
    }

    public void userSignUpAndSignIn(String email, String password, String username, SuccessCallback successCallback, FailureCallback failureCallback) {
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

    @Override
    public void getUserFromID(String userID, DBMapCallback dbMapCallback) {
        userPath.document(userID).addSnapshotListener((documentSnapshot, e) -> dbMapCallback.onCallBack(documentSnapshot.getData()));
    }

    public void getUser(DBMapCallback dbMapCallback) {
        String userID = auth.getCurrentUser().getUid();
        userPath.document(userID).addSnapshotListener((documentSnapshot, e) -> dbMapCallback.onCallBack(documentSnapshot.getData()));
    }

    public void writeMessage(String uniqueChatID, Map<String, Object> messageMap) {
        db.collection("chats").document(uniqueChatID).collection("messages").document().set(messageMap).addOnSuccessListener(aVoid -> {

        }).addOnFailureListener(e -> e.printStackTrace());
    }


    public void getMessages(String uniqueChatID, DBCallback messageCallback) {
        db.collection("chats").document(uniqueChatID).collection("messages").addSnapshotListener((queryDocumentSnapshots, e) -> {
            List<Map<String, Object>> messageMap = new ArrayList<>();
            for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots) {
                messageMap.add(querySnapshot.getData());
            }
            messageCallback.onCallBack(messageMap);
            notifyChatObservers();
            notifyMessagesObserver();
        });
    }

    public void setUsername(String username, SuccessCallback successCallback) {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
            user.updateProfile(profileUpdates).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Username set.");
                    successCallback.onSuccess();
                }
            });
        }
    }


    public void signOut() {
        auth.signOut();
        DataModel.getInstance().loggedOut();
    }


    /**
     * Deleting an ad with the specific adID from the database
     */
    public void deleteAd(List<Map<String, String>> chatReceiverAndUserIDMap, Map<String, String> adIDAndUserID) {
        String adID = adIDAndUserID.get("adID");
        String userID = adIDAndUserID.get("userID");
        advertPath.document((adID)).delete();
        deleteChat(chatReceiverAndUserIDMap, adID, userID);
        notifyAdvertObservers();

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

    @Override
    public void removeChat(String userID, String chatID) {
        userPath.document(userID).collection("myConversations").document(chatID).delete();

    }


    @Override
    public void updateAdToFirebase(File imageFile, Map<String, Object> dataMap) {
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
                    }
                    notifyAdvertObservers();
                });
    }

}