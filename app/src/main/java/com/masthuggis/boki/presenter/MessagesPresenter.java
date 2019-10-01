package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.backend.UserRepository;
import com.masthuggis.boki.backend.advertisementCallback;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.model.iChat;
import com.masthuggis.boki.view.MessagesRecyclerViewAdapter;

import java.util.List;

public class MessagesPresenter {

    private List<iChat> chats;
    private View view;

    public MessagesPresenter(View view) {
        this.view = view;
        if(DataModel.getInstance().isLoggedIn()){
            view.isLoggedIn();
        }
        if (DataModel.getInstance().getUserChats() == null) {
        }else{


        chats = DataModel.getInstance().getUserChats();}
    }


    public void onRowPressed(String userID) {
        view.showDetailsScreen(userID);


    }


    public void bindViewHolderAtPosition(int position, MessagesRecyclerViewAdapter.
            ViewHolder holder) {
        if (chats.size() < position || chats==null) {
            return;
        }
        iChat c = chats.get(position);
        holder.setUserTextView(c.getReceiver());

        // holder.setDateTextView(c.getDatePublished());
        // holder.setUserTextView(c.getTitle());
        //holder.setId(c.getUniqueOwnerID());
    /*    if (c.getImageFile() != null) {
            holder.setMessageImageView(c.getImageFile().toURI().toString());
        }*/
    }

    public int getItemCount() {
        if (chats != null) {
            return chats.size();

        }
        return 0;
    }


    public interface View {
        void showLoadingScreen();

        void showThumbnails();

        void hideLoadingScreen();

        void showDetailsScreen(String id);

        void isLoggedIn();
    }
}