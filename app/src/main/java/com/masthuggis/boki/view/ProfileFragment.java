package com.masthuggis.boki.view;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.masthuggis.boki.R;
import com.masthuggis.boki.injectors.DependencyInjector;
import com.masthuggis.boki.presenter.AdvertsPresenter;
import com.masthuggis.boki.presenter.AdvertsPresenterView;
import com.masthuggis.boki.presenter.ProfilePresenter;

/**
 * Profile page used for displaying the adverts that the user have published. Also have an settings
 * button to navigate to the app-wide settings.
 */
public class ProfileFragment extends AdvertsView implements ProfilePresenter.View, AdvertsPresenterView {
    private ProfilePresenter presenter;

    @Override
    protected AdvertsPresenter getPresenter() {
        if (presenter == null) {
            this.presenter = new ProfilePresenter(this, DependencyInjector.injectDataModel());
        }
        return presenter;
    }

    @Override
    protected View onCreateHeaderLayout() {
        View header = getLayoutInflater().inflate(R.layout.profile_header, null);
        Button signOutBtn = header.findViewById(R.id.signOutButton);
        signOutBtn.setOnClickListener(v -> presenter.onSignOutPressed());
        return header;
    }

    @Override
    protected View onCreateNoResultsFoundLayout() {
        View noResults = getLayoutInflater().inflate(R.layout.profile_no_adverts, null);
        Button goToPublishPageButton = noResults.findViewById(R.id.profileNoAdvertsSellYourBookButton);
        goToPublishPageButton.setOnClickListener(v -> showCreateAdPage());
        return noResults;
    }

    @Override
    public void showLoginScreen() {
        Intent intent = new Intent(getContext(), SignInActivity.class);
        startActivity(intent);
    }

    private void showCreateAdPage() {
        Intent intent = new Intent(getActivity(), CreateAdActivity.class);
        startActivity(intent);
    }
}
