package com.example.nurture_insight.habit_tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurture_insight.Model.Habits;
import com.example.nurture_insight.R;

import java.util.ArrayList;

public class TrackedHabitsAdapter extends RecyclerView.Adapter<TrackedHabitsAdapter.ViewHolder> {

    Context context;
    ArrayList<Habits> habits;

    public TrackedHabitsAdapter(Context context, ArrayList<Habits> habits){
        this.context = context;
        this.habits = habits;
    }

    @NonNull
    @Override
    public TrackedHabitsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tracked_habit_display, parent,false);

        return new TrackedHabitsAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull TrackedHabitsAdapter.ViewHolder holder, int position) {

        String habit_name = habits.get(holder.getAdapterPosition()).getHabits_name();
        holder.eachHabitTitle.setText(habit_name);

    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView eachHabitTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eachHabitTitle = itemView.findViewById(R.id.each_tracked_habit_title);
        }
    }
}
