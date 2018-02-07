package com.jordan.proximateapp.login;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunctionException;
import com.amazonaws.mobileconnectors.lambdainvoker.LambdaInvokerFactory;
import com.amazonaws.regions.Regions;
import com.jordan.proximateapp.R;
import com.jordan.proximateapp.main.data.ws.RequestLoginClass;
import com.jordan.proximateapp.main.data.ws.ResponseLoginClass;
import com.jordan.proximateapp.main.interfaces.LoginInterface;
import com.jordan.proximateapp.net.APIClient;
import com.jordan.proximateapp.net.IApiClient;
import com.jordan.proximateapp.net.data.RequestLogin;
import com.jordan.proximateapp.net.data.ResponseLogin;
import com.jordan.proximateapp.utils.ProgressLayout;
import com.jordan.proximateapp.utils.UI;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            //loginLamda(user, password);
                    /*Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    ProgressLayout.hide();
                    LoginActivity.this.finish();*/
            //ProgressLayout.hide();
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
                response.body();
                ProgressLayout.hide();
                UI.createSimpleCustomDialog(response.body().getMessage(), getSupportFragmentManager());
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                Log.e("Error", t.getMessage());
                ProgressLayout.hide();
                UI.createSimpleCustomDialog(t.getMessage(), getSupportFragmentManager());
            }
        });
    }

    protected void loginLamda(String user, String password) {
        CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
                this.getApplicationContext(), "identity-pool-id", Regions.US_EAST_1);

        LambdaInvokerFactory factory = new LambdaInvokerFactory(this.getApplicationContext(),
                Regions.US_WEST_2, cognitoProvider);

        final LoginInterface myInterface = factory.build(LoginInterface.class);
        RequestLoginClass request = new RequestLoginClass(user, password);

        new AsyncTask<RequestLoginClass, Void, ResponseLoginClass>() {
            @Override
            protected ResponseLoginClass doInBackground(RequestLoginClass... params) {
                try {
                    return myInterface.login(params[0]);
                } catch (LambdaFunctionException lfe) {
                    Log.e("Tag", "Failed to invoke echo", lfe);
                    return null;
                }
            }

            @Override
            protected void onPostExecute(ResponseLoginClass responseLoginClass) {
                if (responseLoginClass == null) {
                    return;
                }

                //GET PARAMS RETURN
            }
        }.execute(request);
    }
}
