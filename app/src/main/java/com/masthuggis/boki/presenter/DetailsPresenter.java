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

    public void createNewChat() {

        if (advertisement.getUniqueOwnerID().equals(DataModel.getInstance().getUserID())) {
            view.showToast();
            return;
        }
        DataModel.getInstance().createNewChat(advertisement, new stringCallback() {
            @Override
            public void onCallback(String chatID) {

                view.openChat(chatID);
            }
        });
    }

    //TODO KANSKE ÄNDRA DETTA SÅ ATT DET GÖRS I SAMMA METOD?


    public void openChat(String chatID) {
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
        if (contactOwnerButtonText.equals("Starta chatt") && (DataModel.getInstance().getUserChats() != null)) {
            for (iChat chats : DataModel.getInstance().getUserChats()) {
                if (chats.getAdvert().getUniqueID().equals(advertisement.getUniqueID())) {
                    openChat(chats.getChatID());
                    return;
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

        void openChat(String uniqueOwnerID);

        void showToast();

        void showEditView(String uniqueID);

        void setOwnerButtonText(String content);
    }


}
