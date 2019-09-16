package com.masthuggis.boki.presenter;

import android.util.Log;

import com.masthuggis.boki.view.RowView;

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
        rowView.setRowIndex(position);
    }

    public int getNumRows() {
        // TODO: get lenght from repo
        return 3;
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
