package com.masthuggis.boki.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.masthuggis.boki.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupTemporaryNavigationToStartPage();
    }

    private void setupTemporaryNavigationToStartPage() {
        Button goToStartButton = findViewById(R.id.goToHomeButton);
        goToStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToStartPage();
            }
        });
    }

    private void navigateToStartPage() {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }
}
