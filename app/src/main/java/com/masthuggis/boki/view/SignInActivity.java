package com.masthuggis.boki.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.masthuggis.boki.R;
import com.masthuggis.boki.backend.FailureCallback;
import com.masthuggis.boki.backend.SuccessCallback;
import com.masthuggis.boki.model.DataModel;
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
        btnSignIn.setOnClickListener(view -> presenter.onSignInButtonPressed(getEmail(), getPassword(), new SuccessCallback() {
            @Override
            public void onSuccess() {
                Context context = getApplicationContext();
                CharSequence text = "Du är nu inloggad som " + DataModel.getInstance().getUserDisplayName();
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }
        }, new FailureCallback() {
            @Override
            public void onFailure() {
                Context context = getApplicationContext();
                CharSequence text = "Inloggning misslyckades.";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }));

        Button btnSignUp = findViewById(R.id.signUpButton);
        btnSignUp.setOnClickListener(view -> presenter.onSignUpButtonPressed());
    }


    //switch between screens
    @Override
    public void showSignUpScreen() {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void showProfileScreen() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        //TODO open Profilefragment instead of mainActivity
        finish();
    }
}
