package com.masthuggis.boki.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.masthuggis.boki.R;
import com.masthuggis.boki.model.DataModel;

/**
 * Settings page that will be used for program-wide settings.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Button signOutButton = findViewById(R.id.sign_out_button);
        if(DataModel.getInstance().isLoggedIn()){
            signOutButton.setVisibility(View.VISIBLE);
        }else{
            signOutButton.setVisibility(View.GONE);
        }
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataModel.getInstance().signOut();
            }
        });
    }
}
