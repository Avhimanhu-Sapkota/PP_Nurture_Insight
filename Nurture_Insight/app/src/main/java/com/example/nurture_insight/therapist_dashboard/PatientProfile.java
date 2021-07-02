package com.example.nurture_insight.therapist_dashboard;

import android.os.Bundle;
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
import com.example.nurture_insight.journal.JournalFragment;
import com.example.nurture_insight.therapist_dashboard.therapistDashboard;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PatientProfile extends Fragment {

    ImageView backButton;
    String patientID, patientName,patientCategory;
    TextView username, focusedOn, moodProgressPercent, moodProgressMessage, journalProgressPercent, journalProgressMessage;
    Button viewJournal;
    ProgressBar moodProgressChart, journalProgressChart;
    ArrayList<Assessment> assessmentsList;
    AssessmentAdapter assessmentAdapter;
    RecyclerView assessmentRecyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_patient_profile, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null){
            patientID = bundle.getString("patientId", "");
        }

        username = (TextView) view.findViewById(R.id.patient_profile_username);
        focusedOn = (TextView) view.findViewById(R.id.patient_profile_focus);
        backButton = (ImageView) view.findViewById(R.id.patient_profile_backButton);
        viewJournal = (Button) view.findViewById(R.id.patient_view_journal_button);
        moodProgressPercent = (TextView) view.findViewById(R.id.patient_mood_progress_percent);
        moodProgressMessage = (TextView) view.findViewById(R.id.patient_mood_progress_message);
        journalProgressPercent = (TextView) view.findViewById(R.id.patient_journal_progress_percent);
        journalProgressMessage = (TextView) view.findViewById(R.id.patient_journal_progress_message);
        moodProgressChart = (ProgressBar) view.findViewById(R.id.patient_mood_progress);
        journalProgressChart = (ProgressBar) view.findViewById(R.id.patient_journal_progress);
        assessmentRecyclerView = (RecyclerView) view.findViewById(R.id.patient_profile_assessment_recyclerView);

        viewJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PatientJournal();
                Bundle bundle = new Bundle();
                bundle.putString("patientId", patientID);
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new therapistDashboard();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        loadPatientDetails();

        moodProgressBar();
        journalProgressBar();
        loadAssessmentResults();

        return view;
    }

    private void loadPatientDetails() {

        final DatabaseReference patientRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(patientID);

        patientRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                patientName = snapshot.child("username").getValue().toString();
                patientCategory = snapshot.child("category").getValue().toString();

                username.setText(patientName);
                String categoryName = getCategoryName(patientCategory);
                focusedOn.setText(patientID + "\nFocused on: " + categoryName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                .child("Mood_Tracker").child(patientID);

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
                .child("Journals").child(patientID);

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
                journalProgressPercent.setText(progress+"%");
                journalProgressMessage.setText("You wrote Journal for "+ counter + " days in this month");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadAssessmentResults() {

        assessmentsList = new ArrayList<>();
        final DatabaseReference assessmentRef = FirebaseDatabase.getInstance().getReference()
                .child("Assessment").child(patientID);

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