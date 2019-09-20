package com.masthuggis.boki.presenter;

import android.util.Log;

import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.view.RowView;

import java.util.List;

public class HomePresenter {
    private View view;
    private List<Advertisement> adverts;

    public HomePresenter(View view) {
        this.view = view;
        this.adverts = Repository.getInstance().getAllAds();
    }

    public void onBindRepositoryRowViewAtPosition(int position, RowView rowView) {
        Advertisement a = adverts.get(position);
        rowView.setId(a.getUniqueID());
        rowView.setTitle(a.getTitle());
        rowView.setPrice(a.getPrice());
        if (a.getImgURL() != null) {
            rowView.setImageUrl(a.getImgURL());
        }
    }

    public int getNumRows() {

        // TODO Måste eventuellt sätta upp en observer på all ads här?
        return Repository.getInstance().getAllAds().size();
    }

    public void onRowPressed(String uniqueIDoFAdvert) {
        // TODO: navigate to new screen with fetched mvp
        // Question: Should presenter do navigation or some navigationManager?
        Log.d("PRINT", "Row id pressed " + uniqueIDoFAdvert);
        view.showDetailsScreen(uniqueIDoFAdvert);
    }

    public interface View {
        void showLoadingScreen();
        void hideLoadingScreen();
        void showDetailsScreen(String id);
    }
}
