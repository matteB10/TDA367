package com.masthuggis.boki.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.masthuggis.boki.R;
import com.masthuggis.boki.model.DataModel;
import com.masthuggis.boki.presenter.DetailsPresenter;
import com.masthuggis.boki.utils.StylingHelper;

import java.util.List;

/**
 * The view showing details of a specific advertisement.
 */
public class DetailsActivity extends AppCompatActivity implements DetailsPresenter.View {
    private DetailsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        String advertID = intent.getExtras().getString("advertID");
        if (advertID != null) {
            presenter = new DetailsPresenter(this, advertID);
        }

        String uniqueOwnerID= DataModel.getInstance().getAdFromAdID(advertID).getUniqueOwnerID();

        setBtnForOwner();

        String receiverUsername=  DataModel.getInstance().getAdFromAdID(advertID).getOwner();
        Button contactOwnerButton = findViewById(R.id.contactOwnerButton);
        contactOwnerButton.setOnClickListener(view -> {
            //TODO HÄR SKA CHATTEN ÖPPNAS TYP
            if (contactOwnerButton.getText().equals("Starta chatt")) {
                presenter.createNewChat(uniqueOwnerID,receiverUsername);

            } else {
                contactOwnerButton.setText("Starta chatt");
            }
        });

        Button changeAd = findViewById(R.id.changeAdBtn);
        changeAd.setOnClickListener(view -> presenter.onChangedAdBtnPressed());

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
        // TODO: fetch img, cache it and set it
        ImageView imageView = (ImageView) findViewById(R.id.detailsImage);
        Glide.with(this).load(url).into(imageView);
        //imageView.setImageURI(Uri.parse(url));
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
     * @param tags
     */
    @Override
    public void setTags(List<String> tags) {
        LinearLayout parentLayout = findViewById(R.id.detailsTagsTableLayout);
        TableRow tableRow = new TableRow(this);
        Button btn;

        for (String str : tags) {
            btn = createTagButton(str);
            tableRow = getTableRow(tableRow, parentLayout);
            tableRow.setLayoutParams(StylingHelper.getTableRowLayoutParams(this));
            tableRow.addView(btn,StylingHelper.getTableRowChildLayoutParams(this));
        }
        parentLayout.addView(tableRow);
    }

    @Override
    public void openChat(String uniqueOwnerID) {
        //TODO ÖPPNA CHATT

    }


    private Button createTagButton(String btnTxt) {
        Button btn = new Button(this);
        btn.setText(btnTxt);
        setTagStyling(btn);
        return btn;
    }

    /**
     * Private method trying to resolve if a tableRow with tags is filled and
     * if a new one should be created.
     *
     * @param tableRow    the current tableRow
     * @param parentLayout width of parent layout
     * @return param tableRow or new tableRow object depending on
     */
    private TableRow getTableRow(TableRow tableRow, LinearLayout parentLayout) {
        if(tableRow.getChildCount() % 4 == 0) {
                parentLayout.addView(tableRow);
                return new TableRow(this);
        }
        return tableRow;
    }

    private void setTagStyling(Button btn) {
        btn.setBackgroundResource(R.drawable.subject_tag_shape_normal);
        btn.setTextSize(12);
        btn.setTextColor(this.getColor(R.color.colorWhite));
        btn.setElevation(4);
    }


    private void setBtnForOwner(){
        if (presenter.isUserOwner()){
            findViewById(R.id.changeAdBtn).setVisibility(View.VISIBLE);
            findViewById(R.id.contactOwnerButton).setVisibility(View.GONE);
        }else{
            findViewById(R.id.changeAdBtn).setVisibility(View.GONE);
            findViewById(R.id.contactOwnerButton).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showEditView(String uniqueID){
        Intent intent = new Intent(DetailsActivity.this, EditAdActivity.class);
        intent.putExtra("advertID", uniqueID);
        startActivity(intent);
    }

}
