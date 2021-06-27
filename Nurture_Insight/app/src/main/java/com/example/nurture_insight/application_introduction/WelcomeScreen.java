package com.example.nurture_insight.application_introduction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;

import com.example.nurture_insight.MainActivity;
import com.example.nurture_insight.R;
import com.example.nurture_insight.SplashActivity;

public class WelcomeScreen extends AppCompatActivity {

    float x1, x2, y1, y2;
    SharedPreferences sharedPreferences;
    Boolean firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        sharedPreferences = getSharedPreferences("OneTimePref", MODE_PRIVATE);
        firstTime = sharedPreferences.getBoolean("firstTime", true);

        if (!(firstTime)) {
            Intent welcomeIntent = new Intent(WelcomeScreen.this, SplashActivity.class);
            startActivity(welcomeIntent);
            finish();
        }

    }

    public boolean onTouchEvent(MotionEvent touchEvent) {
        switch (touchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();

                if (x1 > x2) {
                    Intent welcomeIntent = new Intent(WelcomeScreen.this, WelcomeMessage.class);
                    startActivity(welcomeIntent);
                }
                break;
        }
        return false;
    }
}