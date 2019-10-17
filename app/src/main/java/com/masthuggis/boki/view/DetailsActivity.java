package com.masthuggis.boki.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.masthuggis.boki.R;
import com.masthuggis.boki.injectors.DependencyInjector;
import com.masthuggis.boki.presenter.DetailsPresenter;
import com.masthuggis.boki.utils.StylingHelper;

import java.util.List;

/**
 * The view showing details of a specific advertisement.
 */
public class DetailsActivity extends AppCompatActivity implements DetailsPresenter.View {
    private DetailsPresenter presenter;
    private Button contactOwnerButton;
    private ImageView favouritesIcon;
    private long lastTimeThumbnailWasClicked = System.currentTimeMillis();
    private static final long MIN_THUMBNAIL_CLICK_TIME_INTERVAL = 300;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        favouritesIcon = findViewById(R.id.favouritesIcon);
        Intent intent = getIntent();
        String advertID = intent.getExtras().getString("advertID");
        if (advertID != null) {
            presenter = new DetailsPresenter(this, advertID, DependencyInjector.injectDataModel());
        }

        contactOwnerButton = findViewById(R.id.contactOwnerButton);
        contactOwnerButton.setOnClickListener(view -> {
            if (canProceedWithTapAction()) {
                presenter.contactOwnerBtnClicked(contactOwnerButton.getText().toString()); //Should the logic be based off this string?
            }
        });
        Button changeAd = findViewById(R.id.changeAdButton);
        changeAd.setOnClickListener(view -> presenter.onChangedAdBtnPressed());
        setUpFavouriteIcon();
        setVisibilityOnButtons();
    }


    @Override
    public void setName(String name) {
        TextView nameTextView = findViewById(R.id.detailsName);
        nameTextView.setText(name);
    }

    @Override
    public void setPrice(long price) {
        TextView textView = findViewById(R.id.detailsPrice);
        textView.setText(price + " kr");
    }

    @Override
    public void setDate(String date) {
        TextView textView = findViewById(R.id.datePublishedTextView);
        textView.setText(date);
    }

    @Override
    public void setImageUrl(String url) {
        ImageView imageView = findViewById(R.id.detailsImage);
        Glide.with(this).load(url).override(220, 300).into(imageView);
    }

    @Override
    public void setDescription(String description) {
        TextView textView = findViewById(R.id.details_description);
        textView.setText(description);
    }

    @Override
    public void setCondition(int text, int drawable) {
        TextView textView = findViewById(R.id.conditionTextView);
        ConstraintLayout layout = findViewById(R.id.conditionConstraintLayout);

        textView.setText(text);
        layout.setBackground(getDrawable(drawable));
    }

    /**
     * Set tags as buttons in details
     *
     * @param tags
     */
    @Override
    public void setTags(List<String> tags) {
        LinearLayout parentLayout = findViewById(R.id.detailsTagsTableLayout);
        TableRow tableRow = new TableRow(this);
        Button btn;

        for (String str : tags) {
            btn = createTagButton(str, true);
            tableRow = getTableRow(tableRow, parentLayout);
            tableRow.setLayoutParams(StylingHelper.getTableRowLayoutParams(this));
            tableRow.addView(btn, StylingHelper.getTableRowChildLayoutParams(this));
        }
        parentLayout.addView(tableRow);
    }

    @Override
    public void openChat(String chatID) {
        Intent intent = new Intent(DetailsActivity.this, MessagesActivity.class);
        intent.putExtra("chatID", chatID);
        startActivity(intent);
    }

    /**
     * Private method trying to resolve if a tableRow with tags is filled and
     * if a new one should be created.
     *
     * @param tableRow     the current tableRow
     * @param parentLayout width of parent layout
     * @return param tableRow or new tableRow object depending on
     */
    private TableRow getTableRow(TableRow tableRow, LinearLayout parentLayout) {
        if (tableRow.getChildCount() % 3 == 0) {
            parentLayout.addView(tableRow);
            return new TableRow(this);
        }
        return tableRow;
    }

    private Button createTagButton(String buttonText, boolean isSelected) {
        Button btn = new Button(this);
        btn.setText(buttonText);
        StylingHelper.setTagButtonStyling(btn, isSelected);
        return btn;
    }

    public void showToast() {
        Context context = getApplicationContext();
        CharSequence text = "Du kan inte skicka meddelanden till dig själv.";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    private void setVisibilityOnButtons() {
        if (presenter.isUserOwner()) {
            findViewById(R.id.changeAdButton).setVisibility(View.VISIBLE);
            findViewById(R.id.contactOwnerButton).setVisibility(View.GONE);
        } else {
            findViewById(R.id.changeAdButton).setVisibility(View.GONE);
            findViewById(R.id.contactOwnerButton).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showEditView(String uniqueID) {
        Intent intent = new Intent(DetailsActivity.this, CreateAdActivity.class);
        intent.putExtra("advertID", uniqueID);
        startActivity(intent);
    }

    @Override
    public void setOwnerButtonText(String content) {
        contactOwnerButton.setText(content);
    }

    @Override
    public void setFavouriteIcon() {
        Drawable favouriteIcon = ContextCompat.getDrawable(this, R.drawable.heart_filled_vector);
        favouritesIcon.setImageDrawable(favouriteIcon);
    }

    @Override
    public void setNotFavouriteIcon() {
        Drawable notFavouriteIcon = ContextCompat.getDrawable(this, R.drawable.heart_outline_vector);
        favouritesIcon.setImageDrawable(notFavouriteIcon);
    }

    public void hideFavouriteIcon() {
        favouritesIcon.setVisibility(View.GONE);
    }

    public boolean canProceedWithTapAction() {
        boolean canProceed = tapActionWasNotTooFast();
        lastTimeThumbnailWasClicked = System.currentTimeMillis();
        return canProceed;
    }

    private boolean tapActionWasNotTooFast() {
        long elapsedTimeSinceLastClick = System.currentTimeMillis() - lastTimeThumbnailWasClicked;
        return elapsedTimeSinceLastClick > MIN_THUMBNAIL_CLICK_TIME_INTERVAL;
    }

    private void setUpFavouriteIcon() {
        favouritesIcon = findViewById(R.id.favouritesIcon);
        if (presenter.isUserOwner()) {
            favouritesIcon.setVisibility(View.GONE);
        } else {
            presenter.setUpFavouriteIcon();
            favouritesIcon.setOnClickListener(view -> {
                        presenter.onFavouritesIconPressed();
                    }
            );
        }
    }

    @Override
    public void onBackPressed() {
        if (getIntent().getBooleanExtra("fromFavourites", false)) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("toFavourites", true);
            startActivity(intent);
            return;
        }
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
