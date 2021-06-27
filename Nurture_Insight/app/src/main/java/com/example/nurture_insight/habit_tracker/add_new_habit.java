package com.example.nurture_insight.habit_tracker;

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

import com.example.nurture_insight.Model.Habits;
import com.example.nurture_insight.R;

import java.util.ArrayList;

public class add_new_habit extends Fragment {

    RecyclerView rv_new_habit;
    ArrayList<Habits> habitsArrayList;
    HabitsAdapter habitsAdapter;
    RecyclerView.LayoutManager layoutManager;
    ImageView backButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_habit, container, false);

        rv_new_habit = (RecyclerView) view.findViewById(R.id.new_habits_recyclerView);
        Integer[] habit_titles =  {R.drawable.habit_1,
                R.drawable.habit_2,
                R.drawable.habit_3,
                R.drawable.habit_4,
                R.drawable.habit_5,
                R.drawable.habit_6,
                R.drawable.habit_7,
                R.drawable.habit_8,
                R.drawable.habit_9,
                R.drawable.habit_10,
                R.drawable.habit_11,
                R.drawable.habit_12,
                R.drawable.habit_13};

        habitsArrayList = new ArrayList<>();
        for (int index=0; index < habit_titles.length; index++){
            Habits model = new Habits(habit_titles[index]);
            habitsArrayList.add(model);
        }


        layoutManager = new LinearLayoutManager(getContext());
        rv_new_habit.setLayoutManager(layoutManager);


        habitsAdapter = new HabitsAdapter(getContext(), habitsArrayList);
        rv_new_habit.setAdapter(habitsAdapter);

        backButton = (ImageView) view.findViewById(R.id.add_habit_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new habit_tracker_home();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        return view;
    }
}