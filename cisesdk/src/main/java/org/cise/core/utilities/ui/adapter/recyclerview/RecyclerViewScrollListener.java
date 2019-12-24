package org.cise.core.utilities.ui.adapter.recyclerview;
//
// Created by Zuliadin on 2019-12-24.
//

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.security.InvalidParameterException;

public class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private static final String TAG = "RV-SCROLL";

    private RecyclerViewPaginationListener listener;

    public static final int PAGE_START = 1;

    @NonNull
    private GenericAdapter adapter;

    @NonNull
    private LinearLayoutManager layoutManager;
    /**
     * Set scrolling threshold here (for now i'm assuming 10 item in one page)
     */
    private int size = 10;

    /**
     * Supporting only LinearLayoutManager for now.
     */
    public RecyclerViewScrollListener(@NonNull GenericAdapter adapter, @NonNull LinearLayoutManager layoutManager, int size, RecyclerViewPaginationListener listener) {
        this.adapter = adapter;
        this.layoutManager = layoutManager;
        this.size = size;
        this.listener = listener;
        if (size == 0) {
            throw new InvalidParameterException("size cannot must greater than 0");
        }
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        Log.d(TAG, visibleItemCount + "|" + totalItemCount + "|" + firstVisibleItemPosition + "|" + adapter.isLoader());
        if (null != listener && !adapter.isLoader()) {
            if (visibleItemCount + firstVisibleItemPosition == totalItemCount) {
                recyclerView.post(() -> adapter.showLoader());

                Log.d(TAG, "show loader");
            }
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= size) {
                Log.d(TAG, "load more item");
                listener.loadMoreItems(adapter.getItem(totalItemCount - 1));
            }
        }
    }

    public interface RecyclerViewPaginationListener<E> {

        void loadMoreItems(E e);

    }


}
