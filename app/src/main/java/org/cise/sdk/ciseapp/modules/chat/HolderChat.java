package org.cise.sdk.ciseapp.modules.chat;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.cise.core.utilities.commons.ValueUtils;
import org.cise.core.utilities.ui.adapter.recyclerview.HolderGeneric;
import org.cise.sdk.ciseapp.R;

public class HolderChat extends HolderGeneric<MessageModel> {

    private static final String TAG = HolderChat.class.getSimpleName();

    TextView sender, time, message;

    public HolderChat(View view) {
        super(view);
        Log.d(TAG, "view is null : " + ValueUtils.isNull(sender));
        sender = findViewById(R.id.sender);
        time = findViewById(R.id.time);
        message = findViewById(R.id.message);
    }

    @Override
    protected void onBindViewHolder(MessageModel o) {
        super.onBindViewHolder(o);
        ChatMessage chatMessage = o.getModel();
        sender.setText(chatMessage.getSender());
        time.setText(chatMessage.getTime());
        message.setText(chatMessage.getContent());
    }
}
