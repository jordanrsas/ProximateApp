package com.jordan.proximateapp.main.data.ws;

/**
 * Created by jordan on 06/02/2018.
 */

public class RequestLoginClass {

    String correo;
    String contrasenia;

    public RequestLoginClass() {
    }

    public RequestLoginClass(String user, String password) {
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
