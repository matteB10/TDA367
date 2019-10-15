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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.masthuggis.boki.R;
import com.masthuggis.boki.injectors.DependencyInjector;
import com.masthuggis.boki.presenter.AdvertsPresenter;
import com.masthuggis.boki.presenter.AdvertsPresenterView;
import com.masthuggis.boki.presenter.HomePresenter;
import com.masthuggis.boki.presenter.ProfilePresenter;
import com.masthuggis.boki.utils.GridSpacingItemDecoration;

/**
 * Home page displaying all the adverts that have been published to the market.
 * Will also include searchPerformed and sort buttons in the future.
 */
public class HomeFragment extends AdvertsView implements AdapterView.OnItemSelectedListener {

    private HomePresenter presenter;
    private EditText searchField;

    @Override
    protected AdvertsPresenter getPresenter() {
        if (presenter == null) {
            this.presenter = new HomePresenter(this, DependencyInjector.injectDataModel());
        }
        return presenter;
    }

    @Override
    protected View onCreateHeaderLayout() {
        View header = getLayoutInflater().inflate(R.layout.home_header, null);
        setupSortSpinner(header);
        setupSearchField(header);
        return header;
    }

    private Spinner setupSortSpinner(View view) {
        Spinner spinner = view.findViewById(R.id.sortPickerSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, presenter.getSortOptions());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        return spinner;
    }

    //Adds a listener for when the user performs a searchPerformed, reacts to when enter key is pressed
    //makes the keyboard disappear then calls on the presenter to perform the searchPerformed with given input
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
        // TODO: implement
        return new TextView(getActivity());
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

    @Override
    public void showNoThumbnailsAvailableScreen() {
        // TODO: implement
    }

    @Override
    public void hideNoThumbnailsAvailableScreen() {
        // TODO: implement
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