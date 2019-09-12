package com.masthuggis.boki.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.HomePresenter;
import com.masthuggis.boki.presenter.ProductsRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomePresenter.View {

    private HomePresenter presenter;
    private List<String> adverts;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_fragment, container, false);

        this.presenter = new HomePresenter(this);
        this.adverts = createMockAdvertsData();
        initRec(v);
        return v;

    }

    private void initRec(View v) {
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.advertsRecyclerView);
        ProductsRecyclerViewAdapter adapter = new ProductsRecyclerViewAdapter(adverts);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
    }

    private List<String> createMockAdvertsData() {
        // TODO: Use data from local JSON file for development
        List<String> adverts = new ArrayList<>();
        adverts.add("Grundläggande datorteknik");
        adverts.add("Linjär Algebra");
        adverts.add("Diskret Matematik");
        return adverts;
    }

    @Override
    public void showLoadingScreen() {

    }

    @Override
    public void hideLoadingScreen() {

    }

    @Override
    public void setProductItems() {

    }
}
