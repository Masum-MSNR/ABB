package com.project.androidbloodbank.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.project.androidbloodbank.R;

public class SplashScreen extends AppCompatActivity {

    TextView abbTv;
    ImageView abbLogoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView()
                        .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        );
            }
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_splash_screen);
        abbTv = (TextView) findViewById(R.id.abb);
        abbLogoIv = (ImageView) findViewById(R.id.logo_abb);
        Animation anim_abbTv = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.fadein);
        Animation anim_abbLogoIv = AnimationUtils.loadAnimation(SplashScreen.this, R.anim.bounce);
        abbTv.startAnimation(anim_abbTv);
        abbLogoIv.startAnimation(anim_abbLogoIv);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, Login.class));
                SplashScreen.this.finish();
            }
        }, 3000);
    }
}