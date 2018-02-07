package com.jordan.proximateapp.main.controllers;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;

import com.jordan.proximateapp.main.interfaces.OnEventListener;

/**
 * Created by jordan on 07/02/2018.
 */

public abstract class GenericFragment extends Fragment {

    protected View rootview;
    protected OnEventListener onEventListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnEventListener) {
            this.onEventListener = (OnEventListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @NonNull
    public String getFragmentTag() {
        return getClass().getSimpleName();
    }

    public abstract void initViews();
}
