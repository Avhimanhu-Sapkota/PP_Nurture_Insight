package com.example.nurture_insight.therapist_dashboard;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurture_insight.Model.Users;
import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.R;
import com.example.nurture_insight.habit_tracker.habit_tracker_home;
import com.example.nurture_insight.login_signup.LoginActivity;
import com.example.nurture_insight.login_signup.otpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class UserDisplayAdapter extends RecyclerView.Adapter<UserDisplayAdapter.ViewHolder>  {

    ArrayList<Users> users;
    Context context;
    String phoneNo;

    ProgressDialog loading;

    public UserDisplayAdapter (Context context, ArrayList<Users> users){
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

                holder.eachUserCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addUserAsPatient(userRef);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void addUserAsPatient(DatabaseReference userRef) {

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                phoneNo = snapshot.child("phoneNo").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle(context.getResources().getString(R.string.therapist_dashboard_add_user));
        dialog.setMessage(context.getResources().getString(R.string.tdAddUser_Question));
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {

            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {
                loading = new ProgressDialog(context);
                loading.setTitle("Nurture Insight - User Verification");
                loading.setMessage(context.getString(R.string.signUp_loading_message));
                loading.setCanceledOnTouchOutside(false);
                loading.show();

                sendOtpCode(phoneNo);
            }
        });
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.show();

    }


    private void sendOtpCode(String phoneNo) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+977" + phoneNo, 60, TimeUnit.SECONDS,
                (AppCompatActivity)context  , new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                        Intent intent = new Intent(context, otpActivity.class);
                        intent.putExtra("phone_number", phoneNo);
                        intent.putExtra("otp_code", s);
                        intent.putExtra("from", "AddUser");
                        context.startActivity(intent);
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

