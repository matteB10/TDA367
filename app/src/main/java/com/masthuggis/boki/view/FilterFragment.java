package com.masthuggis.boki.view;

import android.content.Context;
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
import com.masthuggis.boki.utils.FilterPresenter;
import com.masthuggis.boki.utils.StylingHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterFragment extends Fragment implements FilterPresenter.View {

    private FilterPresenter presenter;
    private List<Button> tags = new ArrayList<>();
    private View view;
    private OnFragmentCommunicationListener mListener;



    public static FilterFragment newInstance() {
        return new FilterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.filter_fragment,container,false);
        presenter = new FilterPresenter(this);
        initTags();
        initSeekBar();
        setTagListeners();
        initDoneButton();
        return view;
    }

    /**
     * Get list of subject tags from resources, displays as buttons in view
     */
    private void initTags(){
        presenter.setPreDefTags(Arrays.asList(getResources().getStringArray(R.array.preDefSubjectTags))); //set pre def tags in presenter
        tags = TagHelper.createPreDefTagButtons(presenter.getPreDefTags(),getContext());
        TagHelper.displayPreDefTagButtons(view.findViewById(R.id.filterTagsLinearLayout),new ArrayList<>(tags), getContext());
    }
    /**
     * Set listener on price SeekBar, notifies presenter
     */
    private void initSeekBar(){
        SeekBar maxPrice = view.findViewById(R.id.priceSeekBar);
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
        Button btn = view.findViewById(R.id.applyFilterButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putInt("price",presenter.getMaxPrice());
                args.putStringArrayList("tags",new ArrayList<>(presenter.getActiveTags()));
                mListener.onFiltersChanged(args);
            }
        });
    }


    @Override
    public void addTag(String tag, boolean isSelected) {
        Button btn = TagHelper.getButtonWithText(tag, new ArrayList<>(tags));
        StylingHelper.setTagButtonStyling(btn,isSelected);
    }
    @Override
    public void setMaxPrice(int i){
        TextView t = view.findViewById(R.id.actualPriceTextView);
        String s = i + " kr";
        t.setText(s);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentCommunicationListener) {
            mListener = (OnFragmentCommunicationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentCommunicationListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    interface OnFragmentCommunicationListener {

        void onFiltersChanged(Bundle args);
    }


}
