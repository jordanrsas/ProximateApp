package com.jordan.proximateapp.net.data;

/**
 * Created by jordan on 06/02/2018.
 */

public class ResponseLogin extends ResponseWS {
    String token;
    int id;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
