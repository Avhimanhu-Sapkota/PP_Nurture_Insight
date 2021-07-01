package com.example.nurture_insight.user_profile;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.example.nurture_insight.Home.sosDetailsFragment;
import com.example.nurture_insight.Model.Users;
import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.R;
import com.example.nurture_insight.habit_tracker.habit_tracker_home;
import com.example.nurture_insight.login_signup.LoginActivity;
import com.example.nurture_insight.login_signup.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import io.paperdb.Paper;

public class EditUserProfile extends Fragment {

    ImageView backButton;
    EditText email, username;
    String choseOption, assessmentAnswer;
    Button saveButton, deleteButton;
    RadioGroup scaAnswer;
    RadioButton opHappiness, opSleep, opAnxietyStress, opDepression, opAnger;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_user_profile, container, false);

        backButton = (ImageView) view.findViewById(R.id.user_edit_backButton);
        username = (EditText) view.findViewById(R.id.user_edit_exitText_username);
        email = (EditText) view.findViewById(R.id.user_edit_exitText_email);
        saveButton = (Button) view.findViewById(R.id.user_edit_button);
        deleteButton = (Button) view.findViewById(R.id.user_delete_button);
        scaAnswer = (RadioGroup) view.findViewById(R.id.user_assessment_answer_options);
        opSleep = (RadioButton) view.findViewById(R.id.user_assessment_option1);
        opHappiness = (RadioButton) view.findViewById(R.id.user_assessment_option2);
        opAnger = (RadioButton) view.findViewById(R.id.user_assessment_option3);
        opDepression = (RadioButton) view.findViewById(R.id.user_assessment_option4);
        opAnxietyStress = (RadioButton) view.findViewById(R.id.user_assessment_option5);

        loadUserDetails();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new UserProfileFragment();
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

    }

    private void checkUserDetails() {
        String inputEmail = email.getText().toString();
        String inputName = username.getText().toString();

        if (TextUtils.isEmpty(inputEmail)){
            email.setError(getString(R.string.signUp_error_2));
        }
        else if(!isValidEmail(inputEmail)){
            email.setError(getString(R.string.signUp_error_7));
        }
        else if (TextUtils.isEmpty(inputName)){
            username.setError(getString(R.string.signUp_error_11));
        }
        else{
            assessmentAnswer = getAssessmentAnswer();
            saveUserChanges(inputEmail, inputName, assessmentAnswer);

        }
    }

    private void saveUserChanges(String inputEmail, String inputName, String assessmentAnswer) {

        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(Prevalent.currentOnlineUser.getPhoneNo());

        HashMap<String, Object> userEditMap = new HashMap<>();
        userEditMap.put("username", inputName);
        userEditMap.put("email", inputEmail);
        userEditMap.put("category", assessmentAnswer);

        rootRef.updateChildren(userEditMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
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

                }
                else{
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

    public static boolean isValidEmail(String inputEmail) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches();
    }
}