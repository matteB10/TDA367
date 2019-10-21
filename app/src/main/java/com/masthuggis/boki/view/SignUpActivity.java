package com.masthuggis.boki.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.masthuggis.boki.R;
import com.masthuggis.boki.injectors.DependencyInjector;
import com.masthuggis.boki.presenter.SignUpPresenter;

import java.util.Optional;


public class SignUpActivity extends AppCompatActivity implements ValidatorView, SignUpPresenter.View {
    private SignUpPresenter presenter = new SignUpPresenter(this, DependencyInjector.injectDataModel());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setUpBtns();
    }

    private void setUpBtns() {
        Button btnSignIn = findViewById(R.id.signInButton);
        btnSignIn.setOnClickListener(view -> presenter.onSignInButtonPressed());

        Button btnSignUp = findViewById(R.id.signUpButton);
        btnSignUp.setOnClickListener(view -> presenter.onSignUpButtonPressed(getEmail(), getPassword(), getUsername()));
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

    /**
     * Exists the app if the user presses back button. Makes it impossible to enter the app without
     * signing up or logging in.
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public void showAccessFailedMessage(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    public void accessGranted() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showSignInScreen() {
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
        finish();
    }
}