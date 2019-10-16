package com.masthuggis.boki.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.masthuggis.boki.R;
import com.masthuggis.boki.injectors.DependencyInjector;
import com.masthuggis.boki.presenter.AdvertsPresenter;
import com.masthuggis.boki.presenter.HomePresenter;

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
        setFilterButtonListener(header);
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

    /**
     * Collects data from bundle if passed when creating the fragment
     */
/*
    void collectData(){
        if(this.getArguments() != null){
            try {
                int price = this.getArguments().getInt("price");
                List<String> filterTags = this.getArguments().getStringArrayList("tags");
                presenter.addFilters(price,filterTags);
            }catch (NullPointerException n){
                n.printStackTrace();
                System.out.println("could not find any passed on data");
            }
        }

    }*/

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
        // TODO: implement
        return new TextView(getActivity());
    }
    private void setFilterButtonListener(View view){
        Button filterButton = view.findViewById(R.id.homeFilterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFilterFragment();
            }
        });
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

    private void startFilterFragment(){
        Fragment filter = new FilterFragment();
        loadFragment(filter);
    }
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commitAllowingStateLoss();
            return true;
        }
        return false;
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