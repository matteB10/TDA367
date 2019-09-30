package com.masthuggis.boki.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.SignInPresenter;

public class SignInActivity extends AppCompatActivity implements SignInPresenter.View{
    private SignInPresenter presenter = new SignInPresenter(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        super.onStart();
        setContentView(R.layout.activity_signin);
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

    private void setUpBtns() {
        Button btnSignIn = findViewById(R.id.signInButton);
        btnSignIn.setOnClickListener(view -> presenter.onSignInButtonPressed(getEmail(), getPassword()));

        Button btnSignUp = findViewById(R.id.signUpButton);
        btnSignUp.setOnClickListener(view-> presenter.onSignUpButtonPressed());
    }


    //switch between screens
    @Override
    public void showSignUpScreen() {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}
