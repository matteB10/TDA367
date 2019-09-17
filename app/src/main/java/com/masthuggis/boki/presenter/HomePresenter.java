package com.masthuggis.boki.presenter;

import android.content.Intent;
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
        rowView.setId(a.getUserId());
        rowView.setTitle(a.getName());
        rowView.setPrice(a.getPrice());
        if (a.getImgURL() != null) {
            rowView.setImageUrl(a.getImgURL().toString());
        }
    }

    public int getNumRows() {
        return Repository.getInstance().getAllAds().size();
    }

    public void onRowPressed(long idOfRowPressed) {
        // TODO: navigate to new screen with fetched mvp
        // Question: Should presenter do navigation or some navigationManager?
        Log.d("PRINT", "Row id pressed " + idOfRowPressed);
        view.showDetailsScreen(idOfRowPressed);
    }

    public interface View {
        void showLoadingScreen();
        void hideLoadingScreen();
        void showDetailsScreen(long id);
    }
}
