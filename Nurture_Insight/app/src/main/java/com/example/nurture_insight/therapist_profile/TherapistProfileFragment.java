package com.example.nurture_insight.therapist_profile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurture_insight.Model.Assessment;
import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.R;
import com.example.nurture_insight.ViewHolder.AssessmentAdapter;
import com.example.nurture_insight.instant_help.live_in_present;
import com.example.nurture_insight.journal.JournalFragment;
import com.example.nurture_insight.login_signup.LoginActivity;
import com.example.nurture_insight.therapist_dashboard.therapistDashboard;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class TherapistProfileFragment extends Fragment {

    TextView username, focusedOn, moodProgressPercent, moodProgressMessage, journalProgressPercent, journalProgressMessage;
    CircleImageView profileImage;
    ImageView logout, settings;
    ProgressBar moodProgressChart, journalProgressChart;
    Button therapistDashboardButton, write_journal_button;
    ArrayList<Assessment> assessmentsList;
    AssessmentAdapter assessmentAdapter;
    RecyclerView assessmentRecyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_therapist_profile, container, false);

        username = (TextView)rootView.findViewById(R.id.therapistProfile_username);
        profileImage = (CircleImageView)rootView.findViewById(R.id.therapistProfile_image);
        logout = (ImageView)rootView.findViewById(R.id.therapistProfile_imgView_logout);
        therapistDashboardButton = (Button) rootView.findViewById(R.id.therapist_dashboard_button);
        settings = (ImageView)rootView.findViewById(R.id.therapistProfile_imgView_settings);
        focusedOn = (TextView)rootView.findViewById(R.id.therapist_profile_focus);
        write_journal_button = (Button) rootView.findViewById(R.id.therapist_write_journal_button);
        moodProgressPercent = (TextView)rootView.findViewById(R.id.therapist_mood_progress_percent);
        moodProgressMessage = (TextView)rootView.findViewById(R.id.therapist_mood_progress_message);
        moodProgressChart = (ProgressBar) rootView.findViewById(R.id.therapist_mood_progress);
        journalProgressPercent = (TextView)rootView.findViewById(R.id.therapist_journal_progress_percent);
        journalProgressMessage = (TextView)rootView.findViewById(R.id.therapist_journal_progress_message);
        journalProgressChart = (ProgressBar) rootView.findViewById(R.id.therapist_journal_progress);
        assessmentRecyclerView = (RecyclerView) rootView.findViewById(R.id.therapist_profile_assessment_recyclerView);


        write_journal_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new JournalFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EditTherapistProfile();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                Intent logoutIntent= new Intent(getActivity(), LoginActivity.class);
                startActivity(logoutIntent);
            }
        });

        therapistDashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new therapistDashboard();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        username.setText(Prevalent.currentOnlineUser.getUsername());
        String category = getCategoryName(Prevalent.currentOnlineUser.getCategory());
        focusedOn.setText(Prevalent.currentOnlineUser.getPhoneNo()+ "\n" +"Focused on " + category);

        moodProgressBar();
        journalProgressBar();
        loadAssessmentResults();

        return rootView;
    }

    private String getCategoryName(String category) {

        if(category.equals("happiness")){
            return "Living Happily";
        }
        else if(category.equals("anger")){
            return "Managing Anger";
        }
        else if(category.equals("depression")){
            return "Overcoming Depression";
        }
        else if(category.equals("sleep")){
            return "Getting Enough Sleep";
        }
        else if(category.equals("anxietyStress")){
            return "Beating Anxiety or Stress";
        }
        else{
            return "Self-Care";
        }
    }

    private void moodProgressBar(){
        DatabaseReference moodRef = FirebaseDatabase.getInstance().getReference()
                .child("Mood_Tracker").child(Prevalent.currentOnlineUser.getPhoneNo());

        moodRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int counter=0;

                for(DataSnapshot newSnap: snapshot.getChildren()){
                    String eachMoodId = newSnap.getKey();

                    DateFormat dayFormat=new SimpleDateFormat("MMM");

                    Date eachDate = null;
                    try {
                        eachDate = dayFormat.parse(eachMoodId);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    String finalDay = dayFormat.format(eachDate);
                    Calendar calObj = Calendar.getInstance();
                    String saveCurrentDate = dayFormat.format(calObj.getTime());

                    if (finalDay.equals(saveCurrentDate)){
                        counter++;
                    }
                }

                double res = (counter / 31.0f) * 100;
                int progress = (int) res;
                moodProgressChart.setProgress(progress);
                moodProgressChart.animate();
                moodProgressPercent.setText(progress+"%");
                moodProgressMessage.setText("Mood Tracked for " + counter + " days in this month");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void journalProgressBar(){
        DatabaseReference moodRef = FirebaseDatabase.getInstance().getReference()
                .child("Journals").child(Prevalent.currentOnlineUser.getPhoneNo());

        moodRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int counter=0;

                for(DataSnapshot newSnap: snapshot.getChildren()){
                    String eachJournalId = newSnap.getKey();

                    DateFormat dayFormat=new SimpleDateFormat("MMM");

                    Date eachDate = null;
                    try {
                        eachDate = dayFormat.parse(eachJournalId);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    String finalDay = dayFormat.format(eachDate);
                    Calendar calObj = Calendar.getInstance();
                    String saveCurrentDate = dayFormat.format(calObj.getTime());

                    if (finalDay.equals(saveCurrentDate)){
                        counter++;
                    }
                }

                double res = (counter / 31.0f) * 100;
                int progress = (int) res;
                journalProgressChart.setProgress(progress);
                journalProgressChart.animate();
                journalProgressPercent.setText(progress+"%");
                journalProgressMessage.setText("You wrote Journal for "+ counter + " days in t his month");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadAssessmentResults() {

        assessmentsList = new ArrayList<>();
        final DatabaseReference assessmentRef = FirebaseDatabase.getInstance().getReference()
                .child("Assessment").child(Prevalent.currentOnlineUser.getPhoneNo());

        assessmentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                assessmentsList.clear();
                for (DataSnapshot newSnap: snapshot.getChildren()){
                    String assessmentID = newSnap.getKey();
                    DatabaseReference eachAssessmentRef = assessmentRef.child(assessmentID);

                    Assessment model = new Assessment(eachAssessmentRef);
                    assessmentsList.add(model);
                }
                layoutManager = new LinearLayoutManager(getContext());
                assessmentRecyclerView.setLayoutManager(layoutManager);
                assessmentAdapter = new AssessmentAdapter(getContext(),assessmentsList);
                assessmentRecyclerView.setAdapter(assessmentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}