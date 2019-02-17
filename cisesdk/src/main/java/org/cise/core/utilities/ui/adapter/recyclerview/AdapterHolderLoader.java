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
        info = view.findViewById(R.id.error);
        progressBar = view.findViewById(R.id.progress_bar);
        if (view.getContext() instanceof Listener) {
            listener = (Listener) view.getContext();
        }
        info.setOnClickListener(v -> {
            info.setText(null);
            info.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            if (listener != null) listener.onLoaderShown();
        });
    }

    protected interface Listener {

        void onLoaderShown();

    }

}
