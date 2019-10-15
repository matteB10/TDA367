package com.masthuggis.boki.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.masthuggis.boki.R;
import com.masthuggis.boki.injectors.DependencyInjector;
import com.masthuggis.boki.presenter.AdvertsPresenter;
import com.masthuggis.boki.presenter.AdvertsPresenterView;
import com.masthuggis.boki.presenter.ProfilePresenter;
import com.masthuggis.boki.utils.GridSpacingItemDecoration;

/**
 * Profile page used for displaying the adverts that the user have published. Also have an settings
 * button to navigate to the app-wide settings.
 */
public class ProfileFragment extends AdvertsView implements ProfilePresenter.View, AdvertsPresenterView {
    private ProfilePresenter presenter;
    private View view;
    private Button signOutBtn;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.presenter = new ProfilePresenter(this, DependencyInjector.injectDataModel());
        super.initView(inflater, container, presenter);
        this.view = super.getView();
        return view;
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
