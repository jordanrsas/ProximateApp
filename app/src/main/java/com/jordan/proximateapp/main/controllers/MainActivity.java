package com.jordan.proximateapp.main.controllers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.jordan.proximateapp.R;
import com.jordan.proximateapp.interfaces.DialogDoubleActions;
import com.jordan.proximateapp.login.LoginActivity;
import com.jordan.proximateapp.main.fragments.UserFragment;
import com.jordan.proximateapp.main.interfaces.enums.Direction;
import com.jordan.proximateapp.net.APIClient;
import com.jordan.proximateapp.net.IApiClient;
import com.jordan.proximateapp.net.data.ResponseGetDataUser;
import com.jordan.proximateapp.utils.SharedPreferencesKeys;
import com.jordan.proximateapp.utils.SharedPrefsManager;
import com.jordan.proximateapp.utils.SupportComponent;
import com.jordan.proximateapp.utils.UI;
import com.jordan.proximateapp.utils.ValidatePermissions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.jordan.proximateapp.utils.SharedPreferencesKeys.TOKEN;

/**
 * Created by jordan on 06/02/2018.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.drawer)
    DrawerLayout drawerLayout;

    private CircleImageView imageUser;

    private String mCurrentPhotoPath;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    /*Permissions*/
    public static final int PERMISSION_GENERAL = 111;

    //static final int REQUEST_TAKE_PHOTO = 1;
    private SupportComponent mSupportComponent;
    private int lastCheked;
    private Bitmap imageBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mSupportComponent = new SupportComponent(getSupportFragmentManager());
        SharedPrefsManager.initialize(this);
        ButterKnife.bind(this);
        checkPermissions();

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        imageUser = header.findViewById(R.id.imageProfile);

        String path = SharedPrefsManager.getInstance().getString(SharedPreferencesKeys.PHOTO_PATH);
        if (!TextUtils.isEmpty(path)) {
            printUserImage(path);
        }

        FloatingActionButton fabEditProfile = header.findViewById(R.id.fabEditProfile);
        fabEditProfile.setOnClickListener(this);

        lastCheked = navigationView.getMenu().getItem(0).getItemId();
        navigationView.setCheckedItem(lastCheked);
        loadFragment(UserFragment.newInstance(), Direction.NONE, false);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        dispatchTakePictureIntent();
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Bundle extras = data.getExtras();
            SharedPrefsManager.getInstance().setString(SharedPreferencesKeys.PHOTO_PATH, mCurrentPhotoPath);
            printUserImage(mCurrentPhotoPath);
        }
    }

    private void printUserImage(String path) {
        if (imageBitmap == null) {
            setPic(path);
        }
        imageUser.setImageBitmap(imageBitmap);
    }

    private void setPic(String path) {
        // Get the dimensions of the View
        int targetW = convertDpToPx(90);
        int targetH = convertDpToPx(90);

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        imageBitmap = BitmapFactory.decodeFile(path, bmOptions);
        //mImageView.setImageBitmap(bitmap);
    }

    private int convertDpToPx(int dp) {
        return Math.round(dp * (getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));

    }

    protected void checkPermissions() {
        if (!ValidatePermissions.isAllPermissionsActives(this, ValidatePermissions.getPermissionsCheck())) {
            ValidatePermissions.checkPermissions(this, ValidatePermissions.getPermissionsCheck(), PERMISSION_GENERAL);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        } else {
            showDialogOut();
        }
    }

    private void showDialogOut() {
        UI.createSimpleCustomDialog(getString(R.string.app_name), getString(R.string.desea_cacelar), getSupportFragmentManager(),
                new DialogDoubleActions() {
                    @Override
                    public void actionConfirm(Object... params) {
                        finish();
                    }

                    @Override
                    public void actionCancel(Object... params) {

                    }
                }, true, true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        drawerLayout.closeDrawers();
        if (lastCheked != id) {
            switch (id) {
                case R.id.menu_user:
                    loadFragment(UserFragment.newInstance(), Direction.FORDWARD, false);
                    lastCheked = id;
                    break;
                case R.id.menu_foto:

                    break;
                case R.id.menu_logout:
                    break;
            }
        }
        return true;
    }

    protected void loadFragment(@NonNull GenericFragment fragment, @NonNull Direction Direction,
                                boolean addToBackStack) {
        mSupportComponent.loadFragment(fragment, R.id.container, Direction, addToBackStack);
    }
}
