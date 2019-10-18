package com.masthuggis.boki.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.IListPresenter;

public class ProductsRecyclerViewAdapter extends RecyclerView.Adapter<ProductsRecyclerViewAdapter.ViewHolder> {

    private IListPresenter presenter;
    private Context mContext;

    ProductsRecyclerViewAdapter(Context context, IListPresenter presenter) {
        this.mContext = context;
        this.presenter = presenter;
        getItemCount();
    }

    private void addDecorator(RecyclerView.ItemDecoration decorator) {
        this.addDecorator(decorator);
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
        if(presenter==null){
            return 0;
        }
        return presenter.getItemCount();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        presenter.onBindThumbnailViewAtPosition(position, holder);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements ThumbnailView {

        private TextView titleTextView;
        private TextView priceTextView;
        private ImageView imageView;
        private ConstraintLayout conditionLayout;
        private TextView conditionTextView;
        private String id;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            imageView = itemView.findViewById(R.id.thumbNailImageView);
            conditionLayout = itemView.findViewById(R.id.thumbnailConditionLayout);
            conditionTextView = itemView.findViewById(R.id.conditionTextView);

            itemView.setOnClickListener(this::onItemClicked);
        }

        private void onItemClicked(View v) {
            presenter.onRowPressed(id);
        }

        @Override
        public void setTitle(String name) {
            titleTextView.setText(name);
        }

        @Override
        public void setPrice(long price) {
            priceTextView.setText(price + " kr");
        }


        @Override
        public void setImageURL(String url) {
            Glide.with(mContext).load(url).into(imageView);
        }

        @Override
        public void setId(String id) {
            this.id = id;
        }

        @Override
        public void setCondition(int condition, int drawable) {
            conditionTextView.setText(condition);
            conditionLayout.setBackground(mContext.getDrawable(drawable));
        }
    }
}
