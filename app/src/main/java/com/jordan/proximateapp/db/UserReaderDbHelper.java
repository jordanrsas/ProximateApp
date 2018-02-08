package com.jordan.proximateapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.jordan.proximateapp.db.DBContract.UserEntry;
import com.jordan.proximateapp.net.data.DataUser;

/**
 * Created by jordan on 07/02/2018.
 */

public class UserReaderDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "UserReader.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                    UserEntry._ID + " INTEGER PRIMARY KEY," +
                    UserEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_LAST_NAME + TEXT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_MAIL + TEXT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_NUMBER_DOC + TEXT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_LAST_SESION + TEXT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_DELETED + INT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_DOCUMENT_ID + INT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_DOCUMENT_ABREV + TEXT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_DOCUMENT_LABEL + TEXT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_USER_LABEL + TEXT_TYPE + COMMA_SEP +
                    UserEntry.COLUMN_NAME_SECCION + TEXT_TYPE
                    + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;

    public UserReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean insertUser(DataUser dataUser) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserEntry.COLUMN_NAME_NAME, dataUser.getNombres());
        contentValues.put(UserEntry.COLUMN_NAME_LAST_NAME, dataUser.getApellidos());
        contentValues.put(UserEntry.COLUMN_NAME_MAIL, dataUser.getCorreo());
        contentValues.put(UserEntry.COLUMN_NAME_NUMBER_DOC, dataUser.getNumero_documento());
        contentValues.put(UserEntry.COLUMN_NAME_LAST_SESION, dataUser.getUltima_sesion());
        contentValues.put(UserEntry.COLUMN_NAME_DELETED, dataUser.getEliminado());
        contentValues.put(UserEntry.COLUMN_NAME_DOCUMENT_ID, dataUser.getDocumentos_id());
        contentValues.put(UserEntry.COLUMN_NAME_DOCUMENT_ABREV, dataUser.getDocumentos_abrev());
        contentValues.put(UserEntry.COLUMN_NAME_DOCUMENT_LABEL, dataUser.getDocumentos_label());
        contentValues.put(UserEntry.COLUMN_NAME_USER_LABEL, dataUser.getEstados_usuarios_label());
        contentValues.put(UserEntry.COLUMN_NAME_SECCION, new Gson().toJson(dataUser.getSecciones()).toString());
        db.insert(UserEntry.TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(UserEntry.TABLE_NAME, null, null, null, null, null, null);
    }
}
