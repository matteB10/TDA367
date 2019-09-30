package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.view.MessagesRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MessagesPresenter {

    private List<Advertisement> adverts;
    private View view;

    public MessagesPresenter(View view) {
        this.view = view;
        Repository.getInstance().getAllAds(advertisements -> {
            if (advertisements != null) {
                this.adverts = new ArrayList<>(advertisements);
                this.view.hideLoadingScreen();
                this.view.showThumbnails();

            } else {
                System.out.println(adverts.size());
            }
        });
    }

    public void onRowPressed(String userID) {
        view.showDetailsScreen(userID);


    }


    public void bindViewHolderAtPosition(int position, MessagesRecyclerViewAdapter.
            ViewHolder holder) {
        if (adverts.size() < position) {
            return;
        }
        Advertisement a = adverts.get(position);
        holder.setDateTextView(a.getDatePublished());
        holder.setUserTextView(a.getTitle());
        holder.setId(a.getUniqueOwnerID());
        if (a.getImageFile() != null) {
            holder.setMessageImageView(a.getImageFile().toURI().toString());
        }
    }

    public int getItemCount() {
        if (adverts != null) {
            return adverts.size();

        }
        return 0;
    }


    public interface View {
        void showLoadingScreen();

        void showThumbnails();

        void hideLoadingScreen();

        void showDetailsScreen(String id);
    }
}