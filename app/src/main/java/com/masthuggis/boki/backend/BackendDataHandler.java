package com.masthuggis.boki.backend;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
     * Singleton that converts the contents of a json-file into a String via a Context-object.
     *
     * @param context the Context-object required to load the .json-file via the assets-folder
     * @return the contents of the loaded .json-file formatted as a String.
     */
    private static BackendDataHandler instance;
    private static List<Map<String, Object>> userAdvertsData = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance(); //Reference to database that holds users and adverts
    private FirebaseStorage storage = FirebaseStorage.getInstance(); //Reference to Cloud Storage that holds images
    private StorageReference mainRef = storage.getReference();
    private StorageReference imagesRef = mainRef.child("images"); //Reference to storage location of images

    private static int OMEGALUL = 0;
    private BackendDataHandler() {

    }

    static BackendDataHandler getInstance() {
        if (instance == null)
            instance = new BackendDataHandler();
        return instance;
    }

    public String getMockBooks(Context context) {
        String json;
        try {
            InputStream inputStream = context.getAssets().open("mockBooks.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, StandardCharsets.UTF_8);
            return json;

        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }
    }


    //Also somehow needs to give the application the userID/AdvertID generated by firebase
    void writeAdvertToFirebase(HashMap<String, Object> data) {
        uploadImageToFirebase(data); //URL is known

        //    gs://boki-f5fbc.appspot.com/images/a27f2da8-a2a1-4f3f-ab47-4c7b125b814e
        writeToDatabase(data);
    }

    private void writeToDatabase(HashMap<String, Object> data) {
        String uniqueOwnerID = (String) data.get("uniqueOwnerID");
        db.collection("users").document(uniqueOwnerID).collection("adverts").document()
                .set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully written!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    private void uploadImageToFirebase(HashMap<String, Object> data) {
        String uniqueID = (String) data.get("uniqueAdID");
        String localFilePath = (String) data.get("imgURL");
        try {
            InputStream inputStream = new FileInputStream(new File(localFilePath));
            UploadTask uploadTask = imagesRef.child(uniqueID).putStream(inputStream);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Oklart
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Handle errors here
                    e.printStackTrace();
                }
            });
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }


    void readUserIDAdverts(advertisementDBCallback advertisementDBCallback, String userID) {
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
                advertisementDBCallback.onCallBack(advertDataList);
            }
        });
    }

    //Fetch data for all adverts from all users
    //Not currently possible to attach listeners to subcollections, but possible to query so-called "Collection groups"
    void readAllAdvertData(advertisementDBCallback advertisementDBCallback) {
        List<Map<String, Object>> advertDataList = new ArrayList<>();
        db.collectionGroup("adverts").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> adverts = queryDocumentSnapshots.getDocuments();
                int i=0;
                for (DocumentSnapshot snapshot : adverts) {
                    Map<String, Object> toBeAdded = snapshot.getData();
                    toBeAdded.put("imgURL", downloadFirebaseFile((String)toBeAdded.get("uniqueAdID")).toString());
                    advertDataList.add(toBeAdded);
                    i++;
                }
                advertisementDBCallback.onCallBack(advertDataList);
                OMEGALUL++;
                System.out.println(OMEGALUL); //Isak, explain yourself
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
            imagesRef.child(uniqueID).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    //Local temp file has been created and can be accessed, even outside this function
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //De gick jävla snett de där du, är du lite dum inuti hvuudet?? Tappad som barn? Mobbad som pucko?
                }
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
            imagesRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    //Reference downloaded file from here
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Handle any errors
                }
            });
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }

}
