package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.view.MessagesRecyclerViewAdapter;

import java.util.List;

public class MessagesPresenter {


    public void onMessageClicked(){

    }


    public void bindViewHolderAtPosition(int position, MessagesRecyclerViewAdapter.ViewHolder holder) {

        if (Repository.getInstance().getAdsFromUniqueOwnerID(Repository.mockUniqueUserID).size() < position) {
            return;
        }

        Advertisement a = Repository.getInstance().getAdsFromUniqueOwnerID(Repository.mockUniqueUserID).get(position);

        holder.setDateTextView("TEMPDATE");
        holder.setUserTextView("TEMP USER");
        if (a.getImgURL() != null) {
            holder.setMessageImageView(a.getImgURL());
        }
    }
}
