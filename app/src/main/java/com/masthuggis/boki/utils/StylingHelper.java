package com.masthuggis.boki.utils;


import android.content.Context;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.masthuggis.boki.Boki;
import com.masthuggis.boki.R;
import com.masthuggis.boki.model.Condition;

/**
 * Helper class for styling components programmatically
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
     * @return specified layout parameters for tag tableRows
     */
    public static TableLayout.LayoutParams getTableRowLayoutParams(Context context) {
        int rowHeight = (int) getDPToPixels(context, 20);
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, rowHeight, 1);
        layoutParams.setMargins(0, 0, 0, 8);
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

    /**
     * Sets styling of a tagButton
     *
     * @param btn the button to be styled
     */

    public static void setTagButtonStyling(Button btn, boolean isSelected) {
        if (isSelected) {
            btn.setBackgroundResource(R.drawable.subject_tag_shape_pressed);
        } else {
            btn.setBackgroundResource(R.drawable.subject_tag_shape_normal);
        }
        btn.setTextSize(12);
        btn.setTextColor(Boki.getAppContext().getColor(R.color.colorGreyDark));
        btn.setElevation(4);
    }

}
