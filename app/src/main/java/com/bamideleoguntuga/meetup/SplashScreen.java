package com.bamideleoguntuga.meetup;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bamideleoguntuga.meetup.utils.PreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreen extends AppCompatActivity {
    @BindView(R.id.imageView2) ImageView imageView;

    String token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        //check for token if available
        token = PreferenceUtils.getToken(getApplicationContext());

        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        ButterKnife.bind(this);
        Animation a = AnimationUtils.loadAnimation(this, R.anim.fade);
        imageView.startAnimation(a);//start imageview animation
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(3000);//splash time
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (!token.isEmpty()) {
                        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            }
        };
        timerThread.start();//start the splash activity
    }
}
