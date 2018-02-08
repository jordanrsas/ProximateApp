package com.jordan.proximateapp.main.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jordan.proximateapp.R;
import com.jordan.proximateapp.db.UserReaderDbHelper;
import com.jordan.proximateapp.login.LoginActivity;
import com.jordan.proximateapp.main.controllers.GenericFragment;
import com.jordan.proximateapp.net.APIClient;
import com.jordan.proximateapp.net.IApiClient;
import com.jordan.proximateapp.net.data.DataUser;
import com.jordan.proximateapp.net.data.ResponseGetDataUser;
import com.jordan.proximateapp.utils.ProgressLayout;
import com.jordan.proximateapp.utils.SharedPrefsManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jordan.proximateapp.db.DBContract.UserEntry.COLUMN_NAME_DOCUMENT_ABREV;
import static com.jordan.proximateapp.db.DBContract.UserEntry.COLUMN_NAME_DOCUMENT_LABEL;
import static com.jordan.proximateapp.db.DBContract.UserEntry.COLUMN_NAME_LAST_NAME;
import static com.jordan.proximateapp.db.DBContract.UserEntry.COLUMN_NAME_LAST_SESION;
import static com.jordan.proximateapp.db.DBContract.UserEntry.COLUMN_NAME_MAIL;
import static com.jordan.proximateapp.db.DBContract.UserEntry.COLUMN_NAME_NAME;
import static com.jordan.proximateapp.db.DBContract.UserEntry.COLUMN_NAME_NUMBER_DOC;
import static com.jordan.proximateapp.db.DBContract.UserEntry.COLUMN_NAME_USER_LABEL;
import static com.jordan.proximateapp.utils.SharedPreferencesKeys.DATA_USER_SAVED;
import static com.jordan.proximateapp.utils.SharedPreferencesKeys.LAST_LOGIN;
import static com.jordan.proximateapp.utils.SharedPreferencesKeys.TOKEN;

/**
 * Created by jordan on 07/02/2018.
 */

public class UserFragment extends GenericFragment {


    @BindView(R.id.nombres)
    TextView nombres;
    @BindView(R.id.apellidos)
    TextView apellidos;
    @BindView(R.id.correo)
    TextView correo;
    @BindView(R.id.numero_documento)
    TextView numero_documento;
    @BindView(R.id.ultima_sesion)
    TextView ultima_sesion;
    @BindView(R.id.documentos_abrev)
    TextView documentos_abrev;
    @BindView(R.id.documentos_label)
    TextView documentos_label;
    @BindView(R.id.estados_usuarios_label)
    TextView estados_usuarios_label;

    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.user_fragment, container, false);
        initViews();
        return rootview;
    }

    @Override
    public void initViews() {
        ButterKnife.bind(this, rootview);
        ProgressLayout.show(getContext());
        String lastLogin = SharedPrefsManager.getInstance().getString(LAST_LOGIN);
        Long llogin = Long.parseLong(lastLogin);
        Long acutalTime = System.currentTimeMillis() / 1000;
        if ((acutalTime - llogin) > 86400) {
            SharedPrefsManager.getInstance().clearPrefs();
            goToLogin();
        } else {
            if (SharedPrefsManager.getInstance().getBoolean(DATA_USER_SAVED)) {
                ProgressLayout.hide();
                getDataUser();
            } else {
                getDataUserSession();
            }
        }
    }

    private void getDataUserSession() {
        APIClient.getClient().create(IApiClient.class).getDataUser(SharedPrefsManager.getInstance().getString(TOKEN)).enqueue(new Callback<ResponseGetDataUser>() {
            @Override
            public void onResponse(Call<ResponseGetDataUser> call, Response<ResponseGetDataUser> response) {
                //Log.e("MyError", response.body().getMessage());
                ResponseGetDataUser responseGetDataUser = response.body();
                if (responseGetDataUser != null && responseGetDataUser.getSuccess()) {
                    ProgressLayout.hide();
                    //save data base
                    SharedPrefsManager.getInstance().setBoolean(DATA_USER_SAVED, true);
                    saveDB(responseGetDataUser.getData().get(0));
                    printDataUSer(responseGetDataUser.getData().get(0));

                } else {
                    SharedPrefsManager.getInstance().clearPrefs();
                    goToLogin();
                }
            }

            @Override
            public void onFailure(Call<ResponseGetDataUser> call, Throwable t) {
                Log.e("MyError", t.getMessage());
            }
        });
    }

    private void saveDB(DataUser data) {
        UserReaderDbHelper dbHelper = new UserReaderDbHelper(getContext());
        dbHelper.insertUser(data);
    }

    private void getDataUser() {
        UserReaderDbHelper dbHelper = new UserReaderDbHelper(getContext());
        Cursor userDB = dbHelper.getData();
        userDB.moveToLast();
        DataUser data = new DataUser();
        data.setNombres(userDB.getString(userDB.getColumnIndex(COLUMN_NAME_NAME)));
        data.setApellidos(userDB.getString(userDB.getColumnIndex(COLUMN_NAME_LAST_NAME)));
        data.setCorreo(userDB.getString(userDB.getColumnIndex(COLUMN_NAME_MAIL)));
        data.setNumero_documento(userDB.getString(userDB.getColumnIndex(COLUMN_NAME_NUMBER_DOC)));
        data.setUltima_sesion(userDB.getString(userDB.getColumnIndex(COLUMN_NAME_LAST_SESION)));
        data.setDocumentos_abrev(userDB.getString(userDB.getColumnIndex(COLUMN_NAME_DOCUMENT_ABREV)));
        data.setDocumentos_label(userDB.getString(userDB.getColumnIndex(COLUMN_NAME_DOCUMENT_LABEL)));
        data.setEstados_usuarios_label(userDB.getString(userDB.getColumnIndex(COLUMN_NAME_USER_LABEL)));

        if (!userDB.isClosed()) {
            userDB.close();
        }
        printDataUSer(data);
    }

    private void printDataUSer(DataUser data) {
        nombres.setText(data.getNombres());
        apellidos.setText(data.getApellidos());
        correo.setText(data.getCorreo());
        numero_documento.setText(data.getNumero_documento());
        ultima_sesion.setText(data.getUltima_sesion());
        documentos_abrev.setText(data.getDocumentos_abrev());
        documentos_label.setText(data.getDocumentos_label());
        estados_usuarios_label.setText(data.getEstados_usuarios_label());
    }

    private void goToLogin() {
        ProgressLayout.hide();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
