package com.onevour.core.components.recycleview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.onevour.core.utilities.commons.ValueOf;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
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
public abstract class AdapterGeneric<E extends AdapterModel> extends RecyclerView.Adapter<HolderGeneric> implements HolderGeneric.Listener {

    private static final String TAG = AdapterGeneric.class.getSimpleName();

    private final Map<String, HolderGeneric> cached = new HashMap<>();

    private ArrayList<E> adapterList = new ArrayList<>();

    private final int VIEW_CONTENT = 1;

    private final int VIEW_LOADER = 0;

    private boolean isLoader;

    private final Map<Integer, Class> holders = new HashMap<>();

    private final Map<Integer, Integer> layoutHolders = new HashMap<>();

    private final Map<Integer, Class> layoutHolderBindings = new HashMap<>();

    private final Map<Integer, Integer> typeHolders = new HashMap<>();

    private final List<HolderGeneric.Listener> holderListener = new ArrayList<>();

    protected AdapterGeneric() {
        registerHolder();
    }

    public AdapterGeneric(ArrayList adapterList) {
        this.adapterList = adapterList;
        registerHolder();
    }

    protected abstract void registerHolder();

    /**
     * register if use view binding
     */
    protected <VH extends HolderGeneric> void registerBindView(Class<VH> holder) {
        registerBindView(1, holder);
    }

    protected <VH extends HolderGeneric> void registerBindView(int type, Class<VH> holder) {
        if (type <= 0) {
            throw new IllegalArgumentException("please input type greater than 0");
        }
        if (ValueOf.nonNull(holders.get(type))) {
            throw new IllegalArgumentException("holder already register! ".concat(holder.getName()));
        }
        holders.put(type, holder);
        typeHolders.put(type, type);
        //
        Type typeOfBinding = ((ParameterizedType) holder.getGenericSuperclass()).getActualTypeArguments()[0];
        Class<?> bindingClass = (Class<?>) typeOfBinding;
        layoutHolderBindings.put(type, bindingClass);
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "item view type: ".concat(String.valueOf(position)));
        AdapterModel value = adapterList.get(position);
        if (ValueOf.nonNull(value)) {
            return value.getType();
        }
        return VIEW_LOADER;
    }

    @NonNull
    @Override
    public HolderGeneric onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            Context context = parent.getContext();
            Class<?> holderClass = holders.get(viewType);
            assert holderClass != null;
            Class<?> bindingClass = layoutHolderBindings.get(viewType);
            assert bindingClass != null;
            Method method = bindingClass.getMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
            ViewBinding binding = (ViewBinding) method.invoke(null, LayoutInflater.from(context), parent, false);
            return holderGenerator(holderClass, binding);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    private HolderGeneric holderGenerator(Type type, ViewBinding convertView) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        if (null == cached.get(type.hashCode())) {
            Constructor constructor = ((Class<E>) type).getConstructor(convertView.getClass());
            HolderGeneric holderGeneric = (HolderGeneric) constructor.newInstance(convertView);
            cached.put("VH" + type.hashCode(), holderGeneric);
            return holderGeneric;
        } else {
            return cached.get("VH" + type.hashCode());
        }
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

    public void setHolderListener(HolderGeneric.Listener holderListener) {
        this.holderListener.add(holderListener);
    }

    private void setLoader(boolean loader) {
        isLoader = loader;
        int size = this.adapterList.size();
        if (isLoader) {
            if (size > 0 && ValueOf.nonNull(this.adapterList.get(size - 1))) {
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
        if (ValueOf.isNull(o)) {
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

    public void showLoader() {
        setLoader(true);
    }

    public void removeLoader() {
        setLoader(false);
    }

    public boolean isLoader() {
        return isLoader;
    }

    public interface AdapterListener<E> {

        void onLoadRetry(int index, E o);

    }
}
