package com.masthuggis.boki.backend;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.masthuggis.boki.model.observers.BackendObserver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BackendWriter {
    private FirebaseFirestore db = FirebaseFirestore.getInstance(); //Reference to database that holds users and adverts
    private FirebaseStorage storage = FirebaseStorage.getInstance(); //Reference to Cloud Storage that holds images
    private StorageReference mainRef = storage.getReference();
    private StorageReference imagesRef = mainRef.child("images"); //Reference to storage location of images
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private final List<BackendObserver> backendObservers = new ArrayList<>();
    private CollectionReference advertPath;
    private CollectionReference chatPath;
    private CollectionReference userPath;


    private boolean isWritingImageToDatabase = false;
    private boolean isWritingAdvertToDatabase = false;
    private final BackendReader backendReader = new BackendReader();
    private final BackendWriter backendWriter = new BackendWriter();


    public void updateAdToFirebase(File imageFile, Map<String, Object> dataMap) {
        String adID = (String) dataMap.get("uniqueAdID");
        advertPath.whereEqualTo("uniqueAdID", adID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            advertPath.document(documentSnapshot.getId())
                                    .update("title", dataMap.get("title"));
                            advertPath.document(documentSnapshot.getId())
                                    .update("price", dataMap.get("price"));
                            advertPath.document(documentSnapshot.getId())
                                    .update("description", dataMap.get("description"));
                            advertPath.document(documentSnapshot.getId())
                                    .update("condition", dataMap.get("condition"));
                            advertPath.document(documentSnapshot.getId())
                                    .update("tags", dataMap.get("tags"));
                        }
                        if (imageFile != null) {
                            uploadImageToFirebase(imageFile, dataMap.get("uniqueAdID").toString(), () -> {

                            });
                        }
                    }
                    notifyAdvertObservers();
                });
    }
}
