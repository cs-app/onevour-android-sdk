package org.cise.core.utilities.ui.adapter.recyclerview;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.cise.core.R;
import org.cise.core.utilities.commons.ExceptionUtils;
import org.cise.core.utilities.commons.ValueUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zuliadin on 20/11/2017.
 * Arguments T is ViewHolder<br>
 */
@SuppressWarnings("unchecked")
public class GenericAdapter<T extends GenericHolder, E> extends RecyclerView.Adapter<T> implements GenericHolder.Listener<T> {

    private static final String TAG = "GAdapter";

    private Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    private Map<String, T> cached = new HashMap<>();

    private ArrayList<E> adapterList = new ArrayList<>();

    private final int VIEW_CONTENT = 0;

    private final int VIEW_LOADER = 1;

    private AdapterHolderLoader holderLoader;

    private int lastSelected = -1;

    private boolean isLoader;

    private int layoutId;

    private AdapterListener<E> adapterListener;

    private GenericHolder.Listener<E> holderListener;

    protected GenericAdapter(int layoutId) {
        this.layoutId = layoutId;
    }


    public GenericAdapter(int layoutId, ArrayList adapterList) {
        this.layoutId = layoutId;
        this.adapterList = adapterList;
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "item view type: " + position);
        return adapterList.get(position) != null ? VIEW_CONTENT : VIEW_LOADER;
    }

    @NonNull
    @Override
    public T onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
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
                viewHolder = (T) cached.get("VH" + type.hashCode());
            }
        } catch (InstantiationException e) {
            Log.e(TAG, "Incompatible holder");
            Log.e(TAG, ExceptionUtils.message(e));
        } catch (IllegalAccessException e) {
            Log.e(TAG, ExceptionUtils.message(e));
        } catch (InvocationTargetException e) {
            Log.e(TAG, ExceptionUtils.message(e));
        } catch (NoSuchMethodException e) {
            Log.e(TAG, ExceptionUtils.message(e));
        } finally {
            if (null != viewHolder) {
                Log.d(TAG, "Class : " + viewHolder.getClass());
            }
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GenericHolder holder, int position) {
        E o = adapterList.get(position);
        if (holderListener != null) {
            holder.setListener(holderListener);
        } else {
            holder.setListener(this);
        }
        holder.onBindViewHolder(o);
        holder.onBindViewHolder(o, position);
        holder.onBindViewHolder(adapterList, position);
        holder.onBindViewHolder(o, position, 0 == position && !isLoader, position == getItemCount() - 1 && !isLoader);
        holderReload(holder);
        Log.d(TAG, "bind position " + position);
    }

    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    public void setAdapterListener(AdapterListener<E> adapterListener) {
        this.adapterListener = adapterListener;
    }

    public void setHolderListener(GenericHolder.Listener<E> holderListener) {
        this.holderListener = holderListener;
    }

    private void setLoader(boolean loader) {
        isLoader = loader;
        int size = this.adapterList.size();
        if (isLoader) {
            if (size > 0 && ValueUtils.nonNull(this.adapterList.get(size - 1))) {
                this.adapterList.add(null);
                notifyItemInserted(size);
            } else if (size == 0) {
                this.adapterList.add(null);
                notifyItemInserted(size);
            }
        } else {
            if (size > 0 && this.adapterList.get(size - 1) == null) {
                this.adapterList.remove(size - 1);
                notifyItemRemoved(this.adapterList.size());
            }
        }
    }

    public void addMore(E o) {
        if (ValueUtils.isNull(o)) {
            Log.w(TAG, "cannot insert null value!");
            return;
        }
        int size = this.adapterList.size();
        this.adapterList.add(o);
        notifyItemRangeInserted(size, size + 1);
    }

    public void addMore(final List<E> adapterList) {
        addMore(adapterList, true);
    }

    public void addMore(final List<E> adapterList, boolean isRemoveLoader) {
        if (isRemoveLoader) removeLoader();
        int start = this.adapterList.size();
        this.adapterList.addAll(adapterList);
        int newSize = adapterList.size();
        notifyItemRangeInserted(start, newSize);
    }

    private void holderReload(final GenericHolder holder) {
        if (holder instanceof AdapterHolderLoader) {
            holderLoader = (AdapterHolderLoader) holder;
        }
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
            holderLoader.setListener(() -> {
                if (adapterListener != null) {
                    int size = adapterList.size();
                    if (size > 0) {
                        int index = size - 1;
                        E o = adapterList.get(index);
                        adapterListener.onLoadRetry(index, o);
                    }
                }
            });
        }
    }

    public List<E> getAdapterList() {
        return adapterList;
    }

    public void setValue(List<E> values) {
        setValue(values, true);
    }

    /**
     * reset adapter and add new all
     */
    public void setValue(List<E> values, boolean isRemoveLoader) {
        if (isRemoveLoader) removeLoader();
        this.adapterList.clear();
        this.adapterList.addAll(values);
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

    public void clear() {
        if (this.adapterList != null) {
            this.adapterList.clear();
            notifyDataSetChanged();
        }
    }

    public E getItem(int totalItemCount) {
        int size = adapterList.size();
        if (totalItemCount < size) {
            return adapterList.get(totalItemCount);
        }
        return null;
    }

    @Override
    public void onSelectedHolder(int index, T o) {
        lastSelected = index;
    }

    public interface AdapterListener<E> {

        void onLoadRetry(int index, E o);

    }

    public void showLoader() {
        setLoader(true);
    }

    public void removeLoader() {
        setLoader(false);
    }

    public boolean isLoader() {
        return isLoader;
    }
}
