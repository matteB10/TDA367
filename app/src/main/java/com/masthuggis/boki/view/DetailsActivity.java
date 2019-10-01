package com.masthuggis.boki.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.masthuggis.boki.R;
import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.presenter.DetailsPresenter;
import com.masthuggis.boki.utils.iConditionable;

/**
 * The view showing details of a specific advertisement.
 *
 */
public class DetailsActivity extends AppCompatActivity implements DetailsPresenter.View {
    private DetailsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        String advertID = intent.getExtras().getString("advertID");
        if (advertID != null) {
            presenter = new DetailsPresenter(this, advertID);
        }
        String uniqueOwnerID= DataModel.getInstance().getAdFromAdID(advertID).getUniqueOwnerID();

        Button contactOwnerButton = findViewById(R.id.contactOwnerButton);
        contactOwnerButton.setOnClickListener(view -> {
            //TODO HÄR SKA CHATTEN ÖPPNAS TYP
            if (contactOwnerButton.getText().equals("Skicka meddelande till" + uniqueOwnerID)) {
                presenter.createNewChat(uniqueOwnerID);
                //   Intent intent = new Intent(Intent.ACTION_DIAL);
                // intent.setData(Uri.parse(contactOwnerButton.getText().toString()));
                // startActivity(intent);
            } else {
                contactOwnerButton.setText("Skicka meddelande till" + uniqueOwnerID);
            }
        });
    }

    @Override
    public void setName(String name) {
        TextView nameTextView = findViewById(R.id.detailsName);
        nameTextView.setText(name);
    }

    @Override
    public void setPrice(long price) {
        TextView textView = findViewById(R.id.detailsPrice);
        textView.setText(Long.toString(price) + " kr");
    }

    @Override
    public void setDate(String date) {
        TextView textView = findViewById(R.id.datePublishedTextView);
        textView.setText(date);
    }

    @Override
    public void setImageUrl(String url) {
        // TODO: fetch img, cache it and set it
        ImageView imageView = (ImageView) findViewById(R.id.detailsImage);
        imageView.setImageURI(Uri.parse(url));
    }

    @Override
    public void setDescription(String description) {
        TextView textView = findViewById(R.id.details_description);
        textView.setText(description);
    }

    @Override
    public void setCondition(int text, int drawable) {
        TextView textView = findViewById(R.id.conditionTextView);
        textView.setText(text);
        ConstraintLayout layout = findViewById(R.id.conditionConstraintLayout);
        layout.setBackground(getDrawable(drawable));
    }
    @Override
    public Context getContext(){
        return this;
    }



}
