package com.jordan.proximateapp.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jordan.proximateapp.R;
import com.jordan.proximateapp.main.controllers.MainActivity;
import com.jordan.proximateapp.net.APIClient;
import com.jordan.proximateapp.net.IApiClient;
import com.jordan.proximateapp.net.data.RequestLogin;
import com.jordan.proximateapp.net.data.ResponseLogin;
import com.jordan.proximateapp.utils.ProgressLayout;
import com.jordan.proximateapp.utils.SharedPrefsManager;
import com.jordan.proximateapp.utils.UI;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jordan.proximateapp.utils.SharedPreferencesKeys.ID;
import static com.jordan.proximateapp.utils.SharedPreferencesKeys.IS_LOGGED;
import static com.jordan.proximateapp.utils.SharedPreferencesKeys.LAST_LOGIN;
import static com.jordan.proximateapp.utils.SharedPreferencesKeys.TOKEN;

/**
 * Created by jordan on 05/02/2018.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.txtEmail)
    EditText txtEmail;
    @BindView(R.id.txtPassword)
    EditText txtPassword;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        UI.hideKeyBoard(this);
        ProgressLayout.show(LoginActivity.this);
        final String user = txtEmail.getText().toString().trim();
        final String password = txtPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(user) && !TextUtils.isEmpty(password)) {
            login(new RequestLogin(user, password));
        } else {
            ProgressLayout.hide();
            UI.createSimpleCustomDialog(getString(R.string.error_empty),
                    getSupportFragmentManager());
        }
    }

    private void login(RequestLogin requestLogin) {
        APIClient.getClient().create(IApiClient.class).login(requestLogin).enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                ResponseLogin responseLogin = response.body();
                ProgressLayout.hide();
                if (responseLogin.getSuccess()) {
                    saveSession(responseLogin);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    ProgressLayout.hide();
                    LoginActivity.this.finish();
                } else {
                    UI.createSimpleCustomDialog(responseLogin.getMessage(), getSupportFragmentManager());
                }

            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                Log.e("Error", t.getMessage());
                ProgressLayout.hide();
                UI.createSimpleCustomDialog(t.getMessage(), getSupportFragmentManager());
            }
        });
    }

    private void saveSession(ResponseLogin responseLogin) {
        SharedPrefsManager.initialize(this);
        SharedPrefsManager.getInstance().setBoolean(IS_LOGGED, true);
        SharedPrefsManager.getInstance().setString(TOKEN, responseLogin.getToken());
        SharedPrefsManager.getInstance().setInt(ID, responseLogin.getId());
        Long tsLong = System.currentTimeMillis() / 1000;
        SharedPrefsManager.getInstance().setString(LAST_LOGIN, tsLong.toString());
    }
}
