package org.cise.core.utilities.ui.adapter.recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.cise.core.utilities.commons.ExceptionUtils;
import org.cise.core.utilities.commons.ValueUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * multiple holder<br/>
 * harus ada parameter yang mapping<br/>
 * List -> dan key holder nya<br/>
 * */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AdapterGeneric<E extends AdapterModel> extends RecyclerView.Adapter<HolderGeneric<E>> implements HolderGeneric.Listener {

    private static final String TAG = "GAdapter";

    private Map<String, HolderGeneric> cached = new HashMap<>();

    private ArrayList<E> adapterList = new ArrayList<>();

    private final int VIEW_CONTENT = 1;

    private final int VIEW_LOADER = 0;

    private boolean isLoader;

    private Map<Integer, Class> holders = new HashMap<>();

    private Map<Integer, Integer> layoutHolders = new HashMap<>();

    private Map<Integer, Integer> typeHolders = new HashMap<>();

    private AdapterGeneric.AdapterListener<E> adapterListener;

    private List<HolderGeneric.Listener> holderListener = new ArrayList<>();

    protected AdapterGeneric() {
        registerHolder();
    }

    public AdapterGeneric(ArrayList adapterList) {
        this.adapterList = adapterList;
        registerHolder();
    }

    protected abstract void registerHolder();

    protected <VH extends HolderGeneric> void register(@LayoutRes int layoutId, Class<VH> holder) {
        register(1, layoutId, holder);
    }

    protected <VH extends HolderGeneric> void register(int type, @LayoutRes int layoutId, Class<VH> holder) {
        if (type <= 0) {
            throw new IllegalArgumentException("please input type greater than 0");
        }
        if (ValueUtils.nonNull(holders.get(type))) {
            throw new IllegalArgumentException("holder already register! ".concat(holder.getName()));
        }
        holders.put(type, holder);
        typeHolders.put(type, type);
        layoutHolders.put(type, layoutId);
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "item view type: ".concat(String.valueOf(position)));
        AdapterModel value = adapterList.get(position);
        if (ValueUtils.nonNull(value)) {
            return value.getType();
        }
        return VIEW_LOADER;
        // return adapterList.get(position) != null ? VIEW_CONTENT : VIEW_LOADER;
    }

    @NonNull
    @Override
    public HolderGeneric onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        Integer layoutId = layoutHolders.get(viewType);
        Class holderClass = holders.get(viewType);
        if (ValueUtils.nonNull(layoutId)) {
            View convertView = LayoutInflater.from(context).inflate(layoutId.intValue(), parent, false);
            return holderGenerator(holderClass, convertView);
        }
        throw new NullPointerException("cannot find holder view type ".concat(String.valueOf(viewType)));
    }

    private HolderGeneric holderGenerator(Type type, View convertView) {
        HolderGeneric viewHolder = null;
        try {
            if (ValueUtils.isNull(cached.get(type.hashCode()))) {
                Constructor constructor = ((Class<HolderGeneric>) type).getConstructor(View.class);
                viewHolder = (HolderGeneric) constructor.newInstance(convertView);
                cached.put("VH".concat(String.valueOf(type.hashCode())), viewHolder);
            } else {
                viewHolder = cached.get("VH".concat(String.valueOf(type.hashCode())));
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
            if (ValueUtils.nonNull(viewHolder)) {
                Log.d(TAG, "Class : ".concat(viewHolder.getClass().getSimpleName()));
            }
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderGeneric holder, int position) {
        E o = adapterList.get(position);
        int size = adapterList.size();
        // add listener
        for (HolderGeneric.Listener listener : holderListener) {
            holder.setListener(listener);
        }
        holder.setPosition(position);
        holder.onBindViewHolder(o);
        holder.onBindViewHolder(o, position);
        holder.onBindViewHolder(o, position, size);
        holder.onBindViewHolder(adapterList, position);
        holder.onBindViewHolder(adapterList, position, size);
        holder.onBindViewHolder(o, position, 0 == position && !isLoader, position == getItemCount() - 1 && !isLoader);
        Log.d(TAG, "bind position ".concat(String.valueOf(position)));
    }

    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    public void setAdapterListener(AdapterGeneric.AdapterListener<E> adapterListener) {
        this.adapterListener = adapterListener;
    }

    public void setHolderListener(HolderGeneric.Listener holderListener) {
        this.holderListener.add(holderListener);
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
