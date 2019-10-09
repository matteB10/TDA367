package com.masthuggis.boki.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.ProfilePresenter;
import com.masthuggis.boki.utils.GridSpacingItemDecoration;

/**
 * Profile page used for displaying the adverts that the user have published. Also have an settings
 * button to navigate to the app-wide settings.
 */
public class ProfileFragment extends Fragment implements ProfilePresenter.View {
    private ProfilePresenter presenter;
    private View view;
    private RecyclerView recyclerView;
    private ProductsRecyclerViewAdapter adapter;
    private Button signOutBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.profile_fragment,container,false);
        this.presenter = new ProfilePresenter(this);
        setupHeader();
        return view;
    }


    private void setupHeader() {
        Button settingsButton = view.findViewById(R.id.profileSettingsButton);
        settingsButton.setOnClickListener(view -> presenter.onSettingsButtonPressed());

        signOutBtn = view.findViewById(R.id.signInButton);
        signOutBtn.setOnClickListener(view -> presenter.onSignOutPressed());
    }

    private void setupList() {
        setupRecycler();
        setupAdapter();
        setupListLayout();
    }

    private void setupRecycler() {
        recyclerView = view.findViewById(R.id.profileRecyclerView);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void setupAdapter() {
        adapter = new ProductsRecyclerViewAdapter(getContext(), presenter);
        recyclerView.setAdapter(adapter);
    }

    private void setupListLayout() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, 40, true));
    }

    @Override
    public void updateThumbnails() {
        if (adapter == null) {
            setupList();
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showSettingsScreen() {
        Intent intent = new Intent(getContext(), SettingsActivity.class);
        startActivity(intent);
    }


    @Override
    public void showLoginScreen() {
        Intent intent = new Intent(getContext(), SignInActivity.class);
        startActivity(intent);
    }

    @Override
    public void showLoadingScreen() {
        // TODO: show loading animation
    }

    @Override
    public void hideLoadingScreen() {
        // TODO: hide loading animation


    }
}
