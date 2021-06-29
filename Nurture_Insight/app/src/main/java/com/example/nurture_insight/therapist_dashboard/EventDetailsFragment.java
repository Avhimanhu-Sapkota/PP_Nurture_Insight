package com.example.nurture_insight.therapist_dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.example.nurture_insight.Home.HomeFragment;
import com.example.nurture_insight.Model.events;
import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.R;
import com.example.nurture_insight.therapist_dashboard.EventDisplayAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventDetailsFragment extends Fragment {

    int position;
    String id;
    ImageView backButton;
    Button deleteEventButton;
    TextView eachEventTitle, eachEventLocation, eachEventDate, eachEventDesc, eachEventHost, eachEventHostContact;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null){
            position = bundle.getInt("position");
            id = bundle.getString("id", "");
        }

        backButton = (ImageView) view.findViewById(R.id.eventDetails_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment =  new HomeFragment();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        eachEventTitle = view.findViewById(R.id.eventDetails_title);
        eachEventLocation = view.findViewById(R.id.eventDetails_location);
        eachEventDate = view.findViewById(R.id.eventDetails_date);
        eachEventDesc = view.findViewById(R.id.eventDetails_description);
        eachEventHost = view.findViewById(R.id.eventDetails_host);
        eachEventHostContact = view.findViewById(R.id.eventDetails_host_contact);
        deleteEventButton = view.findViewById(R.id.eventDetails_delete_event);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("events");
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot userSnap: snapshot.getChildren()){
                    if (userSnap.hasChild(id)){
                        DatabaseReference eventRef = userSnap.child(id).getRef();

                        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String eventTitle, eventLocation, eventDateAndTime, eventDesc, eventHost, eventHostContact;
                                events eventObj = snapshot.getValue(events.class);
                                eventTitle = snapshot.child("title").getValue().toString();
                                eventLocation = snapshot.child("location").getValue().toString();
                                eventDateAndTime = snapshot.child("date").getValue().toString() + " at " + snapshot.child("time").getValue().toString();
                                eventDesc = snapshot.child("description").getValue().toString();
                                eventHost = eventObj.getConductor();
                                eventHostContact = eventObj.getContact();

                                eachEventTitle.setText(eventTitle);
                                eachEventLocation.setText(eventLocation);
                                eachEventDate.setText("Event Date: " + eventDateAndTime);
                                eachEventHost.setText("Host: " + eventHost);
                                eachEventHostContact.setText("Contact: " + eventHostContact);
                                eachEventDesc.setText("Event Details: " + eventDesc);

                                if(Prevalent.currentOnlineUser.getPhoneNo().equals(eventHostContact)){
                                    deleteEventButton.setVisibility(View.VISIBLE);
                                }
                                else{
                                    deleteEventButton.setVisibility(View.GONE);
                                }

                                deleteEventButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        eventRef.removeValue();
                                        EventDisplayAdapter.events.remove(eventRef);
                                        HomeFragment.eventsList.remove(eventRef);

                                        Fragment fragment =  new HomeFragment();
                                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                        transaction.replace(R.id.main_fragmentLayout, fragment );
                                        transaction.commit();
                                    }
                                });

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}