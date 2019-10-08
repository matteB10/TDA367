package com.masthuggis.boki.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.masthuggis.boki.R;
import com.masthuggis.boki.model.DataModel;

import javax.net.ssl.SNIServerName;

/**
 * MainActivity is the primary view of the application. This is where the application will take you on launch.
 */
public class MainActivity extends AppCompatActivity {
    private Fragment homeFragment = new HomeFragment();
    private Fragment favoritesFragment = new FavoritesFragment();
    private Fragment profileFragment = new ProfileFragment();
    private Fragment messagesFragment = new ChatFragment();
    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkNavToast();
        setupBottomTabNavigator();
        addFragmentsToViewHierachy();
    }

    private void checkNavToast() {
        if (getIntent().getBooleanExtra("toast", false)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Din annons har lagts upp!", Toast.LENGTH_LONG);
            toast.show();
            getIntent().putExtra("toast",false); //Reset boolean so it only shows once
        }
    }

    private void setupBottomTabNavigator() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    private void addFragmentsToViewHierachy() {
        FragmentManager fm = getSupportFragmentManager();
        activeFragment = homeFragment;

        // Adds all fragments to MainActivity container but hides all but homeFragment.
        fm.beginTransaction().add(R.id.fragment_container, favoritesFragment).hide(favoritesFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, profileFragment).hide(profileFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, messagesFragment).hide(messagesFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, homeFragment).commit();
    }


    /**
     * This is a method handling the navigation between fractals which are parts of the view of the mainActivity class.
     * It does this through the defined ID:s of the different fragments.
     */
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = menuItem -> {

        if (menuItem.getItemId() == R.id.navigation_new_ad) {
            if (!DataModel.getInstance().isLoggedIn()) {
                Context context = getApplicationContext();
                CharSequence text = "Du måste logga in för att kunna lägga upp en ny annons!";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                return false;
            }
            Intent intent = new Intent(this, CreateAdActivity.class);
            startActivity(intent);

        } else {

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
                    Intent intent = new Intent(MainActivity.this, CreateAdActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_messages:
                    showFragment(messagesFragment);
                    break;
                default:
                    return false;
            }
        }
        return true;
    };

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().hide(activeFragment).show(fragment).commit();
        activeFragment = fragment;
    }
}

