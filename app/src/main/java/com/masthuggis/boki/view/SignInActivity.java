package com.masthuggis.boki.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.SignInPresenter;

public class SignInActivity extends AppCompatActivity implements SignInPresenter.View {
    private SignInPresenter presenter = new SignInPresenter(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        super.onStart();
        setContentView(R.layout.activity_signin);
        setUpBtns();

    }

    public String getEmail() {
        EditText email = findViewById(R.id.email);
        return email.getText().toString();
    }

    public String getPassword() {
        EditText password = findViewById(R.id.password);
        return password.getText().toString();
    }

    private void setUpBtns() {
        Button btnSignIn = findViewById(R.id.signInButton);
        btnSignIn.setOnClickListener(view -> presenter.onSignInButtonPressed(getEmail(), getPassword()));

        Button btnSignUp = findViewById(R.id.signUpButton);
        btnSignUp.setOnClickListener(view -> presenter.onSignUpButtonPressed());
    }

    /**
     * Navigate to sign up page when back button is pressed. Used to make it impossible for the user
     * to enter the app without signing in.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void showSignUpScreen() {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void signInSuccess() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void showSignInFailedMessage(String errorMessage) {
        Toast toast = Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG);
        toast.show();
    }

}
