package com.masthuggis.boki.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.HomePresenter;

public class HomeActivity extends AppCompatActivity implements HomePresenter.View {

    private HomePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.presenter = new HomePresenter(this);
    }

    @Override
    public void showLoadingScreen() {
        // TODO: Show loading animation when data is being fetched from database
    }

    @Override
    public void hideLoadingScreen() {
        // TODO: Hide loading animation (data is now loaded from the database)
    }

    @Override
    public void setProductItems() {
        // TODO: When model is defined, will set the product items to display
    }
}
