package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.utils.FormHelper;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Presenter class handling the createAdActivity. Validates input from
 * user and saves in textfields if activity is temporary terminated.
 */

public class CreateAdPresenter {

    private static Advertisement advertisement = Repository.getInstance().createAdvert();


    private View view;
    private static boolean validPrice;


    public CreateAdPresenter(View view) {
        this.view = view;
    }

    public String getDescription() {
        if (advertisement.getDescription() == null) {
            return "";
        }
        return advertisement.getDescription();
    }

    public boolean getIsValidPrice() {
        return this.validPrice;
    }

    public interface View {
        void enablePublishButton();


        //TODO: create methods for future same page error messages in view

    }

    public void titleChanged(String title) {
        advertisement.setTitle(title);
        if (allFieldsValid()) {
            view.enablePublishButton();

        }
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
        if (allFieldsValid()) {
            view.enablePublishButton();
        }
    }

    //cannot check for valid input, all input is valid
    public void descriptionChanged(String description) {
        advertisement.setDescription(description);
    }

    public void imageURIChanged(String imageURI) {
        advertisement.setImgURI(imageURI);

    }

    public void tagsChanged(String tag) {
        advertisement.tagsChanged(tag);
    }

    public void conditionChanged(Advert.Condition condition) {
        advertisement.setCondition(condition);
        if (allFieldsValid()) {
            view.enablePublishButton();
        }

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

    //TODO: Maybe put a standard URL to some image in the catch block
    private String convertURIStringToURLString(String URI) {
        String URLString;
        try {
            URL url = new URL(URI);
            URLString = url.toString();
        } catch (MalformedURLException e) {
            URLString = "";
        }
        return URLString;
    }


    /**
     * Called on click on button in createAdActivity
     * saves advert in temp list and resets current ad in presenter
     */
    public void publishAdvert() {
        setAdvertDate();
        Repository.getInstance().saveAdvert(advertisement);
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

    public String getImgURL() {
        return advertisement.getImgURL();
    }

    //Getter for testing purpose
    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public boolean isTagPressed(String tag) {
        for (String t : advertisement.getTags()) {
            if (t.equals(tag)) {
                return true;
            }
        }
        return false;

    }

    public View getView() {
        return view;
    }

    public String getTitle() {
        return advertisement.getTitle();
    }

    public String getCondition(){
        return advertisement.getCondition().toString();
    }

    public int getPrice() {
        return (int) advertisement.getPrice();
    }


}
