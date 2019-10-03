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
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.CreateAdPresenter;
import com.masthuggis.boki.utils.StylingHelper;
import com.masthuggis.boki.utils.UniqueIdCreator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An acitivity for creating a new advertisement
 * with user input.
 */

public class CreateAdActivity extends AppCompatActivity implements CreateAdPresenter.View {


    static final int REQUEST_IMAGE_CAPTURE = 1;

    private List<Button> preDefTagButtons = new ArrayList<>();
    private List<String> userDefTags = new ArrayList<>();

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
        enablePublishButton(false);
        displayPreDefTagButtons();
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
            try {
                FileOutputStream out = new FileOutputStream(currentImageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
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
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 600, 800, true); //TODO change to preferable resolution
        return scaledBitmap;
    }

    private void setListeners() {
        setImageViewListener();
        setTitleListener();
        setPriceListener();
        setDescriptionListener();
        setPublishAdListener();
        setConditionGroupListener();
        setPreDefTagsListeners();
        setUserDefTagListener();

    }

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

    /**
     * Enables publish ad button when all mandatory fields contains
     * valid input.
     */
    @Override
    public void enablePublishButton(boolean b) {
        publishAdButton = findViewById(R.id.publishAdButton);
        publishAdButton.setEnabled(b);
        publishAdButton.setBackground(getDrawable(StylingHelper.getPrimaryButtonDrawable(b)));

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

    private void setPublishAdListener() {
        publishAdButton = findViewById(R.id.publishAdButton);
        publishAdButton.setOnClickListener(view -> {
            Intent intent = new Intent(CreateAdActivity.
                    this, DetailsActivity.class);
            intent.putExtra("advertID", presenter.getId());
            presenter.publishAdvert();
            startActivity(intent);
            finish();

        });
    }


    private void setPreDefTagsListeners() {
        for (Button btn : preDefTagButtons) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Prevent scrollview from auto scrolling to textfields in focus
                    btn.requestFocus();
                    presenter.preDefTagsChanged(btn.getText().toString());
                }
            });
        }
    }

    private void setUserDefTagListener() {
        EditText userDefTag = findViewById(R.id.tagsEditText);
        userDefTag.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_SPACE)) {
                    presenter.userDefTagsChanged(userDefTag.getText().toString());
                    //clear text field for new user input
                    userDefTag.setText("");
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Reads pre defined subject strings from resources,
     *
     * @return all strings in a list.
     */
    private List<String> getPreDefTagStrings() {
        String[] strArr = getResources().getStringArray(R.array.preDefSubjectTags);
        return Arrays.asList(strArr);
    }

    /**
     * Creates a list with buttons from a list of strings.
     *
     * @param strTags, list of button text strings
     * @return a list of buttons
     */
    private List<Button> createTagButtons(List<String> strTags) {
        List<Button> btnList = new ArrayList<>();
        for (String str : strTags) {
            Button btn = createTagButton(str);
            btnList.add(btn);
        }
        return btnList;
    }

    /**
     * Create a tag button with correct styling
     *
     * @param text
     * @return a button
     */
    private Button createTagButton(String text) {
        Button b = new Button(this);
        b.setText(text);
        styleTagButtons(b, false);
        return b;
    }

    /**
     * @param tags         a list of buttons
     * @param parentLayout the layout in which buttons will be placed
     */
    private void populateTagsLayout(List<Button> tags, LinearLayout parentLayout) {
        TableRow tableRow = new TableRow(this);
        for (Button btn : tags) {
            tableRow.setLayoutParams(StylingHelper.getTableRowLayoutParams(this));
            tableRow.addView(btn, StylingHelper.getTableRowChildLayoutParams(this));
            if (rowFull(tableRow)) {
                parentLayout.addView(tableRow);
                tableRow = new TableRow(this);
            }
        }
        parentLayout.addView(tableRow);
    }

    /**
     * Method to check if row is full
     *
     * @param tableRow the row to be checked
     * @return true if row is full
     */
    private boolean rowFull(TableRow tableRow) {
        return (tableRow.getChildCount() % 3 == 0 && tableRow.getChildCount() != 0);
    }

    /**
     * Called in onCreate, create tag buttons and add them to the view
     */

    private void displayPreDefTagButtons() {
        LinearLayout preDefTagsLayout = findViewById(R.id.preDefTagsLinearLayout);
        List<Button> tagButtons = createTagButtons(getPreDefTagStrings());
        preDefTagButtons = tagButtons;
        populateTagsLayout(tagButtons, preDefTagsLayout);
    }


    private void styleTagButtons(Button btn, boolean isSelected) {
        btn.setBackgroundResource(presenter.getTagDrawable(isSelected));
        btn.setTextSize(12);
        btn.setTextColor(this.getColor(R.color.colorWhite));
        btn.setElevation(StylingHelper.getDPToPixels(this, 4));
    }

    @Override
    public void setTagStyling(String tag, boolean isSelected) {
        for (Button btn : preDefTagButtons) {
            if (btn.getText().equals(tag)) {
                styleTagButtons(btn, isSelected);
            }
        }
    }

    /**
     * Called in presenter when a user defined a new tag
     *
     * @param tag
     */

    @Override
    public void displayUserTagButton(String tag) {
        LinearLayout parentLayout = findViewById(R.id.tagsLinearLayout);
        //must reset parentView for every new tag added to get tags in right formation
        parentLayout.removeAllViewsInLayout();
        userDefTags.add(tag);
        populateTagsLayout(createTagButtons(userDefTags), parentLayout);

    }

}
