package com.jordan.proximateapp.net.data;

/**
 * Created by jordan on 06/02/2018.
 */

public class RequestLogin {

    String correo;
    String contrasenia;

    public RequestLogin() {
    }

    public RequestLogin(String user, String password) {
        this.correo = user;
        this.contrasenia = password;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }
}
