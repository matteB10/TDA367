package com.masthuggis.boki.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.AdvertsPresenter;
import com.masthuggis.boki.presenter.AdvertsPresenterView;

/**
 * Abstract class to be used by views wanting to display a list of adverts.
 */
public abstract class AdvertsView extends Fragment implements AdvertsPresenterView {
    protected AdvertsPresenter presenter;
    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerViewAdapter;
    private LinearLayout noAdvertsFoundContainer;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.adverts_view, container, false);
        this.presenter = getPresenter();

        setupHeader();
        setupNoResultsFoundView();
        setupPullToRefresh();
        presenter.initPresenter();

        return view;
    }

    /**
     * Setups the layout to be used on top of the list of adverts. It could be viewed as a header
     * of the screen.
     */
    private void setupHeader() {
        View header = onCreateHeaderLayout();
        if (header != null) {
            LinearLayout headerContainer = view.findViewById(R.id.advertsViewHeader);
            headerContainer.addView(header);
        }
    }

    /**
     * Setups the layout to be displayed when no results are found. The default is for this view
     * to be hidden. The view will be viewable if the presenter asks it to showed. The layout to
     * be used is implemented in the subclass.
     */
    private void setupNoResultsFoundView() {
        View noResultsFound = onCreateNoResultsFoundLayout();
        noAdvertsFoundContainer = view.findViewById(R.id.advertsViewNoAdvertsFound);
        noAdvertsFoundContainer.addView(noResultsFound);
        noAdvertsFoundContainer.setVisibility(View.GONE);
    }

    /**
     * Setups the pull to refresh by asking the concrete implementation if they want to use the
     * functionality. If they do, the action handler is setup, else it is disabled.
     */
    private void setupPullToRefresh() {
        SwipeRefreshLayout swipeRefresh = view.findViewById(R.id.pullToRefresh);
        if (shouldUsePullToRefresh()) {
            swipeRefresh.setOnRefreshListener(() -> {
                optionalPullToRefreshHandler().onCallback();
                swipeRefresh.setRefreshing(false);
            });
        } else {
            disablePullToRefresh();
        }
    }

    /**
     * Setups the recyclerviews adapter, layout and spacing
     */
    private void setupList() {
        recyclerView = view.findViewById(R.id.advertsViewRecycler);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerViewAdapter = getAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(getLayoutManager());
        recyclerView.addItemDecoration(getSpacingDecorator());
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

    /**
     * Asks the concrete implementations to provide a layout to be used above the adverts list,
     * acting as a header.
     *
     * @return
     */
    protected abstract View onCreateHeaderLayout();

    /**
     * Asks the concrete implementation to provide a layout for when no adverts are found.
     *
     * @return
     */
    protected abstract View onCreateNoResultsFoundLayout();

    /**
     * Asks the concrete implementation to provide the presenter to be used.
     *
     * @return
     */
    protected abstract AdvertsPresenter getPresenter();

    protected abstract RecyclerView.Adapter getAdapter();

    protected abstract RecyclerView.LayoutManager getLayoutManager();

    protected abstract RecyclerView.ItemDecoration getSpacingDecorator();

    /**
     * Gives the concrete implementation the option to provide an action, and therefor activate
     * the pull-to-refresh behavior. If pull-to-refresh is not desired a null can be returned.
     * @return
     */
    protected abstract @Nullable PullToRefreshCallback optionalPullToRefreshHandler();

    /**
     * Updates the data that is displayed in the recyclerView. Will be called when for example
     * the sorting changes or a search is performed.
     */
    @Override
    public void updateThumbnails() {
        if (recyclerViewAdapter == null) {
            setupList();
        } else {
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Shows a message explaining that no data is available with the layout provided by the
     * concrete implementations. Also hides the recycler (by hiding the containing refreshlayout)
     */
    @Override
    public void showNoThumbnailsAvailableScreen() {
        noAdvertsFoundContainer.setVisibility(View.VISIBLE);
        view.findViewById(R.id.pullToRefresh).setVisibility(View.INVISIBLE);
    }

    /**
     * Hides the message explaining that no data is available. For example called whenever there
     * is data to show.
     */
    @Override
    public void hideNoThumbnailsAvailableScreen() {
        noAdvertsFoundContainer.setVisibility(View.GONE);
        view.findViewById(R.id.pullToRefresh).setVisibility(View.VISIBLE);
    }

    /**
     * Opens a details view showing more information about the advert that was pressed.
     *
     * @param id
     */
    @Override
    public void showDetailsScreen(String id) {
        Intent intent = new Intent(getContext(), DetailsActivity.class);
        intent.putExtra("advertID", id);
        startActivity(intent);
    }

    /**
     * Shows a progressbar to indicate that content is loading. For example used when a network
     * request is made.
     */
    @Override
    public void showLoadingScreen() {
        ProgressBar progressBar = view.findViewById(R.id.advertsViewProgressbar);
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Hides the progressbar that indicates that content is loading.
     */
    @Override
    public void hideLoadingScreen() {
        ProgressBar progressBar = view.findViewById(R.id.advertsViewProgressbar);
        progressBar.setVisibility(View.GONE);
    }


    /**
     * If the concrete implementation provides a callback to optionalPullToRefreshHandler pull-to-refresh
     * will be used, in other case it will be disabled.
     * @return
     */
    private boolean shouldUsePullToRefresh() {
        return optionalPullToRefreshHandler() != null;
    }

    private void disablePullToRefresh() {
        view.findViewById(R.id.pullToRefresh).setEnabled(false);
    }
}
