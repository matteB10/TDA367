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
import com.masthuggis.boki.view.ThumbnailView;

public class ProductsRecyclerViewAdapter extends RecyclerView.Adapter<ProductsRecyclerViewAdapter.ViewHolder> {

    private IProductsPresenter presenter;
    private Context mContext;

    public ProductsRecyclerViewAdapter(Context context, IProductsPresenter presenter) {
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
        return presenter.getItemCount();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        presenter.onBindThumbnailViewAtPosition(position, holder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements ThumbnailView {

        private TextView nameTextView;
        private TextView priceTextView;
        private ImageView imageView;
        private String id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.detailsID);
            priceTextView = itemView.findViewById(R.id.price);
            imageView = itemView.findViewById(R.id.imageView);
            setupOnPressActionFor(itemView);
        }

        private void setupOnPressActionFor(View v) {
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.onRowPressed(id);
                }
            });
        }

        @Override
        public void setTitle(String name) {
            nameTextView.setText(name);
        }

        @Override
        public void setPrice(int price) {
            priceTextView.setText(Integer.toString(price) + " kr");
        }

        @Override
        public void setImageUrl(String url) {
            // TODO: implement func to show image
        }

        @Override
        public void setId(String id) {
            this.id =id;
        }


    }

}
