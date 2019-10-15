package com.masthuggis.boki.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.masthuggis.boki.R;
import com.masthuggis.boki.utils.FilterPresenter;
import com.masthuggis.boki.utils.StylingHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterActivity extends AppCompatActivity implements FilterPresenter.View {

    FilterPresenter presenter;
    List<Button> tags = new ArrayList<>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_fragment);
        presenter = new FilterPresenter(this);
        initTags();
        setTagListeners();
        initDoneButton();
    }

    private void initTags(){
        presenter.setPreDefTags(Arrays.asList(getResources().getStringArray(R.array.preDefSubjectTags))); //set pre def tags in presenter
        tags = TagHelper.createPreDefTagButtons(presenter.getPreDefTags(),this);
        TagHelper.displayPreDefTagButtons(findViewById(R.id.filterTagsLinearLayout),new ArrayList<>(tags), this);
    }
    private void setTagListeners(){
        for(Button btn : tags){
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.tagsChanged(btn.getText().toString());

                }
            });
        }
    }
    private void initDoneButton(){
        Button btn = findViewById(R.id.applyFilterButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("filter", presenter.getPreDefTags().toArray());
            }
        });
    }


    @Override
    public void addTag(String tag, boolean isSelected) {
        Button btn = TagHelper.getButtonWithText(tag, new ArrayList<>(tags));
        StylingHelper.setTagButtonStyling(btn,isSelected);
    }

}
