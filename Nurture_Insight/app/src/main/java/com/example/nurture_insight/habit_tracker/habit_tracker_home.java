package com.example.nurture_insight.habit_tracker;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurture_insight.Model.Habits;
import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class habit_tracker_home extends Fragment {

    Button add_habit_button;
    RecyclerView habitTrackerRV, trackedHabitsRV;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Habits> loadHabitsList, trackedHabitList;
    AddedHabitsAdapter addedHabitsAdapter;
    TrackedHabitsAdapter trackedHabitsAdapter;

    CalendarView trackerCalendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_habit_tracker_home, container, false);
        trackedHabitsRV = (RecyclerView) view.findViewById(R.id.trackedHabits_recyclerView);
        habitTrackerRV = (RecyclerView) view.findViewById(R.id.habits_recyclerView);
        add_habit_button = (Button) view.findViewById(R.id.add_habit_button);
        add_habit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new add_new_habit();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        loadHabitsList = new ArrayList<>();
        getHabits();

        trackerCalendar = (CalendarView) view.findViewById(R.id.habit_tracker_calendar);
        trackerCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar clickedCalendar = Calendar.getInstance();
                Calendar calForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                clickedCalendar.set(year, month, dayOfMonth);
                String clickedDate = currentDate.format(clickedCalendar.getTime());
                trackedHabitList = new ArrayList<>();

                displayTrackedHabits(clickedDate);
            }
        });

        return view;
    }

    private void displayTrackedHabits(String clickedDate) {

        final DatabaseReference habitRef = FirebaseDatabase.getInstance().getReference()
                .child("Habit_Tracker").child(Prevalent.currentOnlineUser.getPhoneNo());

        habitRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                trackedHabitList.clear();
                for(DataSnapshot habitSnap: snapshot.getChildren()){
                    String name = habitSnap.getKey();

                    habitRef.child(name).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(clickedDate)){
                                Habits model = new Habits(name);
                                trackedHabitList.add(model);
                            }

                            for(int i=0; i<trackedHabitList.size(); i++){
                                Log.d("UNIQUENAME", "onDataChange: " + trackedHabitList.get(i));
                            }

                            layoutManager = new LinearLayoutManager(getContext());
                            trackedHabitsRV.setLayoutManager(layoutManager);
                            trackedHabitsAdapter = new TrackedHabitsAdapter(getContext(), trackedHabitList);
                            trackedHabitsRV.setAdapter(trackedHabitsAdapter);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void getHabits(){
        loadHabitsList.clear();
        final DatabaseReference habitRef = FirebaseDatabase.getInstance().getReference()
                .child("Habit_Tracker").child(Prevalent.currentOnlineUser.getPhoneNo());

        habitRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                loadHabitsList.clear();
                for (DataSnapshot habitSnapshot: snapshot.getChildren()){
                    String name = habitSnapshot.getKey();

                    String saveCurrentDate;
                    Calendar calForDate = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                    saveCurrentDate = currentDate.format(calForDate.getTime());

                    habitRef.child(name).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(saveCurrentDate)){

                            }
                            else {
                                Habits model = new Habits(name);
                                loadHabitsList.add(model);
                            }

                            layoutManager = new LinearLayoutManager(getContext());
                            habitTrackerRV.setLayoutManager(layoutManager);
                            addedHabitsAdapter = new AddedHabitsAdapter(getContext(), loadHabitsList);
                            habitTrackerRV.setAdapter(addedHabitsAdapter);
                        }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}