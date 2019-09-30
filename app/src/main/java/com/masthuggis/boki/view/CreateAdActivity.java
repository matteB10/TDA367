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
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.masthuggis.boki.R;
import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.presenter.CreateAdPresenter;
import com.masthuggis.boki.utils.UniqueIdCreator;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An acitivity for creating a new advertisement
 * through user input.
 */

public class CreateAdActivity extends AppCompatActivity implements CreateAdPresenter.View {


    static final int REQUEST_IMAGE_CAPTURE = 1;

    private final List<Button> tagButtons = new ArrayList<>();
    private final List<String> preDefTags = new ArrayList<>();
    private String currentImagePath;
    private File currentImageFile;
    private CreateAdPresenter presenter;

    private ImageView imageViewDisplay;
    private EditText title;
    private EditText price;
    private EditText description;
    private Button publishAdButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);
        presenter = new CreateAdPresenter(this);
        imageViewDisplay = findViewById(R.id.addImageView);
        //imageViewDisplay.setImageBitmap(BitmapFactory.decodeFile(currentImageFile.getPath()));
        disablePublishAdButton();
        renderTagButtons();
        setListeners();
        updateDataFromModel();

    }

    private void updateDataFromModel() {
        if (presenter.getImgFile() != null) {
            Bitmap myBitmap = BitmapFactory.decodeFile(presenter.getImgFile().getAbsolutePath());
            imageViewDisplay.setImageBitmap(myBitmap);
        }
        title.setText(presenter.getTitle());
        description.setText(presenter.getDescription());
        if (presenter.getIsValidPrice()) {
            price.setText("" + presenter.getPrice());
        } else {
            showInputPrompt();
        }
    }

    private void showInputPrompt() {
        price.setText("");
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
        String photoFileName = "IMG_" + UniqueIdCreator.getUniqueID();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
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
        return bitmap;
    }

    private Bitmap compressBitmap() {
        BitmapFactory.Options imageOptions = new BitmapFactory.Options();
        imageOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(currentImageFile.getPath());
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true); //TODO change 200x200 to preferable resolution
        return scaledBitmap;
    }

    private void setListeners() {
        setImageViewListener();
        setTitleListener();
        setPriceListener();
        setDescriptionListener();
        setCreateAdvertListener();
        setConditionGroupListener();
        setPreDefTagsListeners();

    }

    private void setConditionGroupListener() {
        Button conditionGoodButton = findViewById(R.id.conditionGoodButton);
        conditionGoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO FIX STYLING
                presenter.conditionChanged(Advert.Condition.GOOD);

            }
        });
        Button conditionNewButton = findViewById(R.id.conditionNewButton);
        conditionNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.conditionChanged(Advert.Condition.NEW);
            }
        });
        Button conditionOKButton = findViewById(R.id.conditionOkButton);
        conditionOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.conditionChanged(Advert.Condition.OK);
            }
        });
    }

    @Override
    public void enablePublishButton() {
        publishAdButton.setEnabled(true);
        publishAdButton.setBackground(getResources().getDrawable(R.drawable.primary_button));

    }


    private void disablePublishAdButton() {
        publishAdButton = findViewById(R.id.publishAdButton);
        publishAdButton.setEnabled(false);
        publishAdButton.setBackground(getResources().getDrawable(R.drawable.disabled_primary_button));
    }

    private void setImageViewListener() {
        imageViewDisplay = findViewById(R.id.addImageView);
        imageViewDisplay.setOnClickListener(view -> dispatchTakePictureIntent());

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

    private void setPreDefTagsListeners() {
        for (Button btn : tagButtons) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.tagsChanged(btn.getText().toString());
                    btn.requestFocusFromTouch();
                    if (presenter.isTagPressed(btn.getText().toString()))
                        btn.setBackgroundResource(R.drawable.subject_tag_shape_pressed);
                    else
                        btn.setBackgroundResource(R.drawable.subject_tag_shape_normal);

                }
            });
        }
    }

    private void setUserDefTagListener() {
        EditText userDefTag = findViewById(R.id.tagsEditText);
        userDefTag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = charSequence.toString();
                presenter.tagsChanged(str);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    /**
     * Reads pre defined subject strings from resources,
     * saves all string in a list.
     */
    private void initPreDefTagStrings() {
        String[] strArr = getResources().getStringArray(R.array.preDefSubjectTags);
        for (String str : strArr)
            preDefTags.add(str);
    }

    /**
     * Create buttons with pre-defined subject tags.
     */
    private void createTagButtons() {
        for (String str : preDefTags) {
            Button btn = new Button(this);
            btn.setText(str);
            setTagStyling(btn);
            tagButtons.add(btn);
        }
    }

    /**
     * Populates view with subject tag buttons.
     */
    private void populateTagsLayout() {
        //TODO: fix styling of button/tableRow height
        TableLayout tableLayout = findViewById(R.id.preDefTagsTableLayout);
        tableLayout.setStretchAllColumns(true);
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new TableRow.LayoutParams());
        int i = 1;

        for (Button btn : tagButtons) {
            tableRow.addView(btn);
            if (i % 3 == 0) {
                tableLayout.addView(tableRow);
                tableRow = new TableRow(this);
            }
            i++;
        }
        tableLayout.addView(tableRow);
    }

    private void renderTagButtons() {
        initPreDefTagStrings();
        createTagButtons();
        populateTagsLayout();
    }

    private void setTagStyling(Button btn) {
        btn.setBackgroundResource(R.drawable.subject_tag_shape_normal);
        btn.setTextSize(12);
        btn.setTextColor(getResources().getColor(R.color.colorWhite));
    }

    private void displayUserTag(String str) {
        Button btn = new Button(this);
        btn.setText(str);
        setTagStyling(btn);
        EditText text = findViewById(R.id.tagsEditText);
        LinearLayout linearLayout = findViewById(R.id.tagsLinearLayout);
        TableRow tr = new TableRow(this);
        tr.addView(btn);
        linearLayout.addView(tr);

    }


}
