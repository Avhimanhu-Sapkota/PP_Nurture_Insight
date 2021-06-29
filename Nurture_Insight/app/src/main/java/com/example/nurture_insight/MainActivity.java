package com.example.nurture_insight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.example.nurture_insight.Home.HomeFragment;
import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.chat_community.ChatCommunityFragment;
import com.example.nurture_insight.habit_tracker.habit_tracker_home;
import com.example.nurture_insight.journal.JournalFragment;
import com.example.nurture_insight.therapist_profile.TherapistProfileFragment;
import com.example.nurture_insight.user_profile.UserProfileFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    SpaceNavigationView navigationView;
    String dayOfTheWeek, saveCurrentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationView = findViewById(R.id.main_navSpace);
        navigationView.initWithSaveInstanceState(savedInstanceState);
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_baseline_home_24));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_baseline_track_changes_24));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_baseline_forum_24));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_baseline_import_contacts_24));

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        dayOfTheWeek = sdf.format(d);

        if(dayOfTheWeek.equals("Saturday")){
            checkAssessment();
        }
        else{
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragmentLayout, new HomeFragment()).commit();
        }

        navigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                navigationView.setCentreButtonSelectable(true);

                if(Prevalent.currentOnlineUser.getType().equals("therapist")){
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_fragmentLayout, new TherapistProfileFragment()).commit();
                }
                else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_fragmentLayout, new UserProfileFragment()).commit();
                }

            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                Fragment selectedFragment = null;

                if(itemIndex == 0){
                    selectedFragment = new HomeFragment();

                }
                else if(itemIndex == 1){
                    selectedFragment = new habit_tracker_home();
                }
                else if(itemIndex == 2){
                    selectedFragment = new ChatCommunityFragment();
                }
                else if(itemIndex == 3){
                    selectedFragment = new JournalFragment();
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.main_fragmentLayout, selectedFragment).commit();
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                Toast.makeText(MainActivity.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkAssessment() {
        DatabaseReference assessmentRef = FirebaseDatabase.getInstance().getReference().child("Assessment")
                .child(Prevalent.currentOnlineUser.getPhoneNo());

        assessmentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(saveCurrentDate)){
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_fragmentLayout, new HomeFragment()).commit();
                }
                else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_fragmentLayout, new weekly_assessment()).commit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        navigationView.onSaveInstanceState(outState);
//    }
}