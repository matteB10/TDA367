package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.utils.FormHelper;
import com.masthuggis.boki.utils.StylingHelper;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Presenter class handling the createAdActivity. Validates input from
 * user and saves in textfields if activity is temporary terminated.
 */

public class CreateAdPresenter {

    private static Advertisement advertisement;


    private View view;
    private static boolean validPrice;


    public CreateAdPresenter(View view) {
        advertisement = Repository.getInstance().createAdvert();
        this.view = view;
    }

    public boolean getIsValidPrice() {
        return this.validPrice;
    }

    public interface View {

        void enablePublishButton(boolean isEnabled);

        void styleConditionButtonPressed(int condition);

        void setTagStyling(String tag, boolean isPressed);


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
    public void tagsChanged(String tag) {
        view.setTagStyling(tag, isTagSelected(tag));
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
        return (advertisement.getTitle().length() > 2 && validPrice && advertisement.getCondition() !=
                Advert.Condition.UNDEFINED);
        //TODO: expand validation to image URI
    }

    public void imageFileChanged(File imageFile) {
        advertisement.setImageFile(imageFile);
    }


    /**
     * Called on click on button in createAdActivity
     * saves advert in temp list and resets current ad in presenter
     */
    public void publishAdvert() {
        setAdvertDate();
        Repository.getInstance().saveAdvert(advertisement, advertisement.getImageFile());
        advertisement = null;

    }

    private void setAdvertDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        String dateString = dateFormat.format(calendar.getTime()); //Saves current time as a string
        advertisement.setDatePublished(dateString);
    }

    public String getId() {
        return advertisement.getUniqueID();
    }

    public File getImgFile() {
        return advertisement.getImageFile(); //advertisement is null here upon second call
    }

    //Getter for testing purpose
    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public int getTagDrawable(boolean isPressed) {
        return StylingHelper.getTagDrawable(isPressed);
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


}
