package com.masthuggis.boki.view;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.masthuggis.boki.presenter.MessagesPresenter;
import com.masthuggis.boki.presenter.ProductsRecyclerViewAdapter;

class MessagesRecyclerViewAdapter extends RecyclerView.Adapter<ProductsRecyclerViewAdapter.ViewHolder> {

    public MessagesRecyclerViewAdapter(MessagesPresenter messagesPresenter) {


    }

    @NonNull
    @Override
    public ProductsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsRecyclerViewAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
