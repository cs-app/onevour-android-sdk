package org.cise.sdk.ciseapp.modules.chat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.cise.core.utilities.commons.RefSession;
import org.cise.core.utilities.commons.ValueUtils;
import org.cise.core.utilities.format.DTFormat;
import org.cise.core.utilities.helper.UIHelper;
import org.cise.core.utilities.json.gson.GsonHelper;
import org.cise.sdk.ciseapp.R;

import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = ChatActivity.class.getSimpleName();

    RefSession session = new RefSession();

    AdapterMessage adapterMessage = new AdapterMessage();

    AtomicBoolean error = new AtomicBoolean();

    StompClient client;

    @BindView(R.id.messages)
    RecyclerView recyclerView;

    @BindView(R.id.message)
    EditText message;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add("Setting");
        item.setOnMenuItemClickListener(item1 -> {
            Log.d(TAG, "click menu");
            showConfig();
            return true;
        });
        return true;
    }

    private void showConfig() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialog_input_text, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        final EditText userInput = promptsView.findViewById(R.id.name);
        ChatMessage chatMessage = session.find(ChatMessage.class);
        if (ValueUtils.isNull(chatMessage)) chatMessage = new ChatMessage();
        userInput.setText(chatMessage.getSender());
        // set dialog message
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", (dialog, id) -> {
            ChatMessage o = new ChatMessage();
            o.setSender(userInput.getText().toString());
            session.save(o);
        }).setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        UIHelper.initRecyclerView(recyclerView, adapterMessage);
        message.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                //got focus
                recyclerView.postDelayed(() -> recyclerView.scrollToPosition(adapterMessage.getItemCount() - 1), 300);

            } else {
                //lost focus
            }
        });
        connection();

    }

    @OnClick(R.id.send)
    public void send() {
        String values = message.getText().toString().trim();
        if (ValueUtils.isEmpty(values)) return;
        sendMessage(values);
        message.setText(null);
    }


    private void onReceiveMessage(ChatMessage chatMessage) {
        new Handler(Looper.getMainLooper()).post(() -> {
            adapterMessage.addMore(new MessageModel(chatMessage));
            recyclerView.scrollToPosition(adapterMessage.getItemCount() - 1);
        });
    }

    private void connection() {
        client = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "wss://ws.sphere154.com/ws/websocket");
        client.connect();
        client.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED:
                    Log.d(TAG, "Stomp connection opened");
                    registerListener();
                    sendMessageJoin();
                    break;

                case ERROR:
                    Log.e(TAG, "Error", lifecycleEvent.getException());
                    error.set(true);

                    break;

                case CLOSED:
                    Log.d(TAG, "Stomp connection closed");
                    if (error.get()) {
                        client.connect();
                        error.set(false);
                    }
                    break;
            }
        });

        Log.d(TAG, "connection status " + client.isConnected());
//        client.topic("/topic/public").safeSubscribe();
    }

    private void registerListener() {
        client.topic("/topic/public").subscribe(message -> {
            Log.i(TAG, "Received message: " + message.getPayload());
            ChatMessage chatMessage = GsonHelper.gson.fromJson(message.getPayload(), ChatMessage.class);
            if (chatMessage.getType() != MessageType.CHAT) {
                chatMessage.setContent(chatMessage.getType().name());
            }
            onReceiveMessage(chatMessage);
        });
    }

    private void sendMessageJoin() {
        if (ValueUtils.isNull(client)) return;
        ChatMessage user = session.find(ChatMessage.class);
        if (ValueUtils.isNull(user)) {
            showError("input name in setting toolbars");
            return;
        }
        user.setType(MessageType.JOIN);
        user.setSender(user.getSender());
        client.send("/app/chat.addUser", GsonHelper.gson.toJson(user)).subscribe(
                () -> Log.d(TAG, "Sent data!"),
                error -> {
                    Log.e(TAG, "Encountered error while sending data!", error);
                    showError(error.getMessage());
                }
        );
    }

    private void sendMessage(String message) {
        if (ValueUtils.isNull(client)) return;
        ChatMessage user = session.find(ChatMessage.class);
        if (ValueUtils.isNull(user)) {
            showError("input name in Setting toolbar");
            return;
        }
        user.setType(MessageType.CHAT);
        user.setSender(user.getSender());
        user.setContent(message);
        user.setTime(DTFormat.now());
        client.send("/app/chat.sendMessage", GsonHelper.gson.toJson(user)).subscribe(
                () -> Log.d(TAG, "Sent data!"),
                error -> {
                    Log.e(TAG, "Encountered error while sending data!", error);
                    showError(error.getMessage());
                }
        );
    }

    private void showError(String message) {
        new Handler(Looper.getMainLooper()).post(() -> {
            Snackbar.make(this.message, message, 1500).show();
        });
    }


}