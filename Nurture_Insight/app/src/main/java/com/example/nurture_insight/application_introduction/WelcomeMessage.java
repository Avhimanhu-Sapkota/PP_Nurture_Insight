package com.example.nurture_insight.application_introduction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.nurture_insight.MainActivity;
import com.example.nurture_insight.R;
import com.example.nurture_insight.SplashActivity;

public class WelcomeMessage extends AppCompatActivity {

    Button btnGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_message);

        btnGetStarted = (Button) findViewById(R.id.wm_button_getStarted);
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("OneTimePref", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("firstTime", false);
                editor.apply();

                Intent splashIntent = new Intent(WelcomeMessage.this, SplashActivity.class);
                startActivity(splashIntent);
                finish();
            }
        });
    }
}