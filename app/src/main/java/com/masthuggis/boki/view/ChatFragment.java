package com.masthuggis.boki.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.masthuggis.boki.R;
import com.masthuggis.boki.injectors.DependencyInjector;
import com.masthuggis.boki.presenter.ChatPresenter;
import com.masthuggis.boki.utils.GridSpacingItemDecoration;
import com.masthuggis.boki.utils.ViewCreator;

/**
 * Fragment for displaying active chats of the current user.
 *
 */
public class ChatFragment extends ListView implements ChatPresenter.View {
    private ChatPresenter presenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.presenter = new ChatPresenter(this, DependencyInjector.injectDataModel());
        View v = super.onCreateView(inflater, container, savedInstanceState);
        presenter.updateData();
        return v;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new MessagesRecyclerViewAdapter(getContext(), presenter);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(getContext(), 1);
    }

    @Override
    protected RecyclerView.ItemDecoration getSpacingDecorator() {
        return new GridSpacingItemDecoration(1, 25, true);
    }

    @Override
    protected View onCreateHeaderLayout() {
        return ViewCreator.createHeader(getContext(), getString(R.string.myMessages));
    }

    @Override
    protected View onCreateNoResultsFoundLayout() {
        return ViewCreator.createSimpleText(getContext(), getString(R.string.noChatsFound));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    /**
     * Displays the messages screen of the chosen chat.
     *
     */
    @Override
    public void showMessagesScreen(String chatID) {
        Intent intent = new Intent(getContext(), MessagesActivity.class);
        intent.putExtra("chatID",chatID);
        startActivity(intent);
    }

    @Override
    public void displayToast(String displayName) {
        Context context = getContext();
        CharSequence text = displayName + " har tagit bort annonsen ni diskuterat, er konversation kommer att arkiveras.";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}
