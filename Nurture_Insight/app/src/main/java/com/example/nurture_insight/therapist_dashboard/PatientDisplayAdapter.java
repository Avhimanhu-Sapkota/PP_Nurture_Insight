package com.example.nurture_insight.therapist_dashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurture_insight.Model.Users;
import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.R;
import com.example.nurture_insight.habit_tracker.habit_tracker_home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PatientDisplayAdapter extends RecyclerView.Adapter<PatientDisplayAdapter.ViewHolder> {

    ArrayList<Users> users;
    Context context;

    public PatientDisplayAdapter (Context context, ArrayList<Users> users){
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.display_each_user, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(users.get(position).getUsersID());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userContact = "Contact at:\n" + snapshot.child("phoneNo").getValue().toString()
                        +"\n"+ snapshot.child("email").getValue().toString();

                holder.eachUserName.setText(snapshot.child("username").getValue().toString());
                holder.eachUserContact.setText(userContact);
                holder.eachUserCard.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        AlertDialog dialog = new AlertDialog.Builder(context).create();
                        dialog.setTitle("Nurture Insight - Remove a Patient");
                        dialog.setMessage("Do You want to remove this user from your patient's List?");
                        dialog.setCancelable(false);
                        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int buttonId) {

                            }
                        });
                        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int buttonId) {
                                HashMap<String, Object> deleteHash = new HashMap<>();
                                deleteHash.put("therapistID", "0000000000");
                                userRef.updateChildren(deleteHash);
                                users.remove(position);
                            }
                        });
                        dialog.setIcon(android.R.drawable.ic_dialog_alert);
                        dialog.show();

                        return true;
                    }
                });

                holder.eachUserCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment fragment = new PatientProfile();
                        Bundle bundle = new Bundle();
                        bundle.putString("patientId", snapshot.child("phoneNo").getValue().toString());
                        fragment.setArguments(bundle);
                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragmentLayout, fragment).addToBackStack(null).commit();

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView eachUserName, eachUserContact;
        CardView eachUserCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            eachUserName = itemView.findViewById(R.id.eachUser_Name);
            eachUserContact = itemView.findViewById(R.id.eachUser_contact);
            eachUserCard = itemView.findViewById(R.id.eachUser_cardView);
        }
    }
}
