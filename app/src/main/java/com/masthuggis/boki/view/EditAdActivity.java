package com.masthuggis.boki.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.EditAdPresenter;

public class EditAdActivity extends AppCompatActivity implements EditAdPresenter.View {

    private EditAdPresenter presenter;

    private ImageView bookImageView;
    private EditText titleEditText;
    private EditText priceEditText;
    private EditText descriptionEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_ad_activity);

        Intent intent = getIntent();
        String advertID = intent.getExtras().getString("advertID");
        presenter = new EditAdPresenter(this, advertID);

        setListeners();
        setBtns();
    }


    private void setBtns(){
        Button removeBtn = findViewById(R.id.removeAdBtn);
        removeBtn.setOnClickListener(view -> presenter.removeAdPressed());

        Button saveBtn = findViewById(R.id.saveBtn);

    }

//----------------------------------------------------------------------------

    private void setListeners(){
        setTitleListener();
    }



    private void setTitleListener() {
        EditText title = findViewById(R.id.titleEditText);
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



    //----------------------------------------------------------------------------
    @Override
    public void setName(String name) {

    }

    @Override
    public void setImageUrl(String url) {

    }

    @Override
    public void setDescription(String description) {

    }
}