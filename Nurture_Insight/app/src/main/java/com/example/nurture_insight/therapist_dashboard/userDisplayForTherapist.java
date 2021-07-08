package com.example.nurture_insight.therapist_dashboard;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurture_insight.Model.Users;
import com.example.nurture_insight.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class userDisplayForTherapist extends Fragment {

    ImageView backButton;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Users> newPatientsList;
    UserDisplayAdapter userDisplayAdapter;
    ProgressDialog loading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_display_for_therapist, container, false);

        backButton = (ImageView) view.findViewById(R.id.tdAddUser_backButton);
        recyclerView = (RecyclerView) view.findViewById(R.id.tdAddUser_recyclerView);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment =  new therapistDashboard();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        newPatientsList = new ArrayList<>();
        loadNewPatients();

        return view;
    }

    public void loadNewPatients(){

        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("Users");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newPatientsList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String therapistID = dataSnapshot.child("therapistID").getValue().toString();
                    String userType = dataSnapshot.child("type").getValue().toString();

                    if(therapistID.equals("0000000000") && userType.equals("user")) {
                        String usersID = dataSnapshot.getKey();

                        Users model = new Users(usersID);
                        newPatientsList.add(model);
                    }

                    layoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(layoutManager);
                    userDisplayAdapter = new UserDisplayAdapter(getContext(),newPatientsList);
                    recyclerView.setAdapter(userDisplayAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}