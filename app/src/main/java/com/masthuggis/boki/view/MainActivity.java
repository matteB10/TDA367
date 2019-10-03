package com.masthuggis.boki.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.masthuggis.boki.R;

/**
 * MainActivity is the primary view of the application. This is where the application will take you on launch.
 */
public class MainActivity extends AppCompatActivity {
    Fragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }


    /**
     * This is a method handling the navigation between fractals which are parts of the view of the mainActivity class.
     * It does this through the defined ID:s of the different fragments.
     */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = menuItem -> {
        if (menuItem.getItemId() == R.id.navigation_new_ad) {
            Intent intent = new Intent(this, CreateAdActivity.class);
            startActivity(intent);
        } else {
            switch (menuItem.getItemId()) {
                case R.id.navigation_favorites:
                    selectedFragment = new FavoritesFragment();
                    break;
                case R.id.navigation_profile:
                    selectedFragment = new ProfileFragment();
                    break;
                case R.id.navigation_new_ad:
                    Intent intent = new Intent(MainActivity.this, CreateAdActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_messages:
                    selectedFragment = new MessagesFragment();
                    break;
                default:
                    selectedFragment = new HomeFragment();
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        }
            return true;

        }
        ;

        @Override
        public void onBackPressed () {

        }
    }

