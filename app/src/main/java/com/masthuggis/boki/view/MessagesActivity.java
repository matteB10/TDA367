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


    @Override
    public void addMessageBox(String messageText, boolean sentByCurrentUser) {
        TextView textView = new TextView(MessagesActivity.this);
        textView.setTextSize(14);
        textView.setText(messageText);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;
        if (!sentByCurrentUser) {
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.incoming_speech_bubble);
        } else {
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.outgoing_speech_bubble);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
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
