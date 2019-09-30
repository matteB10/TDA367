package com.masthuggis.boki.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.ProfilePresenter;
import com.masthuggis.boki.utils.GridSpacingItemDecoration;

/**
 * Profile page used for displaying the adverts that the user have published. Also have an settings
 * button to navigate to the app-wide settings.
 */
public class ProfileFragment extends Fragment implements ProfilePresenter.View {
    private ProfilePresenter presenter = new ProfilePresenter(this);
    private View view;
    private RecyclerView recyclerView;
    private ProductsRecyclerViewAdapter adapter;


    FirebaseUser user;
    FirebaseAuth auth;
    Button signOutButton;
    Button signinButton;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.profile_fragment,container,false);
        setupHeader(view);
        Button signOutButton = view.findViewById(R.id.signOutButton);
        Button signinButton = view.findViewById(R.id.signinButton);

        signinButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            });

        signOutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getActivity(),"you are now signed out",
                    Toast.LENGTH_LONG).show();
        });

        return view;
    }





    private void setupHeader(View v) {
        Button settingsButton = v.findViewById(R.id.profileSettingsButton);
        settingsButton.setOnClickListener(view -> presenter.onSettingsButtonPressed());
    }

    private void setupList() {
        setupRecycler(view);
        setupAdapter();
        setupListLayout();
    }

    private void setupRecycler(View v) {
        recyclerView = v.findViewById(R.id.profileRecyclerView);
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
    public void setIsUserLoggedIn(boolean isUserLoggedIn) {
        if (isUserLoggedIn) {
            // TODO: Display the users books on sale
        } else {
            // TODO: ask user to log in? What actions should be taken?
        }
    }

    @Override
    public void updateItemsOnSale() {
        setupList();
    }

    @Override
    public void showSettingsScreen() {
        Intent intent = new Intent(getContext(), SettingsActivity.class);
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


