package com.masthuggis.boki.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.masthuggis.boki.R;
import com.masthuggis.boki.injectors.DependencyInjector;
import com.masthuggis.boki.presenter.MainPresenter;

/**
 * MainActivity is the primary view of the application. This is where the application will take you on launch.
 * Its main purpose is to handle the different tabs.
 * Used by CreateAdActivity, DetailsActivity, SignInActivity and SignUpActivity.
 * Written by masthuggis.
 */
public class MainActivity extends AppCompatActivity implements MainPresenter.View{
    private MainPresenter presenter;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        this.presenter = new MainPresenter(this, DependencyInjector.injectDataModel());
        favouritesNavigationCheck();
        displayToastMessageIfRequestWasReceived();
    }

    private void displayToastMessageIfRequestWasReceived() {
        String toastKey = getString(R.string.putExtraToastKey);
        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle.get(toastKey) != null){
                displayToastMessage(bundle.getString(toastKey));
            }
        }
    }

    private void favouritesNavigationCheck() {
        boolean navigateToFavourites = getIntent().getBooleanExtra("toFavourites", false);
        presenter.init(navigateToFavourites);
    }

    private void displayToastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSignInScreen() {
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        startActivity(intent);
    }

    @Override
    public void showMainScreen() {
        loadFragment(new HomeFragment());
        setupBottomTabNavigator();
    }

    @Override
    public void showFavouritesScreen() {
        setupBottomTabNavigator();
        showFavouritesViewAndIcon();
    }

    @Override
    public void showBottomNavBar() {
        //BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBottomNavBar() {
        //BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.GONE);
    }

    private void showFavouritesViewAndIcon() {
        View view = bottomNavigationView.findViewById(R.id.navigation_favorites);
        view.performClick();
    }

    /**
     * If back button is pressed the app exits. Is it better to show the previously active tab?
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void setupBottomTabNavigator() {
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
    }

    /**
     * This is a method handling the navigation between fractals which are parts of the view of the mainActivity class.
     * It does this through the defined ID:s of the different fragments.
     */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = menuItem -> {
        Fragment fragment = null;
        switch (menuItem.getItemId()) {
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;
            case R.id.navigation_favorites:
                fragment = new FavoritesFragment();
                break;
            case R.id.navigation_profile:
                fragment = new ProfileFragment();
                break;
            case R.id.navigation_new_ad:
                Intent intent = new Intent(getApplicationContext(), CreateAdActivity.class);
                startActivity(intent);
                return true;
            case R.id.navigation_messages:
                fragment = new ChatFragment();
                break;
            default:
                break;
        }
        return loadFragment(fragment);
    };

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commitAllowingStateLoss();
            return true;
        }
        return false;
    }

    /*
    @Override
    public void onFiltersChanged(Bundle args) {
        Fragment fragment = new HomeFragment();
        fragment.setArguments(args);
        loadFragment(fragment);
    }*/

}

