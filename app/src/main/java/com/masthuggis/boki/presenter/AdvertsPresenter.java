package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.utils.ClickDelayHelper;
import com.masthuggis.boki.utils.StylingHelper;
import com.masthuggis.boki.view.ThumbnailView;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract presenter containing the common logic of presenters showing a list of adverts. Template
 * method pattern is used to make the concrete implementations able to implement their unique
 * way to get data and sort.
 */
public abstract class AdvertsPresenter implements IProductsPresenter {

    protected AdvertsPresenterView view;
    protected final DataModel dataModel;
    private List<Advertisement> adverts;

    AdvertsPresenter(AdvertsPresenterView view, DataModel dataModel) {
        this.view = view;
        this.adverts = new ArrayList<>();
        this.dataModel = dataModel;
    }

    /**
     * Initializes the presenter taking necessary actions. This should always be called after
     * instantiating.
     */
    public void initPresenter() {
        updateAdverts();
    }

    /**
     * Sorts adverts and tells the view to update UI.
     * @param adverts the updated adverts lists that will be displayed.
     */
    void updateAdverts(List<Advertisement> adverts) {
        if (adverts == null || view == null) {
            return;
        }

        if (adverts.isEmpty()) {
            view.showNoThumbnailsAvailableScreen();
        } else {
            view.hideNoThumbnailsAvailableScreen();
            this.adverts = sort(adverts);
        }

        view.hideLoadingScreen();
        view.updateThumbnails();
    }

    /**
     * Asks the concrete implementations to get data and then updates the UI.
     */
    void updateAdverts() {
        if (view == null) {
            return;
        }

        view.showLoadingScreen();
        updateAdverts(getData());
    }

    /**
     * Concrete implementations implements this to call their respective data source.
     */
    public abstract List<Advertisement> getData();

    /**
     * Concrete implementations provides their desired way of sorting. If no sorting is desired,
     * the same list of adverts can be returned.
     *
     * @param adverts
     * @return
     */
    public abstract List<Advertisement> sort(List<Advertisement> adverts);

    /**
     * Binds each recyclerview item by setting the fields of ThumbnailView. It asks for the desired
     * sorting option to render the items in the desired order. It only sorts when the first item
     * is requested, and thereafter uses the sorted list, instead of sorting for each item. It is
     * highly unlikely for the sorting to change will the list is to be rendered.
     *
     * @param position
     * @param thumbnailView
     */
    @Override
    public void onBindThumbnailViewAtPosition(int position, ThumbnailView thumbnailView) {
        if (requestedPositionIsTooLarge(position)) {
            return;
        }

        if (isFirstThumbnailToBeDisplayed(position)) {
            adverts = sort(adverts);
        }

        Advertisement a = adverts.get(position);
        thumbnailView.setId(a.getUniqueID());
        thumbnailView.setTitle(a.getTitle());
        thumbnailView.setPrice(a.getPrice());
        setCondition(a, thumbnailView);
        if (a.getImageUrl() != null) {
            thumbnailView.setImageURL(a.getImageUrl());
        }
    }


    /**
     * Returns of the number of adverts to be rendered in the list. If the list is empty or not defined
     * the list shall not be rendered, and therefor the length is zero.
     *
     * @return
     */
    @Override
    public int getItemCount() {
        if (adverts == null) {
            return 0;
        }

        return adverts.size();
    }

    /**
     * Asks the view to display the details view of the view that was pressed if the tap is valid.
     *
     * @param uniqueAdvertID
     */
    @Override
    public void onRowPressed(String uniqueAdvertID) {
        if (view == null) {
            return;
        }

        if (canProceedWithTapAction()) {
            view.showDetailsScreen(uniqueAdvertID);
        }
    }

    /**
     * When view is destroyed, the reference is deleted.
     */
    public void viewIsBeingDestroyed() {
        view = null;
    }

    /**
     * Asks helper class to make sure there was no taps in fast succession.
     *
     * @return
     */
    private boolean canProceedWithTapAction() {
        return ClickDelayHelper.canProceedWithTapAction();
    }

    private void setCondition(Advertisement a, ThumbnailView thumbnailView) {
        int drawable = StylingHelper.getConditionDrawable(a.getCondition());
        int text = StylingHelper.getConditionText(a.getCondition());
        thumbnailView.setCondition(text, drawable);
    }

    private boolean requestedPositionIsTooLarge(int position) {
        return adverts.size() <= position;
    }

    private boolean isFirstThumbnailToBeDisplayed(int position) {
        return position == 0;
    }
}
