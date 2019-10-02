package com.masthuggis.boki.utils;


import android.content.Context;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.masthuggis.boki.R;
import com.masthuggis.boki.model.Advert;

/**
 * Helper class for styling components programmatically
 */


public class StylingHelper {


    private StylingHelper() {
    }


    public static int getConditionText(Advert.Condition condition) {
        int conditionText = 0;
        switch (condition) {
            case NEW:
                conditionText = R.string.conditionNew;
                break;
            case GOOD:
                conditionText = R.string.conditionGood;
                break;
            case OK:
                conditionText = R.string.conditionOk;
                break;
        }
        return conditionText;

    }

    public static int getConditionDrawable(Advert.Condition condition) {
        int drawable = 0;
        switch (condition) {
            case NEW:
                drawable = R.drawable.button_new_condition;
                break;
            case GOOD:
                drawable = R.drawable.button_good_condition;
                break;
            case OK:
                drawable = R.drawable.button_ok_condition;
                break;
        }
        return drawable;

    }

    /**
     * @param isPressed, if tag is pressed or not
     * @return the desired drawable
     */
    public static int getTagDrawable(boolean isPressed) {
        if (isPressed)
            return R.drawable.subject_tag_shape_pressed;
        return R.drawable.subject_tag_shape_normal;
    }

    /**
     * Helper method converting pixels to dp
     */
    public static float getDPToPixels(Context context, int dp) {
        return (dp * context.getResources().getDisplayMetrics().density);
    }

    /**
     *
     * @return styling drawable for primary action buttons
     * depending on state
     */
    public static int getPrimaryButtonDrawable(boolean isEnabled) {
        if(isEnabled){
            return R.drawable.primary_button;
        }
        return R.drawable.disabled_primary_button;
    }
    /**
     * @return specified layout parameters for tag tableRows
     */
    public static TableLayout.LayoutParams getTableRowLayoutParams(Context context) {
        int rowHeight = (int)getDPToPixels(context, 20);
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, rowHeight, 1);
        layoutParams.setMargins(0, 8, 0, 0);
        layoutParams.setMarginEnd(6);
        return layoutParams;
    }

    /**
     * @return specified layout parameters for tag items in tableRows
     */
    public static TableRow.LayoutParams getTableRowChildLayoutParams(Context context) {
        TableRow.LayoutParams rowLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
        rowLayoutParams.setMarginEnd(6);
        return rowLayoutParams;
    }



}
