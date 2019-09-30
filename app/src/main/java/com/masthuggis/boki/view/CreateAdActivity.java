package com.masthuggis.boki.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.masthuggis.boki.R;
import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.presenter.CreateAdPresenter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * An acitivity for creating a new advertisement
 * through user input.
 */

public class CreateAdActivity extends AppCompatActivity implements CreateAdPresenter.View {


    static final int REQUEST_IMAGE_CAPTURE = 1;

    private File currentImageFile;
    private CreateAdPresenter presenter;

    private ImageView imageViewDisplay;
    private EditText title;
    private EditText price;
    private EditText description;
    private Button conditionNewButton;
    private Button conditionGoodButton;
    private Button conditionOKButton;
    private Button publishAdButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);
        presenter = new CreateAdPresenter(this);
        imageViewDisplay = findViewById(R.id.addImageView);
        //imageViewDisplay.setImageBitmap(BitmapFactory.decodeFile(currentImageFile.getPath()));
        disablePublishAdButton();
        setListeners();


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
                Uri imageURI = FileProvider.getUriForFile(this, "com.masthuggis.boki.fileprovider",
                        currentImageFile);
                System.out.println(imageURI);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    /**
     * Creates an empty file and specifies unique file name.
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String photoFileName = "IMAGE_" + timeStamp + "_";
        File image = File.createTempFile(photoFileName, ".jpg", storageDir);
        return image;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageViewDisplay = findViewById(R.id.addImageView);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = compressBitmap();
            setImageView(bitmap);
            presenter.imageFileChanged(currentImageFile);
        }
    }

    private void setImageView(Bitmap bitmap) {
        imageViewDisplay.setImageBitmap(bitmap);
    }

    public void imageFileChanged(File image) {
        imageViewDisplay.setImageBitmap(BitmapFactory.decodeFile(image.getPath()));
    }

    /**
     * Helper method to decode bitmap
     *
     * @return
     */
    //Want to replace this method with one that compresses the images
    private Bitmap decodeBitmap() {
        int imageWidth = imageViewDisplay.getWidth();
        int imageHeight = imageViewDisplay.getHeight();

        BitmapFactory.Options imageOptions = new BitmapFactory.Options();
        imageOptions.inJustDecodeBounds = true;

        int actualWidth = imageOptions.outWidth;
        int actualHeight = imageOptions.outHeight;

        int scaleFactor = Math.min(actualWidth / imageWidth, actualHeight / imageHeight);

        // Decode the image file into a Bitmap sized to fill the View
        imageOptions.inJustDecodeBounds = false;
        imageOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(currentImageFile.getPath(), imageOptions);
        //ByteArrayOutputStream stream = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);

        //byte[] byteArray = stream.toByteArray();
        //Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        return bitmap;
    }

    private Bitmap compressBitmap() {
        BitmapFactory.Options imageOptions = new BitmapFactory.Options();
        imageOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(currentImageFile.getPath());
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,200,200,true);
        return scaledBitmap;
    }

    private void setListeners() {
        setImageViewListener();
        setTitleListener();
        setPriceListener();
        setDescriptionListener();
        setCreateAdvertListener();
        setConditionGroupListener();
    }

    private void setConditionGroupListener() {
        conditionGoodButton = findViewById(R.id.conditionGoodButton);
        conditionGoodButton.setOnClickListener(view -> {
            //TODO FIX STYLING
            presenter.conditionChanged(Advert.Condition.GOOD);
            conditionGoodButton.setPressed(true);
            conditionOKButton.setPressed(false);
            conditionNewButton.setPressed(false);
        });
        conditionNewButton = findViewById(R.id.conditionNewButton);
        conditionNewButton.setOnClickListener(view -> {
            presenter.conditionChanged(Advert.Condition.NEW);
            conditionNewButton.setPressed(true);
            conditionGoodButton.setPressed(false);
            conditionOKButton.setPressed(false);
        });
        conditionOKButton = findViewById(R.id.conditionOkButton);
        conditionOKButton.setOnClickListener(view -> {
            presenter.conditionChanged(Advert.Condition.OK);
            conditionOKButton.setPressed(true);
            conditionGoodButton.setPressed(false);
            conditionNewButton.setPressed(false);
        });
    }

    @Override
    public void enablePublishButton() {
        publishAdButton.setEnabled(true);
        publishAdButton.setBackgroundColor(getResources().getColor(R.color.colorTeal));

    }


    private void disablePublishAdButton() {
        publishAdButton = findViewById(R.id.publishAdButton);
        publishAdButton.setEnabled(false);
        publishAdButton.setBackgroundColor(getResources().getColor(R.color.colorGreyLight));
    }

    private void setImageViewListener() {
        imageViewDisplay.setOnClickListener(view -> dispatchTakePictureIntent());

    }

    private void setTitleListener() {
        title = (EditText) findViewById(R.id.titleEditText);
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
        price = (EditText) findViewById(R.id.priceEditText);
        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.priceChanged(price.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setDescriptionListener() {
        description = (EditText) findViewById(R.id.descriptionEditText);
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

    private void setCreateAdvertListener() {
        publishAdButton = findViewById(R.id.publishAdButton);
        publishAdButton.setOnClickListener(view -> {
            Intent intent = new Intent(CreateAdActivity.
                    this, DetailsActivity.class);
            intent.putExtra("advertID", presenter.getId());
            presenter.publishAdvert();
            startActivity(intent);
        });
    }
}
