package com.masthuggis.boki.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.masthuggis.boki.R;
import com.masthuggis.boki.view.RowView;

import java.util.List;

public class ProductsRecyclerViewAdapter extends RecyclerView.Adapter<ProductsRecyclerViewAdapter.ViewHolder> {

    private HomePresenter presenter;
    private Context mContext;

    public ProductsRecyclerViewAdapter(Context context, HomePresenter presenter) {
        this.mContext = context;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder v = new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.advert_listitem, parent, false));
        return v;
    }

    @Override
    public int getItemCount() {
        return presenter.getNumRows();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        presenter.onBindRepositoryRowViewAtPosition(position, holder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements RowView {

        private TextView nameTextView;
        private TextView priceTextView;
        private ImageView imageView;
        private int rowIndex;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name);
            priceTextView = itemView.findViewById(R.id.price);
            imageView = itemView.findViewById(R.id.imageView);
            setupOnPressActionFor(itemView);
        }

        private void setupOnPressActionFor(View v) {
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.onRowPressed(rowIndex);
                }
            });
        }

        @Override
        public void setTitle(String name) {
            nameTextView.setText(name);
        }

        @Override
        public void setPrice(int price) {
            priceTextView.setText(Integer.toString(price));
        }

        @Override
        public void setImageUrl(String url) {
            // TODO: implement func to show image
        }

        @Override
        public void setRowIndex(int index) {
            this.rowIndex = index;
        }
    }

}
