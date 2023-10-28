package com.onevour.core.components.recycleview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListUpdateCallback;
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
import java.util.concurrent.Executors;

/***
 * multiple holder<br/>
 * harus ada parameter yang mapping<br/>
 * List -> dan key holder nya<br/>
 * */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AdapterGeneric<E extends AdapterModel> extends RecyclerView.Adapter<HolderGeneric> implements HolderGeneric.Listener {

    private static final String TAG = AdapterGeneric.class.getSimpleName();

    private final Map<String, HolderGeneric> cached = new HashMap<>();

    private boolean isLoader = false;

//    private ArrayList<E> adapterList = new ArrayList<>();

    private final int VIEW_CONTENT = 1;

    private final int VIEW_LOADER = 0;

    private final Map<Integer, Class> holders = new HashMap<>();

    private final Map<Integer, Class> layoutHolderBindings = new HashMap<>();

    private final Map<Integer, Integer> typeHolders = new HashMap<>();

    private final List<HolderGeneric.Listener> holderListener = new ArrayList<>();


    private AsyncListDiffer<E> asyncListDiffer = new AsyncListDiffer<E>(this, new DiffUtil.ItemCallback<E>() {
        @Override
        public boolean areItemsTheSame(@NonNull E oldItem, @NonNull E newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull E oldItem, @NonNull E newItem) {
            return false;
        }
    });


    protected AdapterGeneric() {
        registerHolder();
    }

    public AdapterGeneric(ArrayList adapterList) {
        registerHolder();
    }

    protected abstract void registerHolder();

    /**
     * register if use view binding
     */
    protected <VH extends HolderGeneric> void registerBindView(Class<VH> holder) {
        registerBindView(1, holder);
    }

    protected void registerAsyncListDiffer(AsyncListDiffer<E> asyncListDiffer) {
        this.asyncListDiffer = asyncListDiffer;
    }

    protected void registerAsyncListDiffer(DiffUtil.ItemCallback<E> diffCallback) {
        this.asyncListDiffer = new AsyncListDiffer<>(this, diffCallback);
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
//        AdapterModel value = adapterList.get(position);
        AdapterModel value = asyncListDiffer.getCurrentList().get(position);
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
        E o = getItem(position);
        int size = getItemCount();
        // add listener
        for (HolderGeneric.Listener listener : holderListener) {
            holder.setListener(listener);
        }
        holder.setPosition(position);
        holder.onBindViewHolder(o);
        holder.onBindViewHolder(o, position);
        holder.onBindViewHolder(o, position, size);
        holder.onBindViewHolder(asyncListDiffer.getCurrentList(), position);
        holder.onBindViewHolder(asyncListDiffer.getCurrentList(), position, size);
        holder.onBindViewHolder(o, position, 0 == position && !isLoader, position == getItemCount() - 1 && !isLoader);
        Log.d(TAG, "bind position ".concat(String.valueOf(position)));
    }

    @Override
    public int getItemCount() {
        return this.asyncListDiffer.getCurrentList().size();
    }

    public void setHolderListener(HolderGeneric.Listener holderListener) {
        this.holderListener.add(holderListener);
    }

    private void setLoader(boolean loader) {
        isLoader = loader;
        /*
        int size = this.asyncListDiffer.getCurrentList().size();
        if (isLoader) {
            if (size > 0 && ValueOf.nonNull(this.asyncListDiffer.getCurrentList().get(size - 1))) {
                this.asyncListDiffer.getCurrentList().add(null);
                //notifyItemInserted(size);
            } else if (size == 0) {
                this.asyncListDiffer.getCurrentList().add(null);
                //notifyItemInserted(size);
            }

        } else {
            if (size > 0 && this.asyncListDiffer.getCurrentList().get(size - 1) == null) {
                this.asyncListDiffer.getCurrentList().remove(size - 1);
                // notifyItemRemoved(this.adapterList.size());
            }
        }
        //this.asyncListDiffer.submitList(adapterList);
         */
    }

    public void addMore(E o) {
        if (ValueOf.isNull(o)) {
            Log.w(TAG, "cannot insert null value!");
            return;
        }
//         int size = this.adapterList.size();
//         this.adapterList.add(o);
//         notifyItemRangeInserted(size, size + 1);
        //
//        this.adapterList.add(o);
        ArrayList<E> values = new ArrayList<>(this.asyncListDiffer.getCurrentList());
        values.add(o);
        this.asyncListDiffer.submitList(values);
    }

    public void addMore(final List<E> adapterList) {
        addMore(adapterList, true);
    }

    public void addMore(final List<E> adapterList, boolean isRemoveLoader) {
        if (isRemoveLoader) removeLoader();
//        int start = this.adapterList.size();
//        this.adapterList.addAll(adapterList);
//        int newSize = adapterList.size();
//        notifyItemRangeInserted(start, newSize);
//        this.adapterList.addAll(adapterList);
        ArrayList<E> values = new ArrayList<>(this.asyncListDiffer.getCurrentList());
        values.addAll(adapterList);
        this.asyncListDiffer.submitList(values);
    }

    public List<E> getAdapterList() {
        return new ArrayList<>(this.asyncListDiffer.getCurrentList());
    }

    public void setValue(List<E> values) {
        setValue(values, true);
    }

    /**
     * reset adapter and add new all
     */
    public void setValue(List<E> values, boolean isRemoveLoader) {
        if (isRemoveLoader) removeLoader();
//        this.adapterList.clear();
//        this.adapterList.addAll(values);
//        notifyDataSetChanged();
//        this.adapterList.clear();
//        this.adapterList.addAll(values);
//        asyncListDiffer.addListListener(new AsyncListDiffer.ListListener<E>() {
//            @Override
//            public void onCurrentListChanged(@NonNull List<E> previousList, @NonNull List<E> currentList) {
//
//            }
//        });
        // asyncListDiffer.submitList(adapterList);
        // notifyDataSetChanged();
//        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        ArrayList<E> newValues = new ArrayList<>(values);
        asyncListDiffer.submitList(newValues);
    }

    public void updateItem(int index, E value) {
//        adapterList.set(index, value);
//        notifyItemChanged(index);
//        this.adapterList.set(index, value);
        //this.asyncListDiffer.submitList(adapterList);
    }

    public void clear() {
//        if (this.adapterList != null) {
//            this.adapterList.clear();
//            notifyDataSetChanged();
//            this.adapterList.clear();
            //this.asyncListDiffer.submitList(adapterList);
//        }
    }

    public E getItem(int index) {
//        int size = adapterList.size();
//        if (index < size) {
//            return adapterList.get(index);
//        }
//        return null;
        return new ArrayList<>(this.asyncListDiffer.getCurrentList()).get(index);
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

    @Deprecated
    public interface AdapterListener<E> {

        void onLoadRetry(int index, E o);

    }

}
