package com.masthuggis.boki.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.masthuggis.boki.R;
import com.masthuggis.boki.backend.Repository;
import com.masthuggis.boki.backend.UserRepository;
import com.masthuggis.boki.model.Chat;
import com.masthuggis.boki.model.User;
import com.masthuggis.boki.model.iUser;
import com.masthuggis.boki.presenter.ChatPresenter;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    private ChatPresenter presenter;


    private ImageView advertImageView;
    private TextView username;
    private User user;


    private LinearLayout layout;
    private RelativeLayout layout_2;
    private ImageView sendButton;
    private EditText messageArea;
    private ScrollView scrollView;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        iUser user = UserRepository.getInstance().getCurrentUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        this.presenter = new ChatPresenter();
        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout) findViewById(R.id.layout2);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        messageArea = (EditText) findViewById(R.id.messageArea);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                Map<String, String> map = new HashMap<>();
                if (!messageText.equals("")) {
                    map.put("message", messageText);
                    map.put("user", user.getId());
                    messageArea.setText("");
                    counter++;
                }
                addMessageBox(map.get("message"), counter % 2);
            }
        });
        presenter.messageActivityStarted();

    }

    public void addMessageBox(String message, int type) {
        TextView textView = new TextView(ChatActivity.this);
        textView.setTextSize(14);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;
        if (type == 1) {
            lp2.gravity =Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.incoming_speech_bubble);
        } else {
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.outgoing_speech_bubble);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}
