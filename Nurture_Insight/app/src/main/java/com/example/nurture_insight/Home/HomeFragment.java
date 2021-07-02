package com.example.nurture_insight.Home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurture_insight.Model.Articles;
import com.example.nurture_insight.Model.Mood_Tracker;
import com.example.nurture_insight.Model.Self_care;
import com.example.nurture_insight.Model.events;
import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.R;
import com.example.nurture_insight.Self_Care_Packages.self_care_exercise_list_adapter;
import com.example.nurture_insight.instant_help.affirmations;
import com.example.nurture_insight.instant_help.breathing_control;
import com.example.nurture_insight.instant_help.calm_down_info;
import com.example.nurture_insight.instant_help.live_in_present;
import com.example.nurture_insight.instant_help.quotes;
import com.example.nurture_insight.journal.JournalFragment;
import com.example.nurture_insight.therapist_dashboard.EventDisplayAdapter;
import com.example.nurture_insight.assessment.weekly_assessment;
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

    private static final int PERMISSION_CODE = 101;
    ImageView happyMood, goodMood, mehMood, sadMood, awfulMood, moodBackground;
    ImageView mainIcon1, mainIcon2, mainIcon3, mainIcon4, mainIcon5, mainIcon6, mainIcon7, mainIcon8;
    String userMood, saveCurrentDate, dayOfTheWeek;
    CardView moodCard, moodHistoryCard;
    RecyclerView recyclerViewArticles, eventRecyclerView, packageListRecyclerView;
    self_care_exercise_list_adapter scelAdapter;
    ArrayList<Articles> articles;
    public static ArrayList<events> eventsList;
    ArrayList<Self_care> self_care_arrayList;
    EventDisplayAdapter eventDisplayAdapter;
    ArticlesAdapter articlesAdapter;
    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList barEntries;
    Button sosButton;
    RecyclerView.LayoutManager layoutManager;
    Integer[] imageViewList;
    private boolean permissionGranted;
    LinearLayout homeEventLinearLayout;


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
        mainIcon7 = (ImageView) rootView.findViewById(R.id.main_icon_7);
        mainIcon8 = (ImageView) rootView.findViewById(R.id.main_icon_8);
        sosButton = (Button) rootView.findViewById(R.id.main_button_sos);
        homeEventLinearLayout = (LinearLayout) rootView.findViewById(R.id.home_event_linearLayout);
        moodBackground = (ImageView) rootView.findViewById(R.id.main_card_imgView_bg);
        moodBackground.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("phoneNo", getContext().MODE_PRIVATE);
                if(sharedPreferences.getAll().isEmpty()){
                    Fragment fragment = new sosDetailsFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragmentLayout, fragment );
                    transaction.commit();
                }else{
                    sosEmergency(sharedPreferences);
                }

            }
        });

        mainIcon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new calm_down_info();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        mainIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment =  new breathing_control();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        mainIcon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment =  new live_in_present();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        mainIcon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new BrowseTherapistFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        mainIcon5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment =  new quotes();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        mainIcon6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment =  new affirmations();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        mainIcon7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new JournalFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        mainIcon8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAssessment();
            }
        });

        requestPermission();

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

        packageListRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViewSelfCare);
        self_care_arrayList = new ArrayList<>();

        imageViewList = new Integer[]{R.drawable.each_exercise_bg_1,
                R.drawable.each_exercise_bg_2,
                R.drawable.each_exercise_bg_3,
                R.drawable.each_exercise_bg_4,
                R.drawable.each_exercise_bg_5,
                R.drawable.each_exercise_bg_6,
                R.drawable.each_exercise_bg_7,
                R.drawable.each_exercise_bg_8,
                R.drawable.each_exercise_bg_9,
                R.drawable.each_exercise_bg_10};

        loadSelfCareExercises();

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

    private void loadSelfCareExercises() {
        final DatabaseReference packageRef = FirebaseDatabase.getInstance().getReference()
                .child("Self_care");

        packageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot newSnap: snapshot.getChildren()){
                    String exerciseID = newSnap.getKey();
                    DatabaseReference exerciseRef = packageRef.child(exerciseID);

                    exerciseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot newSnapshot) {
                            for (DataSnapshot finalSnap: newSnapshot.getChildren()){
                                String eachID = finalSnap.getKey();
                                DatabaseReference eachRef = exerciseRef.child(eachID);

                                eachRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot eachSnapshot) {
                                        if (eachSnapshot.child("category").getValue().toString().equals(Prevalent.currentOnlineUser.getCategory())){

                                            Self_care model = new Self_care(eachRef);
                                            self_care_arrayList.add(model);

                                            GridLayoutManager layoutManager1 =
                                                    new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false);

                                            packageListRecyclerView.setLayoutManager(layoutManager1);
                                            scelAdapter = new self_care_exercise_list_adapter(getContext(),self_care_arrayList, imageViewList);
                                            packageListRecyclerView.setAdapter(scelAdapter);

                                        }
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkAssessment() {
        DatabaseReference assessmentRef = FirebaseDatabase.getInstance().getReference().child("Assessment")
                .child(Prevalent.currentOnlineUser.getPhoneNo());

        assessmentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(saveCurrentDate)){
                    new AlertDialog.Builder(getContext())
                            .setTitle(getResources().getString(R.string.assessment_title))
                            .setMessage("Hey! You are doing great! You have already taken the test for the day! ")
                            .setNegativeButton(android.R.string.yes, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                else{
                    Fragment fragment = new weekly_assessment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragmentLayout, fragment );
                    transaction.commit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sosEmergency(SharedPreferences sharedPreferences) {
        String phoneNo = sharedPreferences.getString("phoneNo", " ");
        String userName = Prevalent.currentOnlineUser.getUsername();


        AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.setTitle(getResources().getString(R.string.sosSendTitle));
        dialog.setMessage(getResources().getString(R.string.sosSendMessage));
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {

            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(Prevalent.currentOnlineUser.getPhoneNo(), "", "Hey! I am " + userName + " (" + phoneNo + ") " + getResources().getString(R.string.SosMessage2), null, null);

            }
        });
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.show();

    }

    public void requestPermission(){
        String[] permission={Manifest.permission.SEND_SMS};
        if(ContextCompat.checkSelfPermission(getContext().getApplicationContext(),Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED){

        }else{
            ActivityCompat.requestPermissions(getActivity(),permission,PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISSION_CODE){
            if(permissions[0].equals(Manifest.permission.SEND_SMS)&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                permissionGranted=true;
            }
        }
    }

    private void loadEvents() {
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference()
                .child("events");

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()){
                    homeEventLinearLayout.setVisibility(View.VISIBLE);

                    eventsList.clear();
                    for (DataSnapshot newSnapshot: snapshot.getChildren()){
                        String userID = newSnapshot.getKey();

                        DatabaseReference eventRef = rootRef.child(userID);
                        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                for(DataSnapshot finalSnapshot: snapshot.getChildren()){
                                    String eventID = finalSnapshot.getKey();
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
                }
                else{
                    homeEventLinearLayout.setVisibility(View.GONE);
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

        if(!(TextUtils.isEmpty(userMood))){
            moodMap.put("moodType", userMood);
        }

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

        final DatabaseReference moodTrackerRef = FirebaseDatabase.getInstance()
                .getReference().child("Mood_Tracker")
                .child(Prevalent.currentOnlineUser.getPhoneNo());

        moodTrackerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                barChart.clear();
                barChart.invalidate();
                if(snapshot.hasChild(saveCurrentDate)){
                    moodHistoryCard.setVisibility(View.VISIBLE);
                    moodCard.setVisibility(View.GONE);
                }
                else{
                    moodCard.setVisibility(View.VISIBLE);
                    moodHistoryCard.setVisibility(View.GONE);
                }

                try {
                    getMoodEntriesForGraph();
                } catch (ParseException e) {
                    e.printStackTrace();
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


        moodTrackerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int index = 1;

                if(snapshot.hasChildren()){
                    for(DataSnapshot myDataSnapshot: snapshot.getChildren()){
                        Mood_Tracker dataPoint = myDataSnapshot.getValue(Mood_Tracker.class);
                        String eachDateInString = myDataSnapshot.child("moodDate").getValue().toString();
                        String currentMood = myDataSnapshot.child("moodType").getValue().toString();

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
        barDataSet.setValueTextSize(15f);
        barChart.setDescription(null);
        for (IDataSet set : barChart.getData().getDataSets())
            set.setDrawValues(!set.isDrawValuesEnabled());

        barChart.animateXY(1000, 1000);
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