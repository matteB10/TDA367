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
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider;

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
public class BackendDataFetcher implements iBackend {

    /**
     * Singleton that converts the contents of a json-file into a String via a Context-object.
     *
     * @param context the Context-object required to load the .json-file via the assets-folder
     * @return the contents of the loaded .json-file formatted as a String.
     */
    private static BackendDataFetcher instance;
    private static List<Map<String, Object>> userAdvertsData = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private BackendDataFetcher() {

    }

    static BackendDataFetcher getInstance() {
        if (instance == null)
            instance = new BackendDataFetcher();
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

    //Fetch data for all adverts from all user
    //Not currently possible to attach listeners to subcollections, but possible to query so-called "Collection groups"
    void readAllAdvertData(advertisementDBCallback advertisementDBCallback) {
        List<Map<String,Object>> advertDataList = new ArrayList<>();
        db.collectionGroup("adverts").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> adverts = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot snapshot : adverts) {
                    advertDataList.add(snapshot.getData());
                }
                advertisementDBCallback.onCallBack(advertDataList);
            }
        });
    }


    //Small method for manually testing if firebase returns the correct ID's for users and adverts
    String getFireBaseID(String userID, String advertID) {
        if(advertID != null)
            return db.collection("users").document(userID).collection("adverts").document(advertID).getId();
        return db.collection("users").document(userID).getId();
    }

    private void test() {

    }

}
