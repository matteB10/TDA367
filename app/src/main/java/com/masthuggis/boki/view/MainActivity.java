package com.masthuggis.boki.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.masthuggis.boki.R;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.presenter.MainPresenter;

/**
 * MainActivity is the primary view of the application. This is where the application will take you on launch.
 */
public class MainActivity extends AppCompatActivity implements MainPresenter.View {
    private MainPresenter presenter;
    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkNavToast();
        setupBottomTabNavigator();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

        this.presenter = new MainPresenter(this);
    }

    @Override
    public void showLoginScreen() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
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

            switch(menuItem.getItemId()){
                case R.id.navigation_favorites:
                    activeFragment = new FavoritesFragment();
                    break;
                case R.id.navigation_profile:
                    activeFragment = new ProfileFragment();
                    break;
                case R.id.navigation_new_ad:
                    Intent intent = new Intent(MainActivity.this,CreateAdActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_messages:
                    activeFragment = new MessagesFragment();
                    break;
                default:
                    activeFragment = new HomeFragment();

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,activeFragment).commit();
            return true;
        }
        return true;
    };

}

