package com.masthuggis.boki.presenter;

import android.util.Log;

import com.masthuggis.boki.backend.iRepository;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.view.RowView;

import java.util.List;

public class HomePresenter {
    private View view;
    private iRepository modelRepository;
    private List<Advertisement> adverts;

    public HomePresenter(View view, iRepository modelRepository) {
        this.view = view;
        this.modelRepository = modelRepository;
        this.adverts = modelRepository.getAllAds();
    }

    public void onBindRepositoryRowViewAtPosition(int position, RowView rowView) {
        Advertisement a = adverts.get(position);
        rowView.setId(a.getUUID());
        rowView.setTitle(a.getName());
        rowView.setPrice(a.getPrice());
        if (a.getImgURL() != null) {
            rowView.setImageUrl(a.getImgURL().toString());
        }
    }

    public int getNumRows() {
        return modelRepository.getAllAds().size();
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
