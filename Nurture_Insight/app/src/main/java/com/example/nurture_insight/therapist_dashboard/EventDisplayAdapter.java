package com.example.nurture_insight.therapist_dashboard;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurture_insight.Model.events;
import com.example.nurture_insight.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EventDisplayAdapter extends RecyclerView.Adapter<EventDisplayAdapter.ViewHolder> {

    public static ArrayList<events> events;
    Context context;

    public EventDisplayAdapter (Context context, ArrayList<events> events){
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_event_display, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.cardImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        DatabaseReference eventRef = events.get(position).getEventRef();
        eventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String eventTitle, eventLocation, eventDateAndTime, eventHost, eventHostContact;
                    events eventObj = snapshot.getValue(events.class);
                    eventTitle = snapshot.child("title").getValue().toString();
                    eventLocation = snapshot.child("location").getValue().toString();
                    eventDateAndTime = snapshot.child("time").getValue().toString() + " " + snapshot.child("date").getValue().toString();
                    eventHost = eventObj.getConductor();
                    eventHostContact = eventObj.getContact();

                    Log.d("UNIQUENAME", "onDataChange: " + eventObj + eventTitle + eventLocation + eventHost + eventHostContact + eventDateAndTime);

                    holder.eachEventTitle.setText(eventTitle);
                    holder.eachEventLocation.setText(eventLocation);
                    holder.eachEventDate.setText("Event Date: " + eventDateAndTime);
                    holder.eachEventHost.setText("Host: " + eventHost);
                    holder.eachEventHostContact.setText("Contact: " + eventHostContact);

                    holder.eachEventCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Fragment fragment = new EventDetailsFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("position", position);
                            bundle.putString("id", snapshot.getKey());
                            fragment.setArguments(bundle);
                            AppCompatActivity activity = (AppCompatActivity) v.getContext();
                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragmentLayout, fragment).addToBackStack(null).commit();

                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView eachEventTitle, eachEventLocation, eachEventDate, eachEventHost, eachEventHostContact;
        CardView eachEventCard;
        ImageView cardImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eachEventTitle = itemView.findViewById(R.id.each_event_title);
            eachEventLocation = itemView.findViewById(R.id.each_event_location);
            eachEventDate = itemView.findViewById(R.id.each_event_dateAndTime);
            eachEventHost = itemView.findViewById(R.id.each_event_host);
            eachEventHostContact = itemView.findViewById(R.id.each_event_host_contact);
            eachEventCard = itemView.findViewById(R.id.each_event_card);
            cardImage = itemView.findViewById(R.id.event_background);
        }
    }
}
