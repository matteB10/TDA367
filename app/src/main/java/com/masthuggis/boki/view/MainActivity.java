package com.masthuggis.boki.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.masthuggis.boki.R;

/**
 * MainActivity is the primary view of the application. This is where the application will take you on launch.
 */
public class MainActivity extends AppCompatActivity {
    private Fragment homeFragment;
    private Fragment favoritesFragment;
    private Fragment profileFragment;
    private Fragment messagesFragment;
    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        homeFragment = new HomeFragment();
        activeFragment = homeFragment;
        /*
        favoritesFragment = new FavoritesFragment();
        profileFragment = new ProfileFragment();
        messagesFragment = new MessagesFragment();
        */
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
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
                    if (favoritesFragment == null)
                        favoritesFragment = new FavoritesFragment();
                    activeFragment = favoritesFragment;
                    break;
                case R.id.navigation_profile:
                    if (profileFragment == null)
                        profileFragment = new ProfileFragment();
                    activeFragment = profileFragment;
                    break;
                case R.id.navigation_messages:
                    if (messagesFragment == null)
                        messagesFragment = new MessagesFragment();
                    activeFragment = messagesFragment;
                    break;
                default:
                    if (homeFragment == null)
                        homeFragment = new HomeFragment();
                    activeFragment = homeFragment;
                    break;
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, activeFragment);
            transaction.commit();

        }
        return true;

    };

    @Override
    public void onBackPressed() {

    }
}

