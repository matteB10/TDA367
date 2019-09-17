package com.masthuggis.boki.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.masthuggis.boki.R;
import com.masthuggis.boki.backend.BackendDataFetcher;
import com.masthuggis.boki.model.Book;

public class MainActivity extends AppCompatActivity {

 //   private BackendDataFetcher backendDataFetcher = new BackendDataFetcher();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav =findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
      /*  backendDataFetcher.addNewBook(new Book("testTitle","testAuthor",1,666,1234567890,
                1337, Book.Condition.GOOD,null,null));*/

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;

            switch(menuItem.getItemId()){
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
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
            return true;


        }

    };


}
