package org.cise.core.utilities.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import org.cise.core.R;
import org.cise.core.utilities.ui.adapter.layout.AutoFitGridLayoutManager;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by user on 24/11/2017.
 */

public class UIHelper {

    private static final String TAG = "UIHelper";

    public static void initRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    public static void initHorizontalRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    public static void initRecyclerViewGrid(RecyclerView recyclerView, RecyclerView.Adapter adapter, int width) {
        recyclerView.setLayoutManager(new AutoFitGridLayoutManager(recyclerView.getContext(), width));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
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

    public static void loadImageWithGlide(Context context, ImageView imageView, String url, int imageDrawable) {
        if (context != null && imageView != null && url != null && url.startsWith("http")) {
            Log.d(TAG, url);
            if (url.contains(" ")) {
                StringBuilder sb = new StringBuilder();
                String[] segmentUrl = url.split("/");
                for(String s:segmentUrl){
                    if (s.contains(" ")) {
                        sb.append(Uri.encode(s));
                    } else {
                        sb.append(s);
                    }
                    sb.append("/");
                }
                url = sb.toString().substring(0, sb.toString().toCharArray().length-1);
            }
            Log.d(TAG, url);
            final Uri imageUri = Uri.parse(url);
            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
            circularProgressDrawable.setStrokeWidth(5f);
            circularProgressDrawable.setCenterRadius(30f);
            circularProgressDrawable.start();
            Glide.with(context)
                    .load(imageUri)
                    .listener(new RequestListener() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                            Log.e(TAG, String.valueOf(e.getMessage()));
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .apply(RequestOptions.fitCenterTransform()
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .fitCenter()
                            .error(imageDrawable)
                            .placeholder(circularProgressDrawable))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView);

        }
    }

    public static void loadCircularImageWithGlide(Context context, ImageView imageView, String url, int imageDrawable) {
        if (context != null && imageView != null) {
            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
            circularProgressDrawable.setStrokeWidth(5f);
            circularProgressDrawable.setCenterRadius(30f);
            circularProgressDrawable.start();
            Glide.with(context)
                    .load(url)
                    .apply(RequestOptions.circleCropTransform()
                            .circleCrop()
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .error(imageDrawable)
                            .placeholder(circularProgressDrawable))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView);
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
