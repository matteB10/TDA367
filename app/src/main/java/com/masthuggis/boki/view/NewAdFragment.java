package com.masthuggis.boki.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.masthuggis.boki.R;

public class NewAdFragment extends Fragment {
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.new_ad_fragment, container, false);

        ImageButton newCollectionButton = v.findViewById(R.id.to_new_collection_imagebutton);
        ImageButton newBookButton = v.findViewById(R.id.to_new_book_imagebutton);
        newCollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        newBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO
                // Öppna ny vy för att skapa ny bok. Samt skapa presenter för detta.
                Intent intent = new Intent(getActivity(), CreateAdActivity.class);
                startActivity(intent);

            }
        });
        newCollectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                // Öppna ny vy för att skapa ny samling.

            }
        });
        return v;

    }


}
