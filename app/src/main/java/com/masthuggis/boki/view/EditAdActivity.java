package com.masthuggis.boki.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.EditAdPresenter;

public class EditAdActivity extends AppCompatActivity implements EditAdPresenter.View {

    private EditAdPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_ad_activity);

        Intent intent = getIntent();
        String advertID = intent.getExtras().getString("advertID");

        presenter = new EditAdPresenter(this, advertID);
        setUpBtns();

    }

   private void setUpBtns(){
       Button removeBtn = findViewById(R.id.removeAdBtn);
       removeBtn.setOnClickListener(view -> presenter.removeAdBtnPressed());

       Button editTitle = findViewById(R.id.editTitleBtn);
       editTitle.setOnClickListener(view -> presenter.editTitleBtnPressed());
   }

    public void backToHomeview(){
        Intent intent = new Intent(EditAdActivity.this, HomeFragment.class);
        startActivity(intent);
    }

    @Override
    public void getFieldForTitle() {
        EditText titleEditText = findViewById(R.id.titleEditText);
        titleEditText.setVisibility(View.VISIBLE);
        Button editTitleBtn = findViewById(R.id.editTitleBtn);
        editTitleBtn.setVisibility(View.GONE);

    }
}