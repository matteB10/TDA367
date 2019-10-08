package com.masthuggis.boki.backend;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.BackendObserver;
import com.masthuggis.boki.model.Chat;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.utils.UniqueIdCreator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static BackendDataHandler instance;
    private FirebaseFirestore db = FirebaseFirestore.getInstance(); //Reference to database that holds users and adverts
    private FirebaseStorage storage = FirebaseStorage.getInstance(); //Reference to Cloud Storage that holds images
    private StorageReference mainRef = storage.getReference();
    private StorageReference imagesRef = mainRef.child("images"); //Reference to storage location of images
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private final List<BackendObserver> backendObservers = new ArrayList<>();
    private CollectionReference advertPath;

    private boolean isWritingImageToDatabase = false;
    private boolean isWritingAdvertToDatabase = false;
    private int advertDataListCount = 0;


    private BackendDataHandler() {
        if (getUserID() != null) //Otherwise throws NullPointer on app launch
            advertPath = db.collection("users")
                    .document(getUserID()).collection("adverts");
    }

    public static BackendDataHandler getInstance() {
        if (instance == null)
            instance = new BackendDataHandler();
        return instance;
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
    void writeAdvertToFirebase(File imageFile, HashMap<String, Object> data, @Nullable SuccessCallback callback) {
        uploadImageToFirebase(imageFile, (String) data.get("uniqueAdID"));
        writeToDatabase(data);
    }

    private boolean completedWriteToDatabase() {
        return !isWritingAdvertToDatabase && !isWritingImageToDatabase;
    }

    private void writeToDatabase(HashMap<String, Object> data) {
        isWritingAdvertToDatabase = true;
        String uniqueOwnerID = (String) data.get("uniqueOwnerID");
        db.collection("users").document(uniqueOwnerID).collection("adverts").document()
                .set(data).addOnSuccessListener(aVoid -> {
            Log.d(TAG, "DocumentSnapshot successfully written!");
            isWritingAdvertToDatabase = false;
            this.notifyAdvertObservers();
        })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error writing document", e);
                    isWritingAdvertToDatabase = false;
                });
    }

    private void uploadImageToFirebase(File imageFile, String uniqueAdID) {
        isWritingImageToDatabase = true;
        try {
            InputStream uploadStream = new FileInputStream(imageFile);
            UploadTask uploadTask = imagesRef.child(uniqueAdID).putStream(uploadStream); //Starts upload to firebase
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                isWritingImageToDatabase = false;

            }).addOnFailureListener(e -> {
                isWritingImageToDatabase = false;
                //Handle errors here
                e.printStackTrace();
            });
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }


    void readUserIDAdverts(DBCallback DBCallback, String userID) {
        CollectionReference users = db.collection("users");
        users.document(userID).collection("adverts").addSnapshotListener((value, e) -> {
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }
            List<Map<String, Object>> advertDataList = new ArrayList<>();
            for (QueryDocumentSnapshot document : value) {
                Map<String, Object> advertData = document.getData();
                advertDataList.add(advertData);
            }
            DBCallback.onCallBack(advertDataList);
        });
    }

    //Fetch data for all adverts from all users
    //might want to run this on separate thread created by caller
    void readAllAdvertData(DBCallback dbCallback) {
        db.collectionGroup("adverts").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> adverts = queryDocumentSnapshots.getDocuments();
                updateAdvertsDataListWithImgUrl(adverts, advertDataList -> dbCallback.onCallBack(advertDataList));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Read from firebase failed.");

            }
        });
    }

    private void updateAdvertsDataListWithImgUrl(List<DocumentSnapshot> adverts, DBCallback dbCallback) {
        List<Map<String, Object>> advertDataList = new ArrayList<>();
        for (DocumentSnapshot snapshot : adverts) {
            Map<String, Object> toBeAdded = snapshot.getData();
            getFirebaseURL((String) toBeAdded.get("uniqueAdID"), new UrlCallback() {
                @Override
                public void onCallback(String url) {
                    toBeAdded.put("imgUrl", url);
                    advertDataList.add(toBeAdded);
                    advertDataListCount += 1;
                    if (advertDataListCount == adverts.size()) {
                        advertDataListCount = 0;
                        dbCallback.onCallBack(advertDataList);
                    }
                }
            });
        }
    }

    public void getFirebaseURL(String uniqueID, UrlCallback urlCallback) {
        imagesRef.child(uniqueID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Got the download URL
                urlCallback.onCallback(uri.toString());
            }
        }).addOnFailureListener(e -> e.printStackTrace());
    }


    public void getUserChats(String userID, chatDBCallback chatDBCallback) {
        db.collection("users").document(userID).collection("conversations").addSnapshotListener((queryDocumentSnapshots, e) -> {
            List<Map<String, Object>> chatDataList = new ArrayList<>();
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }
            //TODO ADD IMG URI TO CREATION OF CHATS.
            if (queryDocumentSnapshots.size() <= 0 || queryDocumentSnapshots == null) {
                return;
            }
            for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                chatDataList.add(q.getData());
            }
            if (chatDataList.size() == 0 || chatDataList == null) {
                return;
            }
            chatDBCallback.onCallback(chatDataList);
            notifyChatObservers();
        });
    }

    void createNewChat(HashMap<String, Object> newChatMap, Advertisement advertisement, stringCallback stringCallback) {
        String uniqueChatID = UniqueIdCreator.getUniqueID();
        newChatMap.put("uniqueChatID", uniqueChatID);

        String sender = DataModel.getInstance().getUserID();
        String receiver = advertisement.getUniqueOwnerID();
        DocumentReference df = db.collection("users").document(sender).collection("conversations").document();
        addAdvertisementToMap(newChatMap, advertisement);
        df.set(newChatMap)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully written!");
                    newChatMap.put("receiverUsername", DataModel.getInstance().getUserDisplayName());

                    //TODO REVERSE SENDER AND RECEIVER
                    newChatMap.put("receiver", sender);
                    newChatMap.put("sender", receiver);

                    db.collection("users").document(receiver).collection("conversations")
                            .document().set(newChatMap);

                    stringCallback.onCallback(newChatMap.get("uniqueChatID").toString());

                })
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
    }

    private void addAdvertisementToMap(HashMap<String, Object> dataMap, Advertisement advertisement) {
        dataMap.put("title", advertisement.getTitle());
        dataMap.put("description", advertisement.getDescription());
        dataMap.put("uniqueOwnerID", advertisement.getUniqueOwnerID());
        dataMap.put("condition", advertisement.getCondition());
        dataMap.put("price", advertisement.getPrice());
        dataMap.put("tags", advertisement.getTags());
        dataMap.put("uniqueAdID", advertisement.getUniqueID());
        dataMap.put("date", advertisement.getDatePublished());
        dataMap.put("advertOwnerID", advertisement.getOwner());
        dataMap.put("imgURL", advertisement.getImageUrl());
    }


    //Small method for manually testing if firebase returns the correct ID's for users and adverts
    String getFireBaseID(String userID, String advertID) {
        if (advertID != null)
            return db.collection("users").document(userID).collection("adverts").document(advertID).getId();
        return db.collection("users").document(userID).getId();
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
        if (user != null) {
            return true;
        } else {
            return false;
        }
    }

    public void userSignUp(String email, String password, SuccessCallback successCallback, FailureCallback failureCallback) {
        try {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                successCallback.onSuccess();
                            } else {
                                failureCallback.onFailure(task.getException().getMessage());
                            }
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUserID() {
        String id = auth.getUid();

        return id;
    }

    public Map<String, String> getUser() {
        Map<String, String> userMap = new HashMap<>();
        FirebaseUser user = auth.getCurrentUser();
        String str = user.getUid();
        String username = user.getDisplayName();
        String email = user.getEmail();
        userMap.put("userID", str);
        userMap.put("username", username);
        userMap.put("email", email);
        return userMap;
    }

    void writeMessage(String uniqueChatID, HashMap<String, Object> messageMap) {
        db.collection("messages").document(uniqueChatID).collection("messages").document().set(messageMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();

            }
        });
    }


    void getMessages(String uniqueChatID, Chat chat, DBCallback messageCallback) {
        db.collection("messages").document(uniqueChatID).collection("messages").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                List<Map<String, Object>> messageMap = new ArrayList<>();
                for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots) {
                    messageMap.add(querySnapshot.getData());
                }
                messageCallback.onCallBack(messageMap);
                notifyMessagesObserver();
            }
        });
    }

    void setUsername(String username, SuccessCallback successCallback) {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username).build();
            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Username set.");
                        successCallback.onSuccess();
                    }
                }
            });
        }
    }


    void signOut() {
        auth.signOut();
        DataModel.getInstance().loggedOut();
    }


    public void deleteAd(String adID) {
        CollectionReference advertPath = db.collection("users")
                .document(getUserID()).collection("adverts");
        advertPath.whereEqualTo("uniqueAdID", adID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId());
                                advertPath.document(document.getId())
                                        .delete();
                            }
                        }
                    }
                });
    }


    public void editTitle(String adID, String newTitle) {

        advertPath.whereEqualTo("uniqueAdID", adID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                advertPath.document(documentSnapshot.getId())
                                        .update("title", newTitle);
                            }
                        }
                    }
                });
    }

    public void editPrice(String adID, String newPrice) {
        advertPath.whereEqualTo("uniqueAdID", adID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                advertPath.document(documentSnapshot.getId())
                                        .update("price", newPrice);
                            }
                        }
                    }
                });

    }

    public void editDescription(String adID, String newDescription) {
        advertPath.whereEqualTo("uniqueAdID", adID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                advertPath.document(documentSnapshot.getId())
                                        .update("description", newDescription);
                            }
                        }
                    }
                });
    }
}