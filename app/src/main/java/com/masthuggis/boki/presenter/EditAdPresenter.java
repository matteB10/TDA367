package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;

public class EditAdPresenter {

    private View view;
    private Advertisement advertisement;

    public EditAdPresenter(View view, String advertID){
        this.view = view;
        this.advertisement = DataModel.getInstance().getAdFromAdID(advertID);
        setupView();
    }

    public void removeAdPressed(){
        String adID = advertisement.getUniqueID();
        DataModel.getInstance().removeExistingAdvert(adID);
    }


    private void setupView() {
        view.setName(advertisement.getTitle());
        view.setDescription(advertisement.getDescription());
        if (advertisement.getImageFile() != null) {
            view.setImageUrl(advertisement.getImageFile().toURI().toString());
        }
    }

    public interface View {
        void setName(String name);
        void setImageUrl(String url);
        void setDescription(String description);
    }
//-------------------------------------------------------
public void titleChanged(String title) {
    advertisement.setTitle(title);
}

}
