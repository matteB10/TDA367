package com.masthuggis.boki.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.EditAdPresenter;
import com.masthuggis.boki.utils.StylingHelper;
import com.masthuggis.boki.utils.UniqueIdCreator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.masthuggis.boki.view.CreateAdActivity.REQUEST_IMAGE_CAPTURE;

public class EditAdActivity extends AppCompatActivity implements EditAdPresenter.View {

    private EditAdPresenter presenter;
    private Button saveBtn;
    private EditText title;
    private EditText price;
    private EditText description;
    private File currentImageFile;

    private List<Button> preDefTagButtons = new ArrayList<>();
    private List<Button> userDefTagButtons = new ArrayList<>();



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

    private void setListeners() {
        setTitleListener();
        setPriceListener();
        setDescriptionListener();
        setSaveAdListener();
        setNewImageListener();
        setConditionGroupListener();
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

    private void setNewImageListener(){
        ImageView image = findViewById(R.id.bookImageView);
        image.setOnClickListener(view -> dispatchTakePictureIntent());
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                currentImageFile = createImageFile();
            } catch (Exception i) {
                System.out.println("error creating file");
            }
            if (currentImageFile != null) {
                Uri imageURI = FileProvider.getUriForFile(this,
                        "com.masthuggis.boki.fileprovider", currentImageFile);
                System.out.println(imageURI);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        String photoFileName = "IMG_" + UniqueIdCreator.getUniqueID();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(photoFileName, ".jpg", storageDir);
        return image;
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
    //condition buttons --------------------------------------------
    private void setConditionGroupListener() {
        Button conditionGoodButton = findViewById(R.id.conditionGoodButton);
        Button conditionNewButton = findViewById(R.id.conditionNewButton);
        Button conditionOKButton = findViewById(R.id.conditionOkButton);


        conditionGoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.conditionChanged(R.string.conditionGood);

            }
        });
        conditionNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.conditionChanged(R.string.conditionNew);
            }
        });
        conditionOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.conditionChanged(R.string.conditionOk);
            }
        });
    }

    @Override
    public void styleConditionButtonPressed(int conditionButtonPressed) {
        Button conditionButton = new Button(this);
        switch (conditionButtonPressed) {
            case R.string.conditionNew:
                conditionButton = findViewById(R.id.conditionNewButton);
                break;
            case R.string.conditionGood:
                conditionButton = findViewById(R.id.conditionGoodButton);
                break;
            case R.string.conditionOk:
                conditionButton = findViewById(R.id.conditionOkButton);
                break;
        }
        conditionButton.setElevation(StylingHelper.getDPToPixels(this, 4));
    }



    //getting old information ---------------------------------------
    @Override
    public void setTitle(String name) {
        TextView title = findViewById(R.id.titleEditText);
        title.setText("" + name);
    }

    @Override
    public void setPrice(long price) {
        TextView currentPrice = findViewById(R.id.priceEditText);
        currentPrice.setText(String.valueOf(price));
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