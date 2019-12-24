package org.cise.core.utilities.helper;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentHelper {

    /**
     * start activity
     */
    public static void startActivity(Context context, Intent intent) {
        if (context != null) context.startActivity(intent);
    }

    /**
     * replace fragment and add to backstack
     */
    public static void replace(FragmentManager fm, int viewId, Fragment fragment) {
        replaceAndBackstack(fm, viewId, fragment, null, true);
    }

    /**
     * replace fragment and add to backstack
     */
    public static void replaceAndBackstack(FragmentManager fm, int viewId, Fragment fragment, String tag, boolean isBack) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(viewId, fragment);
        if (isBack) ft.addToBackStack(tag);
        ft.commit();
    }

    /**
     * replace fragment and add to backstack
     */
    public static void replaceAndBackstack(Context context, int viewId, Fragment fragment, String tag) {
        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            FragmentManager fm = activity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(viewId, fragment);
            ft.addToBackStack(tag);
            ft.commit();
        }
    }

    /**
     * show fragment
     */
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

    /**
     * replace fragment, no back stack
     */
    public static void replaceFragment(Context context, int containerLayout, Fragment fragment, String tag) {
        replaceFragment(context, containerLayout, fragment, tag, false);
    }

    /**
     * replace fragment, back stack optional
     */
    public static void replaceFragment(Context context, int containerLayout, Fragment fragment, String tag, boolean isback) {
        if (context instanceof AppCompatActivity) {
            AppCompatActivity activity = (AppCompatActivity) context;
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            Fragment existFragment = fragmentManager.findFragmentByTag(tag);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (null != existFragment) {
                transaction.remove(existFragment);
            }
            transaction.replace(containerLayout, fragment, tag);
            if (isback) {
                transaction.addToBackStack(null);
            }
            transaction.commit();
            if (!isback) {
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
    }
}
