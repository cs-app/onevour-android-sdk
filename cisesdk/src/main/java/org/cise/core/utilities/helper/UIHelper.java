package org.cise.core.utilities.helper;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by user on 24/11/2017.
 */

public class UIHelper {

    private static final String TAG = "UIHelper";
    private static final int proressBarID = 1;

    public static void setValue(TextView... textViews) {
        for (TextView tv : textViews) {
            tv.setText("");
        }
    }

    public static void initRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter){
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public static void initLayoutManager(RecyclerView... recyclerViews) {
        for (RecyclerView rv : recyclerViews) {
            rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
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
