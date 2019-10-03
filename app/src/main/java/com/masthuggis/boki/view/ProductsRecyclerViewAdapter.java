package com.masthuggis.boki.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import com.masthuggis.boki.presenter.IProductsPresenter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class ProductsRecyclerViewAdapter extends RecyclerView.Adapter<ProductsRecyclerViewAdapter.ViewHolder> {

    private IProductsPresenter presenter;
    private Context mContext;

    public ProductsRecyclerViewAdapter(Context context, IProductsPresenter presenter) {
        this.mContext = context;
        this.presenter = presenter;
        getItemCount();
    }

    public void addDecorator(RecyclerView.ItemDecoration decorator) {
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

    public class ViewHolder extends RecyclerView.ViewHolder implements ThumbnailView {

        private TextView titleTextView;
        private TextView priceTextView;
        private ImageView imageView;
        private ConstraintLayout conditionLayout;
        private TextView conditionTextView;
        private String id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            imageView = itemView.findViewById(R.id.thumbNailImageView);
            conditionLayout = itemView.findViewById(R.id.thumbnailConditionLayout);
            conditionTextView = itemView.findViewById(R.id.conditionTextView);

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
            titleTextView.setText(name);
        }

        @Override
        public void setPrice(long price) {
            priceTextView.setText(Long.toString(price) + " kr");
        }


        @Override
        public void setImageURL(String url) {
            Glide.with(mContext).load(url).into(imageView); //Does run when app is first started
            //Needs to be somehow delayed until files are returned from firebase?
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
