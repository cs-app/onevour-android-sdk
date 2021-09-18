package org.cise.core.utilities.ui.adapter.recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;

import org.cise.core.utilities.commons.ValueOf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"rawtypes", "unchecked"})
public class HolderGeneric<T> extends RecyclerView.ViewHolder {

    private static final String TAG = "GAdapterHolder";

    protected Context context;

    private int overrideCount = 0;

    private int position;

    private View view;

    private List<Listener> listener = new ArrayList<>();

    private Map<Class, Listener> cachedListener = new HashMap<>();

    public HolderGeneric(View view) {
        super(view);
        this.view = view;
        this.context = view.getContext();
    }

    protected <T extends View> T findViewById(@IdRes int id) {
        return view.findViewById(id);
    }

    public void setListener(HolderGeneric.Listener listener) {
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

    public <E extends HolderGeneric.Listener> E getListener(Class<E> clazz) {
        HolderGeneric.Listener listenerTMP = cachedListener.get(clazz);
        if (ValueOf.nonNull(listenerTMP)) {
            return (E) listenerTMP;
        }
        for (HolderGeneric.Listener o : listener) {
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
