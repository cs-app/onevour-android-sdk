package org.cise.core.utilities.fragment;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.cise.core.utilities.commons.ValueUtils;

public class FragmentNavigation {


    /**
     * replace fragment and add to Back Stack

     public static void add(FragmentManager fm, int viewId, Fragment fragment, String tag, boolean isBack) {
     FragmentTransaction ft = fm.beginTransaction();
     ft.replace(viewId, fragment);
     if (isBack) ft.addToBackStack(tag);
     ft.commit();
     }
     * replace fragment and add to Back Stack

     public static void add(Context context, int viewId, Fragment fragment, String tag) {
     if (context instanceof AppCompatActivity) {
     AppCompatActivity activity = (AppCompatActivity) context;
     FragmentManager fm = activity.getSupportFragmentManager();
     FragmentTransaction ft = fm.beginTransaction();
     ft.replace(viewId, fragment);
     ft.addToBackStack(tag);
     ft.commit();
     }
     }
     * show fragment

     public static void showDialogFragment(Context context, AppCompatDialogFragment fragment, String tag) {
     if (context instanceof AppCompatActivity) {
     AppCompatActivity activity = (AppCompatActivity) context;
     FragmentManager fm = activity.getSupportFragmentManager();
     Fragment fg = fm.findFragmentByTag(tag);
     if (fg != null) fm.beginTransaction().remove(fg).commit();
     if (!(fragment.getDialog() != null && fragment.getDialog().isShowing())) {
     fm.beginTransaction().add(fragment, tag).commitAllowingStateLoss();
     }
     }
     }
     */

    /**
     * replace fragment, no back stack
     */
    public static void add(Context context, int containerLayout, Fragment fragment, String tag) {
        add(context, containerLayout, fragment, tag, false);
    }

    /**
     * replace fragment, no back stack
     */
    public static void addBackStack(Context context, int containerLayout, Fragment fragment, String tag) {
        add(context, containerLayout, fragment, tag, true);
    }

    /**
     * replace fragment, back stack optional
     */
    public static void add(Context context, int containerLayout, Fragment fragment, String tag, boolean isback) {
        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            Fragment existFragment = fragmentManager.findFragmentByTag(tag);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (ValueUtils.nonNull(existFragment)) transaction.remove(existFragment);
            transaction.replace(containerLayout, fragment, tag);
            if (isback) {
                transaction.addToBackStack(null);
            } else {
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            transaction.commit();
        }
    }

    public static boolean onBackPressed(Context context, String tag) {
        if (!(context instanceof AppCompatActivity)) {
            return true;
        }
        AppCompatActivity activity = (AppCompatActivity) context;
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment existFragment = fragmentManager.findFragmentByTag(tag);
        if (ValueUtils.isNull(existFragment)) {
            return true;
        }
        return !existFragment.isVisible();

    }

}
