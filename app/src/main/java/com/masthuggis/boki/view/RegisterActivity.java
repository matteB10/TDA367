package com.masthuggis.boki.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.masthuggis.boki.R;

public class RegisterActivity extends AppCompatActivity {
    /**Class that creates a new user, that is saved in Firebase, for later login activity.
     * For now it can only create a user with email and password.
     */
    private EditText inputEmail;
    private EditText inputPassword;
    private Button btnSignUp;
    private Button btnLogin;

    private FirebaseAuth auth;
    private ProgressDialog PD;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        PD = new ProgressDialog(this);
        PD.setMessage("Laddar...");
        PD.setCancelable(true);
        PD.setCanceledOnTouchOutside(false);

        auth = FirebaseAuth.getInstance();

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        btnSignUp = (Button) findViewById(R.id.signupButton);
        btnLogin = (Button) findViewById(R.id.signinButton);


        /** Setting the Sign in Button action.
         * Makes sure that all fields are filled and then tries to connect to Firebase.
         * If this task is successful, set a visible message on screen, if not, set the message to
         * what caused trouble.
         * @throws Exception if the registration is not completed.
         */
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                try {
                    if(password.length() >0 && email.length() > 0 ){
                        auth.createUserWithEmailAndPassword(email,password)
                                .addOnCompleteListener
                                        (RegisterActivity.this,
                                                new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(
                                            RegisterActivity.this,
                                            "registred Succesfully",
                                            Toast.LENGTH_LONG).show();
                                            finish();
                                }else{
                                    Toast.makeText(
                                            RegisterActivity.this,
                                            task.getException().getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(
                                RegisterActivity.this,
                                "Fill All Fields",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        /**set the Login button,
         * which is taking user to the Login Activity and login view.
         * @see LoginActivity
         */
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}