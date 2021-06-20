package com.example.nurture_insight;

import android.os.Bundle;
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

import com.example.nurture_insight.Model.Mood_Tracker;
import com.example.nurture_insight.Prevalent.Prevalent;
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
    String userMood;
    CardView moodCard, moodHistoryCard;

    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList barEntries;
    Button sosButton;

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

        return rootView;
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

        barChart.setData(barData);
        barData.setBarWidth(0.3f);
        barDataSet.setColor(getResources().getColor(R.color.ni_blue));
        barDataSet.setValueTextColor(getResources().getColor(R.color.ni_blue));
        barDataSet.setValueTextSize(12f);
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