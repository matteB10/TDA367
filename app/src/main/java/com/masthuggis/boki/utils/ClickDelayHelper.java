package com.masthuggis.boki.utils;
/**
 *
 * Used by ChatPresenter, DetailsPresenter and ListPresenter
 *
 * Written by masthuggis
 */
public class ClickDelayHelper {
    private static long lastTimeThumbnailWasClicked = System.currentTimeMillis();
    private static final long MIN_CLICK_TIME_INTERVAL = 300;


    public static boolean canProceedWithTapAction() {
        long now = System.currentTimeMillis();
        boolean canProceed;
        canProceed = now - lastTimeThumbnailWasClicked >= MIN_CLICK_TIME_INTERVAL;
        lastTimeThumbnailWasClicked = now;
        return canProceed;
    }

}
