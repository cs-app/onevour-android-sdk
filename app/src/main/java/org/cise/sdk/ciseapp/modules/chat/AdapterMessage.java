package org.cise.sdk.ciseapp.modules.chat;

import org.cise.core.utilities.ui.adapter.recyclerview.AdapterGeneric;
import org.cise.sdk.ciseapp.R;

public class AdapterMessage extends AdapterGeneric<MessageModel> {

    @Override
    protected void registerHolder() {
        register(R.layout.holder_chat, HolderChat.class);
    }
}
