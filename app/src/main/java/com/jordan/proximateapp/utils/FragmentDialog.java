package com.jordan.proximateapp.utils;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jordan.proximateapp.R;
import com.jordan.proximateapp.interfaces.DialogDoubleActions;

import java.lang.reflect.Field;

/**
 * Created by jordan on 06/02/2018.
 */

public class FragmentDialog  extends DialogFragment implements ViewTreeObserver.OnGlobalLayoutListener  {
    public static final String KEY_LAYOUT_NOTIFICATION = "KEY_LAYOUT_NOTIFICATION";
    public static final String KEY_CONFIRM_TITLE = "KEY_CONFIRM_TITLE";
    public static final String KEY_MESSAGE_NOTIFICATION = "KEY_MESSAGE_NOTIFICATION";
    public static final String KEY_SHOW_BTN_CONFIRM = "KEY_SHOW_BTN_CONFIRM";
    public static final String KEY_SHOW_BTN_CANCEL = "KEY_SHOW_BTN_CANCEL";

    private int idLayoutDialog;
    private LinearLayout buttonsContainer;
    private DialogDoubleActions dialogActions;
    private boolean showConfirmButton = true;
    private boolean showCancelButton = true;
    private String titleMessage;
    private String messageNotification;
    private String titleBtnAcept;
    private String titleBtnCancel;


    public static FragmentDialog newInstance(@LayoutRes int idLayout, String titleNotification, String messageNotification,
                                                    boolean hasConfirmBtn, boolean hasCancelBtn) {
        FragmentDialog actionsDialog = new FragmentDialog();
        Bundle args = new Bundle();
        args.putInt(FragmentDialog.KEY_LAYOUT_NOTIFICATION, idLayout);
        args.putString(FragmentDialog.KEY_CONFIRM_TITLE, titleNotification);
        args.putString(FragmentDialog.KEY_MESSAGE_NOTIFICATION, messageNotification);
        args.putBoolean(FragmentDialog.KEY_SHOW_BTN_CONFIRM, hasConfirmBtn);
        args.putBoolean(FragmentDialog.KEY_SHOW_BTN_CANCEL, hasCancelBtn);
        actionsDialog.setArguments(args);

        return actionsDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(idLayoutDialog, container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        /**Creamos el {@link StyleButton} de confirmación, si se recibe título como parámetro, lo seteamos.*/
        final Button btnConfirmNotification = rootView.findViewById(R.id.btnConfirmDialog);
        final Button btnCancelNotification = rootView.findViewById(R.id.btnCancelDialog);

        /*Seteamos visibilidad de botones*/
        if (btnConfirmNotification != null) {
            btnConfirmNotification.setVisibility(showConfirmButton ? View.VISIBLE : View.GONE);
            btnConfirmNotification.setText(!TextUtils.isEmpty(titleBtnAcept) ? titleBtnAcept : getString(R.string.title_aceptar));
        }
        if (btnCancelNotification != null) {
            btnCancelNotification.setVisibility(showCancelButton ? View.VISIBLE : View.GONE);
            btnCancelNotification.setText(!TextUtils.isEmpty(titleBtnCancel) ? titleBtnCancel : getString(R.string.title_cancelar));
        }

        /**Creamos el {@link StyleTextView} para el mensaje si existe mensaje dinámico.*/
        if (!TextUtils.isEmpty(titleMessage)) {
            TextView txtTitleNotification = rootView.findViewById(R.id.txtTitleNotification);
            txtTitleNotification.setText(titleMessage);
        }

        /**Creamos el {@link StyleTextView} para el mensaje si existe mensaje dinámico.*/
        if (!TextUtils.isEmpty(messageNotification)) {
            TextView txtMessageNotification = rootView.findViewById(R.id.txtMessageNotification);
            txtMessageNotification.setText(messageNotification);
        }

        if (showConfirmButton && dialogActions != null) {
            btnConfirmNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogActions.actionConfirm();
                    dismiss();
                }
            });
        }

        if (showConfirmButton && dialogActions != null) {
            btnCancelNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogActions.actionCancel();
                    dismiss();
                }
            });
        }

        buttonsContainer = rootView.findViewById(R.id.buttons_container);
        if (buttonsContainer != null) {
            buttonsContainer.getViewTreeObserver().addOnGlobalLayoutListener(this);
        }

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle arg = getArguments();
            idLayoutDialog = arg.getInt(FragmentDialog.KEY_LAYOUT_NOTIFICATION);
            titleMessage = arg.getString(FragmentDialog.KEY_CONFIRM_TITLE, "");
            messageNotification = arg.getString(FragmentDialog.KEY_MESSAGE_NOTIFICATION, "");
            showConfirmButton = arg.getBoolean(FragmentDialog.KEY_SHOW_BTN_CONFIRM, true);
            showCancelButton = arg.getBoolean(FragmentDialog.KEY_SHOW_BTN_CANCEL, true);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_inset);
        return dialog;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            Field dismissed = getClass().getSuperclass().getDeclaredField("mDismissed");
            Field shown = getClass().getSuperclass().getDeclaredField("mShownByMe");
            shown.setAccessible(true);
            dismissed.setAccessible(true);

            dismissed.set(this, false);
            shown.set(this, true);

            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setDialogActions(DialogDoubleActions dialogActions) {
        this.dialogActions = dialogActions;
    }

    public void setTitleBtnAcept(String titleBtnAcept) {
        this.titleBtnAcept = titleBtnAcept;
    }

    public void setTitleBtnCancel(String titleBtnCancel) {
        this.titleBtnCancel = titleBtnCancel;
    }

    @Override
    public void onGlobalLayout() {
        buttonsContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }
}
