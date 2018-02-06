package com.jordan.proximateapp.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.jordan.proximateapp.R;

/**
 * Created by jordan on 06/02/2018.
 */

public class ProgressLayout {

    private static ProgressDialog progressDialog = null;

    private static void createProgressDialog(final Context context) {

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context, R.style.dialogFullScreen);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
        }
    }

    public static void show(final Context context) {
        createProgressDialog(context);

        if (progressDialog != null && !progressDialog.isShowing()) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.layout_progress, null);
            progressDialog.show();
            progressDialog.setContentView(view);
        }
    }

    public static void hide() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }
}
