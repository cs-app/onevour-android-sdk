package com.onevour.sdk.impl.modules.chat;

import com.onevour.core.components.recycleview.AdapterGeneric;
import com.onevour.core.components.recycleview.HolderGeneric;
import com.onevour.sdk.impl.databinding.HolderChatBinding;

public class AdapterMessage extends AdapterGeneric<MessageModel> {

    @Override
    protected void registerHolder() {
        registerBindView(HolderChat.class);
    }

    public static class HolderChat extends HolderGeneric<HolderChatBinding, MessageModel> {

        private static final String TAG = HolderChat.class.getSimpleName();

        public HolderChat(HolderChatBinding binding) {
            super(binding);
        }


        @Override
        protected void onBindViewHolder(MessageModel o) {
            super.onBindViewHolder(o);
            ChatMessage chatMessage = o.getModel();
            binding.sender.setText(chatMessage.getSender());
            binding.time.setText(chatMessage.getTime());
            binding.message.setText(chatMessage.getContent());
        }
    }
}
