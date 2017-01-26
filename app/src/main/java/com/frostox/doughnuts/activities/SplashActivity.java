package com.frostox.doughnuts.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.frostox.doughnuts.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class SplashActivity extends AppCompatActivity {

    ImageView appIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        this.getSupportActionBar().hide();

        appIcon = (ImageView) findViewById(R.id.activity_splash_app_icon);
        Picasso.with(this).load(R.drawable.doughnut).into(appIcon);
        final Animation rotation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.rotate);
        rotation.setFillAfter(true);
        rotation.setFillAfter(true);
        appIcon.startAnimation(rotation);

        Handler handlerTimer = new Handler();
        handlerTimer.postDelayed(new Runnable(){
            public void run() {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // User is signed in
                    // go to home
                    finish();
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    // go to login
                    finish();
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

            }}, 3000);
    }
}
