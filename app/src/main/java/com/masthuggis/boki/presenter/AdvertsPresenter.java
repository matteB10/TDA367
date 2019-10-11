package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.callbacks.advertisementCallback;
import com.masthuggis.boki.model.Advertisement;
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

    public final AdvertsPresenterView view;
    private List<Advertisement> adverts;

    public AdvertsPresenter(AdvertsPresenterView view) {
        this.view = view;
        this.adverts = new ArrayList<>();
    }

    public void updateData() {
        view.showLoadingScreen();
        getData(new advertisementCallback() {
            @Override
            public void onCallback(List<Advertisement> advertisements) {
                AdvertsPresenter.this.updateData(advertisements);
            }
        });
    }

    public void updateData(List<Advertisement> adverts) {
        if (adverts == null || adverts.size() == 0) {
            return;
        }

        this.adverts = sort(adverts);
        view.hideLoadingScreen();
        view.updateThumbnails();
    }

    /**
     * Asks the concrete implementations to get data and then updates the UI.
     */
    public void updateAdverts() { //TODO Add another method with same name, should take a List of Advertisements as parameter, call that method in onCallback :)
        view.showLoadingScreen();
        getData(new advertisementCallback() {
            @Override
            public void onCallback(List<Advertisement> advertisements) {
                AdvertsPresenter.this.updateAdverts(advertisements);
            }
        });
    }



    /**
     * Concrete implementations implements this to call their respective data source.
     * @param advertisementCallback
     */
    public abstract void getData(advertisementCallback advertisementCallback);

    /**
     * Concrete implementations provides their desired way of sorting. If no sorting is desired,
     * the same list of adverts can be returned.
     * @param adverts
     * @return
     */
    public abstract List<Advertisement> sort(List<Advertisement> adverts);

    /**
     * Binds each recyclerview item by setting the fields of ThumbnailView. It asks for the desired
     * sorting option to render the items in the desired order. It only sorts when the first item
     * is requested, and thereafter uses the sorted list, instead of sorting for each item. It is
     * highly unlikely for the sorting to change will the list is to be rendered.
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
     * @return
     */
    @Override
    public int getItemCount() {
        if (stateOfAdvertsIsInvalid(adverts)) {
            return 0;
        }

        return adverts.size();
    }

    /**
     * Asks the view to display the details view of the view that was pressed if the tap is valid.
     * @param uniqueIDoFAdvert
     */
    @Override
    public void onRowPressed(String uniqueIDoFAdvert) {
        if (canProceedWithTapAction()) {
            view.showDetailsScreen(uniqueIDoFAdvert);
        }
    }

    /**
     * Asks helper class to make sure there was no taps in fast succession.
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

    private boolean stateOfAdvertsIsInvalid(List<Advertisement> adverts) {
        return adverts.isEmpty() || adverts == null;
    }

    private boolean isFirstThumbnailToBeDisplayed(int position) {
        return position == 0;
    }
}
