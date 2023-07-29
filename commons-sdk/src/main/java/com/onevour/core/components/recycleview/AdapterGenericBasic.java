package com.onevour.core.components.recycleview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by user on 20/11/2017.
 * Arguments T is entities<br>
 * Arguments E is ViewHolder<br>
 */

@SuppressWarnings("rawtypes")
public class AdapterGenericBasic<E extends HolderGenericBasic> extends RecyclerView.Adapter<E> {

    private final String TAG = getClass().getSimpleName();
    private Map<String, Constructor> cached = new HashMap<>();

    private final List<HolderGenericBasic.Listener> holderListener = new ArrayList<>();

    //private ViewBinding binding;

    private Context context;

    protected List values = new ArrayList();

    public AdapterGenericBasic() {

    }

    public Context getContext() {
        return context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    public void setValues(List values) {
        if (Objects.nonNull(this.values) || !this.values.isEmpty()){
            this.values.clear();
        }
        this.values.addAll(values);
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public E onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            Context context = parent.getContext();
            Type typeOfHolder = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            Class<?> holderClass = (Class<?>) typeOfHolder;
            Type typeOfBinding = ((ParameterizedType) holderClass.getGenericSuperclass()).getActualTypeArguments()[0];
            Class<?> bindingClass = (Class<?>) typeOfBinding;
            Method method = bindingClass.getMethod("inflate", LayoutInflater.class, ViewGroup.class, boolean.class);
            ViewBinding binding = (ViewBinding) method.invoke(null, LayoutInflater.from(context), parent, false);
            return holderGenerator(typeOfHolder, binding);
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

    public void setHolderListener(HolderGenericBasic.Listener holderListener) {
        this.holderListener.add(holderListener);
    }

    @SuppressWarnings({"unchecked", "SuspiciousMethodCalls"})
    private E holderGenerator(Type type, ViewBinding convertView) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        if (null == cached.get(type.hashCode())) {
            Constructor constructor = ((Class<E>) type).getConstructor(convertView.getClass());
            cached.put("VH" + type.hashCode(), constructor);
            return (E) constructor.newInstance(convertView);
        } else {
            return (E) cached.get("VH" + type.hashCode()).newInstance(convertView);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(@NonNull HolderGenericBasic holder, int position) {
        // add listener
        for (HolderGenericBasic.Listener listener : holderListener) {
            holder.setListeners(listener);
        }
        holder.onBindViewHolder(values.get(position));
        holder.onBindViewHolder(values, position);
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

}
