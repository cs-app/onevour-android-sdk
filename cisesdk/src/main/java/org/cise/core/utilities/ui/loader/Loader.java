package org.cise.core.utilities.ui.loader;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.cise.core.R;

public class Loader extends RelativeLayout {

    enum status {
        NORMAL,
        PROGRESS,
        ERROR
    }

    private ProgressBar progress;
    private TextView info;
    private Listener listener;

    public Loader(Context context) {
        super(context);
    }

    public Loader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public Loader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        View rootView = inflate(context, R.layout.loader, this);
        progress = rootView.findViewById(R.id.progress_bar);
        info = rootView.findViewById(R.id.info);
        info.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    setResult(status.PROGRESS);
                    listener.onLoaderClick();
                }
            }
        });
        setResult(status.NORMAL);
    }

    public void load() {
        if (listener != null) {
            setResult(status.PROGRESS);
            listener.onLoaderClick();
        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setInfo(String info) {
        this.info.setText(info);
    }

    private void setResult(status i) {
        switch (i) {
            case NORMAL:
                info.setVisibility(GONE);
                progress.setVisibility(GONE);
                break;
            case PROGRESS:
                progress.setVisibility(VISIBLE);
                info.setVisibility(GONE);
                break;
            case ERROR:
                progress.setVisibility(GONE);
                info.setVisibility(VISIBLE);
                break;
        }
    }

    public void start() {
        setResult(status.PROGRESS);
    }

    public void success() {
        setResult(status.NORMAL);
    }

    public void error(String message) {
        setResult(status.ERROR);
        info.setText(message);
    }

    public interface Listener {

        void onLoaderClick();
    }


}
