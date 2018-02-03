package com.project.astroavi.hellonotif.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.project.astroavi.hellonotif.R;

/**
 * Created by a1274544 on 10/08/17.
 */

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!prefs.getBoolean("splash", false)) {
                    Intent i = new Intent(SplashScreen.this, FirstScreen.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(SplashScreen.this, AppsGridScreen.class);
                    startActivity(i);
                }
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
