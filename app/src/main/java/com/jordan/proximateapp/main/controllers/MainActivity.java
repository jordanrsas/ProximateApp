package com.jordan.proximateapp.main.controllers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jordan.proximateapp.R;
import com.jordan.proximateapp.net.APIClient;
import com.jordan.proximateapp.net.IApiClient;
import com.jordan.proximateapp.net.data.ResponseGetDataUser;
import com.jordan.proximateapp.utils.SharedPrefsManager;

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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.imagenPerfil)
    CircleImageView imagenPerfil;
    @BindView(R.id.btnImagen)
    Button btnImagen;

    private String mCurrentPhotoPath;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    //static final int REQUEST_TAKE_PHOTO = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getDataUserSession();
        btnImagen.setOnClickListener(this);

    }

    private void getDataUserSession() {
        APIClient.getClient().create(IApiClient.class).getDataUser(SharedPrefsManager.getInstance().getString(TOKEN)).enqueue(new Callback<ResponseGetDataUser>() {
            @Override
            public void onResponse(Call<ResponseGetDataUser> call, Response<ResponseGetDataUser> response) {
                Log.e("MyError", response.body().getMessage());
            }

            @Override
            public void onFailure(Call<ResponseGetDataUser> call, Throwable t) {
                Log.e("MyError", t.getMessage());
            }
        });
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
            Bitmap imageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath); //(Bitmap) extras.get("data");
            imagenPerfil.setImageBitmap(imageBitmap);
        }
    }


}
