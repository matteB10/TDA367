package com.masthuggis.boki.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.masthuggis.boki.R;
import com.masthuggis.boki.injectors.DependencyInjector;
import com.masthuggis.boki.presenter.ProfilePresenter;
import com.masthuggis.boki.utils.GridSpacingItemDecoration;
import com.masthuggis.boki.utils.ViewCreator;

/**
 * Profile page used for displaying the adverts that the user have published. Also have an settings
 * button to navigate to the app-wide settings.
 */
public class ProfileFragment extends ListView implements ProfilePresenter.View {
    private ProfilePresenter presenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.presenter = new ProfilePresenter(this, DependencyInjector.injectDataModel());
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
        return null;
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
        LinearLayout container = noResults.findViewById(R.id.profileNoAdvertsFound);
        container.addView(ViewCreator.createSimpleText(getActivity(), getString(R.string.noUserAdvertsFound)), 0);
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
