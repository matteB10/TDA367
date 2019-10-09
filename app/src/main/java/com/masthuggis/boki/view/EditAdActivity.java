package com.masthuggis.boki.view;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class EditAdActivity extends AppCompatActivity implements EditAdPresenter.View {

    private EditAdPresenter presenter;
    private CheckBox compatibilityCB;
    private Button saveBtn;
    private EditText title;
    private EditText price;
    private EditText description;
    private File currentImageFile;
    private ImageView bookImageView;

    private List<Button> preDefTagButtons = new ArrayList<>();
    private List<Button> userDefTagButtons = new ArrayList<>();
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_CROP = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_ad_activity);
        Intent intent = getIntent();
        String advertID = intent.getExtras().getString("advertID");
        presenter = new EditAdPresenter(this, advertID);

        setListeners();
        compatibilityCB = findViewById(R.id.compatabilityCB);
    }

    private void setRemoveBtn() {
        Button removeBtn = findViewById(R.id.removeAdBtn);
        removeBtn.setOnClickListener(view -> {
            presenter.removeAdBtnPressed();
            Intent intent = new Intent(EditAdActivity.this, MainActivity.class);
            intent.putExtra("advertID", presenter.getID());
            startActivity(intent);
            finish();
        });
    }

    private void setSaveAdListener() {
        saveBtn = findViewById(R.id.saveAdBtn);
        saveBtn.setOnClickListener(view -> {
            try {
                presenter.saveAdBtnPressed( createImageFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(EditAdActivity.this, DetailsActivity.class);
            intent.putExtra("advertID", presenter.getID());
            startActivity(intent);
            finish();
        });
    }

    private void setListeners() {
        setTitleListener();
        setPriceListener();
        setDescriptionListener();
        setSaveAdListener();
        setNewImageListener();
        setConditionGroupListener();
        setRemoveBtn();
        setImageViewListener();
    }

    private void setImageViewListener() {
        bookImageView = findViewById(R.id.bookImageView);
        bookImageView.setOnClickListener(view -> dispatchTakePictureIntent());

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

    private void setPriceListener() {
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

    private void setNewImageListener() {
        ImageView bookImageView = findViewById(R.id.bookImageView);
        bookImageView.setOnClickListener(view -> dispatchTakePictureIntent());
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                currentImageFile = createImageFile();
               //TODO WHHYYYY?
                presenter.imageUpdate(currentImageFile);
            } catch (Exception i) {
                System.out.println("error creating file");
            }
            if (currentImageFile != null) {
                Uri imageURI = FileProvider.getUriForFile(this,
                        "com.masthuggis.boki.fileprovider", currentImageFile);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bookImageView = findViewById(R.id.bookImageView);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) { //When image has been taken
            if (isEmulator()) {
                compressOnEmulator();
            } else {
                Uri.Builder builder = new Uri.Builder();
                builder.encodedPath(currentImageFile.getAbsolutePath());
                Uri imageUri = builder.build(); //Build Uri
                cropImage(imageUri);
            }
        } else if (requestCode == REQUEST_IMAGE_CROP) {
            if (data != null) {
                Bundle extras = data.getExtras();
                Bitmap croppedBitmap = extras.getParcelable("data"); //Crops image to given resolution and aspect-ratio
                setImageView(croppedBitmap);
            }
        }
    }
    private boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT)
                || compatibilityCB.isChecked(); //TODO fix crop activity so all users can use it when uploading adverts, this is temporary fix
    }
    private void cropImage(Uri imageUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(imageUri, "image/*");
            cropIntent.putExtra("crop", true);
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 220); //TODO set resolution and aspect ratio so image looks acceptable
            cropIntent.putExtra("outputY", 300);
            cropIntent.putExtra("return-data", true);
            startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);
        } catch (ActivityNotFoundException exception) {
            exception.printStackTrace();
        }
    }
    private void setImageView(Bitmap bitmap) {
        bookImageView.setImageBitmap(bitmap);
    }
    private void compressOnEmulator() {
        Bitmap bitmap = compressBitmap();
        setImageView(bitmap);
        try {
            OutputStream out = new FileOutputStream(currentImageFile.getPath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private Bitmap compressBitmap() {
        BitmapFactory.Options imageOptions = new BitmapFactory.Options();
        imageOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(currentImageFile.getPath());
        return Bitmap.createScaledBitmap(bitmap, 800, 800, false); //TODO is this even necessary?
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
    @Override
    public void imageUpdate(File currentImageFile) {
        presenter.imageUpdate(currentImageFile);
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
        currentPrice.setText(String.valueOf(price) + " kr");
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

    //
}