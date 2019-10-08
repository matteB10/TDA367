package com.masthuggis.boki.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.HomePresenter;
import com.masthuggis.boki.utils.GridSpacingItemDecoration;

/**
 * Home page displaying all the adverts that have been published to the market.
 * Will also include search and sort buttons in the future.
 */
public class HomeFragment extends Fragment implements HomePresenter.View, AdapterView.OnItemSelectedListener {

    private HomePresenter presenter;
    private View view;
    private ProductsRecyclerViewAdapter recyclerViewAdapter;
    private EditText searchField;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.home_fragment, container, false);
        this.presenter = new HomePresenter(this);
        setupSortSpinner();
        setupSearchField();
        hideLoadingScreen();
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

    //Adds a listener for when the user performs a search, reacts to when enter key is pressed
    //makes the keyboard disappear then calls on the presenter to perform the search with given input
    private void setupSearchField() {
        searchField = view.findViewById(R.id.searchFieldEditText);
        searchField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) { //Make sure stuff happens when enter is pressed
                    hideKeyboard();
                    performSearch();
                }
                return false;
            }
        });

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0)
                    performSearch();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
        searchField.clearFocus(); //This and two lines above hides keyboard when search is pressed
    }

    private boolean performSearch() {
        showLoadingScreen();
        presenter.search(searchField.getText().toString(), new SearchCallback() { //Actually perform search
            @Override
            public void onCallback() {
                hideLoadingScreen(); //Necessary callback since search on query happens on other thread
            }
        });
        return true;
    }

    @Override
    public void hideLoadingScreen() {
        ProgressBar progressBar = view.findViewById(R.id.loadingProgressBar);
        progressBar.setVisibility(View.GONE);
        RecyclerView recyclerView = view.findViewById(R.id.advertsRecyclerView);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showLoadingScreen() {
        ProgressBar progressBar = view.findViewById(R.id.loadingProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        RecyclerView recyclerView = view.findViewById(R.id.advertsRecyclerView);
        recyclerView.setVisibility(View.GONE);

    }

    @Override
    public void showDetailsScreen(String id) {
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("advertID", id);
        startActivity(intent);
    }


    @Override
    public void updateThumbnails() {
        if (recyclerViewAdapter == null) {
            setupList();
        } else {
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Called when the sorting option changes. For example, sorting for lowest price.
     *
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