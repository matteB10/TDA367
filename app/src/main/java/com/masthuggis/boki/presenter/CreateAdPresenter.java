package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.AdFactory;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.utils.Condition;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.utils.CurrentTimeHelper;
import com.masthuggis.boki.utils.FormHelper;
import com.masthuggis.boki.utils.UniqueIdCreator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Presenter class handling the createAdActivity. Validates input from
 * user and saves in textfields if activity is temporary terminated.
 */

public class CreateAdPresenter {

    private static Advertisement advertisement;
    private View view;
    private boolean validPrice;
    private DataModel dataModel;
    private boolean imageTaken;


    public CreateAdPresenter(View view, DataModel dataModel) {
        this.dataModel = dataModel;
        advertisement = AdFactory.createAd("", dataModel.getUserID(), UniqueIdCreator.getUniqueID(), "", "",
                0, Condition.UNDEFINED, "", new ArrayList<>(), dataModel.getUserDisplayName());
        this.view = view;
        imageTaken = false;
    }

    /**
     * getting adID to set up the right view for the user
     *
     * @param adID is the individual ID for a specific ad.
     */
    public void setAd(String adID) {
        try {
            advertisement = dataModel.getAdFromAdID(adID);
        } catch (Exception e) {
            view.displayNotFoundToast("Det finns ingen annons som matchar denna data.");
            return;
        }
        validPrice = true;
    }


    public void deleteAdvert() {
        dataModel.removeExistingAdvert(advertisement.getUniqueID(), advertisement.getUniqueOwnerID());
    }

    public void updateAdvert() {
        dataModel.updateAd(view.getCurrentImageFile(), advertisement);
        advertisement = null;
    }

    /**
     * Called on click on button in createAdActivity
     * saves advert and resets current ad in presenter
     */
    public void saveAdvert() {
        if ((advertisement.getDatePublished().equals(""))) {
            setAdvertDate();
        }
        dataModel.saveAdvert(view.getCurrentImageFile(), advertisement);
        advertisement = null;
    }


    public void setUpView() {
        view.setTitle(advertisement.getTitle());
        view.setPrice(advertisement.getPrice());
        view.setDescription(advertisement.getDescription());
        view.setImageUrl(advertisement.getImageUrl());
        view.setTags(advertisement.getTags());
        view.setCondition(advertisement.getCondition(),true);
    }

    /**Method called when the title edit view is changed, to save the
     * new title to the model.
     * @param title
     */
    public void titleChanged(String title) {
        advertisement.setTitle(title);
        view.enablePublishButton(allFieldsValid());
        view.enableSaveButton(allFieldsValid());
    }

    /**
     * If price is parsable to an int, price is updated in advert.
     * boolean variable also set due to default value zero is valid
     * @param price
     */
    public void priceChanged(String price) {
        if (FormHelper.getInstance().isValidPrice(price)) {
            advertisement.setPrice(Integer.parseInt(price));
            validPrice = true;
        } else {
            validPrice = false;
        }
        view.enablePublishButton(allFieldsValid());
        view.enableSaveButton(allFieldsValid());
    }

    /**
     * Updating model with new description information
     * @param description
     */
    public void descriptionChanged(String description) {
        advertisement.setDescription(description);
    }

    /**
     * Sends string from tag clicked to advertisement.
     * Updates styling of tag in view
     *
     * @param tag
     */
    public void preDefTagsChanged(String tag) {
        view.setPreDefTagSelected(tag, isTagSelected(tag));
        advertisement.toggleTag(tag);
    }

    /**
     * Gets string from user defined tag.
     * Shows user-tag as a button if it's added, if tag is already
     * added to advertisement it is removed
     * @param tag
     */
    public void userDefTagsChanged(String tag) {
        if (isTagSelected(tag)) {
            view.displayNewUserTagButton(tag);
        } else {
            view.removeUserTagButton(tag);
        }
        advertisement.toggleTag(tag);
    }

    /** Checks if tag is selected
     * @return true if tag is selected
     */
    private boolean isTagSelected(String tag) {
        return advertisement.isNewTag(tag);
    }


    /**
     * Updates Advert when condition changed, changes
     * styling of changed condition button in view.
     * @param condition, string res condition
     */
    public void conditionChanged(Condition condition) {
        view.setCondition(condition,advertisement.isNewCondition(condition));
        advertisement.setCondition(condition);
        view.enablePublishButton(allFieldsValid());
        view.enableSaveButton(allFieldsValid());

    }

    /**
     * Checks if publish button should be enabled when image has changed
     */
    public void imageChanged() {
        imageTaken = true;
        view.enablePublishButton(allFieldsValid());
        view.enableSaveButton(allFieldsValid());
    }

    /**
     * Controls that minimal required user input is entered.
     * Valid title is all chars, length is arbitrary
     * @return
     */
    private boolean allFieldsValid() {
        boolean valid = advertisement.getTitle().length() > 2 && validPrice && advertisement.isValidCondition() &&
                (imageTaken);
        return (valid);
    }

    private void setAdvertDate() {
        advertisement.setDatePublished(CurrentTimeHelper.getCurrentTime());
    }

    public String getId() {
        return advertisement.getUniqueID();
    }

    //Getter mainly for testing purpose---------------------------------------------------
    Advertisement getAdvertisement() {
        return advertisement;
    }

    public View getView() {
        return view;
    }

    public String getTitle() {
        return advertisement.getTitle();
    }

    public Condition getCondition() {
        return advertisement.getCondition();
    }

    public String getDescription() {
        return advertisement.getDescription();
    }

    public int getPrice() {
        return (int) advertisement.getPrice();
    }

    public List<String> getTags() {
        return advertisement.getTags();
    }

    public void imageTaken() {
        this.imageTaken = true;
    }

    public boolean isImageTaken() {
        return this.imageTaken;
    }

    public interface View {

        void setTitle(String name);

        void setPrice(long price);

        void setImageUrl(String url);

        void setDescription(String description);

        void enablePublishButton(boolean isEnabled);

        void enableSaveButton(boolean b);

        void setPreDefTagSelected(String tag, boolean isPressed);

        void displayNewUserTagButton(String tag);

        void removeUserTagButton(String tag);

        File getCurrentImageFile();

        void setTags(List<String> tags);

        void setCondition(Condition id, boolean pressed);

        void displayNotFoundToast(String toast);
    }

}