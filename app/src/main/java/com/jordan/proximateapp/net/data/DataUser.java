package com.jordan.proximateapp.net.data;

import java.util.ArrayList;

/**
 * Created by jordan on 06/02/2018.
 */

public class DataUser {
    int id;
    String nombres;
    String apellidos;
    String correo;
    String numero_documento;
    String ultima_sesion;
    int eliminado;
    int documentos_id;
    String documentos_abrev;
    String documentos_label;
    String estados_usuarios_label;
    ArrayList<Secciones> secciones;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNumero_documento() {
        return numero_documento;
    }

    public void setNumero_documento(String numero_documento) {
        this.numero_documento = numero_documento;
    }

    public String getUltima_sesion() {
        return ultima_sesion;
    }

    public void setUltima_sesion(String ultima_sesion) {
        this.ultima_sesion = ultima_sesion;
    }

    public int getEliminado() {
        return eliminado;
    }

    public void setEliminado(int eliminado) {
        this.eliminado = eliminado;
    }

    public int getDocumentos_id() {
        return documentos_id;
    }

    public void setDocumentos_id(int documentos_id) {
        this.documentos_id = documentos_id;
    }

    public String getDocumentos_abrev() {
        return documentos_abrev;
    }

    public void setDocumentos_abrev(String documentos_abrev) {
        this.documentos_abrev = documentos_abrev;
    }

    public String getDocumentos_label() {
        return documentos_label;
    }

    public void setDocumentos_label(String documentos_label) {
        this.documentos_label = documentos_label;
    }

    public String getEstados_usuarios_label() {
        return estados_usuarios_label;
    }

    public void setEstados_usuarios_label(String estados_usuarios_label) {
        this.estados_usuarios_label = estados_usuarios_label;
    }

    public ArrayList<Secciones> getSecciones() {
        return secciones;
    }

    public void setSecciones(ArrayList<Secciones> secciones) {
        this.secciones = secciones;
    }
}
