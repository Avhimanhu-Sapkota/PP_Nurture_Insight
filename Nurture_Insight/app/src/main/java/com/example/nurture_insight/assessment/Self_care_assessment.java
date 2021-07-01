package com.example.nurture_insight.assessment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Self_care_assessment extends Fragment {

    String choseOption;
    Button saveButton;
    RadioGroup scaAnswer;
    RadioButton opHappiness, opSleep, opAnxietyStress, opDepression, opAnger;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_self_care_assessment1, container, false);
        saveButton = (Button) view.findViewById(R.id.sc_assessment_save_button);
        scaAnswer = (RadioGroup) view.findViewById(R.id.sc_assessment_answer_options);
        opSleep = (RadioButton) view.findViewById(R.id.sc_assessment_option1);
        opHappiness = (RadioButton) view.findViewById(R.id.sc_assessment_option2);
        opAnger = (RadioButton) view.findViewById(R.id.sc_assessment_option3);
        opDepression = (RadioButton) view.findViewById(R.id.sc_assessment_option4);
        opAnxietyStress = (RadioButton) view.findViewById(R.id.sc_assessment_option5);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(opSleep.isChecked()){
                    choseOption = "sleep";
                    uploadAssessmentAnswer();
                }
                else if(opHappiness.isChecked()){
                    choseOption = "happiness";
                    uploadAssessmentAnswer();
                }
                else if(opAnger.isChecked()){
                    choseOption = "anger";
                    uploadAssessmentAnswer();
                }
                else if(opDepression.isChecked()){
                    choseOption = "depression";
                    uploadAssessmentAnswer();
                }
                else if(opAnxietyStress.isChecked()){
                    choseOption = "anxietyStress";
                    uploadAssessmentAnswer();
                }
                else{
                    new AlertDialog.Builder(getContext())
                            .setTitle("Nurture Insight - Self-Care Assessment")
                            .setMessage("Please Select an Option to Continue")
                            .setNegativeButton(android.R.string.yes, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

            }
        });

        return view;
    }

    private void uploadAssessmentAnswer() {
        DatabaseReference assessmentRef = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(Prevalent.currentOnlineUser.getPhoneNo());

        HashMap<String, Object> assessmentHashMap = new HashMap<>();
        assessmentHashMap.put("category", choseOption);

        assessmentRef.updateChildren(assessmentHashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            new AlertDialog.Builder(getContext())
                                    .setTitle(getResources().getString(R.string.assessment_title))
                                    .setMessage("Congratulations! You have completed your assessment. " +
                                            "Best of Luck for Your Journey! Have a nice time!")
                                    .setNegativeButton(android.R.string.yes, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                            Fragment fragment =  new weekly_assessment();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.main_fragmentLayout, fragment );
                            transaction.commit();

                        }
                        else{
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Nurture Insight - Self-Care Assessment")
                                    .setMessage("Sorry! You assessment score couldn't be recorded because of some issues. " +
                                            "Please, re-take the assessment.")
                                    .setNegativeButton(android.R.string.yes, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    }
                });
    }
}