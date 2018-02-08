package com.jordan.proximateapp.db;

import android.provider.BaseColumns;

/**
 * Created by jordan on 07/02/2018.
 */

public class DBContract {
    private DBContract() {
    }

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_NAME = "nombres";
        public static final String COLUMN_NAME_LAST_NAME = "apellidos";
        public static final String COLUMN_NAME_MAIL = "correo";
        public static final String COLUMN_NAME_NUMBER_DOC = "numero_documento";
        public static final String COLUMN_NAME_LAST_SESION = "ultima_sesion";
        public static final String COLUMN_NAME_DELETED = "eliminado";
        public static final String COLUMN_NAME_DOCUMENT_ID = "documentos_id";
        public static final String COLUMN_NAME_DOCUMENT_ABREV = "documentos_abrev";
        public static final String COLUMN_NAME_DOCUMENT_LABEL = "documentos_label";
        public static final String COLUMN_NAME_USER_LABEL = "estados_usuarios_label";
        public static final String COLUMN_NAME_SECCION = "secciones";
    }
}
