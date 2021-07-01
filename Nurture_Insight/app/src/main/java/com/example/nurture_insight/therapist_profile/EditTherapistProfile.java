package com.example.nurture_insight.therapist_profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.example.nurture_insight.Model.Therapist;
import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.R;
import com.example.nurture_insight.login_signup.LoginActivity;
import com.example.nurture_insight.user_profile.UserProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import io.paperdb.Paper;

public class EditTherapistProfile extends Fragment {

    ImageView backButton;
    EditText email, username, expertise, degree, workAt, bio;
    String choseOption, assessmentAnswer, inputWorkAt2;
    Button saveButton, deleteButton;
    RadioGroup scaAnswer;
    RadioButton opHappiness, opSleep, opAnxietyStress, opDepression, opAnger;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_therapist_profile, container, false);

        backButton = (ImageView) view.findViewById(R.id.therapist_edit_backButton);
        username = (EditText) view.findViewById(R.id.therapist_edit_exitText_username);
        email = (EditText) view.findViewById(R.id.therapist_edit_exitText_email);
        expertise = (EditText) view.findViewById(R.id.therapistEdit_exitText_expertise);
        degree = (EditText) view.findViewById(R.id.therapistEdit_exitText_degree);
        workAt = (EditText) view.findViewById(R.id.therapistEdit_exitText_workat);
        bio = (EditText) view.findViewById(R.id.therapistEdit_exitText_bio);
        saveButton = (Button) view.findViewById(R.id.therapist_edit_button);
        deleteButton = (Button) view.findViewById(R.id.therapist_delete_button);
        scaAnswer = (RadioGroup) view.findViewById(R.id.therapist_assessment_answer_options);
        opSleep = (RadioButton) view.findViewById(R.id.therapist_assessment_option1);
        opHappiness = (RadioButton) view.findViewById(R.id.therapist_assessment_option2);
        opAnger = (RadioButton) view.findViewById(R.id.therapist_assessment_option3);
        opDepression = (RadioButton) view.findViewById(R.id.therapist_assessment_option4);
        opAnxietyStress = (RadioButton) view.findViewById(R.id.therapist_assessment_option5);

        loadUserDetails();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new TherapistProfileFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUserDetails();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                dialog.setTitle("Nurture Insight - Edit Profile");
                dialog.setMessage("Do You want to Delete Your Account?");
                dialog.setCancelable(false);
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int buttonId) {

                    }
                });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int buttonId) {
                        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(Prevalent.currentOnlineUser.getPhoneNo());

                        DatabaseReference therapistRef = FirebaseDatabase.getInstance().getReference()
                                .child("Therapist").child(Prevalent.currentOnlineUser.getPhoneNo());

                        therapistRef.removeValue();
                        rootRef.removeValue();

                        new AlertDialog.Builder(getContext())
                                .setTitle("Nurture Insight - Message")
                                .setMessage("We Feel bad to See You Go. Do join us if you want, you are always welcome!")
                                .setCancelable(false)
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
                        Paper.book().destroy();

                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);


                    }
                });
                dialog.setIcon(android.R.drawable.ic_dialog_alert);
                dialog.show();

            }
        });

        return view;
    }

    private void checkUserDetails() {
        String inputEmail = email.getText().toString();
        String inputName = username.getText().toString();
        String inputExpertise = expertise.getText().toString();
        String inputDegree = degree.getText().toString();
        String inputWorkAt = workAt.getText().toString();
        String inputBio = bio.getText().toString();

        if (TextUtils.isEmpty(inputEmail)){
            email.setError(getString(R.string.signUp_error_2));
        }
        else if(!isValidEmail(inputEmail)){
            email.setError(getString(R.string.signUp_error_7));
        }
        else if (TextUtils.isEmpty(inputName)){
            username.setError(getString(R.string.signUp_error_11));
        }
        else if (TextUtils.isEmpty(inputExpertise)){
            expertise.setError(getString(R.string.therapistInfo_error1));
        }
        else if (TextUtils.isEmpty(inputDegree)){
            degree.setError(getString(R.string.therapistInfo_error2));
        }
        else if (TextUtils.isEmpty(inputWorkAt)){
            workAt.setText("Nurture Insight");
        }
        else if (TextUtils.isEmpty(inputBio)){
            bio.setError(getString(R.string.therapistInfo_error3));
        }
        else{
            inputWorkAt2 = workAt.getText().toString();
            assessmentAnswer = getAssessmentAnswer();
            saveUserChanges(inputEmail, inputName, assessmentAnswer, inputExpertise, inputDegree, inputWorkAt2, inputBio);

        }
    }

    private void saveUserChanges(String inputEmail, String inputName, String assessmentAnswer, String inputExpertise, String inputDegree, String inputWorkAt2, String inputBio) {

        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(Prevalent.currentOnlineUser.getPhoneNo());

        HashMap<String, Object> userEditMap = new HashMap<>();
        userEditMap.put("username", inputName);
        userEditMap.put("email", inputEmail);
        userEditMap.put("category", assessmentAnswer);

        rootRef.updateChildren(userEditMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    DatabaseReference therapistRef = FirebaseDatabase.getInstance().getReference()
                            .child("Therapist").child(Prevalent.currentOnlineUser.getPhoneNo());

                    HashMap<String, Object> therapistDataMap = new HashMap<>();
                    therapistDataMap.put("expertise", inputExpertise);
                    therapistDataMap.put("degree", inputDegree);
                    therapistDataMap.put("worksAt", inputWorkAt2);
                    therapistDataMap.put("bio", inputBio);

                    therapistRef.updateChildren(therapistDataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                new AlertDialog.Builder(getContext())
                                        .setTitle("Nurture Insight - Edit Profile")
                                        .setMessage("Your changes have been made. Please Login to continue!")
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        }).show();

                                Paper.book().destroy();

                                Intent intent = new Intent(getContext(), LoginActivity.class);
                                startActivity(intent);

                            } else {
                                new AlertDialog.Builder(getContext())
                                        .setTitle("Nurture Insight - Edit Profile")
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
                else {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Nurture Insight - Edit Profile")
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

    private String getAssessmentAnswer() {
        if(opSleep.isChecked()){
            choseOption = "sleep";
        }
        else if(opHappiness.isChecked()){
            choseOption = "happiness";
        }
        else if(opAnger.isChecked()){
            choseOption = "anger";
        }
        else if(opDepression.isChecked()){
            choseOption = "depression";
        }
        else if(opAnxietyStress.isChecked()){
            choseOption = "anxietyStress";
        }
        else{
            choseOption = "happiness";
            new AlertDialog.Builder(getContext())
                    .setTitle("Nurture Insight - Self-Care Assessment")
                    .setMessage("Please Select an Option to Continue")
                    .setNegativeButton(android.R.string.yes, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        return choseOption;
    }

    private void loadUserDetails() {

        username.setText(Prevalent.currentOnlineUser.getUsername());
        email.setText(Prevalent.currentOnlineUser.getEmail());

        if(Prevalent.currentOnlineUser.getCategory().equals("sleep")){
            opSleep.setChecked(true);
        }
        else if(Prevalent.currentOnlineUser.getCategory().equals("happiness")){
            opHappiness.setChecked(true);
        }
        else if(Prevalent.currentOnlineUser.getCategory().equals("anger")){
            opAnger.setChecked(true);
        }
        else if(Prevalent.currentOnlineUser.getCategory().equals("depression")){
            opDepression.setChecked(true);
        }
        else if(Prevalent.currentOnlineUser.getCategory().equals("anxietyStress")){
            opAnxietyStress.setChecked(true);
        }
        else{
            opHappiness.setChecked(true);
        }

        DatabaseReference therapistRef = FirebaseDatabase.getInstance().getReference()
                .child("Therapist").child(Prevalent.currentOnlineUser.getPhoneNo());

        therapistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Therapist therapistObj = snapshot.getValue(Therapist.class);
                expertise.setText(therapistObj.getExpertise());
                degree.setText(therapistObj.getDegree());
                workAt.setText(therapistObj.getWorksAt());
                bio.setText(therapistObj.getBio());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static boolean isValidEmail(String inputEmail) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches();
    }
}