package org.cise.core.utilities.ui.adapter.recyclerview;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.cise.core.R;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 20/11/2017.
 * Arguments T is ViewHolder<br>
 */
@SuppressWarnings("unchecked")
public class GenericAdapter<T extends GenericHolder, E> extends RecyclerView.Adapter<T> implements GenericHolder.Listener {

    private static final String TAG = "GAdapter";

    private Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    private Map<String, T> cached = new HashMap<>();
    private boolean isLoader;
    private int layoutId;
    private ArrayList<E> adapterList = new ArrayList<>();

    private final int VIEW_CONTENT = 0;
    private final int VIEW_LOADER = 1;
    private AdapterHolderLoader holderLoader;
    private int lastSelected = -1;

    private GenericHolder.AdapterListener adapterListener;

    protected GenericAdapter() {

    }

    protected GenericAdapter(int layoutId) {
        this.layoutId = layoutId;
    }

    protected GenericAdapter(int layoutId, ArrayList<E> adapterList) {
        this.layoutId = layoutId;
        this.adapterList = adapterList;
    }

    public GenericAdapter(int layoutId, ArrayList adapterList, GenericHolder.AdapterListener adapterListener) {
        this.layoutId = layoutId;
        this.adapterList = adapterList;
        this.adapterListener = adapterListener;
    }

    public GenericAdapter(int layoutId, GenericHolder.AdapterListener adapterListener) {
        this.layoutId = layoutId;
        this.adapterListener = adapterListener;
    }

    public void setAdapterListener(GenericHolder.AdapterListener adapterListener) {
        this.adapterListener = adapterListener;
    }

    public void setLoader(boolean loader) {
        isLoader = loader;
        if (isLoader) {
            this.adapterList.add(null);
        } else {
            int size = this.adapterList.size();
            if (size > 0) {
                if (this.adapterList.get(size - 1) == null) this.adapterList.remove(size - 1);
            }
        }
    }

    public void addMore(E o) {
        int size = this.adapterList.size();
        this.adapterList.add(o);
        notifyItemRangeInserted(size - 1, size);
    }

    public void addMore(final List<E> adapterList) {
        int newSize = adapterList.size();
        int start = this.adapterList.size();
        if (isLoader) {
            this.adapterList.remove(start - 1);
            start--;
        }
        this.adapterList.addAll(adapterList);
        notifyItemRangeInserted(start, newSize);
    }

    public void loaderFinish() {
        final int newSize = adapterList.size();
        setLoader(false);
        notifyItemRemoved(newSize - 1);
    }

    @Override
    public int getItemViewType(int position) {
        return adapterList.get(position) != null ? VIEW_CONTENT : VIEW_LOADER;
    }

    @Override
    public T onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        Log.d(TAG, "viewType : " + viewType);
        if (VIEW_CONTENT == viewType) {
            View convertView = LayoutInflater.from(context).inflate(this.layoutId, parent, false);
            return holderGenerator(type, convertView);
        } else {
            View convertView = LayoutInflater.from(context).inflate(R.layout.adapter_loader, parent, false);
            return (T) new AdapterHolderLoader(convertView);
        }
    }

    private T holderGenerator(Type type, View convertView) {
        T viewHolder = null;
        try {
            if (null == cached.get(type.hashCode())) {
                Constructor constructor = ((Class<GenericHolder>) type).getConstructor(View.class);
                viewHolder = (T) constructor.newInstance(convertView);
                cached.put("VH" + type.hashCode(), viewHolder);
            } else {
                viewHolder = (T) cached.get("VH" + type.hashCode());//.newInstance(convertView);
            }
        } catch (InstantiationException e) {
            Log.e(TAG, String.valueOf("Incompatible holder"));
            Log.e(TAG, String.valueOf(e.getMessage()));
            for (StackTraceElement s : e.getStackTrace()) {
                Log.e(TAG, String.valueOf(s));
            }
        } catch (IllegalAccessException e) {
            Log.e(TAG, String.valueOf(e.getMessage()));
            for (StackTraceElement s : e.getStackTrace()) {
                Log.e(TAG, String.valueOf(s));
            }
        } catch (InvocationTargetException e) {
            Log.e(TAG, String.valueOf(e.getMessage()));
            for (StackTraceElement s : e.getStackTrace()) {
                Log.e(TAG, String.valueOf(s));
            }
        } catch (NoSuchMethodException e) {
            Log.e(TAG, String.valueOf(e.getMessage()));
            for (StackTraceElement s : e.getStackTrace()) {
                Log.e(TAG, String.valueOf(s));
            }
        } finally {
            if (null != viewHolder) {
                Log.d(TAG, "Class : " + String.valueOf(viewHolder.getClass()));
            }
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GenericHolder holder, int position) {
        Object o = adapterList.get(position);
        holder.setListener(this);
        holder.setAdapterListener(adapterListener);
        holder.onBindViewHolder(o);
        holder.onBindViewHolder(o, position);
        holder.onBindViewHolder(adapterList, position);
        updateHolderReload(holder);
    }

    private void updateHolderReload(final GenericHolder holder) {
        if (holder instanceof AdapterHolderLoader) {
            holderLoader = (AdapterHolderLoader) holder;
        }
    }

    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    public void setError(String s) {
        if (holderLoader != null) {
            if (s == null) {
                holderLoader.info.setText(null);
                holderLoader.info.setVisibility(View.GONE);
                holderLoader.progressBar.setVisibility(View.GONE);
            } else {
                holderLoader.info.setText(s);
                holderLoader.info.setVisibility(View.VISIBLE);
                holderLoader.progressBar.setVisibility(View.GONE);
            }
        }
    }

    public List<E> getAdapterList() {
        return adapterList;
    }

    /**
     * reset adapter and add new all
     */
    public void setAdapterList(List<E> adapterList) {
        this.adapterList.clear();
        this.adapterList.addAll(adapterList);
        notifyDataSetChanged();
    }


    public void updateItem(int index, E value) {
        adapterList.set(index, value);
        notifyItemChanged(index);
    }

    public void updateItemLastSelect(E value) {
        adapterList.set(lastSelected, value);
        notifyItemChanged(lastSelected);
    }

    @Override
    public void onSelectedHolder(int index) {
        lastSelected = index;
    }


    public void clearAdapterList() {
        if (this.adapterList != null) {
            this.adapterList.clear();
            notifyDataSetChanged();
        }
    }
}
