package com.jordan.proximateapp.net;

import com.jordan.proximateapp.net.data.RequestLogin;
import com.jordan.proximateapp.net.data.ResponseLogin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by jordan on 06/02/2018.
 */

public interface IApiClient {

    @POST("/catalog/dev/webadmin/authentication/login")
    Call<ResponseLogin> login(@Body RequestLogin requestLogin);
}
