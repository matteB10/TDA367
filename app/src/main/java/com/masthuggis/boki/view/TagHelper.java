package com.masthuggis.boki.view;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.masthuggis.boki.R;
import com.masthuggis.boki.utils.StylingHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 *
 * Used by CreateAdActivity, DetailsActivity, FilterFragment and TagHelper.
 * Written by masthuggis
 */
class TagHelper {


    /**
     * Reads predefined subject strings from resources,
     *
     * @return all strings in a list.
     */
    private static List<String> getPreDefTagStrings(Context context) {
        String[] strArr = context.getResources().getStringArray(R.array.preDefSubjectTags);
        return Arrays.asList(strArr);
    }
    /**
     * Checks if tag is preDefined or userDefined
     * @param tag
     * @return
     */
     static boolean isPreDefTag(String tag, Context context) {
        List<String> preDefTags = getPreDefTagStrings(context);
        return (preDefTags.contains(tag));
    }

    /**
     * Creates a list with buttons from a list of strings.
     *
     * @param strTags, list of button text strings
     * @return a list of buttons
     */
     static List<Button> createTagButtons(List<String> strTags, Context context, boolean isSelected) {
        List<Button> btnList = new ArrayList<>();
        for (String str : strTags) {
            Button btn = createTagButton(str, isSelected, context);
            btnList.add(btn);
        }
        return btnList;
    }
    /**
     * Creates a list with buttons from pre-defined tag strings
     * from string resources.
     *
     * @return a list of buttons
     */
     static List<Button> createPreDefTagButtons(Context context) {
        List<String> preDefTagString = getPreDefTagStrings(context);
        List<Button> btnList = new ArrayList<>();
        for (String str : preDefTagString) {
            Button btn = createTagButton(str, false, context);
            btnList.add(btn);
        }
        return btnList;
    }

    /**
     * Create a tag button with correct styling
     *
     * @param text
     * @return a button
     */
      static Button createTagButton(String text, boolean isSelected, Context context) {
        Button b = new Button(context);
        b.setText(text);
        StylingHelper.setTagButtonStyling(b, isSelected, context);
        return b;
    }
    /**
     * Takes a string and a list of buttons as parameter,
     * return button from list if its text matches the given string.
     */

     static Button getButtonWithText(String text, List<Button> buttons) {
        for (Button btn : buttons) {
            if (btn.getText().toString().equals(text)) {
                return btn;
            }
        }
        return null;
    }
    /**
     * @param tags         a list of buttons
     * @param parentLayout the layout in which buttons will be placed
     */
     static void populateTagsLayout(List<Button> tags, ViewGroup parentLayout, Context context) {
        for (Button btn : tags) {
            TableRow tableRow = getCurrentTagRow(parentLayout, context);
            tableRow.addView(btn, getTableRowChildLayoutParams(context));
        }
    }
    /**
     * @param tags         a list of Strings
     * @param parentLayout the layout in which tag buttons will be placed
     */
    static void displayTags(List<String> tags, ViewGroup parentLayout, Context context, boolean isSelected) {
        List<Button> tagButtons = createTagButtons(tags,context,isSelected);
        populateTagsLayout(tagButtons,parentLayout,context);
    }

    /**
     * Method to check if row is full
     *
     * @param tableRow the row to be checked
     * @return true if row is full
     */
    private static boolean rowFull(TableRow tableRow) {
        return (tableRow.getChildCount() % 3 == 0 && tableRow.getChildCount() != 0);
    }


    private static TableRow getCurrentTagRow(ViewGroup parentView, Context context) {
        ViewGroup parentLayout = parentView;
        int noOfRows = parentLayout.getChildCount();
        for (int i = 0; i < noOfRows; i++) {
            if (!(rowFull((TableRow) parentLayout.getChildAt(i)))) {
                TableRow tr = (TableRow) parentLayout.getChildAt(i);
                tr.setLayoutParams(TagHelper.getTableRowLayoutParams(context));
                return tr;
            }
        }
        TableRow tr = new TableRow(context);
        parentLayout.addView(tr, getTableRowLayoutParams(context));
        return tr;
    }

    /**
     * Clear rows in layout from children
     */
     static void clearLayout(ViewGroup layout) {
        ViewGroup tr;
        int noOfRows = layout.getChildCount();
        for (int i = 0; i < noOfRows; i++) {
            tr = (TableRow) layout.getChildAt(i);
            tr.removeAllViews();
        }
    }
    /**
     * @return specified layout parameters for tag tableRows
     */
     private static TableLayout.LayoutParams getTableRowLayoutParams(Context context) {
        int rowHeight = (int) StylingHelper.getDPToPixels(context, 20);
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, rowHeight, 1);
        layoutParams.setMargins(0, 0, 0, 8);
        return layoutParams;
    }

    /**
     * @return specified layout parameters for tag items in tableRows
     */
     private static TableRow.LayoutParams getTableRowChildLayoutParams(Context context) {
        TableRow.LayoutParams rowLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
        rowLayoutParams.setMarginEnd(6);
        return rowLayoutParams;
    }
}
