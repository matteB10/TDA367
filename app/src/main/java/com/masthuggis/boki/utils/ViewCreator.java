package com.masthuggis.boki.utils;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
/**
 *
 * Used by ChatFragment, FavoritesFragment, HomeFragment and ProfileFragment.
 * Written by masthuggis
 */
public class ViewCreator {

    public static TextView createHeader(Context context, String title) {
        TextView textView = new TextView(context);
        textView.setText(title);
        textView.setTextAppearance(android.R.style.TextAppearance_Material_Display1);
        return textView;
    }

    public static TextView createSimpleText(Context context, String title) {
        TextView textView = new TextView(context);
        textView.setText(title);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextAppearance(android.R.style.TextAppearance_Medium);
        return textView;
    }

}
