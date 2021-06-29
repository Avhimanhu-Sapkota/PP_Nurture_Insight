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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurture_insight.Model.Habits;
import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class HabitsAdapter  extends RecyclerView.Adapter<HabitsAdapter.ViewHolder> {
    ArrayList<Habits> habits;
    Context context;
    String habit_title="default";

    public HabitsAdapter(Context context, ArrayList<Habits> habits){
        this.context = context;
        this.habits = habits;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_habit, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.eachHabitTitle.setImageResource(habits.get(position).getHabit_title());
        holder.eachHabitTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHabit(position);
            }
        });

    }

    private void addHabit(int position) {

        switch (position){
            case 0:
                habit_title = "Write a Journal";
                break;
            case 1:
                habit_title = "Talk to Someone";
                break;
            case 2:
                habit_title = "Deep Breathing";
                break;
            case 3:
                habit_title = "Savour a Meal";
                break;
            case 4:
                habit_title = "Stretch or Do Yoga";
                break;
            case 5:
                habit_title = "Immerse in Nature";
                break;
            case 6:
                habit_title = "Positive Self-Talk";
                break;
            case 7:
                habit_title = "Guided Meditation";
                break;
            case 8:
                habit_title = "Listen or Read a Book";
                break;
            case 9:
                habit_title = "Take a Mindful Break";
                break;
            case 10:
                habit_title = "Get Enough Sleep";
                break;
            case 11:
                habit_title = "Drink 6 Glasses of Water";
                break;
            case 12:
                habit_title = "Do something that makes me Happy";
                break;

        }

        DatabaseReference habitRef = FirebaseDatabase.getInstance().getReference()
                .child("Habit_Tracker").child(Prevalent.currentOnlineUser.getPhoneNo());

        habitRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(habit_title)){
                    new AlertDialog.Builder(context)
                            .setTitle("Nurture Insight - Add a New Habit")
                            .setMessage("You have already added this habit into your daily schedule")
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

                else{
                    HashMap<String, Object> habitMap = new HashMap<>();
                    habitMap.put("ID", 1);

                    AlertDialog dialog = new AlertDialog.Builder(context).create();
                    dialog.setTitle(context.getResources().getString(R.string.add_habit_title));
                    dialog.setMessage(context.getResources().getString(R.string.habit_add_msg));
                    dialog.setCancelable(false);
                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, "No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int buttonId) {

                        }
                    });
                    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int buttonId) {

                            habitRef.child(habit_title).updateChildren(habitMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Fragment fragment = null;
                                        fragment = new habit_tracker_home();

                                        FragmentTransaction transaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                                        transaction.replace(R.id.main_fragmentLayout, fragment );
                                        transaction.commit();

                                    }
                                    else{
                                        new AlertDialog.Builder(context)
                                                .setTitle(context.getResources().getString(R.string.add_habit_title))
                                                .setMessage("Habit could not be added to your schedule")
                                                .setNegativeButton(android.R.string.yes, null)
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();
                                    }
                                }
                            });


                        }
                    });
                    dialog.setIcon(android.R.drawable.ic_dialog_alert);
                    dialog.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public int getItemCount() {

        return habits.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView eachHabitTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eachHabitTitle = itemView.findViewById(R.id.each_habit_image);
        }
    }
}
