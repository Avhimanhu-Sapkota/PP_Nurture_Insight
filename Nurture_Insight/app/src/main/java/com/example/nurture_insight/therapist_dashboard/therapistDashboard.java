package com.example.nurture_insight.therapist_dashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurture_insight.Model.Users;
import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.R;
import com.example.nurture_insight.TherapistProfileFragment;
import com.example.nurture_insight.journal.JournalFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class therapistDashboard extends Fragment {

    ImageView backButton;
    Button addPatientButton;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Users> patientsList;
    PatientDisplayAdapter patientDisplayAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_therapist_dashboard, container, false);

        addPatientButton = (Button) view.findViewById(R.id.td_add_patient);
        backButton = (ImageView) view.findViewById(R.id.therapist_dashboard_backButton);
        recyclerView = (RecyclerView) view.findViewById(R.id.td_recyclerView);

        addPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new userDisplayForTherapist();

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

        patientsList = new ArrayList<>();
        loadPatients();

        return view;
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