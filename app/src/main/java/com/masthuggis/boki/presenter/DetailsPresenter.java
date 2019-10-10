package com.masthuggis.boki.presenter;

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


    public DetailsPresenter(View view, String advertID) {
        this.view = view;
        this.advertisement = DataModel.getInstance().getAdFromAdID(advertID);
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

        if (advertisement.getUniqueOwnerID().equals(DataModel.getInstance().getUserID())) {
            view.showToast();
            return;
        }
        //public void createNewChat(String uniqueOwnerID,String advertID, stringCallback stringCallback,String receiverUsername) {

            DataModel.getInstance().createNewChat(advertisement.getUniqueOwnerID(),advertisement.getUniqueID(), new stringCallback() {
            @Override
            public void onCallback(String chatID) {

                view.openChat(chatID);
            }
        },advertisement.getOwner());
    }


    private void openChat(String chatID) {
        view.openChat(chatID);
    }

    public boolean isUserOwner() {
        try {
            String loggedinUser = DataModel.getInstance().getUserID();
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
            if (DataModel.getInstance().getUserChats() != null) {
                for (iChat chats : DataModel.getInstance().getUserChats()) {
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
    }


}
