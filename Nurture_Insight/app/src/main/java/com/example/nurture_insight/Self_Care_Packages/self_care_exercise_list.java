package com.example.nurture_insight.Self_Care_Packages;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurture_insight.Home.HomeFragment;
import com.example.nurture_insight.Model.Self_care;
import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.R;
import com.example.nurture_insight.therapist_dashboard.EventDisplayAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class self_care_exercise_list extends Fragment {

    ImageView backButton;
    String packageType;
    RecyclerView packageListRecyclerView;
    TextView packageListTitle, packageListDesc;
    ArrayList<Self_care> self_care_arrayList;
    RecyclerView.LayoutManager layoutManager;
    self_care_exercise_list_adapter scelAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_self_care_exercise_list, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null){
            packageType = bundle.getString("type", " ");
        }
        String packageListTitleStr = "Nurture Insight - Self Care\n" + packageType;

        backButton = (ImageView) view.findViewById(R.id.sce_list_backButton);
        packageListDesc = (TextView) view.findViewById(R.id.sce_list_message);
        packageListTitle = (TextView) view.findViewById(R.id.sce_list_title);
        packageListRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewSCEList);

        String packageListDescStr = getPackageListMessage();

        packageListTitle.setText(packageListTitleStr);
        packageListDesc.setText(packageListDescStr);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment =  new self_care_package_home();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        self_care_arrayList = new ArrayList<>();
        loadSelfCareExercises();

        return view;
    }

    private void loadSelfCareExercises() {
        String packageID = getPackageListTitle();

        final DatabaseReference exerciseRef = FirebaseDatabase.getInstance().getReference()
                .child("Self_care").child(packageID);

        exerciseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                self_care_arrayList.clear();
                for (DataSnapshot newSnap: snapshot.getChildren()){
                    String eachID = newSnap.getKey();
                    DatabaseReference eachRef = exerciseRef.child(eachID);

                    Self_care model = new Self_care(eachRef);
                    self_care_arrayList.add(model);

                }

                layoutManager = new LinearLayoutManager(getContext());
                packageListRecyclerView.setLayoutManager(layoutManager);
                scelAdapter = new self_care_exercise_list_adapter(getContext(),self_care_arrayList);
                packageListRecyclerView.setAdapter(scelAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private String getPackageListTitle() {
        String title = " ";
        switch (packageType){
            case "Guided Meditation":
                title = "meditation";
                break;

            case "Relaxation Music":
                title = "music";
                break;

            case "Body and Mind Yoga":
                title = "yoga";
                break;

            case "Uplifting Stories":
                title = "stories";
                break;

            case "Breathwork Exercises":
                title = "breathwork";
                break;
        }

        return title;
    }

    private String getPackageListMessage() {
        String message = " ";
        switch (packageType){
            case "Guided Meditation":
                message = "Browse Nurture Insight - Guided Meditation. Sit comfortably or lie down, close your eyes and start your meditation. These meditations have been designed to uplift your mental well-being. Let\'s Meditate!";
                break;

            case "Relaxation Music":
                message = "Browse Nurture Insight - Relaxation Music and Enjoy listening to them. Savour the notes and change your mood within minutes. Happy Listening!";
                break;

            case "Body and Mind Yoga":
                message = "Browse Nurture Insight - Body and Mind Yoga and follow the steps to get complete benefit of yoga that connects you to your mind and mental health. Enjoy doing Yoga!";
                break;

            case "Uplifting Stories":
                message = "Browse Nurture Insight - Uplifting Stories. These stories will motivate you and help you to get over Mental Health Stigma. Most of them have Happy Endings. Let\'s recall those amazing childhood stories days!";
                break;

            case "Breathwork Exercises":
                message = "Browse Nurture Insight - BreathWork Exercises. These exercises will help you to focus on your breathing in order to calm you down and make you realize about living in present. Enjoy the Breathwork Exercises. Best of Luck! ";
            break;
        }

        return message;
    }
}