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
import java.util.List;

/**
 * An acitivity for creating a new advertisement
 * with user input.
 */

public class CreateAdActivity extends AppCompatActivity implements CreateAdPresenter.View {


    static final int REQUEST_IMAGE_CAPTURE = 1;

    private final List<Button> tagButtons = new ArrayList<>();
    private final List<String> preDefTags = new ArrayList<>();

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
        enablePublishButton(false);
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
            try {
                FileOutputStream out = new FileOutputStream(currentImageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

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
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 600, 800, true); //TODO change 200x200 to preferable resolution
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

    private void setCreateAdvertListener() {
        publishAdButton = findViewById(R.id.publishAdButton);
        publishAdButton.setOnClickListener(view -> {
            Intent intent = new Intent(CreateAdActivity.
                    this, DetailsActivity.class);
            presenter.publishAdvert();
            intent.putExtra("advertID", presenter.getId());
            startActivity(intent);
            finish();

        });
    }


    private void setPreDefTagsListeners() {
        for (Button btn : tagButtons) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Prevent scrollview from auto scrolling to textfields in focus
                    btn.requestFocus();
                    presenter.tagsChanged(btn.getText().toString());
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
     * <p>
     * private void createTagButtons() {
     * for (String str : preDefTags) {
     * Button btn = new Button(this);
     * btn.setText(str);
     * styleTagButtons(btn, false);
     * tagButtons.add(btn);
     * }
     * }
     */
    private Button getTagButton(String text) {
        Button b = new Button(this);
        b.setText(text);
        styleTagButtons(b, false);
        return b;
    }

    /**
     * Populates view with subject tag buttons.
     */
    private void populateTagsLayout() {
        LinearLayout parentLayout = findViewById(R.id.preDefTagsLinearLayout);
        TableRow tableRow = new TableRow(this);
        Button btn;

        for (String str : preDefTags) {
            btn = getTagButton(str);
            tagButtons.add(btn);
            tableRow = getTableRow(tableRow, parentLayout);
            tableRow.setLayoutParams(StylingHelper.getTableRowLayoutParams(this));
            tableRow.addView(btn, StylingHelper.getTableRowChildLayoutParams(this));
        }
        parentLayout.addView(tableRow);
    }


    /**
     * Private method trying to resolve if a tableRow with tags is filled and
     * if a new one should be created.
     *
     * @param tableRow     current tableRow
     * @param parentLayout
     * @return param tableRow or new tableRow object
     */
    private TableRow getTableRow(TableRow tableRow, LinearLayout parentLayout) {
        if (tableRow.getChildCount() % 4 == 0) {
            parentLayout.addView(tableRow);
            return new TableRow(this);
        }
        return tableRow;
    }


    private void renderTagButtons() {
        initPreDefTagStrings();
        populateTagsLayout();
    }

    private void styleTagButtons(Button btn, boolean isSelected) {
        btn.setBackgroundResource(presenter.getTagDrawable(isSelected));
        btn.setTextSize(12);
        btn.setTextColor(this.getColor(R.color.colorWhite));
        btn.setElevation(StylingHelper.getDPToPixels(this, 4));
    }

    private void displayUserTag(String str) {
        Button btn = new Button(this);
        btn.setText(str);
        styleTagButtons(btn, false);
        EditText text = findViewById(R.id.tagsEditText);
        LinearLayout linearLayout = findViewById(R.id.tagsLinearLayout);
        TableRow tr = new TableRow(this);
        tr.addView(btn);
        linearLayout.addView(tr);

    }

    @Override
    public void setTagStyling(String tag, boolean isSelected) {
        for (Button btn : tagButtons) {
            if (btn.getText().equals(tag)) {
                styleTagButtons(btn, isSelected);
            }
        }
    }


}
