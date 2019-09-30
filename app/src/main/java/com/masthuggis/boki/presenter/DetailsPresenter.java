package com.masthuggis.boki.presenter;

import android.content.Context;

import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.utils.ConditionStylingHelper;
import com.masthuggis.boki.utils.iConditionable;

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
        view.setDate(advertisement.getDatePublished());
        setCondition();
        if (advertisement.getImageFile() != null) {
            view.setImageUrl(advertisement.getImageFile().toURI().toString());
        }

    }
    private void setCondition(){
        int color = ConditionStylingHelper.getConditionDrawable(advertisement.getCondition());
        int text = ConditionStylingHelper.getConditionText(advertisement.getCondition());
        view.setCondition(text,color);
    }

    public interface View extends iConditionable {
        void setName(String name);

        void setPrice(long price);
        void setDate( String date);
        void setImageUrl(String url);
        void setDescription(String description);
        Context getContext();

    }


}
