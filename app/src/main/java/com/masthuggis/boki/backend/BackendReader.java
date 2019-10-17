package com.masthuggis.boki.backend;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.masthuggis.boki.model.observers.BackendObserver;

import java.util.ArrayList;
import java.util.List;

public class BackendReader {

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
}
