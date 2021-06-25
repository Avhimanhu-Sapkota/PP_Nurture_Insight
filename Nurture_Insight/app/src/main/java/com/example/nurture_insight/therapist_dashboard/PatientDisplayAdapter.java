package com.example.nurture_insight.therapist_dashboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurture_insight.Model.Users;
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

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userContact = "Contact at:\n" + snapshot.child("phoneNo").getValue().toString()
                        +"\n"+ snapshot.child("email").getValue().toString();

                holder.eachUserName.setText(snapshot.child("username").getValue().toString());
                holder.eachUserContact.setText(userContact);

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
