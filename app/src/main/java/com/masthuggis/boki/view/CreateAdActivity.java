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
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.CreateAdPresenter;
import com.masthuggis.boki.utils.StylingHelper;
import com.masthuggis.boki.utils.UniqueIdCreator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An acitivity for creating a new advertisement
 * with user input.
 */

public class CreateAdActivity extends AppCompatActivity implements CreateAdPresenter.View {


    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_CROP = 2;
    private List<Button> preDefTagButtons = new ArrayList<>();
    private List<Button> userDefTagButtons = new ArrayList<>();
    private CreateAdPresenter presenter;
    private File currentImageFile;
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

    //If image can be cropped or not depends on if the app is run on an emulator or not
    //Can't start crop request on emulator for some unknown reason...
    private boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    private void updateDataFromModel() {
        if (presenter.getImageUrl() != null) {
            Glide.with(this).load(presenter.getImageUrl()).into(imageViewDisplay);
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
            if (isEmulator()) { //TODO maybe remove this when development is done
                //Do stuff without cropping image since you can't crop on emulator
                compressOnEmulator();
            } else {
                //Picture has been taken, needs to be cropped
                Uri.Builder builder = new Uri.Builder();
                builder.encodedPath(currentImageFile.getAbsolutePath());
                Uri imageUri = builder.build(); //Build Uri
                cropImage(imageUri);
            }
        } else if (requestCode == REQUEST_IMAGE_CROP) {
            if (data != null) {
                Bundle extras = data.getExtras();
                Bitmap croppedBitmap = extras.getParcelable("data");
                setImageView(croppedBitmap);
            }
        }
    }

    //Compresses images without trying to crop image with android OS
    private void compressOnEmulator() {
        Bitmap bitmap = compressBitmap();
        setImageView(bitmap);
        try {
            OutputStream out = new FileOutputStream(currentImageFile.getPath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public File getCurrentImageFile() {
        return this.currentImageFile;
    }

    private void setImageView(Bitmap bitmap) {
        imageViewDisplay.setImageBitmap(bitmap);
    }


    /**
     * Helper method to decode bitmap
     */

    private Bitmap compressBitmap() {
        BitmapFactory.Options imageOptions = new BitmapFactory.Options();
        imageOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(currentImageFile.getPath());
        return Bitmap.createScaledBitmap(bitmap, 1080, 1920, false); //TODO is this even necessary?
    }

    private void setListeners() {
        setImageViewListener();
        setTitleListener();
        setPriceListener();
        setDescriptionListener();
        setPublishAdListener();
        setConditionGroupListener();
        setPreDefTagsListeners();
        setUserTagTextFieldListener();

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
                    this, MainActivity.class); //Need to send something here to show snackbar
            intent.putExtra("advertID", presenter.getId());
            intent.putExtra("toast", true);
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

    private void setUserDefTagsListener(Button btn) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.userDefTagsChanged(btn.getText().toString());

            }
        });
    }


    private void setUserTagTextFieldListener() {
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
    private void populateTagsLayout(List<Button> tags, ViewGroup parentLayout) {
        for (Button btn : tags) {
            ViewGroup tableRow = getCurrenTagRow(parentLayout.getId());
            tableRow.addView(btn, StylingHelper.getTableRowChildLayoutParams(this));
        }
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
        Button btn = createTagButton(tag);
        userDefTagButtons.add(btn);
        ViewGroup currentUserTagTableRow = getCurrenTagRow(R.id.tagsLinearLayout);
        setUserDefTagsListener(btn);
        currentUserTagTableRow.addView(btn, StylingHelper.getTableRowChildLayoutParams(this));
    }

    @Override
    public void removeUserTagButton(String tag) {
        userDefTagButtons.remove(getButtonFromText(tag));
        updateUserDefTags();
    }

    private ViewGroup getCurrenTagRow(int parentViewID) {
        ViewGroup parentLayout = findViewById(parentViewID);
        int noOfRows = parentLayout.getChildCount();
        for (int i = 0; i < noOfRows; i++) {
            if (!(rowFull((TableRow) parentLayout.getChildAt(i)))) {
                return (TableRow) parentLayout.getChildAt(i);
            }
        }
        ViewGroup tr = new TableRow(this);
        parentLayout.addView(tr, StylingHelper.getTableRowLayoutParams(this));
        return tr;
    }

    private void updateUserDefTags() {
        ViewGroup parentLayout = findViewById(R.id.tagsLinearLayout);
        clearLayout(parentLayout);
        populateTagsLayout(userDefTagButtons, parentLayout);
    }

    private Button getButtonFromText(String text) {
        for (Button btn : userDefTagButtons) {
            if (btn.getText().toString().equals(text)) {
                return btn;
            }
        }
        return null;
    }

    /**
     * Clear rows in layout from children
     */
    private void clearLayout(ViewGroup layout) {
        ViewGroup tr;
        int noOfRows = layout.getChildCount();
        for (int i = 0; i < noOfRows; i++) {
            tr = (TableRow) layout.getChildAt(i);
            tr.removeAllViews();
        }
    }

}
