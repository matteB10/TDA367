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
        rowView.setRowIndex(position);
        rowView.setTitle(a.getName());
        rowView.setPrice(a.getPrice());
        if (a.getImgURL() != null) {
            rowView.setImageUrl(a.getImgURL().toString());
        }
    }

    public int getNumRows() {
        return Repository.getInstance().getAllAds().size();
    }

    public void onRowPressed(int rowIndex) {
        // TODO: navigate to new screen with fetched mvp
        Log.d("PRINT", "Row index pressed " + rowIndex);
    }

    public interface View {
        void showLoadingScreen();
        void hideLoadingScreen();
    }
}
