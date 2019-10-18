package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.utils.ClickDelayHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract presenter containing the common logic of presenters showing a list of data. Template
 * method pattern is used to make the concrete implementations able to implement their unique
 * way to get data and sort.
 */
public abstract class ListPresenter<T, U> implements IListPresenter<U> {

    protected ListPresenterView view;
    protected final DataModel dataModel;
    private List<T> data;

    ListPresenter(ListPresenterView view, DataModel dataModel) {
        this.view = view;
        this.data = new ArrayList<>();
        this.dataModel = dataModel;
    }

    /**
     * Sorts data and tells the view to update UI.
     *
     * @param data the updated data lists that will be displayed.
     */
    void updateData(List<T> data) {
        if (data == null || view == null) {
            return;
        }

        if (data.isEmpty()) {
            view.showNoThumbnailsAvailableScreen();
        } else {
            view.hideNoThumbnailsAvailableScreen();

            this.data = sort(data);
        }

        view.hideLoadingScreen();
        view.updateThumbnails();
    }

    /**
     * Sorts data and tells the view to update UI.
     * @param data the updated data lists that will be displayed.
     */
    void updateData(List<T> data, boolean sort) {
        if (data == null || view == null) {
            return;
        }

        if (data.isEmpty()) {
            view.showNoThumbnailsAvailableScreen();
        } else {
            view.hideNoThumbnailsAvailableScreen();

            if (sort) {
                this.data = sort(data);
            }else{
                this.data = data;
            }
        }

        view.hideLoadingScreen();
        view.updateThumbnails();
    }

    /**
     * Asks the concrete implementations to get data and then updates the UI.
     */
    public void updateData() {
        if (view == null) {
            return;
        }

        view.showLoadingScreen();
        updateData(getData());
    }

    /**
     * Get data currently showed in view
     */
    protected List<T> getCurrentDisplayedData() {
        return data;
    }

    void setCurrentDisplayedData(List<T> data) {
        this.data = data;
    }

    /**
     * Concrete implementations implements this to call their respective data source.
     */
    public abstract List<T> getData();

    /**
     * Concrete implementations provides their desired way of sorting. If no sorting is desired,
     * the same list of data can be returned.
     *
     * @param data list of data to be sorted
     * @return
     */
    public abstract List<T> sort(List<T> data);

    /**
     * Binds each recyclerview item by setting the fields of ThumbnailView. It asks for the desired
     * sorting option to render the items in the desired order. It only sorts when the first item
     * is requested, and thereafter uses the sorted list, instead of sorting for each item. It is
     * highly unlikely for the sorting to change will the list is to be rendered.
     *
     * @param position
     * @param dataView
     */
    @Override
    public abstract void onBindThumbnailViewAtPosition(int position, U dataView);

    /**
     * Returns of the number of data to be rendered in the list. If the list is empty or not defined
     * the list shall not be rendered, and therefor the length is zero.
     *
     * @return
     */
    @Override
    public int getItemCount() {
        if (data == null) {
            return 0;
        }

        return data.size();
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
}
