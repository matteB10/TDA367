package com.masthuggis.boki.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.EditAdPresenter;

public class EditAdActivity extends AppCompatActivity implements EditAdPresenter.View {

    private EditAdPresenter presenter;
    private Button removeBtn;
    private Button saveBtn;
    private EditText title;
    private EditText price;
    private EditText description;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_ad_activity);

        Intent intent = getIntent();
        String advertID = intent.getExtras().getString("advertID");

        presenter = new EditAdPresenter(this, advertID);
        setUpBtns();
        setListeners();
    }



    private void setUpBtns() {
        Button removeBtn = findViewById(R.id.removeAdBtn);
        removeBtn.setOnClickListener(view -> presenter.removeAdBtnPressed());
    }

    //set listeners-------------------------------------------------------------

    private void setListeners() {
        setTitleListener();
        setPriceListener();
        setDescriptionListener();
        setSaveAdListener();
    }

    private void setTitleListener() {
        title = findViewById(R.id.titleEditText);
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.titleChanged(title.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void setPriceListener(){
        price = findViewById(R.id.priceEditText);
        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.priceChange(price.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setDescriptionListener() {
        description = findViewById(R.id.descriptionEditText);
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.descriptionChanged(description.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void setSaveAdListener(){
        saveBtn = findViewById(R.id.saveAdBtn);
        saveBtn.setOnClickListener(view -> {
            Intent intent = new Intent(EditAdActivity.this, DetailsActivity.class);
            intent.putExtra("advertID", presenter.getID());
            startActivity(intent);
            finish();

        });
    }


    //getting old information ---------------------------------------
    @Override
    public void setTitle(String name) {
        TextView title = findViewById(R.id.titleEditText);
        title.setText(name);
    }

    @Override
    public void setPrice(long price) {
        TextView currentPrice = findViewById(R.id.priceEditText);
        currentPrice.setText(price + " kr");
    }

    @Override
    public void setImageUrl(String url) {
        ImageView imageView = (ImageView) findViewById(R.id.bookImageView);
        Glide.with(this).load(url).into(imageView);
    }

    @Override
    public void setDescription(String description) {
        TextView currentDescription = findViewById(R.id.descriptionEditText);
        currentDescription.setText(description);
    }
}