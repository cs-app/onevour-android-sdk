package org.cise.sdk.ciseapp.modules.fragmentbottom.controllers;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.cise.core.utilities.helper.UIHelper;
import org.cise.core.utilities.ui.adapter.recyclerview.GenericAdapter;
import org.cise.core.utilities.ui.adapter.recyclerview.RecyclerViewScrollListener;
import org.cise.sdk.ciseapp.R;
import org.cise.sdk.ciseapp.models.SampleData;
import org.cise.sdk.ciseapp.modules.adapter.components.AdapterSampleData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BottomLoaderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BottomLoaderFragment extends BottomSheetDialogFragment implements
        GenericAdapter.AdapterListener<SampleData>, RecyclerViewScrollListener.PaginationListener<SampleData> {


    public static final String TAG = "BLF";

    @BindView(R.id.rv_sample)
    RecyclerView rvSample;

    AdapterSampleData adapter = new AdapterSampleData();

    public BottomLoaderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BottomLoaderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BottomLoaderFragment newInstance() {
        BottomLoaderFragment fragment = new BottomLoaderFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_loader, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UIHelper.initRecyclerView(rvSample, adapter, this, this, true);
        init();
    }

    private void init() {
        // 1240009959983
        List<SampleData> sampleDatas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            sampleDatas.add(new SampleData("Sample Data " + (i + 1)));
        }
//        adapter.setValue(sampleDatas);
        new Handler().postDelayed(() -> {
            adapter.setError("Error, tap for reload");
        }, 1500);
    }

    private void requestNextPage(boolean resultSuccess) {
        if (Looper.myLooper() == null) Looper.prepare();
        new Handler().postDelayed(() -> {
            List<SampleData> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                list.add(new SampleData("next : " + i));
            }
            if (resultSuccess) {
                adapter.addMore(list);
                rvSample.requestLayout();
            } else {
                adapter.setError("Error, tap for reload");
            }
        }, 3000);
    }

    @Override
    public void onLoadRetry(int index, SampleData o) {
        requestNextPage(true);
    }

    @Override
    public void loadMoreItems(SampleData sampleData) {
        requestNextPage(false);
    }


}
