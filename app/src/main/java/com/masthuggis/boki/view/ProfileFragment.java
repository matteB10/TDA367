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
    private Button signOutBtn;

    @Override
    protected AdvertsPresenter getPresenter() {
        if (presenter == null) {
            this.presenter = new ProfilePresenter(this, DependencyInjector.injectDataModel());
        }
        return presenter;
    }

    @Override
    protected View onCreateHeader() {
        View header = getLayoutInflater().inflate(R.layout.profile_header, null);
        signOutBtn = header.findViewById(R.id.signOutButton);
        signOutBtn.setOnClickListener(view -> presenter.onSignOutPressed());
        return header;
    }

    @Override
    public void showLoginScreen() {
        Intent intent = new Intent(getContext(), SignInActivity.class);
        startActivity(intent);
    }
}
