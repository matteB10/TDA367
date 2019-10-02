package com.masthuggis.boki.backend;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
    FirebaseAuth auth = FirebaseAuth.getInstance();


    private BackendDataHandler() {

    }

    static BackendDataHandler getInstance() {
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
                //Oklart
            }).addOnFailureListener(e -> {
                //Handle errors here
                e.printStackTrace();
            });
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }


    void readUserIDAdverts(advertisementDBCallback advertisementDBCallback, String userID) {
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
            advertisementDBCallback.onCallBack(advertDataList);
        });
    }

    //Fetch data for all adverts from all users
    //might want to run this on separate thread created by caller
    void readAllAdvertData(advertisementDBCallback advertisementDBCallback) {
        List<Map<String, Object>> advertDataList = new ArrayList<>();
        db.collectionGroup("adverts").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> adverts = queryDocumentSnapshots.getDocuments();
            for (DocumentSnapshot snapshot : adverts) {
                Map<String, Object> toBeAdded = snapshot.getData();
                toBeAdded.put("imgFile", downloadFirebaseFile((String) toBeAdded.get("uniqueAdID")));
                advertDataList.add(toBeAdded);
            }
            advertisementDBCallback.onCallBack(advertDataList);
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
    private File downloadFirebaseFile(String uniqueID) {
        try {
            File localFile = File.createTempFile("images", "jpg");
            imagesRef.child(uniqueID).getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                //Local temp file has been created and can be accessed
                //through variable localFile
            }).addOnFailureListener(e -> {
                System.out.println("Download from firebase failed.");
                e.printStackTrace();
            });
            return localFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; //Best solution //TODO fix this return case to something more informative perhaps
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
        try{
            auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                        }
                    });

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void userSignUp(String email, String password) {
        try {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                        } else {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
