package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.utils.FormHelper;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Presenter class handling the createAdActivity. Validates input from
 * user and saves in textfields if activity is temporary terminated.
 */

public class CreateAdPresenter{

    private Advertisement advertisement;


    private View view;
    private boolean validPrice;


    public CreateAdPresenter(View view) {
        this.view = view;
        this.advertisement = Repository.getInstance().createAdvert();
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
        Repository.getInstance().saveAdvert(advertisement);
        advertisement = null;
    }
    public String getId(){
        return advertisement.getUniqueID();
    }

    //Getter for testing purpose
    public Advertisement getAdvertisement(){
        return advertisement;
    }
    public boolean isTagPressed(String tag){
        for(String t :advertisement.getTags()){
            if(t.equals(tag)){
                return true;
            }
        }
        return false;

    }

}
