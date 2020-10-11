package org.cise.core.utilities.ui.adapter.recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.cise.core.utilities.commons.ValueUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
public class HolderGenericV2<T> extends RecyclerView.ViewHolder {

    private static final String TAG = "GAdapterHolder";

    protected Context context;

    private int overrideCount = 0;

    private int position;

    private List<Listener> listener = new ArrayList<>();

    private Map<Class, Listener> cachedListener = new HashMap<>();

    public HolderGenericV2(View view) {
        super(view);
        this.context = view.getContext();
    }

    public void setListener(HolderGenericV2.Listener listener) {
        this.listener.add(listener);
    }

    protected Context getContext() {
        return context;
    }

    protected void onBindViewHolder(List<T> o, int position) {
        overrideCount++;
        info();
    }

    protected void onBindViewHolder(List<T> o, int position, int size) {
        overrideCount++;
        info();
    }

    protected void onBindViewHolder(T o) {
        overrideCount++;
        info();
    }

    protected void onBindViewHolder(T o, int position) {
        overrideCount++;
        info();
    }

    protected void onBindViewHolder(T o, int position, int size) {
        overrideCount++;
        info();
    }

    protected void onBindViewHolder(T o, int position, boolean isFirst, boolean isLast) {
        overrideCount++;
        info();
    }

    private void info() {
        if (overrideCount == 3) {
            Log.w(TAG, "override at least 1 onBindViewHolder");
        }
    }

    protected List<Listener> getListener() {
        return listener;
    }

    public <E extends HolderGenericV2.Listener> E getListener(Class<E> clazz) {
        HolderGenericV2.Listener listenerTMP = cachedListener.get(clazz);
        if (ValueUtils.nonNull(listenerTMP)) {
            return (E) listenerTMP;
        }
        for (HolderGenericV2.Listener o : listener) {
            for (Class<?> impl : o.getClass().getInterfaces()) {
                if (impl.equals(clazz)) {
                    cachedListener.put(clazz, o);
                    return (E) o;
                }
            }
        }
        return null;
    }

    protected void setPosition(int position) {
        this.position = position;
    }

    public int getCurrentPosition() {
        return position;
    }

    public interface AdapterListener<E> {

        void onLoadRetry(int index, E o);

    }

    public interface Listener {

    }
}
