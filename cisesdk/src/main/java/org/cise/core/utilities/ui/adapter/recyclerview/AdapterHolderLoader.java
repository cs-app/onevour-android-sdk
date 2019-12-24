package org.cise.core.utilities.ui.adapter.recyclerview;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.cise.core.R;

public class AdapterHolderLoader extends GenericHolder {

    private static final String TAG = "GAHLoader";

    private Listener listener;

    protected TextView info;

    protected ProgressBar progressBar;

    protected AdapterHolderLoader(View view) {
        super(view);
        info = view.findViewById(R.id.error_info);
        progressBar = view.findViewById(R.id.progress_bar);
        info.setOnClickListener(v -> {
            info.setText(null);
            info.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            if (getContext() instanceof Listener) {
                listener = (Listener) getContext();
            }
            if (null != listener) listener.onLoaderShown();
        });
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    protected interface Listener {

        void onLoaderShown();

    }

}
