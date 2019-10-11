package com.masthuggis.boki.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.masthuggis.boki.R;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.presenter.MainPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity is the primary view of the application. This is where the application will take you on launch.
 */

public class MainActivity extends AppCompatActivity implements MainPresenter.View {
    private MainPresenter presenter;
    private Fragment homeFragment;
    private Fragment favoritesFragment;
    private Fragment profileFragment;
    private Fragment chatFragment;
    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.presenter = new MainPresenter(this);
    }

    @Override
    public void showSignInScreen() {
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
    }

    @Override
    public void showMainScreen() {
        setupBottomTabNavigator();
        addFragmentsToViewHierachy();
    }

    /**
     * If back button is pressed the app exits. Is it better to show the previously active tab?
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void setupBottomTabNavigator() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    private void addFragmentsToViewHierachy() {
        FragmentManager fm = getSupportFragmentManager();
        initFragments();

        // Adds all fragments to MainActivity container but hides all but homeFragment.
        fm.beginTransaction().add(R.id.fragment_container, favoritesFragment).hide(favoritesFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, profileFragment).hide(profileFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, chatFragment).hide(chatFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, homeFragment).commit();
    }

    private void initFragments() {
        homeFragment = new HomeFragment();
        favoritesFragment = new FavoritesFragment();
        profileFragment = new ProfileFragment();
        chatFragment = new ChatFragment();
        activeFragment = homeFragment;
    }

    /**
     * This is a method handling the navigation between fractals which are parts of the view of the mainActivity class.
     * It does this through the defined ID:s of the different fragments.
     */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = menuItem -> {
        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                showFragment(homeFragment);
                break;
            case R.id.navigation_favorites:
                showFragment(favoritesFragment);
                break;
            case R.id.navigation_profile:
                showFragment(profileFragment);
                break;
            case R.id.navigation_new_ad:
                Intent intent = new Intent(this, CreateAdActivity.class);
                startActivity(intent);
                return true;
            case R.id.navigation_messages:
                showFragment(chatFragment);
                break;
            default:
                return false;
        }
        return true;
    };

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().hide(activeFragment).show(fragment).commit();
        activeFragment = fragment;
    }

}

