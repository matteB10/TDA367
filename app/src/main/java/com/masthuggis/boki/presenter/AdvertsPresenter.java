package com.masthuggis.boki.presenter;

import com.masthuggis.boki.backend.callbacks.advertisementCallback;
import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.utils.ClickDelayHelper;
import com.masthuggis.boki.utils.StylingHelper;
import com.masthuggis.boki.view.ThumbnailView;

import java.util.ArrayList;
import java.util.List;

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

    public abstract void getData(advertisementCallback advertisementCallback);

    public abstract List<Advertisement> sort(List<Advertisement> adverts);

    @Override
    public void onBindThumbnailViewAtPosition(int position, ThumbnailView thumbnailView) {
        if (adverts.size() <= position) {
            return;
        }

        Advertisement a = sort(adverts).get(position);
        thumbnailView.setId(a.getUniqueID());
        thumbnailView.setTitle(a.getTitle());
        thumbnailView.setPrice(a.getPrice());
        setCondition(a, thumbnailView);
        if (a.getImageUrl() != null) {
            thumbnailView.setImageURL(a.getImageUrl());
        }
    }

    @Override
    public int getItemCount() {
        if (adverts.isEmpty() || adverts == null) {
            return 0;
        }

        return adverts.size();
    }

    @Override
    public void onRowPressed(String uniqueIDoFAdvert) {
        view.showDetailsScreen(uniqueIDoFAdvert);
    }

    @Override
    public boolean canProceedWithTapAction() {
        return ClickDelayHelper.canProceedWithTapAction();
    }

    private void setCondition(Advertisement a, ThumbnailView thumbnailView) {
        int drawable = StylingHelper.getConditionDrawable(a.getCondition());
        int text = StylingHelper.getConditionText(a.getCondition());
        thumbnailView.setCondition(text, drawable);
    }
}
