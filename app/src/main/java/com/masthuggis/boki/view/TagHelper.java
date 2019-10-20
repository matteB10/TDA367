package com.masthuggis.boki.view;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;

import com.masthuggis.boki.utils.StylingHelper;

import java.util.ArrayList;
import java.util.List;

public class TagHelper {

    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

    }*/

    /**
     * Creates a list with buttons from a list of strings.
     *
     * @param strTags, list of button text strings
     * @return a list of buttons
     */
    public static List<Button> createPreDefTagButtons(List<String> strTags, Context context) {
        List<Button> btnList = new ArrayList<>();
        for (String str : strTags) {
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
    private static Button createTagButton(String text, boolean isSelected, Context context) {
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
    private static void populateTagsLayout(List<Button> tags, ViewGroup parentLayout, Context context) {
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

    /**
     * Called in onCreate, create tag buttons and add them to the view
     */
    public static void displayPreDefTagButtons(ViewGroup layout, List<Button> tagButtons, Context context) {
        ViewGroup preDefTagsLayout = layout;
        populateTagsLayout(tagButtons, preDefTagsLayout, context);
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
}
