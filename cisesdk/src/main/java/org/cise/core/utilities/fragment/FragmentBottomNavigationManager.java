package org.cise.core.utilities.fragment;
//
// Created by Zuliadin on 2019-12-30.
//

import android.content.Context;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.cise.core.utilities.commons.ValueUtils;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

public class FragmentBottomNavigationManager {

    private Fragment activeFragment;

    private SparseArray<Fragment.SavedState> stateSparse = new SparseArray<>();

    private String SAVED_STATE_CONTAINER_KEY = "STATE_CONTAINER";

    private String SAVED_STATE_CURRENT_TAB_KEY = "STATE_CONTAINER_CURRENT";

    // fragment attribute

    private Context context;

    private int containerLayout;

    private int selectedIndexFragment;

    private FragmentManager fm;

    // fragment mapping

    private Map<Integer, FragmentWrapper> map = new HashMap<>();

    public void setup(Context context, int containerLayout, FragmentManager fragmentManager, BottomNavigationView navigation, BottomNavigationView.OnNavigationItemSelectedListener listener) {
        this.context = context;
        this.containerLayout = containerLayout;
        this.fm = fragmentManager;
        navigation.setOnNavigationItemSelectedListener(listener);
    }

    public void register(int menuId, Fragment fragment, String tag) {
        if (ValueUtils.isZero(menuId)) {
            throw new InvalidParameterException("menu id must > 0");
        }
        if (ValueUtils.isNull(fragment)) {
            throw new NullPointerException("fragment cannot set null");
        }
        if (ValueUtils.isEmpty(tag)) {
            throw new NullPointerException("tag cannot set null");
        }
        this.map.put(menuId, new FragmentWrapper(fragment, tag));
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        if (ValueUtils.nonNull(activeFragment)) {
            stateSparse.put(selectedIndexFragment, fm.saveFragmentInstanceState(activeFragment));
        }
        return active(item.getItemId());
    }

    public boolean active(int fragmentId) {
        selectedIndexFragment = fragmentId;
        FragmentWrapper fragmentWrapper = map.get(selectedIndexFragment);
        if (ValueUtils.isNull(fragmentWrapper)) {
            return false;
        }
        if (ValueUtils.nonNull(activeFragment) && activeFragment.equals(fragmentWrapper.getFragment())) {
            return true;
        }
        return initFragment(selectedIndexFragment, fragmentWrapper);
    }

    private boolean initFragment(int fragmentId, FragmentWrapper fragmentWrapper) {
        if (ValueUtils.isNull(context, fragmentWrapper, fm) && containerLayout == 0) {
            return false;
        }
        Fragment fragment = fragmentWrapper.getFragment();
        String tag = fragmentWrapper.getKey();
        if (ValueUtils.nonNull(stateSparse)) {
            Fragment.SavedState state = stateSparse.get(fragmentId);
            if (ValueUtils.nonNull(state)) {
                if (ValueUtils.isNull(fragment.getFragmentManager())) {
                    fragment.setInitialSavedState(state);
                }
            }
        }
        FragmentNavigation.add(context, containerLayout, fragment, tag);
        activeFragment = fragment;
        return true;
    }

    // state handler
    public void onCreateStateHandler(Bundle savedInstanceState) {
        if (ValueUtils.nonNull(savedInstanceState)) {
            stateSparse = savedInstanceState.getSparseParcelableArray(SAVED_STATE_CONTAINER_KEY);
            selectedIndexFragment = savedInstanceState.getInt(SAVED_STATE_CURRENT_TAB_KEY);
        }
    }

    // state handler
    public void onSaveInstanceState(Bundle outState) {
        if (ValueUtils.nonNull(outState)) {
            outState.putSparseParcelableArray(SAVED_STATE_CONTAINER_KEY, stateSparse);
            outState.putInt(SAVED_STATE_CONTAINER_KEY, selectedIndexFragment);
        }
    }

    class FragmentWrapper {

        private Fragment fragment;

        private String key;

        public FragmentWrapper(Fragment fragment, String key) {
            this.fragment = fragment;
            this.key = key;
        }

        public Fragment getFragment() {
            return fragment;
        }

        public String getKey() {
            return key;
        }
    }

}
