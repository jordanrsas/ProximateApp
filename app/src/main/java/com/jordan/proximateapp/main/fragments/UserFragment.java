package com.jordan.proximateapp.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jordan.proximateapp.R;
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
        getDataUserSession();
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
