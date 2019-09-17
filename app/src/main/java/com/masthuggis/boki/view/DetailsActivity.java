package com.masthuggis.boki.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.widget.TextView;

import com.masthuggis.boki.R;
import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.model.Advertisement;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // TODO: get book from repositiory (presenter will do it)
        // TODO: setup MVP structure
        Intent intent = getIntent();
        String advertID = intent.getExtras().getString("advertID");
        Advertisement ad = Repository.getInstance().getAdFromId(advertID);


        TextView testTextView = findViewById(R.id.detailsID);
        testTextView.setText((advertID));

    }
}
