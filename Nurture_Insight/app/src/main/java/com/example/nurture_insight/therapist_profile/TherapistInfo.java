package com.example.nurture_insight.therapist_profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.nurture_insight.MainActivity;
import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class TherapistInfo extends AppCompatActivity {

    EditText expertiseEditText, degreeEditText, workAtEditText, bioEditText;
    Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_therapist_info);

        expertiseEditText = (EditText) findViewById(R.id.therapistInfo_exitText_expertise);
        degreeEditText = (EditText) findViewById(R.id.therapistInfo_exitText_degree);
        workAtEditText = (EditText) findViewById(R.id.therapistInfo_exitText_workat);
        bioEditText = (EditText) findViewById(R.id.therapistInfo_exitText_bio);
        saveButton = (Button) findViewById(R.id.therapistInfo_button_saveInfo);

        DatabaseReference newReference = FirebaseDatabase.getInstance().getReference().child("Therapist");

        newReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot therapistSnapshot) {
                if(therapistSnapshot.child(Prevalent.currentOnlineUser.getPhoneNo()).exists()){
                    Intent signUpIntent= new Intent(TherapistInfo.this, MainActivity.class);
                    startActivity(signUpIntent);
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTherapistInfo();
            }
        });
    }

    private void saveTherapistInfo() {
        String inputExpertise = expertiseEditText.getText().toString();
        String inputDegree = degreeEditText.getText().toString();
        String inputWorkAt = workAtEditText.getText().toString();
        String inputBio = bioEditText.getText().toString();

        if (TextUtils.isEmpty(inputExpertise)){
            expertiseEditText.setError(getString(R.string.therapistInfo_error1));
        }
        else if (TextUtils.isEmpty(inputDegree)){
            degreeEditText.setError(getString(R.string.therapistInfo_error2));
        }
        else if (TextUtils.isEmpty(inputBio)){
            bioEditText.setError(getString(R.string.therapistInfo_error3));
        }
        else if (TextUtils.isEmpty(inputWorkAt)){
            workAtEditText.setText("Nurture Insight");
        }
        else{
            String inputWorkAt2 = workAtEditText.getText().toString();
            final DatabaseReference RootReference;
            RootReference = FirebaseDatabase.getInstance().getReference();

            HashMap<String, Object> therapistDataMap = new HashMap<>();
            therapistDataMap.put("expertise", inputExpertise);
            therapistDataMap.put("degree", inputDegree);
            therapistDataMap.put("worksAt", inputWorkAt);
            therapistDataMap.put("bio", inputBio);
            therapistDataMap.put("phoneNo", Prevalent.currentOnlineUser.getPhoneNo());

            RootReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    RootReference.child("Therapist").child(Prevalent.currentOnlineUser.getPhoneNo())
                            .updateChildren(therapistDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d("Therapist", "onComplete: " + inputExpertise + inputDegree + inputWorkAt2 + bioEditText);
                                    if(task.isSuccessful()){
                                        Intent welcomeIntent = new Intent(TherapistInfo.this, MainActivity.class);
                                        startActivity(welcomeIntent);
                                        finish();
                                    }
                                    else{
                                        new AlertDialog.Builder(TherapistInfo.this)
                                                .setTitle(getString(R.string.therapistInfo_title))
                                                .setMessage(R.string.signUp_error_5)
                                                .setCancelable(false)
                                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        finish();
                                                    }
                                                }).show();
                                    }
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