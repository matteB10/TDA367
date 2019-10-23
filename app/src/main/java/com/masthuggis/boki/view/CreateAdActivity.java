package com.masthuggis.boki.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.masthuggis.boki.R;
import com.masthuggis.boki.injectors.DependencyInjector;
import com.masthuggis.boki.presenter.CreateAdPresenter;
import com.masthuggis.boki.utils.Condition;
import com.masthuggis.boki.utils.ImageHandler;
import com.masthuggis.boki.utils.StylingHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * An acitivity for creating a new or editing an advertisement
 * with user input.
 */

public class CreateAdActivity extends AppCompatActivity implements CreateAdPresenter.View {

    private List<Button> preDefTagButtons = new ArrayList<>();
    private List<Button> userDefTagButtons = new ArrayList<>();
    private CreateAdPresenter presenter;
    private EditText title;
    private EditText price;
    private EditText description;
    private Button publishAdButton;
    private Button saveAdButton;
    private Button deleteAdButton;

    private ImageHandler imageHandler;

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

        imageHandler = new ImageHandler(this, findViewById(R.id.addImageView));

        /**If there is an existing ad, and user wants to edit*/
        if (intent.getExtras() != null) {
            String advertID = intent.getExtras().getString(getString(R.string.keyForAdvert));
            presenter.setAd(advertID);
            presenter.imageTaken();
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
        setTitleListener();
        setPriceListener();
        setDescriptionListener();
        setPublishAdListener();
        setPreDefTagsListeners();
        setUserTagTextFieldListener();
        setDeleteBtnListener();
        setSaveBtnListener();
    }


    private void setButtonVisibility(boolean editMode) {
        if (editMode) {
            publishAdButton.setVisibility(View.GONE);
            enableSaveButton(true);
        } else {
            deleteAdButton.setVisibility(View.GONE);
            saveAdButton.setVisibility(View.GONE);
        }
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
        startActivity(intent);
        finish();
    }

    /**
     * Method that is run after android OS has completed taking a picture
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (imageHandler.onActivityResult(requestCode, resultCode)) {
            presenter.imageChanged();
        }
    }

    @Override
    public File getCurrentImageFile() {
        return imageHandler.getCurrentImageFile();
    }

    //Tags----------------------------------------------------------

    /**
     * Called in onCreate, create tag buttons and add them to the view
     */
    private void displayPreDefTagButtons() {
        LinearLayout preDefTagsLayout = findViewById(R.id.preDefTagsLinearLayout);
        preDefTagButtons = TagHelper.createPreDefTagButtons(this);
        TagHelper.populateTagsLayout(new ArrayList<>(preDefTagButtons),preDefTagsLayout,this);
    }

    @Override
    public void setPreDefTagSelected(String tag, boolean isSelected) {
        for(Button btn : preDefTagButtons){
            if(btn.getText().equals(tag)){
                StylingHelper.setTagButtonStyling(btn,isSelected, this);
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
    public void displayNewUserTagButton(String tag) {
        Button btn = TagHelper.createTagButton(tag, true, this);
        userDefTagButtons.add(btn);
        setUserDefTagsListener(btn);
        updateUserDefTags();
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
     * Used to update tags layout correctly if a tag
     * has been deleted.
     */
    private void updateUserDefTags() {
        ViewGroup parentLayout = findViewById(R.id.tagsLinearLayout);
        TagHelper.clearLayout(parentLayout);
        TagHelper.populateTagsLayout(new ArrayList<>(userDefTagButtons), parentLayout,this);
    }

    /**
     * Takes in a string and returns matching button if possible
     *
     * @param btnText, the button text
     * @return a button if btnText matches the text of a button
     */
    private Button getButtonFromText(String btnText, List<Button> buttons){
        try {
           return TagHelper.getButtonWithText(btnText,buttons);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.errorToastCreateAd), Toast.LENGTH_SHORT).show();
        }
        return null;
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


    /**
     * Called when a radiobutton is clicked.
     * @param view is the specific button that is checked.
     **/

    public void onRadioButtonClicked(View view) {
        presenter.conditionChanged(StylingHelper.getConditionFromView(view.getId()));
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
        imageHandler.onImageUpdated(url);
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
            if (TagHelper.isPreDefTag(str,this)) {
                setPreDefTagSelected(str, true); //if str is preDefTag, style as selected
            } else {
                displayNewUserTagButton(str);
            }
        }
    }

    @Override
    public void setCondition(Condition condition, boolean pressed){
        RadioButton conditionNew = findViewById(R.id.conditionNewButton);
        RadioButton conditionGood = findViewById(R.id.conditionGoodButton);
        RadioButton conditionOk = findViewById(R.id.conditionOkButton);
        switch (condition) {
            case NEW:
                conditionNew.setBackgroundResource(StylingHelper.getConditionDrawable(condition,true));
                conditionGood.setBackgroundResource(StylingHelper.getConditionDrawable(Condition.GOOD,false));
                conditionOk.setBackgroundResource(StylingHelper.getConditionDrawable(Condition.OK,false));
                break;
            case GOOD:
                conditionGood.setBackgroundResource(StylingHelper.getConditionDrawable(condition,true));
                conditionNew.setBackgroundResource(StylingHelper.getConditionDrawable(Condition.NEW,false));
                conditionOk.setBackgroundResource(StylingHelper.getConditionDrawable(Condition.OK,false));
                break;
            case OK:
                conditionOk.setBackgroundResource(StylingHelper.getConditionDrawable(condition,true));
                conditionNew.setBackgroundResource(StylingHelper.getConditionDrawable(Condition.NEW,false));
                conditionGood.setBackgroundResource(StylingHelper.getConditionDrawable(Condition.GOOD,false));
                break;
        }
    }

    @Override
    public void displayNotFoundToast(String toast) {
        Toast.makeText(getApplicationContext(),toast , Toast.LENGTH_SHORT).show();

    }
}
