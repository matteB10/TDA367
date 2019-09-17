package com.masthuggis.boki.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.masthuggis.boki.R;
import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.presenter.DetailsPresenter;

public class DetailsActivity extends AppCompatActivity implements DetailsPresenter.View {
    private DetailsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        String advertID = intent.getExtras().getString("advertID");
        if (advertID != null || advertID == "") {
            presenter = new DetailsPresenter(this, advertID);
        }
    }

    @Override
    public void setName(String name) {
        TextView nameTextView = findViewById(R.id.detailsName);
        nameTextView.setText(name);
    }

    @Override
    public void setPrice(int price) {
        TextView textView = findViewById(R.id.detailsPrice);
        textView.setText(Integer.toString(price) + " kr");
    }

    @Override
    public void setImageUrl(String url) {
        // TODO: fetch img, cache it and set it
    }
}
