package com.masthuggis.boki.view;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.masthuggis.boki.R;
import com.masthuggis.boki.injectors.DependencyInjector;
import com.masthuggis.boki.presenter.MessagesPresenter;
/**
 * Class containing all visual logic for initializing and displaying the Messages View.
 * Used by ChatFragment and DetailsActivity.
 * Written by masthuggis
 */
public class MessagesActivity extends AppCompatActivity implements MessagesPresenter.View {
    private MessagesPresenter presenter;


    private LinearLayout layout;
    private EditText messageArea;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String chatID = getIntent().getExtras().get("chatID").toString();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        layout = findViewById(R.id.layout1);
        ImageView sendButton = findViewById(R.id.sendButton);
        messageArea = findViewById(R.id.messageArea);
        scrollView = findViewById(R.id.scrollView);

        this.presenter = new MessagesPresenter(this, chatID, DependencyInjector.injectDataModel());
        sendButton.setOnClickListener(v -> {
            presenter.sendMessage(messageArea.getText().toString());
            messageArea.setText("");
        });

    }

    public void addSentMessageBox(String messageText) {
        TextView textView = createMessageTextView(messageText);
        createMessageBox(Gravity.RIGHT, textView);
        addMessagesBoxToLayout(textView);

    }

    @Override
    public void addReceivedMessageBox(String messageText) {
        TextView textView = createMessageTextView(messageText);
        createMessageBox(Gravity.LEFT, textView);
        addMessagesBoxToLayout(textView);
    }

    private void addMessagesBoxToLayout(TextView textView) {
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

    private TextView createMessageTextView(String messageText) {
        TextView textView = new TextView(MessagesActivity.this);
        textView.setTextSize(14);
        textView.setText(messageText);
        return textView;
    }

    private void createMessageBox(int gravity, TextView tv) {
        LinearLayout.LayoutParams innerLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        innerLayout.weight = 1.0f;
        innerLayout.gravity = gravity;
        if (gravity == Gravity.LEFT) {
            tv.setBackgroundResource(R.drawable.outgoing_speech_bubble);
        } else {
            tv.setBackgroundResource(R.drawable.incoming_speech_bubble);
        }
        tv.setLayoutParams(innerLayout);
    }


    @Override
    public void update() {
        layout.removeAllViews();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }
}
