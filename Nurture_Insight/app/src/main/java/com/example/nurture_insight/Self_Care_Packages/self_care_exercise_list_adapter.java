package com.example.nurture_insight.Self_Care_Packages;

import android.app.AlertDialog;
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

import com.example.nurture_insight.Model.Self_care;
import com.example.nurture_insight.R;
import com.example.nurture_insight.therapist_dashboard.EventDetailsFragment;
import com.example.nurture_insight.therapist_dashboard.EventDisplayAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class self_care_exercise_list_adapter  extends RecyclerView.Adapter<self_care_exercise_list_adapter.ViewHolder> {

    ArrayList<Self_care> self_care;
    Context context;
    Integer[] images;

    public self_care_exercise_list_adapter(Context context, ArrayList<Self_care> self_care, Integer[] images) {
        this.self_care = self_care;
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_sc_exercise, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DatabaseReference requiredRef = self_care.get(position).getEachRef();

        requiredRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Self_care self_care_obj = snapshot.getValue(Self_care.class);

                    final int random = new Random().nextInt((9 - 1) + 1) + 1;
                    holder.eachImage.setImageResource(images[random]);

                    holder.eachImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    holder.eachTitle.setText(self_care_obj.getName());
                    holder.eachDuration.setText("Duration: " + self_care_obj.getDuration());

                    holder.eachCardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Fragment fragment = new self_care_exercise();
                            Bundle bundle = new Bundle();
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
        return self_care.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView eachCardView;
        TextView eachTitle, eachCategory, eachDuration;
        ImageView eachImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eachCardView = itemView.findViewById(R.id.self_care_package_list_cardView);
            eachTitle = itemView.findViewById(R.id.eachExercise_title);
            eachDuration = itemView.findViewById(R.id.eachExercise_duration);
            eachImage = itemView.findViewById(R.id.eachExercise_image);

        }
    }
}
