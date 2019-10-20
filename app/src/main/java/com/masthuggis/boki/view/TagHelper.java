package com.masthuggis.boki.view;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;

import com.masthuggis.boki.R;
import com.masthuggis.boki.utils.StylingHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagHelper {




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
    public static boolean isPreDefTag(String tag, Context context) {
        List<String> preDefTags = getPreDefTagStrings(context);
        return (preDefTags.contains(tag));
    }

    /**
     * Creates a list with buttons from a list of strings.
     *
     * @param strTags, list of button text strings
     * @return a list of buttons
     */
    public static List<Button> createTagButtons(List<String> strTags, Context context) {
        List<Button> btnList = new ArrayList<>();
        for (String str : strTags) {
            Button btn = createTagButton(str, false, context);
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
    public static List<Button> createPreDefTagButtons(Context context) {
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
    public static Button createTagButton(String text, boolean isSelected, Context context) {
        Button b = new Button(context);
        b.setText(text);
        StylingHelper.setTagButtonStyling(b, isSelected);
        return b;
    }
    /**
     * Takes a string and a list of buttons as parameter,
     * return button from list if its text matches the given string.
     */

    public static Button getButtonWithText(String text, List<Button> buttons) {
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
    public static void populateTagsLayout(List<Button> tags, ViewGroup parentLayout, Context context) {
        for (Button btn : tags) {
            TableRow tableRow = getCurrentTagRow(parentLayout, context);
            tableRow.addView(btn, StylingHelper.getTableRowChildLayoutParams(context));
        }
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

    public static void displayTagButtons(ViewGroup layout, List<Button> tagButtons, Context context) {
        ViewGroup tagsLayout = layout;
        populateTagsLayout(tagButtons, tagsLayout, context);
    }


    private static TableRow getCurrentTagRow(ViewGroup parentView, Context context) {
        ViewGroup parentLayout = parentView;
        int noOfRows = parentLayout.getChildCount();
        for (int i = 0; i < noOfRows; i++) {
            if (!(rowFull((TableRow) parentLayout.getChildAt(i)))) {
                return (TableRow) parentLayout.getChildAt(i);
            }
        }
        TableRow tr = new TableRow(context);
        parentLayout.addView(tr, StylingHelper.getTableRowLayoutParams(context));
        return tr;
    }
    public static  void styleSelectedTag(String tag, List<Button> tagButtons, boolean isSelected) {
        for (Button btn : tagButtons) {
            if (btn.getText().equals(tag)) {
                StylingHelper.setTagButtonStyling(btn, isSelected);
            }
        }
    }

    /**
     * Clear rows in layout from children
     */
    public static void clearLayout(ViewGroup layout) {
        ViewGroup tr;
        int noOfRows = layout.getChildCount();
        for (int i = 0; i < noOfRows; i++) {
            tr = (TableRow) layout.getChildAt(i);
            tr.removeAllViews();
        }
    }
}
