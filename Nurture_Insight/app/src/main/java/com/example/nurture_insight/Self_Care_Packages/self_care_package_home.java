package com.example.nurture_insight.Self_Care_Packages;

import android.os.Bundle;
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

import com.example.nurture_insight.Home.HomeFragment;
import com.example.nurture_insight.Model.Self_Care_Packages;
import com.example.nurture_insight.R;

import java.util.ArrayList;

public class self_care_package_home extends Fragment {

    ImageView backButton;
    RecyclerView scp_home_recyclerView;
    ArrayList<Self_Care_Packages> self_care_packages;
    Self_Care_Packages_Adapter self_care_packages_adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_self_care_package_home, container, false);

        backButton = (ImageView) view.findViewById(R.id.scp_home_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment =  new HomeFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        scp_home_recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewSCPackages);
        Integer[] packageImages ={R.drawable.scp_meditation,
                R.drawable.scp_music,
                R.drawable.scp_yoga,
                R.drawable.scp_stories,
                R.drawable.scp_breathwork,
        };

        String[] packageName = {"Guided Meditation", "Relaxation Music", "Body and Mind Yoga", "Uplifting Stories", "Breathwork Exercises"};

        String[] packageDesc = {"Browse Nurture Insight - Guided Meditation. Sit comfortably or lie down, close your eyes and start your meditation. These meditations have been designed to uplift your mental well-being. Let\'s Meditate!",
                "Browse Nurture Insight - Relaxation Music and Enjoy listening to them. Savour the notes and change your mood within minutes. Happy Listening!",
                "Browse Nurture Insight - Body and Mind Yoga and follow the steps to get complete benefit of yoga that connects you to your mind and mental health. Enjoy doing Yoga!",
                "Browse Nurture Insight - Uplifting Stories. These stories will motivate you and help you to get over Mental Health Stigma. Most of them have Happy Endings. Let\'s recall those amazing childhood stories days!",
                "Browse Nurture Insight - BreathWork Exercises. These exercises will help you to focus on your breathing in order to calm you down and make you realize about living in present. Enjoy the Breathwork Exercises. Best of Luck! "};

        self_care_packages = new ArrayList<>();

        for (int index=0; index < packageImages.length; index++){
            Self_Care_Packages model = new Self_Care_Packages(packageImages[index], packageName[index], packageDesc[index]);
            self_care_packages.add(model);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        scp_home_recyclerView.setLayoutManager(layoutManager);
        self_care_packages_adapter = new Self_Care_Packages_Adapter(getContext(), self_care_packages);
        scp_home_recyclerView.setAdapter(self_care_packages_adapter);

        return view;
    }
}