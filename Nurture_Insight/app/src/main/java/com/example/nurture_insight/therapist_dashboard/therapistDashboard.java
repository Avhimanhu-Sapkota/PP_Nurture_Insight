package com.example.nurture_insight.therapist_dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurture_insight.Model.events;
import com.example.nurture_insight.Model.Users;
import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.R;
import com.example.nurture_insight.therapist_profile.TherapistProfileFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class therapistDashboard extends Fragment {

    ImageView backButton;
    Button addPatientButton, addEventButton;
    RecyclerView recyclerView, eventRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Users> patientsList;
    ArrayList<events> eventsList;
    EventDisplayAdapter eventDisplayAdapter;
    PatientDisplayAdapter patientDisplayAdapter;
    LinearLayout eventLinearLayout;
    TextView td_message1, td_message2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_therapist_dashboard, container, false);

        addPatientButton = (Button) view.findViewById(R.id.td_add_patient);
        addEventButton = (Button) view.findViewById(R.id.td_add_event_button);
        backButton = (ImageView) view.findViewById(R.id.therapist_dashboard_backButton);
        recyclerView = (RecyclerView) view.findViewById(R.id.td_recyclerView);
        eventLinearLayout = (LinearLayout) view.findViewById(R.id.td_slider_linearLayout);
        eventRecyclerView = (RecyclerView) view.findViewById(R.id.td_event_recyclerView);
        td_message1 = (TextView) view.findViewById(R.id.td_message1_patient);
        td_message2 = (TextView) view.findViewById(R.id.td_message2_details);

        addPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new userDisplayForTherapist();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new add_mental_health_events();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new TherapistProfileFragment();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        final DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference()
                .child("events");

        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(Prevalent.currentOnlineUser.getPhoneNo())){
                    eventLinearLayout.setVisibility(View.VISIBLE);
                }
                else{
                    eventLinearLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        patientsList = new ArrayList<>();
        loadPatients();


        eventsList = new ArrayList<>();
        loadEvents();

        return view;
    }

    private void loadEvents() {
        final DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference()
                .child("events").child(Prevalent.currentOnlineUser.getPhoneNo());

        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventsList.clear();
                for (DataSnapshot newSnapshot: snapshot.getChildren()){
                    String eventID = newSnapshot.getKey();
                    DatabaseReference finalEventRef = eventRef.child(eventID);

                    events model = new events(finalEventRef);
                    eventsList.add(model);

                }

                layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                eventRecyclerView.setLayoutManager(layoutManager);
                eventRecyclerView.setItemAnimator(new DefaultItemAnimator());
                eventDisplayAdapter = new EventDisplayAdapter(getContext(),eventsList);
                eventRecyclerView.setAdapter(eventDisplayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void loadPatients() {
        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("Users");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                patientsList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String therapistID = dataSnapshot.child("therapistID").getValue().toString();

                    if(therapistID.equals(Prevalent.currentOnlineUser.getPhoneNo())) {
                        String usersID = dataSnapshot.getKey();
                        Users model = new Users(usersID);
                        patientsList.add(model);
                    }

                    if(patientsList.isEmpty()) {
                        td_message1.setVisibility(View.GONE);
                        td_message2.setVisibility(View.GONE);
                    }else{
                        td_message1.setVisibility(View.VISIBLE);
                        td_message2.setVisibility(View.VISIBLE);
                    }

                    layoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(layoutManager);
                    patientDisplayAdapter = new PatientDisplayAdapter(getContext(),patientsList);
                    recyclerView.setAdapter(patientDisplayAdapter);

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}