package com.masthuggis.boki.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.masthuggis.boki.R;
import com.masthuggis.boki.backend.BackendDataFetcher;

/**
 * MainActivity is the main activity of the application. It is the center which everything else builds upon.
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        BackendDataFetcher backendDataFetcher = new BackendDataFetcher();
        backendDataFetcher.loadJSONFromFB();

    }

    /**
     * @navListener Handles swapping between fragments when navigating through the bottom panel. It does this
     * by using a OnNavigationItemSelectedListener which uses the items ID and a switch case.
     */

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            switch (menuItem.getItemId()) {
                case R.id.navigation_favorites:
                    selectedFragment = new FavoritesFragment();
                    break;
                case R.id.navigation_profile:
                    selectedFragment = new ProfileFragment();
                    break;
                case R.id.navigation_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.navigation_new_ad:
                    selectedFragment = new NewAdFragment();
                    break;
                case R.id.navigation_messages:
                    selectedFragment = new MessagesFragment();
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;


        }

    };


}
