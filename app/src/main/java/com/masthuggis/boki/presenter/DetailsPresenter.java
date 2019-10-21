package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.utils.StylingHelper;

import java.util.List;

/**
 * DetailsPresenter is the presenter class for the view called DetailsActivity.
 * <p>
 * Validates input from the user to
 * <p>
 * It is the layer between the view and model and should therefore
 */
public class DetailsPresenter {
    private View view;
    private Advertisement advertisement;
    private DataModel dataModel;
    private boolean advertExists;
    private boolean contactOwnerButtonClickedOnceBefore = false;

    public DetailsPresenter(View view, String advertID, DataModel dataModel) {
        this.dataModel = dataModel;
        this.view = view;
        this.advertisement = this.dataModel.getAdFromAdID(advertID);
        if (advertisement == null) {
            view.nothingToDisplay("Hittar ingen annons, var vänlig uppdatera vyn.");
            advertExists = false;
            return;
        } else {
            advertExists = true;
        }
        setupView();

        setUpFavouriteIcon();
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
        view.setTags(advertisement.getTags());
        setCondition();
        if (advertisement.getImageUrl() != null) { //the url here is null right after upload
            view.setImageUrl(advertisement.getImageUrl());
        }
    }

    private void setCondition() {
        int drawable = StylingHelper.getConditionDrawable(advertisement.getCondition(),false);
        int text = StylingHelper.getConditionText(advertisement.getCondition());
        view.setCondition(text, drawable);
    }

    private void createNewChat() {
        if (advertisement.getUniqueOwnerID().equals(dataModel.getUserID())) {
            view.showCanNotSendMessageToHimselfToast();
            return;
        }

        dataModel.createNewChat(advertisement.getUniqueOwnerID(), dataModel.getUserID(), advertisement.getUniqueID(),
                advertisement.getImageUrl(), chatID -> view.openChat(chatID));
    }

    private void openChat(String chatID) {
        view.openChat(chatID);
    }

    public boolean isUserOwner() {
        return dataModel.isUserOwner(advertisement);
    }

    public void onChangedAdBtnPressed() {
        String uniqueID = advertisement.getUniqueID();
        view.showEditView(uniqueID);
    }

    public void contactOwnerBtnClicked() {
        if (contactOwnerButtonClickedOnceBefore) {
            String chatID = dataModel.findChatID(advertisement.getUniqueID());
            if (chatID != null) {
                openChat(chatID);
            } else {
                createNewChat();
            }
        } else {
            view.setOwnerButtonText("Starta chatt");
        }

        contactOwnerButtonClickedOnceBefore = true;
    }

    public void onFavouritesIconPressed() {
        if (currentAdvertIsFavourite()) {
            dataModel.removeFromFavourites(advertisement);
            view.setIsNotAFavouriteIcon();
        } else {
            dataModel.addToFavourites(advertisement);
            view.setIsAFavouriteIcon();
        }
    }

    public void setUpFavouriteIcon() {
        if (isUserOwner()) {
            view.hideFavouriteIcon();
        } else if (currentAdvertIsFavourite()) {
            view.setIsAFavouriteIcon();
        } else {
            view.setIsNotAFavouriteIcon();
        }
    }

    private boolean currentAdvertIsFavourite() {
        return dataModel.isAFavourite(advertisement);
    }
    public boolean advertExists(){
        return advertExists;
    }

    public interface View {
        void setName(String name);

        void setPrice(long price);

        void setDate(String date);

        void setImageUrl(String url);

        void setDescription(String description);

        void setTags(List<String> tags);

        void openChat(String chatID);

        void showCanNotSendMessageToHimselfToast();

        void showEditView(String uniqueID);

        void setOwnerButtonText(String content);

        void setIsAFavouriteIcon();

        void setIsNotAFavouriteIcon();

        void hideFavouriteIcon();

        void nothingToDisplay(String message);

        void setCondition(int condition, int color);
    }
}
