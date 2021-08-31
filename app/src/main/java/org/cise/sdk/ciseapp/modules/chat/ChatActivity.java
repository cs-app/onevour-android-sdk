package org.cise.sdk.ciseapp.modules.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.cise.core.utilities.json.gson.GsonHelper;
import org.cise.sdk.ciseapp.R;

import butterknife.ButterKnife;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = ChatActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        connection();
    }

    private void connection() {
        StompClient client = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "wss://ws.sphere154.com/ws/websocket");
        client.connect();
        client.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {

                case OPENED:
                    Log.d(TAG, "Stomp connection opened");
                    break;

                case ERROR:
                    Log.e(TAG, "Error", lifecycleEvent.getException());
                    break;

                case CLOSED:
                    Log.d(TAG, "Stomp connection closed");
                    break;
            }
        });

        Log.d(TAG, "connection status " + client.isConnected());
        client.topic("/topic/public").subscribe(message -> {
            Log.i(TAG, "Received message: " + message.getPayload());
        });
        ChatMessage user = new ChatMessage();
        user.setType(MessageType.JOIN);
        user.setSender("ANDROID");
        user.setContent("join");
        client.send("/app/chat.addUser", GsonHelper.gson.toJson(user)).subscribe(
                () -> Log.d(TAG, "Sent data!"),
                error -> Log.e(TAG, "Encountered error while sending data!", error)
        );

        client.send("/app/chat.sendMessage", "world").subscribe(
                () -> Log.d(TAG, "Sent data!"),
                error -> Log.e(TAG, "Encountered error while sending data!", error)
        );
    }


}