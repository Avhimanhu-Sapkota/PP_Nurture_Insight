package com.example.nurture_insight.user_profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurture_insight.Model.Assessment;
import com.example.nurture_insight.Model.Therapist;
import com.example.nurture_insight.Model.Users;
import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.R;
import com.example.nurture_insight.ViewHolder.AssessmentAdapter;
import com.example.nurture_insight.journal.JournalFragment;
import com.example.nurture_insight.login_signup.LoginActivity;
import com.example.nurture_insight.therapist_dashboard.EventDisplayAdapter;
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

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class UserProfileFragment extends Fragment {

    TextView username, focusedOn, moodProgressPercent, moodProgressMessage, journalProgressPercent, journalProgressMessage;
    CircleImageView profileImage;
    ImageView logout, editProfile;
    ProgressBar moodProgressChart, journalProgressChart;
    ArrayList<Assessment> assessmentsList;
    AssessmentAdapter assessmentAdapter;
    RecyclerView assessmentRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    TextView eachTherapistName, eachTherapistExpertise, eachTherapistWorksAt, eachTherapistBio, eachTherapistContact;
    LinearLayout eachTherapist_LinearLayout;
    Button user_write_journal_button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);

         username = (TextView)rootView.findViewById(R.id.profile_username);
         focusedOn = (TextView)rootView.findViewById(R.id.profile_focus);
         moodProgressPercent = (TextView)rootView.findViewById(R.id.mood_progress_percent);
         moodProgressMessage = (TextView)rootView.findViewById(R.id.mood_progress_message);
         journalProgressPercent = (TextView)rootView.findViewById(R.id.journal_progress_percent);
         journalProgressMessage = (TextView)rootView.findViewById(R.id.journal_progress_message);
         profileImage = (CircleImageView)rootView.findViewById(R.id.profile_image);
         logout = (ImageView)rootView.findViewById(R.id.profile_imgView_logout);
         editProfile = (ImageView) rootView.findViewById(R.id.imageView_settings);
         moodProgressChart = (ProgressBar) rootView.findViewById(R.id.mood_progress);
         journalProgressChart = (ProgressBar) rootView.findViewById(R.id.journal_progress);
        assessmentRecyclerView = (RecyclerView) rootView.findViewById(R.id.profile_assessment_recyclerView);
        eachTherapist_LinearLayout = (LinearLayout) rootView.findViewById(R.id.eachTherapist_LinearLayout);
        user_write_journal_button = (Button) rootView.findViewById(R.id.user_write_journal_button);
        eachTherapistName = (TextView) rootView.findViewById(R.id.user_eachTherapist_Name);
        eachTherapistExpertise = (TextView) rootView.findViewById(R.id.user_eachTherapist_expertise);
        eachTherapistWorksAt = (TextView) rootView.findViewById(R.id.user_eachTherapist_worksAt);
        eachTherapistBio = (TextView) rootView.findViewById(R.id.user_eachTherapist_bio);
        eachTherapistContact = (TextView) rootView.findViewById(R.id.user_eachTherapist_contact);

        user_write_journal_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new JournalFragment();
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

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new EditUserProfile();
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
        loadTherapistInfo();

        return rootView;

        }

    private void loadTherapistInfo() {

        if (Prevalent.currentOnlineUser.getTherapistID().equals("0000000000")){
            eachTherapist_LinearLayout.setVisibility(View.GONE);
        }
        else{
            eachTherapist_LinearLayout.setVisibility(View.VISIBLE);

            DatabaseReference therapistRef = FirebaseDatabase.getInstance().getReference().child("Therapist").child(Prevalent.currentOnlineUser.getTherapistID());
            therapistRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        Therapist therapistObj = snapshot.getValue(Therapist.class);

                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getTherapistID());
                        userRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot newSnapshot) {
                                String therapistName = newSnapshot.child("username").getValue().toString();
                                String contactDetails = Prevalent.currentOnlineUser.getTherapistID() + "\n" + newSnapshot.child("email").getValue().toString();

                                eachTherapistName.setText(therapistName);
                                eachTherapistContact.setText(contactDetails);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        eachTherapistExpertise.setText(therapistObj.getExpertise());
                        eachTherapistWorksAt.setText("Works at: " + therapistObj.getWorksAt());
                        eachTherapistBio.setText(therapistObj.getBio());
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }



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
                journalProgressMessage.setText("You wrote Journal for "+ counter + " days in this month");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}