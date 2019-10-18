package com.masthuggis.boki.view;

import android.content.Context;
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
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.masthuggis.boki.R;
import com.masthuggis.boki.injectors.DependencyInjector;
import com.masthuggis.boki.presenter.HomePresenter;
import com.masthuggis.boki.utils.GridSpacingItemDecoration;
import com.masthuggis.boki.utils.ViewCreator;

/**
 * Home page displaying all the adverts that have been published to the market.
 * Will also include searchPerformed and sort buttons in the future.
 */
public class HomeFragment extends ListView implements AdapterView.OnItemSelectedListener {

    private HomePresenter presenter;
    private EditText searchField;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.presenter = new HomePresenter(this, DependencyInjector.injectDataModel());
        View v = super.onCreateView(inflater, container, savedInstanceState);
        presenter.updateAdverts();
        return v;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new ProductsRecyclerViewAdapter(getContext(), presenter);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(getContext(), 2);
    }

    @Override
    protected RecyclerView.ItemDecoration getSpacingDecorator() {
        return new GridSpacingItemDecoration(2, 40, true);
    }

    @Nullable
    @Override
    protected PullToRefreshCallback optionalPullToRefreshHandler() {
        return () -> presenter.updateFromUserInteraction();
    }

    @Override
    protected View onCreateHeaderLayout() {
        View header = getLayoutInflater().inflate(R.layout.home_header, null);
        setupSortSpinner(header);
        setupSearchField(header);
        return header;
    }

    private void setupSortSpinner(View view) {
        Spinner spinner = view.findViewById(R.id.sortPickerSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, presenter.getSortOptions());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    /**
     * Adds a listener for when the user performs a searchPerformed, reacts to when enter key is pressed
     * makes the keyboard disappear then calls on the presenter to perform the searchPerformed with given input
     * @param view
     */
    private void setupSearchField(View view) {
        searchField = view.findViewById(R.id.searchFieldEditText);
        searchField.setOnKeyListener((v, keyCode, keyEvent) -> {
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                hideKeyboard();
                performSearch();
            }
            return false;
        });

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                performSearch();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    protected View onCreateNoResultsFoundLayout() {
        return ViewCreator.createSimpleText(getActivity(), getString(R.string.noAdvertsInMarketFound));
    }

    /**
     * Notifies the presenter that the view has been destroyed. Ideally used by the presenter
     * to prevent memory leaks.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.viewIsBeingDestroyed();
    }


    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(searchField.getWindowToken(), 0);
        searchField.clearFocus(); //This and two lines above hides keyboard when searchPerformed is pressed
    }

    private void performSearch() {
        String query = searchField.getText().toString();
        presenter.searchPerformed(query);
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