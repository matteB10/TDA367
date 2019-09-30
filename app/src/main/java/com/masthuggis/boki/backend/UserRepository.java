package com.masthuggis.boki.backend;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class UserRepository {
    private static UserRepository userRepository;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    public static UserRepository getInstance() {
        if (userRepository == null) {
            userRepository = new UserRepository();
        }
        return userRepository;
    }


    public void signIn(String email, String password, Activity activity) {
        try{
            auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                    }
                }
        });

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void signUp(String email, String password, Activity activity) {

        try {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
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
}
