package com.masthuggis.boki.backend;

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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.masthuggis.boki.model.Chat;
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
    private static List<Map<String, Object>> userAdvertsData = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance(); //Reference to database that holds users and adverts
    private FirebaseStorage storage = FirebaseStorage.getInstance(); //Reference to Cloud Storage that holds images
    private StorageReference mainRef = storage.getReference();
    private StorageReference imagesRef = mainRef.child("images"); //Reference to storage location of images
    private FirebaseAuth auth = FirebaseAuth.getInstance();


    private BackendDataHandler() {

    }

    public static BackendDataHandler getInstance() {
        if (instance == null)
            instance = new BackendDataHandler();
        return instance;
    }


    //Also somehow needs to give the application the userID/AdvertID generated by firebase
    void writeAdvertToFirebase(HashMap<String, Object> data, File imageFile) {
        uploadImageToFirebase(imageFile, (String) data.get("uniqueAdID"));
        writeToDatabase(data);
    }

    private void writeToDatabase(HashMap<String, Object> data) {
        String uniqueOwnerID = (String) data.get("uniqueOwnerID");
        db.collection("users").document(uniqueOwnerID).collection("adverts").document()
                .set(data).addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
    }

    private void uploadImageToFirebase(File imageFile, String uniqueAdID) {
        try {
            InputStream inputStream = new FileInputStream(imageFile);
            UploadTask uploadTask = imagesRef.child(uniqueAdID).putStream(inputStream);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
            }).addOnFailureListener(e -> {
                //Handle errors here
                e.printStackTrace();
            });
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }


    void readUserIDAdverts(DBCallback DBCallback, String userID) {
        CollectionReference users = db.collection("users");
        users.document(userID).collection("adverts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
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
            }
        });
    }

    //Fetch data for all adverts from all users
    //might want to run this on separate thread created by caller
    void readAllAdvertData(DBCallback DBCallback) {
        List<Map<String, Object>> advertDataList = new ArrayList<>();
        db.collectionGroup("adverts").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> adverts = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot snapshot : adverts) {
                    Map<String, Object> toBeAdded = snapshot.getData();
                    downloadFirebaseFile((String) toBeAdded.get("uniqueAdID"), file -> {
                        toBeAdded.put("imgFile", file);
                        advertDataList.add(toBeAdded);
                        DBCallback.onCallBack(advertDataList);
                    });
                    //advertDataList.add(toBeAdded);
                }
                //DBCallback.onCallBack(advertDataList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Read from firebase failed.");

            }
        });
    }

    /**
     * All images in firebase are stored in the images-folder with their
     * uniqueAdID as filenames
     */
    private void downloadFirebaseFile(String uniqueID, FileCallback fileCallback) {
        try {
            File localFile = File.createTempFile("images", "jpg");
            imagesRef.child(uniqueID).getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                //Local temp file has been created and can be accessed
                //through variable localFile
                fileCallback.onCallback(localFile); //return chain of callbacks when image is downloaded
            }).addOnFailureListener(e -> {
                System.out.println("Download from firebase failed.");
                e.printStackTrace();
            });
        } catch (IOException e) {
            e.printStackTrace();
            fileCallback.onCallback(null);
        }
    }


    public void getUserChats(String userID, chatDBCallback chatDBCallback) {
        List<Map<String, Object>> chatDataList = new ArrayList<>();
        db.collection("users").document(userID).collection("conversations").addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }

            assert queryDocumentSnapshots != null;
            for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                chatDataList.add(q.getData());
            }
            chatDBCallback.onCallback(chatDataList);
        });
    }

    public void createNewChat(HashMap<String, Object> newChatMap) {
        String uniqueChatID = UniqueIdCreator.getUniqueID();
        newChatMap.put("uniqueChatID", uniqueChatID);
        String sender = (String) newChatMap.get("sender");
        String receiver = (String) newChatMap.get("receiver");
        db.collection("users").document(receiver).collection("conversations").document().set(newChatMap)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "DocumentSnapshot successfully written!");

                    db.collection("users").document(sender).collection("conversations")
                            .document().set(newChatMap);
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
    }


    //Small method for manually testing if firebase returns the correct ID's for users and adverts
    String getFireBaseID(String userID, String advertID) {
        if (advertID != null)
            return db.collection("users").document(userID).collection("adverts").document(advertID).getId();
        return db.collection("users").document(userID).getId();
    }


    //Downloads the file as a local tempFile rather than as an array of bytes.
    private void testDownloadFromCloudStorage() {
        try {
            File localFile = File.createTempFile("images", "jpg");
            imagesRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                //Reference downloaded file from here
            }).addOnFailureListener(e -> {
                //Handle any errors
            });
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }


    public void userSignIn(String email, String password) {
        try {
            Task<AuthResult> authResultTask = auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void userSignUp(String email, String password) {
        try {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                            } else {
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
//        userMap.put("email", auth.getCurrentUser().getEmail());
        //      userMap.put("displayname", auth.getCurrentUser().getDisplayName());
        FirebaseUser user = auth.getCurrentUser();
        String str = user.getUid();
        userMap.put("userID", str);
        return userMap;
    }

    public void writeMessage(String uniqueChatID, HashMap<String, Object> messageMap) {
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


    public void getMessages(String uniqueChatID, Chat chat, DBCallback messageCallback) {
        List<Map<String, Object>> messageMap = new ArrayList<>();
        db.collection("messages").document(uniqueChatID).collection("messages").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                messageMap.clear();
                for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots) {
                    messageMap.add(querySnapshot.getData());
                }
                messageCallback.onCallBack(messageMap);
                chat.updateChatObservers();

            }
        });

    }
}


