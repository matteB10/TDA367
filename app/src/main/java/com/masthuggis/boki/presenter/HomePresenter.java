package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.Advert;
import com.masthuggis.boki.view.RowView;

import java.util.ArrayList;
import java.util.List;

public class HomePresenter {
    private View view;

    public HomePresenter(View view) {
        this.view = view;
    }

    public void onBindRepositoryRowViewAtPosition(int position, RowView rowView) {
        // TODO: get adverts from repository.
        //Repository repo = repositories.get(position);
        rowView.setTitle("Linjär algebra");
        rowView.setPrice(200);
        rowView.setImageUrl("");
    }

    public int getNumRows() {
        // TODO: get lenght from repo
        return 3;
    }

    public interface View {
        void showLoadingScreen();
        void hideLoadingScreen();
    }
}
