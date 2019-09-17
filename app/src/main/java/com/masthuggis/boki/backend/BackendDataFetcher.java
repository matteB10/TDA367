package com.masthuggis.boki.backend;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.masthuggis.boki.model.Book;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
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
    FirebaseFirestore db = FirebaseFirestore.getInstance();
  /*  FirebaseUser user = new FirebaseUser() {
        @Override
        public void writeToParcel(Parcel parcel, int i) {

        }

        @NonNull
        @Override
        public String getUid() {
            return "EliteIsak";
        }

        @NonNull
        @Override
        public String getProviderId() {
            return null;
        }

        @Override
        public boolean isAnonymous() {
            return false;
        }

        @Nullable
        @Override
        public List<String> getProviders() {
            return null;
        }

        @NonNull
        @Override
        public List<? extends UserInfo> getProviderData() {
            return null;
        }

        @NonNull
        @Override
        public FirebaseUser zza(@NonNull List<? extends UserInfo> list) {
            return null;
        }

        @Override
        public FirebaseUser zzce() {
            return null;
        }

        @NonNull
        @Override
        public FirebaseApp zzcc() {
            return null;
        }

        @Nullable
        @Override
        public String getDisplayName() {
            return null;
        }

        @Nullable
        @Override
        public Uri getPhotoUrl() {
            return null;
        }

        @Nullable
        @Override
        public String getEmail() {
            return null;
        }

        @Nullable
        @Override
        public String getPhoneNumber() {
            return null;
        }

        @Override
        public boolean isEmailVerified() {
            return false;
        }

        @Nullable
        @Override
        public String zzcf() {
            return null;
        }

        @NonNull
        @Override
        public zzcz zzcg() {
            return null;
        }

        @Override
        public void zza(@NonNull zzcz zzcz) {

        }

        @NonNull
        @Override
        public String zzch() {
            return null;
        }

        @NonNull
        @Override
        public String zzci() {
            return null;
        }

        @Nullable
        @Override
        public FirebaseUserMetadata getMetadata() {
            return null;
        }
    };*/


    private BackendDataFetcher() {

    }

    public static BackendDataFetcher getInstance() {
        if(instance == null)
            instance = new BackendDataFetcher();
        return instance;
    }

    public String getMockBooks(Context context) {
        String json = null;
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

    public void addNewBook(Book book) {
        Map<String, Object> data = new HashMap<>();
        data.put("Title", book.getTitle());
        data.put("Author", book.getAuthor());
        data.put("Edition", book.getEdition());
        data.put("ISBN", book.getIsbn());
        data.put("Year Published", book.getYearPublished());
        data.put("Price", book.getPrice());
        data.put("Condition", book.getCondition());
        data.put("Pre Defined Tags", book.getPreDefinedTags());
        data.put("User tags", book.getUserTags());


        db.collection("books").document("randomID")
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
}
