package com.masthuggis.boki.backend;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.masthuggis.boki.backend.callbacks.DBCallback;
import com.masthuggis.boki.backend.callbacks.DBMapCallback;
import com.masthuggis.boki.backend.callbacks.FailureCallback;
import com.masthuggis.boki.backend.callbacks.SuccessCallback;
import com.masthuggis.boki.backend.callbacks.stringCallback;
import com.masthuggis.boki.model.observers.BackendObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import static androidx.constraintlayout.widget.Constraints.TAG;

class BackendReader {

    private FirebaseStorage storage = FirebaseStorage.getInstance(); //Reference to Cloud Storage that holds images
    private StorageReference mainRef = storage.getReference();
    private StorageReference imagesRef = mainRef.child("images"); //Reference to storage location of images
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private final List<BackendObserver> backendObservers;
    private CollectionReference advertPath;
    private CollectionReference chatPath;
    private CollectionReference userPath;


    BackendReader(List<BackendObserver> backendObservers) {
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


    void getUser(DBMapCallback dbMapCallback) {
        String userID = auth.getCurrentUser().getUid();
        userPath.document(userID).addSnapshotListener((documentSnapshot, e) -> dbMapCallback.onCallBack(documentSnapshot.getData()));
    }


    void getUserFromID(String userID, DBMapCallback dbMapCallback) {
        userPath.document(userID).addSnapshotListener((documentSnapshot, e) -> dbMapCallback.onCallBack(documentSnapshot.getData()));
    }


    boolean isUserSignedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null;
    }


    void initialAdvertFetch(DBCallback dbCallback) {
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

    void attachMarketListener(DBCallback DBCallback) {
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

    void getFavouriteIDs(String userID, DBMapCallback dbMapCallback) {
        userPath.document(userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot user = task.getResult();
                dbMapCallback.onCallBack(user.getData());
            }
        });
    }


    void getMessages(String uniqueChatID, DBCallback messageCallback) {
        chatPath.document(uniqueChatID).collection("messages").addSnapshotListener((queryDocumentSnapshots, e) -> {
            List<Map<String, Object>> messageMap = new ArrayList<>();
            for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots) {
                messageMap.add(querySnapshot.getData());
            }
            messageCallback.onCallBack(messageMap);
            notifyChatObservers();
            notifyMessagesObserver();
        });
    }


    void getUserChats(String userID, DBCallback DBCallback, FailureCallback failureCallback) {
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
            this.createChats(queryDocumentSnapshots, chatDataList, () -> {
                notifyChatObservers();
                DBCallback.onCallBack(chatDataList);
            });
        });
    }

    private void createChats(QuerySnapshot queryDocumentSnapshots, List<Map<String, Object>> chatDataList, SuccessCallback successCallback) {
        for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
            String uniqueChatID = (q.getData().get("uniqueChatID").toString());
            boolean isActive = ((boolean) q.getData().get("isActive"));
            chatPath.document(uniqueChatID).get().addOnSuccessListener(documentSnapshot -> {
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
                public void onFailure(@Nullable Exception e) {

                }
            });
        }

    }
}
