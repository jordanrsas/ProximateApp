package com.jordan.proximateapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jordan.proximateapp.login.LoginActivity;
import com.jordan.proximateapp.main.controllers.MainActivity;
import com.jordan.proximateapp.utils.SharedPreferencesKeys;
import com.jordan.proximateapp.utils.SharedPrefsManager;
import com.jordan.proximateapp.utils.ValidatePermissions;

import java.util.Timer;
import java.util.TimerTask;

import static com.jordan.proximateapp.utils.SharedPreferencesKeys.IS_LOGGED;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Intent intent;
                SharedPrefsManager.initialize(SplashActivity.this);
                if (SharedPrefsManager.getInstance().getBoolean(IS_LOGGED)) {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }

                startActivity(intent);
                SplashActivity.this.finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask, 2000);
    }
}
