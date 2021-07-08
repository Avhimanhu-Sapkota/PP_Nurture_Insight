package com.example.nurture_insight.therapist_dashboard;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class add_mental_health_events extends Fragment {

    ImageView backButton;
    TextView pickDate, pickTime;
    Button createEventButton;
    DatePickerDialog.OnDateSetListener mDateListener;
    TimePickerDialog.OnTimeSetListener mTimeListener;
    EditText eventTitle, eventDesc, eventLocation;
    String pickedTimeStr, pickedDateStr;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_mental_health_events, container, false);

        eventTitle = (EditText) view.findViewById(R.id.add_event_Form_answer1);
        eventLocation = (EditText) view.findViewById(R.id.add_event_Form_answer2);
        eventDesc = (EditText) view.findViewById(R.id.add_event_Form_answer3);
        pickDate = (TextView) view.findViewById(R.id.td_event_date);
        pickTime = (TextView) view.findViewById(R.id.td_event_time);
        createEventButton = (Button) view.findViewById(R.id.td_create_event);
        backButton = (ImageView) view.findViewById(R.id.add_event_backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment =  new therapistDashboard();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment);
                transaction.commit();
            }
        });
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        mDateListener, year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar pickedDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                pickedDate.set(year, month, dayOfMonth);
                pickedDateStr  = currentDate.format(pickedDate.getTime());
                pickDate.setText(pickedDateStr);
            }
        };

        pickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int hour = cal.get(Calendar.HOUR);
                int minutes = cal.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(
                        getContext(), android.R.style.Theme_Holo_Dialog_MinWidth,
                        mTimeListener, hour, minutes, false
                );

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar pickedTime = Calendar.getInstance();

                pickedTimeStr  = hourOfDay + ":" + minute;
                pickTime.setText(pickedTimeStr);
            }
        };

        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadEvent();
            }
        });

        return view;
    }

    private void uploadEvent() {

        String inputTitle = eventTitle.getText().toString();
        String inputLocation = eventLocation.getText().toString();
        String inputDesc = eventDesc.getText().toString();

        if(TextUtils.isEmpty(inputTitle)){
            eventTitle.setError("You must provided the title of the event");
        }
        else if(TextUtils.isEmpty(inputLocation)){
            eventLocation.setError("You must provided the location or online link of the event");
        }
        else if(TextUtils.isEmpty(inputDesc)){
            eventDesc.setError("You must provided the details of the event");
        }
        else if(TextUtils.isEmpty(pickedDateStr)){
            pickDate.setError("You must provided the date of the event");
        }
        else if(TextUtils.isEmpty(pickedTimeStr)){
            pickTime.setError("You must provided the date of the event");
        }
        else{
            pickDate.setText(pickedDateStr);
            pickTime.setText(pickedTimeStr);

            HashMap<String, Object> eventMap = new HashMap<>();
            eventMap.put("title", inputTitle);
            eventMap.put("location", inputLocation);
            eventMap.put("description", inputDesc);
            eventMap.put("date", pickedDateStr);
            eventMap.put("time", pickedTimeStr);
            eventMap.put("conductor", Prevalent.currentOnlineUser.getUsername());
            eventMap.put("contact", Prevalent.currentOnlineUser.getPhoneNo());
            eventMap.put("eventStatus", "notCompleted");

            String saveCurrentDate, saveCurrentTime;
            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
            saveCurrentDate = currentDate.format(calForDate.getTime());
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
            saveCurrentTime = currentTime.format(calForDate.getTime());

            final DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("events")
                    .child(Prevalent.currentOnlineUser.getPhoneNo());

            eventRef.child(saveCurrentDate+saveCurrentTime).updateChildren(eventMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        new AlertDialog.Builder(getContext())
                                .setTitle(getResources().getString(R.string.td_create_event_title))
                                .setMessage(getResources().getString(R.string.td_create_event_success))
                                .setNegativeButton(android.R.string.yes, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

                        Fragment fragment =  new therapistDashboard();

                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.main_fragmentLayout, fragment );
                        transaction.commit();
                    }
                    else{
                        new AlertDialog.Builder(getContext())
                                .setTitle(getResources().getString(R.string.td_create_event_title))
                                .setMessage(getResources().getString(R.string.td_create_event_failed))
                                .setNegativeButton(android.R.string.yes, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
            });


        }

    }
}