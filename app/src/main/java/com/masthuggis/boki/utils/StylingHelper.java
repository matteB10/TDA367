package com.masthuggis.boki.utils;


import android.content.Context;
import android.widget.Button;

import com.masthuggis.boki.R;

/**
 * Helper class for styling components programmatically
 * Used by AdvertsPresenterHelper, CreateAdActivity, DetailsPresenter,
 * FilterFragment and TagHelper.
 * Written by masthuggis.
 */


public class StylingHelper {


    private StylingHelper() {
    }


    public static int getConditionText(Condition condition) {
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

    public static int getConditionDrawable(Condition condition, boolean isPressed) {
        int drawable = 0;
        switch (condition) {
            case NEW:
                if (isPressed) {
                    drawable = R.drawable.button_new_condition_checked;
                } else {
                    drawable = R.drawable.button_new_condition;
                }
                break;
            case GOOD:
                if (isPressed) {
                    drawable = R.drawable.button_good_condition_checked;
                } else {
                    drawable = R.drawable.button_good_condition;
                }
                break;
            case OK:
                if (isPressed) {
                    drawable = R.drawable.button_ok_condition_checked;
                } else {
                    drawable = R.drawable.button_ok_condition;
                }
                break;
        }
        return drawable;
    }

    public static Condition getConditionFromView(int conditionButton) {
        switch (conditionButton) {
            case R.id.conditionGoodButton:
                return Condition.GOOD;
            case R.id.conditionNewButton:
                return Condition.NEW;
            case R.id.conditionOkButton:
                return Condition.OK;
            default:
                return Condition.UNDEFINED;
        }
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
     * @return styling drawable for primary action buttons
     * depending on state
     */
    public static int getPrimaryButtonDrawable(boolean isEnabled) {
        if (isEnabled) {
            return R.drawable.primary_button;
        }
        return R.drawable.disabled_primary_button;
    }


    /**
     * Sets styling of a tagButton
     *
     * @param btn the button to be styled
     */

    public static void setTagButtonStyling(Button btn, boolean isSelected, Context context) {
        if (isSelected) {
            btn.setBackgroundResource(R.drawable.subject_tag_shape_pressed);
        } else {
            btn.setBackgroundResource(R.drawable.subject_tag_shape_normal);
        }
        btn.setTextSize(12);
        btn.setTextColor(context.getResources().getColor(R.color.colorGreyDark));
        btn.setElevation(4);
    }

}
