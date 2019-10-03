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
import com.masthuggis.boki.presenter.MessagesPresenter;

public class MessagesRecyclerViewAdapter extends RecyclerView.Adapter<MessagesRecyclerViewAdapter.ViewHolder> {

    private MessagesPresenter presenter;

    public MessagesRecyclerViewAdapter(MessagesPresenter messagesPresenter) {
        this.presenter = messagesPresenter;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent
                .getContext()).inflate(R.layout.messages_listitem, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        presenter.bindViewHolderAtPosition(position, holder);

    }

    @Override
    public int getItemCount() {
        if(presenter==null){
            return 0;
        }
        return presenter.getItemCount();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView userTextView;
        private TextView dateTextView;
        private ImageView messageImageView;
        private String chatID;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userTextView = itemView.findViewById(R.id.message_user);
            dateTextView = itemView.findViewById(R.id.message_time);
            messageImageView = itemView.findViewById(R.id.messageImageView);
            setupOnPressActionFor(itemView);


        }

        private void setupOnPressActionFor(View v) {
            v.setOnClickListener(view -> presenter.onRowPressed(chatID));
        }

        public void setUserTextView(String userTextView) {
            this.userTextView.setText(userTextView);
        }

        public void setDateTextView(String dateTextView) {
            this.dateTextView.setText(dateTextView);
        }

        public void setMessageImageView(String messageImageView) {
            if (messageImageView == null) {
                return;
            }
            this.messageImageView.setImageURI(Uri.parse(messageImageView));
        }

        public void setChatID(String chatID) {
            this.chatID = chatID;
        }
    }


}
