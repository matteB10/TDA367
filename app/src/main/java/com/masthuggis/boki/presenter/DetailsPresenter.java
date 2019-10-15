package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.callbacks.MarkedAsFavouriteCallback;
import com.masthuggis.boki.backend.callbacks.stringCallback;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.model.iChat;
import com.masthuggis.boki.utils.StylingHelper;
import com.masthuggis.boki.utils.iConditionable;

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
    boolean isMarkedAsFavourite;


    public DetailsPresenter(View view, String advertID, DataModel dataModel) {
        this.dataModel = dataModel;
        this.view = view;
        this.advertisement = this.dataModel.getAdFromAdID(advertID);
        setupView();
        //initFavouriteStar();

        dataModel.isAdMarkedAsFavourite(advertID, new MarkedAsFavouriteCallback() {
            @Override
            public void onCallback(boolean markedAsFavourite) {
                isMarkedAsFavourite = markedAsFavourite;
            }
        });
        initFavveStar();
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
        int drawable = StylingHelper.getConditionDrawable(advertisement.getCondition());
        int text = StylingHelper.getConditionText(advertisement.getCondition());
        view.setCondition(text, drawable);
    }

    private void createNewChat() {

        if (advertisement.getUniqueOwnerID().equals(dataModel.getUserID())) {
            view.showToast();
            return;
        }
        //public void createNewChat(String uniqueOwnerID,String advertID, stringCallback stringCallback,String receiverUsername) {

        dataModel.createNewChat(advertisement.getUniqueOwnerID(), advertisement.getUniqueID(), new stringCallback() {
            @Override
            public void onCallback(String chatID) {

                view.openChat(chatID);
            }
        }, advertisement.getOwner());
    }

    private void initFavouriteStar() {
        dataModel.isAdMarkedAsFavourite(advertisement.getUniqueID(), new MarkedAsFavouriteCallback() {
            @Override
            public void onCallback(boolean markedAsFavourite) {
                if (markedAsFavourite) {
                    view.setFavouriteIcon();
                } else {
                    view.setNotFavouriteIcon();
                }
            }
        });
    }

    private void initFavveStar() {
        if (advertisement.isMarkedAsFavourite()) {
            view.setFavouriteIcon();
        } else {
            view.setNotFavouriteIcon();
        }
    }

    private void openChat(String chatID) {
        view.openChat(chatID);
    }

    public boolean isUserOwner() {
        try {
            String loggedinUser = dataModel.getUserID();
            String ownerofAd = advertisement.getUniqueOwnerID();
            return loggedinUser.equals(ownerofAd);
        } catch (Exception e) {
            return false;
        }
    }

    public void onChangedAdBtnPressed() {
        String uniqueID = advertisement.getUniqueID();
        view.showEditView(uniqueID);
    }

    public void contactOwnerButtonClicked(String contactOwnerButtonText) {
        if (contactOwnerButtonText.equals("Starta chatt")) {
            if (dataModel.getUserChats() != null) {
                for (iChat chats : dataModel.getUserChats()) {
                    if (chats.getUniqueIDAdID().equals(advertisement.getUniqueID())) {
                        openChat(chats.getChatID());
                        return;
                    }
                }
            }
            createNewChat();
        } else {
            view.setOwnerButtonText("Starta chatt");
        }
    }

    //Necessary to change local variable (isMarkedAsFavourite) inside method, otherwise it has to update from firebase while in Detail View
    public void onFavouritesIconPressed() {
        if (isMarkedAsFavourite) {
            advertisement.markAsNotFavourite();
            dataModel.removeFromFavourites(advertisement.getUniqueID());
            view.setNotFavouriteIcon();
        } else {
            advertisement.markAsFavourite();
            dataModel.addToFavourites(advertisement.getUniqueID());
            view.setFavouriteIcon();
        }
    }

    public interface View extends iConditionable {
        void setName(String name);

        void setPrice(long price);

        void setDate(String date);

        void setImageUrl(String url);

        void setDescription(String description);

        void setTags(List<String> tags);

        void openChat(String chatID);

        void showToast();

        void showEditView(String uniqueID);

        void setOwnerButtonText(String content);

        void setFavouriteIcon();

        void setNotFavouriteIcon();
    }


}
