package com.onevour.sdk.impl.modules.fragmentbottom.controllers;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import com.onevour.core.utilities.helper.UIHelper;
import com.onevour.core.components.recycleview.RecyclerViewScrollListener;

import com.onevour.sdk.impl.SampleData;
import com.onevour.sdk.impl.databinding.FragmentBottomLoaderBinding;
import com.onevour.sdk.impl.modules.adapter.components.AdapterSampleData;
import com.onevour.sdk.impl.modules.main.models.SampleMV;

import java.util.ArrayList;
import java.util.List;

//import butterknife.BindView;
//import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BottomLoaderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BottomLoaderFragment extends BottomSheetDialogFragment implements AdapterSampleData.HolderSampleData.Listener, RecyclerViewScrollListener.PaginationListener<SampleMV> {


    public static final String TAG = "BLF";

//    @BindView(R.id.rv_sample)
//    RecyclerView rvSample;

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

    FragmentBottomLoaderBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBottomLoaderBinding.inflate(inflater, container, false);
//        View view = inflater.inflate(R.layout.fragment_bottom_loader, container, false);
//        ButterKnife.bind(this, view);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        UIHelper.initRecyclerView(rvSample, adapter,  this, this, true);
        UIHelper.initRecyclerView(binding.rvSample, adapter);
        init();
    }

    private void init() {
        // 1240009959983
        List<SampleData> sampleDatas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            sampleDatas.add(new SampleData("Sample Data ".concat(String.valueOf(i + 1))));
        }
//        adapter.setValue(sampleDatas);
        new Handler().postDelayed(() -> {

        }, 1500);
    }

    private void requestNextPage(boolean resultSuccess) {
        if (Looper.myLooper() == null) Looper.prepare();
        new Handler().postDelayed(() -> {
            List<SampleData> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                list.add(new SampleData("next : ".concat(String.valueOf(i))));
            }
            if (resultSuccess) {

                binding.rvSample.requestLayout();
            } else {

            }
        }, 3000);
    }


    @Override
    public void loadMoreItems(SampleMV sampleMV) {
        requestNextPage(false);
    }

    @Override
    public void onSelectedText(String value) {

    }
}
