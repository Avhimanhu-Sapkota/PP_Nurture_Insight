package com.example.nurture_insight.journal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.R;
import com.example.nurture_insight.instant_help.affirmations;
import com.example.nurture_insight.login_signup.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class journaling_form extends Fragment {

    Button save_journal_button;
    EditText journal_answer_1, journal_answer_2, journal_answer_3, journal_answer_4, journal_answer_5;
    String saveCurrentDate;
    String inputAnswer1, inputAnswer2, inputAnswer3, inputAnswer4, inputAnswer5;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_journaling_form, container, false);

        journal_answer_1 = (EditText) view.findViewById(R.id.journaling_q1_answer);
        journal_answer_2 = (EditText) view.findViewById(R.id.journaling_q2_answer);
        journal_answer_3 = (EditText) view.findViewById(R.id.journaling_q3_answer);
        journal_answer_4 = (EditText) view.findViewById(R.id.journaling_q4_answer);
        journal_answer_5 = (EditText) view.findViewById(R.id.journaling_q5_answer);
        save_journal_button = (Button) view.findViewById(R.id.save_journal_button);


        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());


        final DatabaseReference savedJournalRef = FirebaseDatabase.getInstance().getReference()
                .child("Journals").child(Prevalent.currentOnlineUser.getPhoneNo());

        savedJournalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(saveCurrentDate)){
                    loadJournal();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        save_journal_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadJournal();
            }
        });

        return view;
    }

    private void loadJournal() {

        final DatabaseReference savedJournalRef = FirebaseDatabase.getInstance().getReference()
                .child("Journals").child(Prevalent.currentOnlineUser.getPhoneNo()).child(saveCurrentDate);

        savedJournalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                journal_answer_1.setText(snapshot.child("answer_1").getValue().toString());
                journal_answer_2.setText(snapshot.child("answer_2").getValue().toString());
                journal_answer_3.setText(snapshot.child("answer_3").getValue().toString());
                journal_answer_4.setText(snapshot.child("answer_4").getValue().toString());
                journal_answer_5.setText(snapshot.child("answer_5").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void uploadJournal() {

        inputAnswer1 = journal_answer_1.getText().toString();
        inputAnswer2 = journal_answer_2.getText().toString();
        inputAnswer3 = journal_answer_3.getText().toString();
        inputAnswer4 = journal_answer_4.getText().toString();
        inputAnswer5 = journal_answer_5.getText().toString();

        if(TextUtils.isEmpty(inputAnswer1) || TextUtils.isEmpty(inputAnswer2) || TextUtils.isEmpty(inputAnswer3)
                || TextUtils.isEmpty(inputAnswer4) || TextUtils.isEmpty(inputAnswer5)){
            new AlertDialog.Builder(getContext())
                    .setTitle(getString(R.string.journal_title))
                    .setMessage( getString(R.string.journal_error))
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }
        else{

            final DatabaseReference journalRef = FirebaseDatabase.getInstance().getReference()
                    .child("Journals").child(Prevalent.currentOnlineUser.getPhoneNo());

            HashMap<String, Object> journalMap = new HashMap<>();
            journalMap.put("answer_1", inputAnswer1);
            journalMap.put("answer_2", inputAnswer2);
            journalMap.put("answer_3", inputAnswer3);
            journalMap.put("answer_4", inputAnswer4);
            journalMap.put("answer_5", inputAnswer5);

            journalRef.child(saveCurrentDate).updateChildren(journalMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Fragment fragment = null;
                        fragment = new JournalFragment();

                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.main_fragmentLayout, fragment );
                        transaction.commit();

                        new AlertDialog.Builder(getContext())
                                .setTitle(getString(R.string.journal_title))
                                .setMessage("Thank You for Writing your Journal Today. You are amazing!")
                                .setCancelable(false)
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                    }
                    else{
                        new AlertDialog.Builder(getContext())
                                .setTitle(getString(R.string.journal_title))
                                .setMessage(R.string.signUp_error_5)
                                .setCancelable(false)
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                    }
                }
            });
        }
    }
}