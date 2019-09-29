package com.masthuggis.boki.view;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.masthuggis.boki.R;
import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.presenter.MessagesPresenter;

public class MessagesRecyclerViewAdapter extends RecyclerView.Adapter<MessagesRecyclerViewAdapter.ViewHolder> {

    private MessagesPresenter presenter;

    public MessagesRecyclerViewAdapter(MessagesPresenter messagesPresenter) {
        this.presenter = messagesPresenter;


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MessagesRecyclerViewAdapter.ViewHolder v = new ViewHolder(LayoutInflater.from(parent
                .getContext()).inflate(R.layout.messages_listitem, parent, false));
        return v;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        presenter.bindViewHolderAtPosition(position, holder);

    }

    @Override
    public int getItemCount() {
        return Repository.getInstance().getAllAds().size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userTextView;
        private TextView dateTextView;
        private ImageView messageImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userTextView = itemView.findViewById(R.id.message_user);
            dateTextView = itemView.findViewById(R.id.message_time);
            messageImageView = itemView.findViewById(R.id.messageImageView);


        }

        public void setUserTextView(String userTextView) {
            this.userTextView.setText(userTextView);
        }

        public void setDateTextView(String dateTextView) {
            this.dateTextView.setText(dateTextView);
        }

        public void setMessageImageView(String messageImageView) {
            this.messageImageView.setImageURI(Uri.parse(messageImageView));
        }
    }
}
