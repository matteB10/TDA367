package com.masthuggis.boki.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.widget.TextView;

import com.masthuggis.boki.R;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // TODO: get book from repositiory (presenter will do it)
        // TODO: setup MVP structure
        Intent intent = getIntent();
        long advertID = intent.getExtras().getLong("advertID");

        TextView testTextView = findViewById(R.id.detailsID);
        testTextView.setText(Long.toString(advertID));

    }
}
