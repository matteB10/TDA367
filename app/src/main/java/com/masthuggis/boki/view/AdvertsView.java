package com.masthuggis.boki.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.AdvertsPresenter;
import com.masthuggis.boki.presenter.AdvertsPresenterView;
import com.masthuggis.boki.utils.GridSpacingItemDecoration;

abstract class AdvertsView extends Fragment implements AdvertsPresenterView {
    protected AdvertsPresenter presenter;
    private View view;
    private ProductsRecyclerViewAdapter recyclerViewAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.adverts_view, container, false);
        this.presenter = getPresenter();
        this.presenter.initPresenter();

        setupHeader();
        onCreateHeader();
        return view;
    }

    public View getView() {
        return view;
    }

    private void setupHeader() {
        View header = onCreateHeader();
        LinearLayout headerContainer = view.findViewById(R.id.advertsViewHeader);
        headerContainer.addView(header);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.viewIsBeingDestroyed();
    }

    protected abstract View onCreateHeader();

    protected abstract AdvertsPresenter getPresenter();

    private void setupList() {
        RecyclerView recyclerView = view.findViewById(R.id.advertsViewRecycler);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerViewAdapter = new ProductsRecyclerViewAdapter(getContext(), presenter);
        recyclerView.setAdapter(recyclerViewAdapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 40, true));
    }

    @Override
    public void updateThumbnails() {
        if (recyclerViewAdapter == null) {
            setupList();
        } else {
            recyclerViewAdapter.notifyDataSetChanged();
        }

        //LinearLayout noResultsContainer = view.findViewById(R.id.noResultsFound);
        //noResultsContainer.setVisibility(View.GONE);
    }

    @Override
    public void showNoThumbnailsAvailableScreen() {
        //LinearLayout noResultsContainer = view.findViewById(R.id.noResultsFound);
        //noResultsContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDetailsScreen(String id) {
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("advertID", id);
        startActivity(intent);
    }

    @Override
    public void showLoadingScreen() {
        ProgressBar progressBar = view.findViewById(R.id.advertsViewProgressbar);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingScreen() {
        ProgressBar progressBar = view.findViewById(R.id.advertsViewProgressbar);
        progressBar.setVisibility(View.GONE);
    }
}
