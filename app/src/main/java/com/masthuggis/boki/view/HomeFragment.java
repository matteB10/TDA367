package com.masthuggis.boki.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.HomePresenter;
import com.masthuggis.boki.presenter.ProductsRecyclerViewAdapter;
import com.masthuggis.boki.utils.GridSpacingItemDecoration;

public class HomeFragment extends Fragment implements HomePresenter.View {

    private HomePresenter presenter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment, container, false);

        this.presenter = new HomePresenter(this);
        setupList(v);

        return v;

    }

    private void setupList(View v) {
        RecyclerView recyclerView = v.findViewById(R.id.advertsRecyclerView);
        ProductsRecyclerViewAdapter adapter = new ProductsRecyclerViewAdapter(getContext(), presenter);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 40, true));
    }

    @Override
    public void hideLoadingScreen() {
        // TODO: implement loading screen and hide everything else
    }

    @Override
    public void showLoadingScreen() {
        // TODO: display the screen again
    }

}
