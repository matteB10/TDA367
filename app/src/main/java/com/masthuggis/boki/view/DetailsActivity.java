package com.masthuggis.boki.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.masthuggis.boki.R;
import com.masthuggis.boki.injectors.DependencyInjector;
import com.masthuggis.boki.presenter.DetailsPresenter;

import java.util.List;

/**
 * The view showing details of a specific advertisement.
 * Used by FavoritesFragment and ListView.
 * Written by masthuggis.
 */
public class DetailsActivity extends AppCompatActivity implements DetailsPresenter.View {
    private DetailsPresenter presenter;
    private Button contactOwnerButton;
    private ImageView favouritesIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        favouritesIcon = findViewById(R.id.favouritesIcon);
        Intent intent = getIntent();
        String advertID = intent.getExtras().getString(getString(R.string.keyForAdvert));

        if (advertID != null) {
            presenter = new DetailsPresenter(this, advertID, DependencyInjector.injectDataModel());
        }

        if (presenter.advertExists()) {
            setupUI();
        }
    }

    private void setupUI() {
        contactOwnerButton = findViewById(R.id.contactOwnerButton);
        contactOwnerButton.setOnClickListener(view -> {
            if (presenter.canProceedWithTapAction()) {
                presenter.contactOwnerBtnClicked();
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
        TagHelper.displayTags(tags,parentLayout,this,true);
    }

    @Override
    public void openChat(String chatID) {
        Intent intent = new Intent(DetailsActivity.this, MessagesActivity.class);
        intent.putExtra("chatID", chatID);
        startActivity(intent);
    }


    public void showCanNotSendMessageToYourselfToast() {
        Context context = getApplicationContext();
        CharSequence text = getString(R.string.cantSendMessageToYourself);
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
        intent.putExtra(getString(R.string.keyForAdvert), uniqueID);
        startActivity(intent);
    }

    @Override
    public void setOwnerButtonText(String content) {
        contactOwnerButton.setText(content);
    }

    @Override
    public void setIsAFavouriteIcon() {
        Drawable favouriteIcon = ContextCompat.getDrawable(this, R.drawable.heart_filled_vector);
        favouritesIcon.setImageDrawable(favouriteIcon);
    }

    @Override
    public void setIsNotAFavouriteIcon() {
        Drawable notFavouriteIcon = ContextCompat.getDrawable(this, R.drawable.heart_outline_vector);
        favouritesIcon.setImageDrawable(notFavouriteIcon);
    }

    public void hideFavouriteIcon() {
        favouritesIcon.setVisibility(View.GONE);
    }

    @Override
    public void nothingToDisplay() {
        Toast.makeText(getApplicationContext(), getString(R.string.noAdFoundDetailsView), Toast.LENGTH_LONG).show();
        super.onBackPressed();

        finish();
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
