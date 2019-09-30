package com.masthuggis.boki.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.masthuggis.boki.R;

/**
 * Settings page that will be used for program-wide settings.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
}
