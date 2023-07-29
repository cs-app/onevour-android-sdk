package com.onevour.core.utilities.ui.loader;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.onevour.core.R;


public class ButtonLoader extends RelativeLayout {

    enum state {
        NORMAL,
        PROGRESS,
        ERROR
    }

    private ProgressBar progress;
    private Button button;
    private TextView info;
    private Listener listener;

    public ButtonLoader(Context context) {
        super(context);
    }

    public ButtonLoader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public ButtonLoader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        View rootView = inflate(context, R.layout.button_loader, this);
        progress = rootView.findViewById(R.id.progress_bar);
        button = rootView.findViewById(R.id.button);
        info = rootView.findViewById(R.id.info);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    setResult(state.PROGRESS);
                    listener.onBtnLoaderClick();
                }
            }
        });
        setResult(state.NORMAL);
    }

    public void setLabel(String s) {
        button.setText(s);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setInfo(String info) {
        this.info.setText(info);
    }

    private void setResult(state state) {
        switch (state){
            case NORMAL:
                progress.setVisibility(GONE);
                info.setVisibility(GONE);
                button.setVisibility(VISIBLE);
                break;
            case PROGRESS:
                info.setText(null);
                info.setVisibility(GONE);
                button.setVisibility(GONE);
                progress.setVisibility(VISIBLE);
                break;
            case ERROR:
                progress.setVisibility(GONE);
                button.setVisibility(VISIBLE);
                info.setVisibility(VISIBLE);
                break;
        }
    }

    public void success() {
        setResult(state.NORMAL);
    }

    public void progress(){
        setResult(state.PROGRESS);
    }

    public void error() {
        setResult(state.NORMAL);
    }

    public void error(String message) {
        setResult(state.ERROR);
        info.setText(message);
    }

    public interface Listener {

        void onBtnLoaderClick();
    }


}
