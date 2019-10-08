package com.masthuggis.boki.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.SignUpPresenter;


public class SignUpActivity extends AppCompatActivity implements SignUpPresenter.View {
    private SignUpPresenter presenter = new SignUpPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setUpBtns();
    }


    private String getEmail() {
        EditText email = findViewById(R.id.email);
        return email.getText().toString();
    }

    private String getPassword() {
        EditText password = findViewById(R.id.password);
        return password.getText().toString();
    }

    private String getUsername() {
        EditText username = findViewById(R.id.username_editText);
        return username.getText().toString();
    }


    private void setUpBtns() {
        Button btnSignIn = findViewById(R.id.signInButton);
        btnSignIn.setOnClickListener(view -> presenter.onSignInButtonPressed());

        Button btnSignUp = findViewById(R.id.signUpButton);
        btnSignUp.setOnClickListener(view -> presenter.onSignUpButtonPressed(getEmail(), getPassword(), getUsername()));
    }

    /**
     * Exists the app if the user presses back button. Makes it impossible to enter the app without
     * signing up or logging in.
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void showSignInScreen() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
    }

    @Override
    public void signedIn() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}