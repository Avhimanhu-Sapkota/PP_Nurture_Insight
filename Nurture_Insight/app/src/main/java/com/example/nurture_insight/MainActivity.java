package com.example.nurture_insight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.Toast;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

public class MainActivity extends AppCompatActivity {

    SpaceNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.main_navSpace);
        navigationView.initWithSaveInstanceState(savedInstanceState);
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_baseline_home_24));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_baseline_explore_24));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_baseline_forum_24));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_baseline_import_contacts_24));

        getSupportFragmentManager().beginTransaction().replace(R.id.main_fragmentLayout, new HomeFragment()).commit();

        navigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                navigationView.setCentreButtonSelectable(true);

                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragmentLayout, new UserProfileFragment()).commit();
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {

                Fragment selectedFragment = null;

                if(itemIndex == 0){
                    selectedFragment = new HomeFragment();
                }
                else if(itemIndex == 1){
                    selectedFragment = new ScrollingFragment();
                }
                else if(itemIndex == 2){
                    selectedFragment = new ChatCommunityFragment();
                }
                else if(itemIndex == 3){
                    selectedFragment = new ScrollingFragment();
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragmentLayout, selectedFragment).commit();
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                Toast.makeText(MainActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
            }
        });
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        navigationView.onSaveInstanceState(outState);
//    }
}