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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
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
import java.util.Arrays;
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

        displayPreDefTagButtons();
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
            Intent intent = new Intent(EditAdActivity.this, MainActivity.class);
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

        setPreDefTagsListeners();
        setUserTagTextFieldListener();
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

    private void setTagListener(Button btn){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.userDefTagsChanged(btn.getText().toString());

            }
        });
    }

    //----------------------------------------------------------------
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

    //setting up old information ---------------------------------------
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

    //Tag-methods from createad and detailview-----------------------------------------
    @Override
    public void setTags(List<String> tags) {
        LinearLayout parentLayout = findViewById(R.id.tagsLinearLayout);
        TableRow tableRow = new TableRow(this);
        Button btn;

        for (String str : tags) {
            btn = createTagButton(str);
            tableRow = getTableRow(tableRow, parentLayout);
            tableRow.setLayoutParams(StylingHelper.getTableRowLayoutParams(this));
            tableRow.addView(btn, StylingHelper.getTableRowChildLayoutParams(this));
        }
        parentLayout.addView(tableRow);
    }

    private TableRow getTableRow(TableRow tableRow, LinearLayout parentLayout) {
        if (tableRow.getChildCount() % 4 == 0) {
            parentLayout.addView(tableRow);
            return new TableRow(this);
        }
        return tableRow;
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
                    userDefTag.setText("");
                    return true;
                }
                return false;
            }
        });
    }
    private List<String> getPreDefTagStrings() {
        String[] strArr = getResources().getStringArray(R.array.preDefSubjectTags);
        return Arrays.asList(strArr);
    }
    private List<Button> createTagButtons(List<String> strTags) {
        List<Button> btnList = new ArrayList<>();
        for (String str : strTags) {
            Button btn = createTagButton(str);
            btnList.add(btn);
        }
        return btnList;
    }
    private Button createTagButton(String btnTxt) {
        Button btn = new Button(this);
        btn.setText(btnTxt);
        setTagStyling(btn);
        return btn;
    }


    private void setTagStyling(Button btn) {
        btn.setBackgroundResource(R.drawable.subject_tag_shape_normal);
        btn.setTextSize(12);
        btn.setTextColor(this.getColor(R.color.colorWhite));
        btn.setElevation(4);
    }
    private void populateTagsLayout(List<Button> tags, ViewGroup parentLayout) {
        for (Button btn : tags) {
            ViewGroup tableRow = getCurrenTagRow(parentLayout.getId());
            tableRow.addView(btn, StylingHelper.getTableRowChildLayoutParams(this));
        }
    }
    private boolean rowFull(TableRow tableRow) {
        return (tableRow.getChildCount() % 3 == 0 && tableRow.getChildCount() != 0);
    }
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

    @Override
    public File getCurrentImageFile() {
        return null;
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

    private void clearLayout(ViewGroup layout) {
        ViewGroup tr;
        int noOfRows = layout.getChildCount();
        for (int i = 0; i < noOfRows; i++) {
            tr = (TableRow) layout.getChildAt(i);
            tr.removeAllViews();
        }
    }

}