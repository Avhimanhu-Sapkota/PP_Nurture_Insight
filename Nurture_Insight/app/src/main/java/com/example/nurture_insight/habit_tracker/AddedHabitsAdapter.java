package com.example.nurture_insight.habit_tracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurture_insight.Model.Habits;
import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class AddedHabitsAdapter extends RecyclerView.Adapter<AddedHabitsAdapter.ViewHolder> {

    Context context;
    ArrayList<Habits> habits;

    public AddedHabitsAdapter(Context context, ArrayList<Habits> habits){
        this.context = context;
        this.habits = habits;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_loaded_habit, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String habit_name = habits.get(position).getHabits_name();

                    holder.eachHabitTitle.setText(habit_name);
                    holder.background.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            trackHabit(holder, habit_name);
                        }
                    });



    }

    private void trackHabit(ViewHolder holder, String habit_name) {
        String saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        DatabaseReference habitRef = FirebaseDatabase.getInstance().getReference()
                .child("Habit_Tracker").child(Prevalent.currentOnlineUser.getPhoneNo()).child(habit_name);

        HashMap<String, Object> habitMap = new HashMap<>();
        habitMap.put("status", "done");

        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle(context.getResources().getString(R.string.add_habit_title));
        dialog.setMessage(context.getResources().getString(R.string.habit_completed_msg));
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {

            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {

                habitRef.child(saveCurrentDate).updateChildren(habitMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    holder.background.setImageResource(R.drawable.habit_1);
                                }
                            }
                        });
            }
        });
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView eachHabitTitle;
        ImageView background;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eachHabitTitle = itemView.findViewById(R.id.each_habit_title);
            background = itemView.findViewById(R.id.habit_background);
        }
    }
}
