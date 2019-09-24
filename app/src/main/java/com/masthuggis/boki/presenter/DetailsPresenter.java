package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.model.Advertisement;

/**
 * DetailsPresenter is the presenter class for the view called DetailsActivity.
 *
 * Validates input from the user to
 *
 * It is the layer between the view and model and should therefore
 */


public class DetailsPresenter {
    private View view;
    private Advertisement advertisement;

    public DetailsPresenter(View view, String advertID) {
        this.view = view;
        this.advertisement = Repository.getInstance().getAdFromAdID(advertID);

        setupView();
    }

    /**
     * @setupView Provides a way for the view to show the values of the model. Is called from the single
     * constructor of the class.
     */

    private void setupView() {
        view.setName(advertisement.getTitle());
        view.setPrice(advertisement.getPrice());
        view.setDescription(advertisement.getDescription());
        setCondition();
        if (advertisement.getImgURL() != null) {
            view.setImageUrl(advertisement.getImgURL());
        }

    }

    public interface View {
        void setName(String name);

        void setPrice(long price);

        void setImageUrl(String url);

        void setDescription(String description);
        void setConditionGood();
        void setConditionNew();
        void setConditionOk();

    }

    //TODO kommentera detta.

    private void setCondition(){
        switch (advertisement.getCondition()){

            case NEW:
                view.setConditionNew();
                break;
            case GOOD:
                view.setConditionGood();
                break;
            case OK:
                view.setConditionOk();
        }

    }

}
