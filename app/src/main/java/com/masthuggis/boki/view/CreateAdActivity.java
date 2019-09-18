package com.masthuggis.boki.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.CreateAdPresenter;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateAdActivity extends AppCompatActivity implements CreateAdPresenter.View {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    String currentImagePath;
    CreateAdPresenter presenter;

    ImageView imageViewDisplay;
    EditText title;
    EditText price;
    EditText author;
    EditText ISBN;
    EditText description;
    RadioGroup conditionToggleGroup;
    Button conditionGood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);
        presenter = new CreateAdPresenter(this);
        conditionToggleGroup = (RadioGroup) findViewById(R.id.conditionToggleGroup);
        conditionGood = (Button) findViewById(R.id.conditionGoodButton);
        conditionToggleGroup.check(conditionGood.getId());

        imageViewDisplay = (ImageView) findViewById(R.id.addImageView);


    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File imageFile = null;
            try {
                imageFile = createImageFile();
            } catch (Exception i) {
                System.out.println("error creating file");
            }
            if (imageFile != null) {
                Uri imageURI = FileProvider.getUriForFile(this, "com.masthuggis.boki.fileprovider",
                        imageFile);
                System.out.println(imageURI);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String photoFileName = "IMAGE_" + timeStamp + "_";
        File image = File.createTempFile(photoFileName, ".jpg", storageDir);
        currentImagePath = image.getAbsolutePath();
        return image;
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageViewDisplay = (ImageView) findViewById(R.id.addImageView);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap bitmap = decodeBitmap();
            setImageView(bitmap);
        }

    }

    private void setImageView(Bitmap bitmap) {
        imageViewDisplay.setImageBitmap(bitmap);
        //TODO: fix better solution for setting image in portrait
        //imageViewDisplay.setRotation(90);
    }

    private Bitmap decodeBitmap() {
        // Get the dimensions of the View
        int imageWidth = imageViewDisplay.getWidth();
        int imageHeight = imageViewDisplay.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options imageOptions = new BitmapFactory.Options();
        imageOptions.inJustDecodeBounds = true;

        int actualWidth = imageOptions.outWidth;
        int actualHeight = imageOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(actualWidth / imageWidth, actualHeight / imageHeight);

        // Decode the image file into a Bitmap sized to fill the View
        imageOptions.inJustDecodeBounds = false;
        imageOptions.inSampleSize = scaleFactor;
        Bitmap bitmap = BitmapFactory.decodeFile(currentImagePath, imageOptions);

        return bitmap;

    }




    private void setOnClickListeners(){
        setImageViewListener();

    }

    @Override
    public void deleteTag(String tag) {

    }

    private void setImageViewListener(){
        imageViewDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

    }

}

