package com.masthuggis.boki.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.masthuggis.boki.R;
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
        Button contactOwnerButton = findViewById(R.id.contactOwnerButton);
        contactOwnerButton.setOnClickListener(view -> {
            //TODO HÄR SKA CHATTEN ÖPPNAS TYP
            if (contactOwnerButton.getText() == "+46738083104") {
             //   Intent intent = new Intent(Intent.ACTION_DIAL);
               // intent.setData(Uri.parse(contactOwnerButton.getText().toString()));
               // startActivity(intent);
            } else {
                contactOwnerButton.setText("+46738083104");
            }
        });
    }

    @Override
    public void setName(String name) {
        TextView nameTextView = findViewById(R.id.detailsName);
        nameTextView.setText(name);
    }

    @Override
    public void setPrice(int price) {
        TextView textView = findViewById(R.id.detailsPrice);
        textView.setText(price + " kr");
    }

    @Override
    public void setImageUrl(String url) {
        // TODO: fetch img, cache it and set it
        ImageView imageView = (ImageView)findViewById(R.id.detailsImage);
        imageView.setImageURI(Uri.parse(url));
    }

    @Override
    public void setDescription(String description) {
        TextView textView = findViewById(R.id.details_description);
        textView.setText(description);
    }
    @Override
    public void setCondition(String condition){
        TextView textView = findViewById(R.id.conditionTextView);
        textView.setText(condition);
        //TODO:fix change color background
    }



}
