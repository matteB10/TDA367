package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.AdFactory;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.utils.CurrentTimeHelper;
import com.masthuggis.boki.utils.FormHelper;

import java.io.File;

/**
 * Presenter class handling the createAdActivity. Validates input from
 * user and saves in textfields if activity is temporary terminated.
 */

public class CreateAdPresenter {

    private static Advertisement advertisement;


    private View view;
    private static boolean validPrice;


    public CreateAdPresenter(View view) {
        advertisement = AdFactory.createAd();
        this.view = view;
    }

    public boolean getIsValidPrice() {
        return this.validPrice;
    }

    public interface View {

        void enablePublishButton(boolean isEnabled);

        void styleConditionButtonPressed(int condition);

        void setTagStyling(String tag, boolean isPressed);

        void displayUserTagButton(String tag);

        void removeUserTagButton(String tag);

        File getCurrentImageFile();




        //TODO: create methods for future same page error messages in view

    }

    public void titleChanged(String title) {
        advertisement.setTitle(title);
        view.enablePublishButton(allFieldsValid());
    }

    /**
     * If price is parsable to an int, price is updated in advert.
     *
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

    }

    //cannot check for valid input, all input is valid
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
        view.setTagStyling(tag, isTagSelected(tag));
        advertisement.tagsChanged(tag);
    }

    /**
     * Gets string from user defined tag.
     * Shows user tag as a button if its added, if tag is already
     * added to advertisement it is removed
     * @param tag
     */
    public void userDefTagsChanged(String tag) {
        if(isTagSelected(tag)) {
            view.displayUserTagButton(tag);
        }else{
            view.removeUserTagButton(tag);
        }
        advertisement.tagsChanged(tag);
    }

    /**
     * Checks if tag is selected
     *
     * @return true if tag is selected
     */
    private boolean isTagSelected(String tag) {
        return advertisement.isNewTag(tag);
    }

    /**
     * Updates Advert when condition changed, changes
     * styling of changed condition button in view.
     *
     * @param condition, string res condition
     */
    public void conditionChanged(int condition) {
        advertisement.setCondition(condition);
        view.styleConditionButtonPressed(condition);
        view.enablePublishButton(allFieldsValid());

    }

    /**
     * Controls that minimal required user input is entered.
     * Valid title is all chars, length is arbitrary
     *
     * @return
     */
    private boolean allFieldsValid() {
        return (advertisement.getTitle().length() > 2 && validPrice && advertisement.isValidCondition());
        //TODO: expand validation to image
    }


    /**
     * Called on click on button in createAdActivity
     * saves advert in temp list and resets current ad in presenter
     */
    //Need to change imageFile in advert to inputStream from View
    public void publishAdvert() {
        setAdvertDate();
        DataModel.getInstance().saveAdvert(view.getCurrentImageFile(), advertisement);
        advertisement = null;
    }

    private void setAdvertDate() {
        advertisement.setDatePublished(CurrentTimeHelper.getCurrentTime());      //Saves current time as a string
    }

    public String getId() {
        return advertisement.getUniqueID();
    }

    //Getter for testing purpose
    public Advertisement getAdvertisement() {
        return advertisement;
    }


    public View getView() {
        return view;
    }

    public String getTitle() {
        return advertisement.getTitle();
    }

    public String getCondition() {
        return advertisement.getCondition().toString();
    }

    public String getDescription() {
        return advertisement.getDescription();
    }

    public int getPrice() {
        return (int) advertisement.getPrice();
    }

    public String getImageUrl() {
        return advertisement.getImageUrl();
    }

}