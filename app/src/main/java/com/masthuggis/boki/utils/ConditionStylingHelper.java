package com.masthuggis.boki.utils;

import android.content.Context;


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


    public String getConditionText (Advert.Condition condition, Context context){
        String conditionText = "";
        switch (condition){
            case NEW:
                conditionText = context.getResources().getString(R.string.conditionNew);
                break;
            case GOOD:
                conditionText = context.getResources().getString(R.string.conditionGood);
                break;
            case OK:
                conditionText = context.getResources().getString(R.string.conditionOk);
                break;
        }
        return conditionText;

    }
    public int getConditionColor(Advert.Condition condition, Context context){
        int color = 0;
        switch (condition) {
            case NEW:
                color = context.getResources().getColor(R.color.colorCoral);
                break;
            case GOOD:
                color = context.getResources().getColor(R.color.colorTeal);
                break;
            case OK:
                color = context.getResources().getColor(R.color.colorYellow);
                break;
        }
        return color;

    }



}
