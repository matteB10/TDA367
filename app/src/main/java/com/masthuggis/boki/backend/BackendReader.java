package com.masthuggis.boki.backend;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.masthuggis.boki.backend.callbacks.DBCallback;
import com.masthuggis.boki.backend.callbacks.DBMapCallback;
import com.masthuggis.boki.backend.callbacks.FailureCallback;
import com.masthuggis.boki.backend.callbacks.SuccessCallback;
import com.masthuggis.boki.backend.callbacks.stringCallback;
import com.masthuggis.boki.model.observers.ChatObserver;
import com.masthuggis.boki.model.observers.MessagesObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * BackendWriter is a Java class which communicates with the backend database through reading only. The database used currently is Googles Firebase Firestore.
 */

class BackendReader {


    private FirebaseStorage storage = FirebaseStorage.getInstance(); //Reference to Cloud Storage that holds images
    private StorageReference mainRef = storage.getReference();
    private StorageReference imagesRef = mainRef.child("images"); //Reference to storage location of images
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private final List<ChatObserver> chatObservers;
    private final List<MessagesObserver> messagesObservers;

    private CollectionReference advertPath;
    private CollectionReference chatPath;
    private CollectionReference userPath;

    /**
     * The single constructor of the class which needs a list of backendobservers to make sure it updates the current backendobservers correctly.
     *
     *
     */
    BackendReader(List<ChatObserver> chatObservers, List<MessagesObserver> messagesObservers) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        advertPath = db.collection("market");
        chatPath = db.collection("chats");
        userPath = db.collection("users");
        this.chatObservers = chatObservers;
        this.messagesObservers=messagesObservers;
    }

    private void notifyChatObservers() {
        for (ChatObserver chatObserver: chatObservers) {
            chatObserver.onChatUpdated();
        }
    }

    private void notifyMessagesObserver() {
        for (MessagesObserver messagesObserver: messagesObservers) {
            messagesObserver.onMessagesChanged();
        }
    }

    /**
     * Fetches the information needed to create a new User from Firebase Firestore. Uses the authorization provided by Firebase to find
     * the currentUserID.
     *
     * @param dbMapCallback A callback used to send information back to the model when the fetch is done.
     */
    void fetchCurrentUser(DBMapCallback dbMapCallback) {
        String userID = auth.getCurrentUser().getUid();
        userPath.document(userID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable DocumentSnapshot documentSnapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    dbMapCallback.onCallBack(documentSnapshot.getData());
                }
            }
        });
    }

    /**
     * Fetches the information needed to create a new user from Firebase Firestore. Uses the ID provided by method call to find the information
     * needed to create a new User from Firebase Firestore.
     *
     * @param dbMapCallback A callback used to send information back to the model when the fetch is done.
     */
    void fetchUserFromID(String userID, DBMapCallback dbMapCallback) {
        userPath.document(userID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable DocumentSnapshot documentSnapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    dbMapCallback.onCallBack(documentSnapshot.getData());
                }
            }
        });
    }

    /**
     * Checks if there is a signed in user through Firebase Authorization.
     *
     * @return
     */
    boolean isUserSignedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user != null;
    }

    /**
     * A fetch of all advertisements from Firebase. Only used once to initialize the application.
     *
     * @param dbCallback returns all information about advertisements through a callback when the fetch is done.
     */
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

    /**
     * Attaches a snapshot listener to the pathway where all information about all the advertisements are stored
     * in Firebase Firestore. The code inside the eventlistener is ran every time an advertisement is
     * edited/added or removed.
     *
     * @param DBCallback returns all information about advertisements through a callback when the fetch is done.
     */
    void attachMarketListener(DBCallback DBCallback) {
        advertPath.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @androidx.annotation.Nullable FirebaseFirestoreException e) { //Runs every time change happens i market
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
                BackendReader.this.updateAdvertsDataListWithImgUrl(advertDataList, DBCallback::onCallBack);
            }
        });
    }

    /**
     * Adds a URL in the form of a string to all dataMaps containing the information needed to create a new
     * advertisement.
     *
     * @param adverts    list of datamaps containing information needed to later create advertisements.
     * @param dbCallback returns all information about advertisements through a callback when the fetch is done.
     */
    private void updateAdvertsDataListWithImgUrl(List<Map<String, Object>> adverts, DBCallback dbCallback) {
        List<Map<String, Object>> advertsWithImages = new ArrayList<>();
        for (Map<String, Object> map : adverts) {
            fetchImageURL((String) map.get("uniqueAdID"), url -> {
                map.put("imgUrl", url);
                advertsWithImages.add(map);
                if (advertsWithImages.size() == adverts.size()) {
                    dbCallback.onCallBack(adverts);
                }
            });
        }
    }

    /**
     * Helper method that actually fetches the imageURL belonging to a specific advertisement
     * using the uniqueID.
     *
     * @param uniqueID       used to find the correct advertisement.
     * @param stringCallback used to return imageURL when the fetch is done.
     */

    private void fetchImageURL(String uniqueID, stringCallback stringCallback) {
        imagesRef.child(uniqueID).getDownloadUrl().addOnSuccessListener(uri -> {
            //Got the download URL
            stringCallback.onCallback(uri.toString());
        }).addOnFailureListener(e -> e.printStackTrace());
    }

    /**
     * Fetches favouriteIDS needed to later know which advertisements are the users favourites.
     *
     * @param userID        Used to find specific path to user in Firebase Firestore.
     * @param dbMapCallback returns a map containing all information needed to create a favourite
     *                      when fetch is done.
     */
    void fetchFavouriteIDS(String userID, DBMapCallback dbMapCallback) {
        userPath.document(userID).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot user = task.getResult();
                assert user != null;
                dbMapCallback.onCallBack(user.getData());
            }
        });
    }

    /**
     * Attaches a snapshotlistener to the path of a specific chat. The code within the eventlistener is ran every time
     * an update is made to the pathway. Notifies observers.
     *
     * @param uniqueChatID    Used to find path to the specific chat.
     * @param messageCallback Returns a List of maps containing the information needed to create all messages
     *                        contained in a chat.
     */
    void attachMessagesSnapshotListener(String uniqueChatID, DBCallback messageCallback) {
        chatPath.document(uniqueChatID).collection("messages").addSnapshotListener((queryDocumentSnapshots, e) -> {
            List<Map<String, Object>> messageMap = new ArrayList<>();
            if (queryDocumentSnapshots == null || queryDocumentSnapshots.size() <= 0) {
                notifyChatObservers();
                notifyMessagesObserver();
                return;
            }
            for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots) {
                messageMap.add(querySnapshot.getData());
            }
            messageCallback.onCallBack(messageMap);
            notifyChatObservers();
            notifyMessagesObserver();
        });
    }

    /**
     * Attaches a snapshotlistener to the path of a users chats. The code within the eventlistener is ran every time
     * an update is made to the pathway. Notifies observers.
     *
     * @param userID          Used to find the pathway to the users chats in Firebase.
     * @param DBCallback      Returns a List of maps containing the information needed to create new chats.
     * @param failureCallback Notifies failure in fetch.
     */
    void attachChatSnapshotListener(String userID, DBCallback DBCallback, FailureCallback failureCallback) {
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
            this.createChatDataList(queryDocumentSnapshots, chatDataList, () -> {
                notifyChatObservers();
                DBCallback.onCallBack(chatDataList);
            });
        });
    }

    /**
     * Helper method used to add information from Firebase to lists of maps so that it contains all information needed to create
     * chats.
     *
     * @param queryDocumentSnapshots Contains documentsnapshots from Firebase.
     * @param chatDataList           A list is to be filled with information from Firebase.
     * @param successCallback        initiates a void callback which allows the caller to decide what to do on a successful method call.
     */
    private void createChatDataList(QuerySnapshot queryDocumentSnapshots, List<Map<String, Object>> chatDataList, SuccessCallback successCallback) {
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
