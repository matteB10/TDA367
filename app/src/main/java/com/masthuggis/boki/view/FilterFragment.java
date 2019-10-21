package com.masthuggis.boki.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.FilterPresenter;
import com.masthuggis.boki.utils.StylingHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterFragment extends Fragment implements FilterPresenter.View {

    private FilterPresenter presenter;
    private List<Button> tags = new ArrayList<>();
    private View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.filter_fragment, container, false);
        presenter = new FilterPresenter(this);
        initTags();
        initSeekBar();
        setTagListeners();
        initDoneButton();
        presenter.setUpView();
        return view;
    }

    /**
     * Get list of subject tags from resources, displays as buttons in view
     */
    private void initTags() {
        presenter.setPreDefTags(Arrays.asList(getResources().getStringArray(R.array.preDefSubjectTags))); //set pre def tags in presenter
        tags = TagHelper.createTagButtons(presenter.getPreDefTags(), getContext(), false);
        TagHelper.clearLayout(view.findViewById(R.id.filterTagsLinearLayout));
        TagHelper.populateTagsLayout(new ArrayList<>(tags), view.findViewById(R.id.filterTagsLinearLayout), getContext());
    }

    /**
     * Set listener on price SeekBar, notifies presenter
     */
    private void initSeekBar() {
        SeekBar maxPrice = view.findViewById(R.id.priceSeekBar);
        maxPrice.setProgress(500); //Maximum price set as default
        maxPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                presenter.maxPriceChanged(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setTagListeners() {
        for (Button btn : tags) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.tagsChanged(btn.getText().toString());

                }
            });
        }
    }

    private void initDoneButton() {
        Button btn = view.findViewById(R.id.applyFilterButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeFragment nextFrag = new HomeFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, nextFrag, "HomeFragment")
                        .addToBackStack(null)
                        .commitAllowingStateLoss();


            }
        });
    }


    @Override
    public void setUpFilters(String tag, boolean isSelected) {
        Button btn = TagHelper.getButtonWithText(tag, new ArrayList<>(tags));
        StylingHelper.setTagButtonStyling(btn, isSelected, getContext());
    }

    @Override
    public void setMaxPrice(int i) {
        TextView textView = view.findViewById(R.id.actualPriceTextView);
        SeekBar seekBar = view.findViewById(R.id.priceSeekBar);
        String s = i + " kr";
        textView.setText(s);
        seekBar.setProgress(i);
    }

    /**
     * Notifies the presenter that the view has been destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSetView();

    }


}