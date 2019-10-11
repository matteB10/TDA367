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
import com.masthuggis.boki.backend.callbacks.DBCallback;
import com.masthuggis.boki.backend.callbacks.DBMapCallback;
import com.masthuggis.boki.backend.callbacks.FailureCallback;
import com.masthuggis.boki.backend.callbacks.SuccessCallback;
import com.masthuggis.boki.backend.callbacks.stringCallback;
import com.masthuggis.boki.model.Chat;
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

    private boolean isWritingImageToDatabase = false;
    private boolean isWritingAdvertToDatabase = false;


    BackendDataHandler() {
        advertPath = db.collection("market");
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
    public void writeAdvertToFirebase(File imageFile, HashMap<String, Object> data) {
        uploadImageToFirebase(imageFile, (String) data.get("uniqueAdID"), new SuccessCallback() {
            @Override
            public void onSuccess() {
                writeToDatabase(data);
            }
        });

    }


    private void writeToDatabase(HashMap<String, Object> data) {
        isWritingAdvertToDatabase = true;
        db.collection("market").document((String) data.get("uniqueAdID"))
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
            });
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }


    //might want to run this on separate thread created by caller
    public void readAllAdvertData(DBCallback DBCallback) {
        List<Map<String, Object>> advertDataList = new ArrayList<>();
        db.collection("market").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) { //Runs every time change happens i market
                if (e != null) { //Can't attach listener, is path correct to firebase?
                    Log.w(TAG, "Listen failed", e);
                    return;
                }
                List<DocumentSnapshot> adverts = queryDocumentSnapshots.getDocuments();
                updateAdvertsDataListWithImgUrl(adverts, DBCallback::onCallBack);
            }
        });
    }


    private void updateAdvertsDataListWithImgUrl(List<DocumentSnapshot> adverts, DBCallback dbCallback) {
        List<Map<String, Object>> advertDataList = new ArrayList<>();
        for (DocumentSnapshot snapshot : adverts) {
            Map<String, Object> toBeAdded = snapshot.getData();
            getFirebaseURL((String) toBeAdded.get("uniqueAdID"), new stringCallback() {
                @Override
                public void onCallback(String url) {
                    toBeAdded.put("imgUrl", url);
                    advertDataList.add(toBeAdded);
                    if (advertDataList.size() == adverts.size()) {
                        dbCallback.onCallBack(advertDataList);
                    }
                }
            });
        }
    }

    private void getFirebaseURL(String uniqueID, stringCallback stringCallback) {
        imagesRef.child(uniqueID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Got the download URL
                stringCallback.onCallback(uri.toString());
            }
        }).addOnFailureListener(e -> e.printStackTrace());
    }


    public void getUserChats(String userID, DBCallback DBCallback,FailureCallback failureCallback) {
        db.collection("users").document(userID).collection("myConversations").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                List<Map<String, Object>> chatDataList = new ArrayList<>();
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                if (queryDocumentSnapshots == null || queryDocumentSnapshots.size() <= 0) {
                    failureCallback.onFailure("User have no chats.");
                    return;
                }
                BackendDataHandler.this.createChats(queryDocumentSnapshots, chatDataList, new SuccessCallback() {
                    @Override
                    public void onSuccess() {
                        DBCallback.onCallBack(chatDataList);
                        notifyChatObservers();
                    }
                });
            }
        });
    }

    private void createChats(QuerySnapshot queryDocumentSnapshots, List<Map<String, Object>> chatDataList, SuccessCallback successCallback) {
        for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
            String uniqueChatID = (q.getData().get("uniqueChatID").toString());
            db.collection("chats").document(uniqueChatID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Map<String, Object> data = documentSnapshot.getData();
                    if (data == null) {
                        return;
                    }
                    data.put("uniqueChatID", uniqueChatID);
                    chatDataList.add(data);
                    if (chatDataList.size() == queryDocumentSnapshots.size()) {
                        successCallback.onSuccess();
                    }
                }
            });


        }
    }

    public void createNewChat(String uniqueOwnerID, String advertID, stringCallback stringCallback, String receiverUsername) {

        //Här stoppar vi in uniquechatID i en map för att kunna sätta i båda användarnas firebase så de båda hittar sin chatt.

        String uniqueChatID = UniqueIdCreator.getUniqueID();
        HashMap<String, Object> chatMap = new HashMap<>();
        chatMap.put("uniqueChatID", uniqueChatID);

        String senderID = DataModel.getInstance().getUserID();
        String receiverID = uniqueOwnerID;


        HashMap<String, Object> messagesMap = new HashMap<>();

        messagesMap.put("userOneName", DataModel.getInstance().getUserDisplayName());
        messagesMap.put("userTwoName", receiverUsername);
        messagesMap.put("userOneID", senderID);
        messagesMap.put("userTwoID", receiverID);
        messagesMap.put("advertID", advertID);


        DocumentReference dfSender = db.collection("users").document(senderID).collection("myConversations").document();
        DocumentReference dfReceiver = db.collection("users").document(receiverID).collection("myConversations").document();
        DocumentReference dfUniqueChat = db.collection("chats").document(uniqueChatID);
        dfUniqueChat.set(messagesMap).addOnSuccessListener(aVoid -> {
            dfSender.set(chatMap).addOnSuccessListener(bVoid -> {
                dfReceiver.set(chatMap).addOnSuccessListener(cVoid -> {
                    stringCallback.onCallback(uniqueChatID);
                });
            });
        });
    }

    @Override
    public void addAdToFavourites(String adID, String userID) {
        db.collection("users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                try {
                    List<String> favourites = (List<String>) task.getResult().getData().get("favourites");
                    if (isAlreadyFavourite(favourites, adID)) //Prevents users from adding the same advert to favourites more than once
                        return;
                    favourites.add(adID);
                    db.collection("users").document(userID).update("favourites", favourites); //Should write updated favourites to firebase
                } catch (NullPointerException e) { //Is thrown if users favourite-array is currently empty
                    List<String> favourites = new ArrayList<>();
                    favourites.add(adID);
                    db.collection("users").document(userID).update("favourites", favourites); //Adds initial ad as user favourite
                }
            }
        });
    }

    private boolean isAlreadyFavourite(List<String> favourites, String adID) {
        for (String id : favourites) {
            if (id.equals(adID))
                return true;
        }
        return false;
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

    public void userSignUpAndSignIn(String email, String password, String username,SuccessCallback successCallback, FailureCallback failureCallback) {
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("email", email);
        userMap.put("username", username);
        try {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        String uniqueUserID = auth.getCurrentUser().getUid();
                                        userMap.put("userID", uniqueUserID);
                                        db.collection("users").document(uniqueUserID).set(userMap);
                                        successCallback.onSuccess();
                                    }
                                });

                            } else {
                                failureCallback.onFailure(task.getException().getMessage());
                            }
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getUser(DBMapCallback dbMapCallback) {
        Map<String, String> userMap = new HashMap<>();
        String userID = auth.getCurrentUser().getUid();
        db.collection("users").document(userID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                dbMapCallback.onCallBack(documentSnapshot.getData());
            }
        });

    }

    public void writeMessage(String uniqueChatID, HashMap<String, Object> messageMap) {
        db.collection("chats").document(uniqueChatID).collection("messages").document().set(messageMap).addOnSuccessListener(new OnSuccessListener<Void>() {
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


    public void getMessages(String uniqueChatID, Chat chat, DBCallback messageCallback) {
        db.collection("chats").document(uniqueChatID).collection("messages").addSnapshotListener(new EventListener<QuerySnapshot>() {
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

    public void setUsername(String username, SuccessCallback successCallback) {
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


    public void signOut() {
        auth.signOut();
        DataModel.getInstance().loggedOut();
    }


    /**
     * Deleting an ad with the specific adID from the database
     *
     * @param adID
     */
    public void deleteAd(String adID) {
        advertPath.document(adID).delete();
        notifyAdvertObservers();

    }

    public void updateAd(String adID, String newTitle, long newPrice, String newDescription,
                         List<String> tags, String newCondition, File imageFile) {
        advertPath.whereEqualTo("uniqueAdID", adID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                advertPath.document(documentSnapshot.getId())
                                        .update("title", newTitle);
                                advertPath.document(documentSnapshot.getId())
                                        .update("price", newPrice);
                                advertPath.document(documentSnapshot.getId())
                                        .update("description", newDescription);
                                advertPath.document(documentSnapshot.getId())
                                        .update("condition", newCondition);
                                advertPath.document(documentSnapshot.getId())
                                        .update("tags", tags);
                            }
                            if (imageFile != null) {
                                uploadImageToFirebase(imageFile, adID, new SuccessCallback() {
                                    @Override
                                    public void onSuccess() {

                                    }
                                });
                            }
                        }
                        notifyAdvertObservers();

                    }
                });
    }


}