package org.cise.core.utilities.helper;

import android.content.Context;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import org.cise.core.utilities.ui.adapter.layout.AutoFitGridLayoutManager;

/**
 * Created by user on 24/11/2017.
 */

public class UIHelper {

    private static final String TAG = "UIHelper";

    public static void initRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter){
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public static void initRecyclerViewGrid(RecyclerView recyclerView, RecyclerView.Adapter adapter, int width){
        recyclerView.setLayoutManager(new AutoFitGridLayoutManager(recyclerView.getContext(), width));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public static void initLayoutManager(RecyclerView... recyclerViews) {
        for (RecyclerView rv : recyclerViews) {
            rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
            rv.setItemAnimator(new DefaultItemAnimator());
        }
    }

    public static void initLayoutManagerGrid(int width, RecyclerView... recyclerViews) {
        for (RecyclerView rv : recyclerViews) {
            rv.setLayoutManager(new AutoFitGridLayoutManager(rv.getContext(), width));
            rv.setItemAnimator(new DefaultItemAnimator());
        }
    }

    public static void hideSoftInput(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
