package com.onevour.core.utilities.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.onevour.core.utilities.ui.adapter.layout.AutoFitGridLayoutManager;
import com.onevour.core.components.recycleview.AdapterGeneric;
import com.onevour.core.components.recycleview.RecyclerViewScrollListener;

/**
 * Created by Zuliadin on 24/11/2017.
 */

public class UIHelper {

    private static final String TAG = "UIHelper";

    private static final int sizePage = 20;

    public static void initRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        initRecyclerView(recyclerView, adapter, null, null, sizePage, false);
    }

    public static void initRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter, AdapterGeneric.AdapterListener adapterListener) {
        initRecyclerView(recyclerView, adapter, adapterListener, null, sizePage, false);
    }

    public static void initRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter, AdapterGeneric.AdapterListener adapterListener, RecyclerViewScrollListener.PaginationListener scrollListener, boolean isLoadFirst) {
        initRecyclerView(recyclerView, adapter, adapterListener, scrollListener, sizePage, isLoadFirst);
    }

    public static void initRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter, AdapterGeneric.AdapterListener adapterListener, RecyclerViewScrollListener.PaginationListener scrollListener, int sizePage, boolean isLoadFirst) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        if (adapter instanceof AdapterGeneric) {
            AdapterGeneric genericAdapter = ((AdapterGeneric) adapter);
            if (isLoadFirst) genericAdapter.showLoader();
            if (null != adapterListener) {
                //genericAdapter.setAdapterListener(adapterListener);
            }
            if (null != scrollListener) {
                recyclerView.addOnScrollListener(new RecyclerViewScrollListener(genericAdapter, layoutManager, sizePage, scrollListener));
            }

        }
    }

    public static void initHorizontalRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    public static void initRecyclerViewGrid(RecyclerView recyclerView, RecyclerView.Adapter adapter, int width) {
        recyclerView.setLayoutManager(new AutoFitGridLayoutManager(recyclerView.getContext(), width));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    /**
     * auto fit grid
     */
    public static void initRecyclerViewGridInLine(RecyclerView recyclerView, RecyclerView.Adapter adapter, int count) {
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(), count));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

    public static void initLayoutManager(RecyclerView... recyclerViews) {
        for (RecyclerView rv : recyclerViews) {
            rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
            rv.setItemAnimator(new DefaultItemAnimator());
            rv.setHasFixedSize(true);
        }
    }

    /**
     * auto fit grid
     */
    public static void initLayoutManagerGrid(int width, RecyclerView... recyclerViews) {
        for (RecyclerView rv : recyclerViews) {
            rv.setLayoutManager(new AutoFitGridLayoutManager(rv.getContext(), width));
            rv.setItemAnimator(new DefaultItemAnimator());
            rv.setHasFixedSize(true);
        }
    }

    public static void hideSoftInput(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    public static void setMarginsInDP(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            DisplayMetrics metrics = view.getContext().getResources().getDisplayMetrics();
            int pxL = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, left, metrics));
            int pxR = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, right, metrics));
            int pxT = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, top, metrics));
            int pxB = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, bottom, metrics));
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(pxL, pxT, pxR, pxB);
            view.requestLayout();
        }
    }

    public static int widthScreen(Context context) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public static int heightScreen(Context context) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public static void snapScroll(RecyclerView recyclerView) {
        LinearSnapHelper snapHelper = new LinearSnapHelper() {

            @Override
            public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
                View centerView = findSnapView(layoutManager);
                if (centerView == null)
                    return RecyclerView.NO_POSITION;

                int position = layoutManager.getPosition(centerView);
                int targetPosition = -1;
                if (layoutManager.canScrollHorizontally()) {
                    if (velocityX < 0) {
                        targetPosition = position - 1;
                    } else {
                        targetPosition = position + 1;
                    }
                }

                if (layoutManager.canScrollVertically()) {
                    if (velocityY < 0) {
                        targetPosition = position - 1;
                    } else {
                        targetPosition = position + 1;
                    }
                }

                final int firstItem = 0;
                final int lastItem = layoutManager.getItemCount() - 1;
                targetPosition = Math.min(lastItem, Math.max(targetPosition, firstItem));
                return targetPosition;
            }
        };
        snapHelper.attachToRecyclerView(recyclerView);
    }
}
