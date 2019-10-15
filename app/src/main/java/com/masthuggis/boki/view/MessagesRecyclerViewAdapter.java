package com.masthuggis.boki.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.masthuggis.boki.R;
import com.masthuggis.boki.presenter.ChatPresenter;

public class MessagesRecyclerViewAdapter extends RecyclerView.Adapter<MessagesRecyclerViewAdapter.ViewHolder> {
    private Context mContext;
    /**
     * MessagesRecyclerViewAdapter is the adapter class of the recycler view used in MessagesFragment.
     */
    private ChatPresenter presenter;

    MessagesRecyclerViewAdapter(Context context, ChatPresenter chatPresenter) {
        this.mContext = context;
        this.presenter = chatPresenter;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent
                .getContext()).inflate(R.layout.messages_listitem, parent, false));
    }

    /**
     * Binds a messages_listitme to a specific position based on its position in a list.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        presenter.bindViewHolderAtPosition(position, holder);

    }

    /**
     * Used to know how many items it's supposed to display in the view.
     *
     * @return
     */

    @Override
    public int getItemCount() {
        if (presenter == null) {
            return 0;
        }
        return presenter.getItemCount();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements ChatThumbnailView {
        private TextView userTextView;
        private TextView dateTextView;
        private ImageView messageImageView;
        private String chatID;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            userTextView = itemView.findViewById(R.id.message_user);
            dateTextView = itemView.findViewById(R.id.message_time);
            messageImageView = itemView.findViewById(R.id.messageImageView);
            setupOnPressActionFor(itemView);


        }

        private void setupOnPressActionFor(View v) {
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClicked();
                }
            });
        }

        private void onItemClicked() {
            if (presenter.canProceedWithTapAction()) {
                presenter.onRowPressed(chatID);
            }
        }

        @Override
        public void setUserTextView(String userTextView) {
            this.userTextView.setText(userTextView);
        }

        @Override
        public void setDateTextView(String dateTextView) {
            this.dateTextView.setText(dateTextView);
        }

        @Override
        public void setMessageImageView(String messageImageView) {
            if (messageImageView == null) {
                return;
            }
            Glide.with(mContext).load(messageImageView).into(this.messageImageView);
        }
        @Override
        public void setChatID(String chatID) {
            this.chatID = chatID;
        }
       /* public void setDisabled(){
            backgroundLayout.setBackgroundResource(R.drawable.card_background_grey);

        }*/
    }


}
