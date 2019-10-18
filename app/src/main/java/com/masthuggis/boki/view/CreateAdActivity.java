package com.masthuggis.boki.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.masthuggis.boki.R;
import com.masthuggis.boki.injectors.DependencyInjector;
import com.masthuggis.boki.presenter.CreateAdPresenter;
import com.masthuggis.boki.utils.StylingHelper;
import com.masthuggis.boki.utils.UniqueIdCreator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * An acitivity for creating a new advertisement
 * with user input.
 */

public class CreateAdActivity extends AppCompatActivity implements CreateAdPresenter.View {


    static final int REQUEST_IMAGE_CAPTURE = 1;
    private List<Button> preDefTagButtons = new ArrayList<>();
    private List<Button> userDefTagButtons = new ArrayList<>();
    private CreateAdPresenter presenter;
    private File currentImageFile;
    private ImageView imageViewDisplay;
    private EditText title;
    private EditText price;
    private EditText description;
    private Button publishAdButton;
    private Button saveAdButton;
    private Button deleteAdButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);
        presenter = new CreateAdPresenter(this, DependencyInjector.injectDataModel());
        enablePublishButton(false);
        Intent intent = getIntent();
        displayPreDefTagButtons();
        setListeners();
        setUpView();


        /**If there is an existing ad, and user wants to edit*/
        if (intent.getExtras() != null) {
            String advertID = intent.getExtras().getString("advertID");
            presenter.setAd(advertID);
            presenter.setUpView();
        }
    }

    /**
     * Setting up view depending on if the user are creating an ad or editing an existing one
     */
    private void setUpView() {
        TextView headerTextView = findViewById(R.id.headerTextView);
        boolean editMode = getIntent().getExtras() != null;
        if (editMode) {
            headerTextView.setText(getResources().getString(R.string.addAdvertHeaderEdit));
        }
        setButtonVisibility(editMode);
    }

    private void setListeners() {
        setImageViewListener();
        setTitleListener();
        setPriceListener();
        setDescriptionListener();
        setPublishAdListener();
        setRadioGroupListener();
        setPreDefTagsListeners();
        setUserTagTextFieldListener();
        setDeleteBtnListener();
        setSaveBtnListener();
    }


    private void setButtonVisibility(boolean editMode) {
        if (editMode) {
            publishAdButton.setVisibility(View.GONE);
        } else {
            deleteAdButton.setVisibility(View.GONE);
            saveAdButton.setVisibility(View.GONE);
        }
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

    /**
     * Enables save(update) ad button when all mandatory fields contains
     * valid input.
     */
    @Override
    public void enableSaveButton(boolean b) {
        saveAdButton = findViewById(R.id.saveAdBtn);
        saveAdButton.setEnabled(b);
        saveAdButton.setBackground(getDrawable(StylingHelper.getPrimaryButtonDrawable(b)));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent); //TODO check here to see what fragment was the previous one, maybe
        finish();
    }


    /**
     * Sends a request to the operating system for the application's use of the device's camera
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                currentImageFile = createImageFile();
            } catch (Exception i) {
                System.out.println("error creating file");
                i.printStackTrace();
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
     * @throws IOException if image creation fails
     */
    private File createImageFile() throws IOException {
        String photoFileName = "IMG_" + UniqueIdCreator.getUniqueID();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(photoFileName, ".jpg", storageDir);
        return image;
    }

    /**
     * Method that is run after android OS has completed taking a picture
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageViewDisplay = findViewById(R.id.addImageView);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) { //When image has been taken
            compressAndRotateImage();
            presenter.imageChanged();
        }
    }

    /**
     * Compresses the size of the image taken by the user, checks if rotation is necessary and, if so, rotates the image to portrait mode
     * Also sets the image held by the ImageView shown to the user to the compressed and rotated image
     */
    private void compressAndRotateImage() {
        try {
            Bitmap initialBitmap = BitmapFactory.decodeFile(currentImageFile.getPath());
            Bitmap rotatedImage = rotateImageIfRequired(initialBitmap, currentImageFile.getAbsolutePath());
            Bitmap finalBitmap = compressBitmap(rotatedImage);
            setImageView(finalBitmap); //Set compressed and scaled image so user sees actual result
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param input the bitmap that should be compressed
     * @return a bitmap of the same image, but compressed
     * @throws IOException if the path used to create an OutputStream isn't valid
     */
    private Bitmap compressBitmap(Bitmap input) throws IOException {
        BitmapFactory.Options imageOptions = new BitmapFactory.Options();
        imageOptions.inJustDecodeBounds = true;
        Bitmap scaled = Bitmap.createScaledBitmap(input, 1024, 1024, false);
        OutputStream out = new FileOutputStream(currentImageFile.getPath());
        scaled.compress(Bitmap.CompressFormat.JPEG, 80, out); //Compresses bitmap and writes result to currentImage's path
        return BitmapFactory.decodeFile(currentImageFile.getPath());
    }


    /**
     *
     * @param imageBitmap the Bitmap to be rotated
     * @param imagePath the path to the image taken, used in order to determine how much rotation is needed
     * @return a bitmap of the rotated image
     * @throws IOException if the supplied image path is invalid
     */
    private Bitmap rotateImageIfRequired(Bitmap imageBitmap, String imagePath) throws IOException {
        int rotationDegrees = getRotationDegrees(imagePath);
        return rotateImage(imageBitmap, rotationDegrees);
    }

    /**
     *
     * @param imagePath path to the image taken by the user, holds metadata about its orientation
     * @return the degrees of rotation needed for picture to be oriented in portrait mode
     * @throws IOException if supplied image path is invalid
     */
    private int getRotationDegrees(String imagePath) throws IOException {
        ExifInterface exifInterface = new ExifInterface(imagePath);
        int orientationTag = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (orientationTag) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
            default:
                return 0;

        }
    }

    /**
     * @param image the bitmap to be rotated
     * @param degrees the degrees of rotation
     * @return a rotated bitmap of the same image as was given as input
     */
    private Bitmap rotateImage(Bitmap image, int degrees) {
        if (degrees == 0) {
            return image;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
    }

    public File getCurrentImageFile() {
        return this.currentImageFile;
    }

    private void setImageView(Bitmap bitmap) {
        imageViewDisplay.setImageBitmap(bitmap);
    }

    //Tags----------------------------------------------------------

    /**
     * Reads predefined subject strings from resources,
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
    private List<Button> createPreDefTagButtons(List<String> strTags) {
        List<Button> btnList = new ArrayList<>();
        for (String str : strTags) {
            Button btn = createTagButton(str, false);
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
    private Button createTagButton(String text, boolean isSelected) {
        Button b = new Button(this);
        b.setText(text);
        StylingHelper.setTagButtonStyling(b, isSelected);
        return b;
    }

    /**
     * @param tags         a list of buttons
     * @param parentLayout the layout in which buttons will be placed
     */
    private void populateTagsLayout(List<Button> tags, ViewGroup parentLayout) {
        for (Button btn : tags) {
            ViewGroup tableRow = getCurrentTagRow(parentLayout.getId());
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
        List<Button> tagButtons = createPreDefTagButtons(getPreDefTagStrings());
        preDefTagButtons = tagButtons;
        populateTagsLayout(tagButtons, preDefTagsLayout);
    }

    @Override
    public void setPreDefTagSelected(String tag, boolean isSelected) {
        for (Button btn : preDefTagButtons) {
            if (btn.getText().equals(tag)) {
                StylingHelper.setTagButtonStyling(btn, isSelected);
            }
        }
    }

    /**
     * Called in presenter when a user defined a new tag, adds new
     * tag as a button in layout.
     *
     * @param tag
     */
    @Override
    public void displayUserTagButton(String tag) {
        Button btn = createTagButton(tag, true);
        userDefTagButtons.add(btn);
        ViewGroup currentUserTagTableRow = getCurrentTagRow(R.id.tagsLinearLayout);
        setUserDefTagsListener(btn);
        currentUserTagTableRow.addView(btn, StylingHelper.getTableRowChildLayoutParams(this));
    }

    /**
     * Called from presenter when a user have clicked on a user def tag.
     * Removes selected tag and updates view.
     *
     * @param tag
     */
    @Override
    public void removeUserTagButton(String tag) {
        Button btn = getButtonFromText(tag, userDefTagButtons);
        if (btn == null) {
            return;
        }
        userDefTagButtons.remove(btn);
        updateUserDefTags();
    }

    /**
     * Returns first not filled row in a layout given as parameter,
     * or a new row if all rows are filled or layout has no rows.
     * @param parentViewID
     * @return a new TableRow
     */
    private ViewGroup getCurrentTagRow(int parentViewID) {
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

    /**
     * Used to update tags layout correctly if a tag
     * has been deleted.
     */
    private void updateUserDefTags() {
        ViewGroup parentLayout = findViewById(R.id.tagsLinearLayout);
        clearLayout(parentLayout);
        populateTagsLayout(userDefTagButtons, parentLayout);
    }

    /**
     * Takes in a string and returns matching button if possible
     *
     * @param btnText, the button text
     * @return a button if btnText matches the text of a button
     */
    private Button getButtonFromText(String btnText, List<Button> buttons) throws NoSuchElementException {
        try {
            for (Button btn : buttons) {
                if (btn.getText().toString().equals(btnText)) {
                    return btn;
                }
            }
            throw new NoSuchElementException();

        } catch (NoSuchElementException e) {
            Toast.makeText(getApplicationContext(), "Ingen knapp med detta namn hittas tyvärr, prova uppdatera vyn.", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    /**
     * Checks if tag is preDef or userDef
     * @param tag
     * @return
     */
    private boolean isPreDefTag(String tag) {
        List<String> preDefTags = getPreDefTagStrings();
        return (preDefTags.contains(tag));
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

    //Listeners-----------------------------------------------------
    private void setDeleteBtnListener() {
        deleteAdButton = findViewById(R.id.removeAdBtn);
        deleteAdButton.setOnClickListener(view -> {
            presenter.deleteAdvert();
            getBackToMain(getString(R.string.toastDeletedAd));
        });
    }

    private void setSaveBtnListener() {
        saveAdButton = findViewById(R.id.saveAdBtn);
        saveAdButton.setOnClickListener(view -> {
            presenter.updateAdvert();
            getBackToMain(getString(R.string.toastUpdatedAd));
        });
    }

    private void setPublishAdListener() {
        publishAdButton = findViewById(R.id.publishAdButton);
        publishAdButton.setOnClickListener(view -> {
            presenter.saveAdvert();
            getBackToMain(getString(R.string.toastCreatedAd));
        });
    }

    private void getBackToMain(String toastMessage) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(getString(R.string.putExtraToastKey), toastMessage);
        startActivity(intent);
        finish();
    }

    private void setRadioGroupListener() {
        RadioGroup group = findViewById(R.id.conditionRadioGroup);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                presenter.conditionChanged(checkedId);
            }
        });
    }

    /**
     * Called when a radiobutton is checked. Updates the listener.
     *
     * @param view is the specific button that is checked.
     */
    public void onRadioButtonClicked(View view) {
        setRadioGroupListener();
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
            public void onClick(View v) {
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

    //setters -------------------------------------------------------
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
        ImageView imageView = (ImageView) findViewById(R.id.addImageView);
        Glide.with(this).load(url).into(imageView);
    }

    @Override
    public void setDescription(String description) {
        TextView currentDescription = findViewById(R.id.descriptionEditText);
        currentDescription.setText(description);
    }

    /**
     * Used when editing an already existing advertisement
     * @param tags, all tags saved in advertisement
     */
    @Override
    public void setTags(List<String> tags) {
        for (String str : tags) {
            if (isPreDefTag(str)) {
                setPreDefTagSelected(str, true); //if str is preDefTag, style as selected
            } else {
                displayUserTagButton(str);
            }
        }
    }

    @Override
    public void toggleCondition(String id){
        RadioButton conditionNew = findViewById(R.id.conditionNewButton);
        RadioButton conditionGood = findViewById(R.id.conditionGoodButton);
        RadioButton conditionOk = findViewById(R.id.conditionOkButton);
        switch (id) {
            case "NEW":
                conditionNew.toggle();
                break;
            case "GOOD":
                conditionGood.toggle();
                break;
            case "OK":
                conditionOk.toggle();
                break;
        }
    }

    @Override
    public void displayNotFoundToast(String toast) {
        Toast.makeText(getApplicationContext(),toast , Toast.LENGTH_SHORT).show();

    }
}
