package com.jordan.proximateapp.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.jordan.proximateapp.App;
import com.jordan.proximateapp.interfaces.DialogDoubleActions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordan on 07/02/2018.
 */

public class ValidatePermissions {


    public static String[] permissionsCheck = new String[]{
            Manifest.permission.SEND_SMS,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE
            , Manifest.permission.CALL_PHONE
            , Manifest.permission.RECORD_AUDIO};

    /**
     * Devuelve el arreglo de permisos a validar.
     * *@return {@link String[]} arreglo con permisos
     */
    public static String[] getPermissionsCheck() {
        return permissionsCheck;
    }

    /**
     * Obtenemos todos los permisos que necesita la app
     *
     * @params contexto {@link Context}  context
     * @params appPackage {@link String} nombre del package
     * *@return {@link List <String>} lista de Permisos
     */
    public static List<String> getGrantedPermissions(Context c, final String appPackage) {
        List<String> permissionNeeded = new ArrayList<String>();

        try {
            PackageInfo pi = c.getPackageManager().getPackageInfo(appPackage, PackageManager.GET_PERMISSIONS);
            for (int i = 0; i < pi.requestedPermissions.length; i++) {
                if ((pi.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
                    permissionNeeded.add(pi.requestedPermissions[i]);
                }
            }


        } catch (Exception e) {

        }

        return permissionNeeded;
    }

    /**
     * Valida que un permiso este habilitado, en caso de que no, solicitamos el permiso.
     *
     * @params activity {@link Activity}  context
     * @params permission {@link String} permiso.
     * @params PERMISSIONS_REQUEST entero con el request de la solicitud de permiso.
     * *@return {@link List<String>} lista de Permisos
     */
    public static boolean getSinglePermission(final Activity activity, String permission, int PERMISSION_REQUEST) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity, permission)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(activity,
                    new String[]{permission},
                    PERMISSION_REQUEST);

            return false;

        }

        return true;
    }

    /**
     * Valida que un permiso se encuentre habilitado.
     *
     * @params activity {@link Activity} activity.
     * @params permission {@link String} permiso.
     * @returns boolean que indica si el permiso esta habilitado.
     */
    public static boolean isPermissionActive(Activity activity, String permission) {

        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;

    }

    /**
     * Valida que todos los permisos contenidos en un arreglo se encuentren habilitados.
     *
     * @params activity {@link Activity} activity.
     * @params permissions {@link String[]} arreglo de permisos.
     * @returns boolean que indica si todos los permisos estan habilitados.
     */
    public static boolean isAllPermissionsActives(Activity activity, String[] permissions) {

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;

    }

    /**
     * Valida un permiso y en caso de que alguno este deshabilitado, lo solicitamos y mostramos explicación del permiso.
     *
     * @params activity {@link Activity} activity
     * @params permission {@link String} permiso
     * @params explanation {@link String} texto con la explicación del permiso.
     * @params PERMISSIONS_REQUEST entero con el request de la solicitud de permiso.
     */
    public static void checkSinglePermissionWithExplanation(final Activity activity, String permission, String title, String explanation, DialogDoubleActions actionsDialog, int PERMISSION_REQUEST) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity, permission)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showDialogPermission(activity, title, explanation, actionsDialog);
            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{permission},
                        PERMISSION_REQUEST);
            }
        }
    }

    /**
     * Valida un arreglo de permisos y en caso de que alguno este deshabilitado, lo solicitamos.
     *
     * @params activity {@link Activity} activity
     * @params listPermissions {@link String[]} arreglo de permisos
     * @params PERMISSIONS_REQUEST entero con el request de la solicitud de permiso.
     */
    public static void checkPermissions(Activity activity, String[] listPermissions, int PERMISSIONS_REQUEST) {

        for (String permission : listPermissions) {

            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(activity, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity,
                        updatePermissionsRequired(activity, listPermissions),
                        PERMISSIONS_REQUEST);
                break;
            }
        }
    }

    private static String[] updatePermissionsRequired(Activity activity, String[] permissionsCheck) {
        List<String> listPermision = new ArrayList<String>();
        for (String permission : permissionsCheck) {
            if (!isPermissionActive(activity, permission)) {
                listPermision.add(permission);
            }
        }

        String[] permissionCheckUpdated = new String[listPermision.size()];
        for (int i = 0; i < listPermision.size(); i++)
            permissionCheckUpdated[i] = listPermision.get(i);

        return permissionCheckUpdated;
    }

    /**
     * Mostramos un Dialog solicitando la activación de permisos.
     *
     * @params activity {@link Activity} activity
     * @params actionsDialog {@link com.jordan.proximateapp.interfaces.DialogDoubleActions} con las acciones del Dialog
     */
    public static void showDialogPermission(final Activity activity, String title, String msg, final DialogDoubleActions actionsDialog) {

        new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        actionsDialog.actionConfirm();
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }


    /**
     * Abrimos los detalles de la aplicación
     *
     * @params activity {@link Activity} activity
     * @params REQUEST_DETAILS_APP requestCode
     */
    public static void openDetailsApp(Activity activity, int REQUEST_DETAILS_APP) {
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivityForResult(i, REQUEST_DETAILS_APP);
    }

    public static List<String> getNamesPermissions() {

        PackageManager packageManager = App.getInstance().getPackageManager();
        List<String> listPermissionName = new ArrayList<>();
        CharSequence labelPermission;
        for (String permission : permissionsCheck) {

            labelPermission = getPermissionLabel(permission, packageManager);
            if (labelPermission != null && !labelPermission.toString().isEmpty())
                listPermissionName.add(labelPermission.toString());
        }

        return listPermissionName;

    }

    static CharSequence getPermissionLabel(String permission, PackageManager packageManager) {
        try {
            PermissionInfo permissionInfo = packageManager.getPermissionInfo(permission, 0);
            return permissionInfo.loadLabel(packageManager);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
