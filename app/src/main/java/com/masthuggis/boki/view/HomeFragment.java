package com.masthuggis.boki.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.HomePresenter;
import com.masthuggis.boki.presenter.ProductsRecyclerViewAdapter;
import com.masthuggis.boki.utils.GridSpacingItemDecoration;

/**
 * Home page displaying all the adverts that have been published to the market.
 * Will also include filter and sort buttons in the future.
 */
public class HomeFragment extends Fragment implements HomePresenter.View, AdapterView.OnItemSelectedListener {

    private HomePresenter presenter;
    private View view;
    private ProductsRecyclerViewAdapter recyclerViewAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.home_fragment, container, false);
        this.presenter = new HomePresenter(this);
        setupSortSpinner();
        return view;
    }

    private void setupSortSpinner() {
        Spinner spinner = view.findViewById(R.id.sortPickerSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, presenter.getSortOptions());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void setupList() {
        RecyclerView recyclerView = view.findViewById(R.id.advertsRecyclerView);
        recyclerViewAdapter = new ProductsRecyclerViewAdapter(getContext(), presenter);
        recyclerView.setAdapter(recyclerViewAdapter);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 40, true));
    }

    @Override
    public void hideLoadingScreen() {
        ProgressBar progressBar = view.findViewById(R.id.loadingProgressBar);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingScreen() {
        ProgressBar progressBar = view.findViewById(R.id.loadingProgressBar);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDetailsScreen(String id) {
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("advertID", id);
        startActivity(intent);
    }


    @Override
    public void updateThumbnails() {
        if (recyclerViewAdapter == null)
            setupList();
        recyclerViewAdapter.notifyDataSetChanged();
    }

    /**
     * Called when the sorting option changes. For example, sorting for lowest price.
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        presenter.sortOptionSelected(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        presenter.sortOptionSelected(0);
    }
}