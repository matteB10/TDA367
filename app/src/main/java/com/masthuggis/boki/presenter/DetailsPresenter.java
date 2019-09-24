package com.masthuggis.boki.presenter;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.masthuggis.boki.R;
import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.model.Advertisement;

public class DetailsPresenter {
    private View view;
    private Advertisement advertisement;

    public DetailsPresenter(View view, String advertID) {
        this.view = view;
        this.advertisement = Repository.getInstance().getAdFromId(advertID);

        setupView();
    }

    private void setupView() {
        view.setName(advertisement.getTitle());
        view.setPrice(advertisement.getPrice());
        view.setDescription(advertisement.getDescription());
        setCondition();
        if (advertisement.getImgURL() != null) {
            view.setImageUrl(advertisement.getImgURL());}



    }

    public interface View {
        void setName(String name);
        void setPrice(int price);
        void setImageUrl(String url);
        void setDescription(String description);
        void setConditionGood();
        void setConditionNew();
        void setConditionOk();
    }

    private void setCondition(){
        switch (advertisement.getConditon()){
            //TODO: get access to res/colors to return right color depending on condition
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
