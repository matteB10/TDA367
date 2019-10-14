package com.masthuggis.boki.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.FavouritesPresenter;

public class FavoritesFragment extends Fragment {
    private FavouritesPresenter presenter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        initPresenter();
        return inflater.inflate(R.layout.favorites_fragment, container, false);
    }

    private void initPresenter() {
        this.presenter = new FavouritesPresenter();
    }
}
