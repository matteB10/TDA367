package com.masthuggis.boki.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.SignUpPresenter;


public class SignUpActivity extends AppCompatActivity implements SignUpPresenter.View {
    private  SignUpPresenter presenter = new SignUpPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setUpBtns();
    }


    public String getEmail(){
        EditText email = findViewById(R.id.email);
        return email.getText().toString();
    }

    public String getPassword(){
        EditText password = findViewById(R.id.password);
        return password.getText().toString();
    }


    private void setUpBtns(){
        Button btnSignIn = findViewById(R.id.signInButton);
        btnSignIn.setOnClickListener(view -> presenter.onSignInButtonPressed());

        Button btnSignUp = findViewById(R.id.signUpButton);
        btnSignUp.setOnClickListener(view->presenter.onSignUpButtonPressed(getEmail(),getPassword()));

    }


    //switch between screens
    @Override
    public void showSignInScreen() {
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);
    }
    public void showProfileScreen() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
    }
}