package com.onevour.core.components.recycleview;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by user on 23/11/2017.
 */

public abstract class HolderGenericBasic<S extends ViewBinding, E> extends RecyclerView.ViewHolder {

    protected Context context;

    protected List<Listener> listeners = new ArrayList<>();

    protected Map<Class, Listener> cachedListener = new HashMap<>();

    protected E value;

    protected S binding;

    public HolderGenericBasic(S binding) {
        super(binding.getRoot());
        this.binding = binding;
        this.context = binding.getRoot().getContext();
    }

    public void setListeners(Listener listeners) {
        this.listeners.add(listeners);
    }

    protected Context getContext() {
        return context;
    }

    protected void onBindViewHolder(List<E> o, int position) {
        value = o.get(position);
    }

    protected abstract void onBindViewHolder(E o);

    @SuppressWarnings("unchecked")
    public <E extends Listener> E getListener(Class<E> clazz) {
        Listener listenerTMP = cachedListener.get(clazz);
        if (Objects.nonNull(listenerTMP)) {
            return (E) listenerTMP;
        }
        for (Listener o : listeners) {
            for (Class<?> impl : o.getClass().getInterfaces()) {
                if (impl.equals(clazz)) {
                    cachedListener.put(clazz, o);
                    return (E) o;
                }
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public <E extends Listener> Optional<E> listener(Class<E> clazz) {
        Listener listenerTMP = cachedListener.get(clazz);
        if (Objects.nonNull(listenerTMP)) {
            return Optional.ofNullable((E) listenerTMP);
        }
        for (Listener o : listeners) {
            for (Class<?> impl : o.getClass().getInterfaces()) {
                if (impl.equals(clazz)) {
                    cachedListener.put(clazz, o);
                    return Optional.ofNullable((E) o);
                }
            }
        }
        return Optional.empty();
    }

    public interface Listener {

    }

}
