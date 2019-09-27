package com.masthuggis.boki.presenter;

import android.content.Context;

import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.utils.ConditionStylingHelper;

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
        if (advertisement.getImgURL() != null) {
            view.setImageUrl(advertisement.getImgURL());
        }

    }
    private void setCondition(){
        ConditionStylingHelper helper = ConditionStylingHelper.getInstance();
        int color = helper.getConditionColor(advertisement.getCondition(),view.getContext());
        String text = helper.getConditionText(advertisement.getCondition(), view.getContext());
        view.setCondition(text,color);
    }

    public interface View {
        void setName(String name);

        void setPrice(long price);
        void setDate( String date);
        void setImageUrl(String url);
        void setCondition(String text, int color);
        void setDescription(String description);
        Context getContext();

    }


}
