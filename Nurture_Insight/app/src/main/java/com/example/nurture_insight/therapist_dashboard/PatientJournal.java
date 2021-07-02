package com.example.nurture_insight.therapist_dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.example.nurture_insight.Home.HomeFragment;
import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.R;
import com.example.nurture_insight.journal.journal_display;
import com.example.nurture_insight.journal.journaling_form;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PatientJournal extends Fragment {

    TextView journal_title;
    CalendarView journal_calendar;
    ImageView backButton;
    String patientID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_patient_journal, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null){
            patientID = bundle.getString("patientId", "");
        }

        journal_title = (TextView) view.findViewById(R.id.patient_journal_title);
        journal_calendar = (CalendarView) view.findViewById(R.id.patient_journal_calendar);
        backButton = (ImageView) view.findViewById(R.id.patient_journal_backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new PatientProfile();
                Bundle bundle = new Bundle();
                bundle.putString("patientId", patientID);
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        String journal_title_name =  patientID + "\'s Journal";
        journal_title.setText(journal_title_name);

        journal_calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                Calendar clickedCalendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                clickedCalendar.set(year, month, dayOfMonth);

                String clickedDate = currentDate.format(clickedCalendar.getTime());

                Fragment fragment = new patient_journal_display();
                Bundle bundle = new Bundle();
                bundle.putString("clickedDate", clickedDate);
                bundle.putString("patientID", patientID);
                fragment.setArguments(bundle);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();

            }
        });

        return view;
    }
}