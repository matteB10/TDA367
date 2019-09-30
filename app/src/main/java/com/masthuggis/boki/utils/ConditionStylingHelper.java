package com.masthuggis.boki.utils;


import com.masthuggis.boki.R;
import com.masthuggis.boki.model.Advert;


public class ConditionStylingHelper {

    private static ConditionStylingHelper stylingHelper;


    private ConditionStylingHelper(){
    }

    public static ConditionStylingHelper getInstance(){
        if (stylingHelper == null){
            stylingHelper = new ConditionStylingHelper();
        }
        return stylingHelper;
    }


    public static int getConditionText (Advert.Condition condition){
        int conditionText = 0;
        switch (condition){
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
    public static int getConditionDrawable(Advert.Condition condition){
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



}
