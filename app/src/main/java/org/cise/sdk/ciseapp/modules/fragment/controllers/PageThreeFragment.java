package org.cise.sdk.ciseapp.modules.fragment.controllers;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.cise.core.utilities.commons.ValueUtils;
import org.cise.sdk.ciseapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PageThreeFragment extends Fragment {

    private static PageThreeFragment fragment;

    public PageThreeFragment() {
        // Required empty public constructor
    }

    public static PageThreeFragment newInstance() {
        if (ValueUtils.isNull(fragment)) {
            fragment = new PageThreeFragment();
        }
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_page_three, container, false);
    }

}
