package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.utils.ClickDelayHelper;
import com.masthuggis.boki.utils.StylingHelper;

import java.util.List;

/**
 * DetailsPresenter is the presenter class for the view called DetailsActivity.
 * Used by DetailsActivity.
 * Written by masthuggis
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

            view.nothingToDisplay();
            advertExists = false;
            return;
        } else {
            advertExists = true;
        }
        setupView();

        setUpFavouriteIcon();
    }

    /**
     * Provides a way for the view to show the values of the model. Is called from the single
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

    /**
     * Creates new chat with the owner of the advertisement.
     */
    private void createNewChat() {
        if (advertisement.getUniqueOwnerID().equals(dataModel.getUserID())) {
            view.showCanNotSendMessageToYourselfToast();
            return;
        }

        dataModel.createNewChat(advertisement.getUniqueOwnerID(), advertisement.getOwner(), advertisement.getUniqueID(),
                advertisement.getImageUrl(), chatID -> view.openChat(chatID));
    }

    private void openChat(String chatID) {
        view.openChat(chatID);
    }

    public boolean isUserOwner() {
        return dataModel.isUserOwner(advertisement);
    }

    /**
     * Tells the view to open the editview.
     */
    public void onChangedAdBtnPressed() {
        String uniqueID = advertisement.getUniqueID();
        view.showEditView(uniqueID);
    }

    /**
     * Checks if the current user already has an active chat associated with the advertisement.
     * If so, the method finds and opens the already existing chat.
     * Otherwise it creates and opens a new chat.
     */
    public void contactOwnerBtnClicked() {
        if (contactOwnerButtonClickedOnceBefore) {
            String chatID = dataModel.findChatID(advertisement.getUniqueID());
            if (chatID != null) {
                openChat(chatID);
            } else {
                createNewChat();
            }
        } else {
            view.setOwnerButtonText();
        }

        contactOwnerButtonClickedOnceBefore = true;
    }

    /**
     * Updates the current advertisement to be marked as a favourite if not already marked as such,
     * if so it removes the advertisement from the users favourites.
     * Also updates the favourite-icon in the DetailsView to correctly represent if the
     * advertisement is a favourite or not.
     */
    public void onFavouritesIconPressed() {
        if (currentAdvertIsFavourite()) {
            dataModel.removeFromFavourites(advertisement);
            view.setIsNotAFavouriteIcon();
        } else {
            dataModel.addToFavourites(advertisement);
            view.setIsAFavouriteIcon();
        }
    }

    /**
     * Initializes the favourite-icon according to if the current advertisement
     * is part of the users favourites or not.
     */
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

    /**
     * Used to make sure a user cant open multiple views of the same type by tapping a button.
     */
    public boolean canProceedWithTapAction() {
        return ClickDelayHelper.canProceedWithTapAction();
    }
    public interface View {
        void setName(String name);

        void setPrice(long price);

        void setDate(String date);

        void setImageUrl(String url);

        void setDescription(String description);

        void setTags(List<String> tags);

        void openChat(String chatID);

        void showCanNotSendMessageToYourselfToast();

        void showEditView(String uniqueID);

        void setOwnerButtonText();

        void setIsAFavouriteIcon();

        void setIsNotAFavouriteIcon();

        void hideFavouriteIcon();

        void nothingToDisplay();

        void setCondition(int condition, int color);
    }
}
