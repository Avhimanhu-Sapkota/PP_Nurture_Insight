package com.example.nurture_insight.instant_help;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.nurture_insight.Home.BrowseTherapistFragment;
import com.example.nurture_insight.Home.HomeFragment;
import com.example.nurture_insight.R;

public class calm_down_info_1 extends AppCompatActivity {

    ImageView backButton;
    float x1,x2,y1,y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calm_down_info_1);

        backButton = (ImageView) findViewById(R.id.cdi1_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment =  new HomeFragment();

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_fragmentLayout, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

            }
        });
    }

    public boolean onTouchEvent(MotionEvent touchEvent){
        switch (touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();

                if (x1 > x2){
                    Intent welcomeIntent = new Intent(calm_down_info_1.this, calm_down_info_2.class);
                    startActivity(welcomeIntent);
                }
                else if (x1 < x2){
                    Fragment fragment = new BrowseTherapistFragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.main_fragmentLayout, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                break;
        }
        return false;
    }
}