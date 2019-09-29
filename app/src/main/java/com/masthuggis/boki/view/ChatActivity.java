package com.masthuggis.boki.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.masthuggis.boki.R;
import com.masthuggis.boki.model.User;
import com.masthuggis.boki.presenter.ChatPresenter;

public class ChatActivity extends AppCompatActivity {
    private ChatPresenter presenter;


    private ImageView advertImageView;
    private TextView username;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Tveksamt om detta behövs.
        Toolbar toolbar = findViewById(R.id.chatToolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        presenter.messageActivityStarted();
    }


}
