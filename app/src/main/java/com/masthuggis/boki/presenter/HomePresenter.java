package com.masthuggis.boki.presenter;

import android.content.Intent;
import android.util.Log;

import com.masthuggis.boki.backend.BookRepository;
import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.view.RowView;

import java.util.List;

public class HomePresenter {
    private View view;
    private List<Advert> adverts;

    public HomePresenter(View view) {
        this.view = view;
        this.adverts = BookRepository.getAllAdverts();
    }

    public void onBindRepositoryRowViewAtPosition(int position, RowView rowView) {
        Advert a = adverts.get(position);
        rowView.setId(a.getId());
        rowView.setTitle(a.getName());
        rowView.setPrice(a.getPrice());
        if (a.getImgUrl() != null) {
            rowView.setImageUrl(a.getImgUrl().toString());
        }
    }

    public int getNumRows() {
        return BookRepository.getAllAdverts().size();
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
