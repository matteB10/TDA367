package com.masthuggis.boki.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.HomePresenter;
import com.masthuggis.boki.presenter.ProductsRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomePresenter.View {

    private HomePresenter presenter;
    private List<String> adverts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.presenter = new HomePresenter(this);
        this.adverts = createMockAdvertsData();

        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.advertsRecyclerView);
        ProductsRecyclerViewAdapter adapter = new ProductsRecyclerViewAdapter(this, adverts);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        // TODO: Show loading animation when data is being fetched from database
    }

    @Override
    public void hideLoadingScreen() {
        // TODO: Hide loading animation (data is now loaded from the database)
    }

    @Override
    public void setProductItems() {
        // TODO: When model is defined, will set the product items to display
    }
}
