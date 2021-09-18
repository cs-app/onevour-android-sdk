package org.cise.sdk.ciseapp.modules.fragment.controllers;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.cise.core.utilities.commons.ValueOf;
import org.cise.core.utilities.fragment.FragmentNavigation;
import org.cise.sdk.ciseapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class PageTwoFragment extends Fragment {

    private static PageTwoFragment fragment;

    public PageTwoFragment() {
        // Required empty public constructor
    }

    public static PageTwoFragment newInstance() {
        if (ValueOf.isNull(fragment)) {
            fragment = new PageTwoFragment();
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_page_two, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_next)
    public void nextPage() {
        FragmentNavigation.addBackStack(getActivity(), R.id.container, PageThreeFragment.newInstance(), "C");
    }

}
