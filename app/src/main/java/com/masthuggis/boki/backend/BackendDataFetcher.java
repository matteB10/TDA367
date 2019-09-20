package com.masthuggis.boki.backend;

import android.content.Context;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private BackendDataFetcher() {

    }

    public static BackendDataFetcher getInstance() {
        if(instance == null)
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

    /**
     * This is strictly testing for later implementation of Firebase.
     * @param advert
     */

   /* public void addNewadvert(Advertisement advert) {
        Map<String, Object> data = new HashMap<>();
        data.put("Title", advert.getTitle());
        data.put()
        data.put("Author", advert.getAuthor());
        data.put("Edition", advert.getEdition());
        data.put("ISBN", advert.getIsbn());
        data.put("Year Published", advert.getYearPublished());
       // data.put("Price", advert.getPrice());
        data.put("Condition", advert.getCondition());
        data.put("Pre Defined Tags", advert.getPreDefinedTags());
        data.put("User tags", advert.getUserTags());


        db.collection("adverts").document("randomID")
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

    }*/
}
