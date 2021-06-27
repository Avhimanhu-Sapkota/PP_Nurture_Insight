package com.example.nurture_insight.Home;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurture_insight.Home.ArticlesAdapter;
import com.example.nurture_insight.Home.BrowseTherapistFragment;
import com.example.nurture_insight.Model.Articles;
import com.example.nurture_insight.Model.Mood_Tracker;
import com.example.nurture_insight.Model.events;
import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.R;
import com.example.nurture_insight.instant_help.affirmations;
import com.example.nurture_insight.instant_help.breathing_control;
import com.example.nurture_insight.instant_help.calm_down_info_1;
import com.example.nurture_insight.instant_help.live_in_present;
import com.example.nurture_insight.instant_help.quotes;
import com.example.nurture_insight.therapist_dashboard.EventDisplayAdapter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    ImageView happyMood, goodMood, mehMood, sadMood, awfulMood;
    ImageView mainIcon1, mainIcon2, mainIcon3, mainIcon4, mainIcon5, mainIcon6;
    String userMood;
    CardView moodCard, moodHistoryCard;
    RecyclerView recyclerViewArticles, eventRecyclerView;
    ArrayList<Articles> articles;
    ArrayList<events> eventsList;
    EventDisplayAdapter eventDisplayAdapter;
    ArticlesAdapter articlesAdapter;
    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList barEntries;
    Button sosButton;
    RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        moodHistoryCard = (CardView) rootView.findViewById(R.id.main_cardView_moodGraph);
        moodCard = (CardView) rootView.findViewById(R.id.main_cardView_mood);
        happyMood = (ImageView) rootView.findViewById(R.id.main_imgMood_happy);
        goodMood = (ImageView) rootView.findViewById(R.id.main_imgMood_good);
        mehMood = (ImageView) rootView.findViewById(R.id.main_imgMood_meh);
        sadMood = (ImageView) rootView.findViewById(R.id.main_imgMood_sad);
        awfulMood = (ImageView) rootView.findViewById(R.id.main_imgMood_awful);
        barChart = (BarChart) rootView.findViewById(R.id.main_mood_barChart);
        sosButton = (Button) rootView.findViewById(R.id.main_button_sos);
        mainIcon4 = (ImageView) rootView.findViewById(R.id.main_icon_4);
        mainIcon1 = (ImageView) rootView.findViewById(R.id.main_icon_1);
        mainIcon2 = (ImageView) rootView.findViewById(R.id.main_icon_2);
        mainIcon3 = (ImageView) rootView.findViewById(R.id.main_icon_3);
        mainIcon5 = (ImageView) rootView.findViewById(R.id.main_icon_5);
        mainIcon6 = (ImageView) rootView.findViewById(R.id.main_icon_6);

        mainIcon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new BrowseTherapistFragment();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        mainIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new breathing_control();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        mainIcon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new live_in_present();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        mainIcon5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new quotes();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        mainIcon6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new affirmations();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        mainIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), calm_down_info_1.class);
                startActivity(intent);
            }
        });

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawGridLinesBehindData(false);
        xAxis.setDrawLabels(false);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(false);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false);

        moodTrackerDisplay();

        happyMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userMood="happy";
                recordUserMood(userMood);
            }
        });
        goodMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userMood="good";
                recordUserMood(userMood);
            }
        });
        mehMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userMood="meh";
                recordUserMood(userMood);
            }
        });
        sadMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userMood="sad";
                recordUserMood(userMood);
            }
        });
        awfulMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userMood="awful";
                recordUserMood(userMood);
            }
        });


        recyclerViewArticles = (RecyclerView) rootView.findViewById(R.id.recyclerViewArticles);
        Integer[] articlesToDisplay = {R.drawable.anger2,
                R.drawable.anxiety2,
                R.drawable.depression2,
                R.drawable.self_esteem2,
                R.drawable.stress2};


        articles = new ArrayList<>();
        for (int index=0; index<articlesToDisplay.length; index++){
            Articles model = new Articles(articlesToDisplay[index]);
            articles.add(model);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false
        );
        recyclerViewArticles.setLayoutManager((layoutManager));
        recyclerViewArticles.setItemAnimator(new DefaultItemAnimator());

        articlesAdapter = new ArticlesAdapter(getContext(), articles);
        recyclerViewArticles.setAdapter(articlesAdapter);

        eventRecyclerView = (RecyclerView) rootView.findViewById(R.id.home_event_recyclerView);
        eventsList = new ArrayList<>();
        loadEvents();

        return rootView;
    }

    private void loadEvents() {
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference()
                .child("events");

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventsList.clear();
                for (DataSnapshot newSnapshot: snapshot.getChildren()){
                    String userID = newSnapshot.getKey();

                    DatabaseReference eventRef = rootRef.child(userID);
                    eventRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for(DataSnapshot finalSnapshot: snapshot.getChildren()){
                                String eventID = finalSnapshot.getKey();
                                DatabaseReference finalEventRef = eventRef.child(eventID);

                                events model = new events(finalEventRef);
                                eventsList.add(model);

                                finalEventRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot finalSnapshot) {

                                        String saveCurrentDate;
                                        Calendar calForDate = Calendar.getInstance();
                                        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                                        saveCurrentDate = currentDate.format(calForDate.getTime());
                                        String eventDate = finalSnapshot.child("date").getValue().toString();

                                        try {
                                            Date currentDateD = currentDate.parse(saveCurrentDate);
                                            Date eventDateD = currentDate.parse(eventDate);
/*
                                            new AlertDialog.Builder(getContext())
                                                    .setTitle("HELLO")
                                                    .setMessage("The date is "+ currentDateD + "The Mood is " + eventDateD)
                                                    .setNegativeButton(android.R.string.no, null)
                                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                                    .show();*/

                                            if(currentDateD.compareTo(eventDateD) > 0){

                                                Log.d("UNIQUENAME1", "onDataChange: " + "NO");
                                            }
                                            else if (currentDateD.compareTo(eventDateD) < 0){

                                                Log.d("UNIQUENAME1", "onDataChange: " + "YES");
                                            }
                                            else{

                                                Log.d("UNIQUENAME1", "onDataChange: " + "EQUAL");
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        /*try {

                                            if(eventDateD.before(currentDateD)){


                                                events model = new events(finalEventRef);
                                                eventsList.add(model);
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }*/

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

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

    private void recordUserMood(String userMood) {

        String saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        final DatabaseReference moodTrackerRef = FirebaseDatabase.getInstance().getReference().child("Mood_Tracker");

        final HashMap<String, Object> moodMap = new HashMap<>();
        moodMap.put("moodDate", saveCurrentDate);
        moodMap.put("moodType", userMood);

        moodTrackerRef.child(Prevalent.currentOnlineUser.getPhoneNo()).child(saveCurrentDate)
                .updateChildren(moodMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            moodTrackerDisplay();
                            Toast.makeText(getContext(),getResources().getString(R.string.mood_message), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void moodTrackerDisplay(){

        String saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());


        final DatabaseReference moodTrackerRef = FirebaseDatabase.getInstance()
                .getReference().child("Mood_Tracker")
                .child(Prevalent.currentOnlineUser.getPhoneNo());

        moodTrackerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.hasChild(saveCurrentDate)){
                    moodCard.setVisibility(View.GONE);
                    try {
                        getMoodEntriesForGraph();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    moodHistoryCard.setVisibility(View.VISIBLE);

                }
                else{
                    moodCard.setVisibility(View.VISIBLE);
                    moodHistoryCard.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getMoodEntriesForGraph() throws ParseException {
        barEntries = new ArrayList<>();

        String saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        Date myCurrentDate = currentDate.parse(saveCurrentDate);


        final DatabaseReference moodTrackerRef = FirebaseDatabase.getInstance()
                .getReference().child("Mood_Tracker")
                .child(Prevalent.currentOnlineUser.getPhoneNo());


        moodTrackerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int index = 1;

                if(snapshot.hasChildren()){
                    for(DataSnapshot myDataSnapshot: snapshot.getChildren()){
                        Mood_Tracker dataPoint = myDataSnapshot.getValue(Mood_Tracker.class);
                        String eachDateInString = dataPoint.getMoodDate();
                        String currentMood = dataPoint.getMoodType();

                        try {
                            Date eachDate = currentDate.parse(eachDateInString);
                            if((myCurrentDate.getTime() - eachDate.getTime()) < 604800000){
                                int moodNumber = getMoodNumber(currentMood);

                                barEntries.add(new BarEntry(index, moodNumber));
                                index++;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }

                    showBarGraph(barEntries);
                }
                else{
                    barChart.clear();
                    barChart.invalidate();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showBarGraph(ArrayList barEntries) {

        barDataSet = new BarDataSet(barEntries, getResources().getString(R.string.mood_tracker_msg));
        barData = new BarData(barDataSet);
        barChart.setNoDataTextColor(getResources().getColor(R.color.ni_blue));
        barChart.setGridBackgroundColor(getResources().getColor(R.color.ni_lightBlue));
        barChart.setData(barData);
        barData.setBarWidth(0.3f);
        barDataSet.setColor(getResources().getColor(R.color.ni_blue));
        barDataSet.setValueTextColor(getResources().getColor(R.color.ni_blue));
        barDataSet.setValueTextSize(15f);
        barChart.setDescription(null);


        for (IDataSet set : barChart.getData().getDataSets())
            set.setDrawValues(!set.isDrawValuesEnabled());

        barChart.animateXY(2000, 2000);
        barChart.invalidate();

    }

    private int getMoodNumber(String currentMood) {
        switch (currentMood) {
            case "happy":
                return 5;
            case "good":
                return 4;
            case "meh":
                return 3;
            case "sad":
                return 2;
            default:
                return 1;
        }
    }
}