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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.masthuggis.boki.R;
import com.masthuggis.boki.injectors.DependencyInjector;
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

        if (intent.getExtras() != null) { //If there is an ad (user is editing existing ad)
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
        setConditionGroupListener();
        setPreDefTagsListeners();
        setUserTagTextFieldListener();
        setDeleteBtnListener();
        setSaveBtnListener();
    }


    private void setButtonVisibility(boolean editMode) {
        if(editMode){
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

    //images--------------------------------------------------------
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageViewDisplay = findViewById(R.id.addImageView);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) { //When image has been taken
            compressImage();
            presenter.imageChanged();
        }
    }

    //Compresses images without trying to crop image with android OS
    private void compressImage() {
        Bitmap bitmap = compressBitmap();
        try {
            OutputStream out = new FileOutputStream(currentImageFile.getPath());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            Bitmap image = BitmapFactory.decodeFile(currentImageFile.getPath());
            setImageView(image); //Set compressed and scaled image so user sees actual result
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

    //Compresses image, sets resolution, should probably only be used when running app on emulator
    private Bitmap compressBitmap() {
        BitmapFactory.Options imageOptions = new BitmapFactory.Options();
        imageOptions.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(currentImageFile.getPath());
        return Bitmap.createScaledBitmap(bitmap, 800, 800, false); //TODO is this even necessary?
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
        userDefTagButtons.remove(getButtonFromText(tag, userDefTagButtons));
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
    private Button getButtonFromText(String btnText, List<Button> buttons) {
        for (Button btn : buttons) {
            if (btn.getText().toString().equals(btnText)) {
                return btn;
            }
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
    private void getBackToMain(String toastMessage){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(getString(R.string.putExtraToastKey),toastMessage);
        startActivity(intent);
        finish();
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
}
