package com.masthuggis.boki.utils;

public class ClickDelayHelper {
    private static long lastTimeThumbnailWasClicked = System.currentTimeMillis();
    private static final long MIN_THUMBNAIL_CLICK_TIME_INTERVAL = 300;

    private static final long MIN_CLICK_TIME_INTERVAL = 300;


    public static boolean canProceedWithTapAction() {
        long now = System.currentTimeMillis();
        boolean canProceed;
        canProceed = now - lastTimeThumbnailWasClicked >= MIN_CLICK_TIME_INTERVAL;
        lastTimeThumbnailWasClicked = now;
        return canProceed;
    }
    private static boolean tapActionWasNotTooFast() {
        long elapsedTimeSinceLastClick = System.currentTimeMillis() - lastTimeThumbnailWasClicked;
        return elapsedTimeSinceLastClick > MIN_THUMBNAIL_CLICK_TIME_INTERVAL;
    }
}
