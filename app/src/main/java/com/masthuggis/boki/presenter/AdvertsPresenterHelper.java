package com.masthuggis.boki.presenter;

import com.masthuggis.boki.model.Advertisement;
import com.masthuggis.boki.utils.StylingHelper;
import com.masthuggis.boki.view.ThumbnailView;

import java.util.List;

public class AdvertsPresenterHelper {
    public static void onBindThumbnailViewAtPosition(int position, ThumbnailView thumbnailView, List<Advertisement> adverts) {
        if (requestedPositionIsTooLarge(position, adverts)) {
            return;
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

    private static void setCondition(Advertisement a, ThumbnailView thumbnailView) {
        int drawable = StylingHelper.getConditionDrawable(a.getCondition(),false);
        int text = StylingHelper.getConditionText(a.getCondition());
        thumbnailView.setCondition(text, drawable);
    }

    private static boolean requestedPositionIsTooLarge(int position, List<Advertisement> adverts) {
        return adverts.size() <= position;
    }
}
