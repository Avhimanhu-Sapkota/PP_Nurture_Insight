package com.example.nurture_insight.assessment;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.example.nurture_insight.Home.HomeFragment;
import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class weekly_assessment extends Fragment {

    ImageView backButton;
    TextView question;
    String[][] questionList;
    Button nextButton;
    String saveCurrentDate;
    RadioGroup assessmentOptions;
    RadioButton option1, option2, option3, option4, option5;
    int questionNo=0, assessmentScore=0;
    int [][] choseAnswer = new int[5][2];
    SharedPreferences sharedPreferences;
    Boolean messageSent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_weekly_assessment, container, false);

        backButton = (ImageView) view.findViewById(R.id.assessment_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment =  new HomeFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        nextButton = (Button) view.findViewById(R.id.assessment_next_button);
        question = (TextView) view.findViewById(R.id.assessment_question);
        assessmentOptions = (RadioGroup) view.findViewById(R.id.assessment_answer_options);
        option1 = (RadioButton) view.findViewById(R.id.assessment_option1);
        option2 = (RadioButton) view.findViewById(R.id.assessment_option2);
        option3 = (RadioButton) view.findViewById(R.id.assessment_option3);
        option4 = (RadioButton) view.findViewById(R.id.assessment_option4);
        option5 = (RadioButton) view.findViewById(R.id.assessment_option5);

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        questionList = getQuestions();
        displayQuestions();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int isChecked = assessmentOptions.getCheckedRadioButtonId();

                if (isChecked == 0){
                    new AlertDialog.Builder(getContext())
                            .setTitle(getResources().getString(R.string.assessment_title))
                            .setMessage("Please Select an Option to Continue")
                            .setNegativeButton(android.R.string.yes, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                else{
                    getAssessmentScore();

                    if(questionNo == 4){
                        uploadAssessmentScore();
                    }
                    else{
                        questionNo++;
                        displayQuestions();
                    }
                }

            }
        });

        return view;
    }

    private void uploadAssessmentScore() {

        if(assessmentScore<=8){
            sharedPreferences = getContext().getSharedPreferences("AssessmentPref", getContext().MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("messageSent", false);
            editor.apply();

            }

        DatabaseReference assessmentRef = FirebaseDatabase.getInstance().getReference().child("Assessment")
                .child(Prevalent.currentOnlineUser.getPhoneNo());

        HashMap<String, Object> assessmentHashMap = new HashMap<>();
        assessmentHashMap.put("score", assessmentScore);

        assessmentRef.child(saveCurrentDate).updateChildren(assessmentHashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            new AlertDialog.Builder(getContext())
                                    .setTitle(getResources().getString(R.string.assessment_title))
                                    .setMessage("Congratulations! You have completed your assessment. " +
                                            "Your Assessment Score is " + assessmentScore + ". We believe in You despite your score! Have a nice time!")
                                    .setNegativeButton(android.R.string.yes, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                            Fragment fragment =  new HomeFragment();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.main_fragmentLayout, fragment );
                            transaction.commit();

                        }
                        else{
                            new AlertDialog.Builder(getContext())
                                    .setTitle(getResources().getString(R.string.assessment_title))
                                    .setMessage("Sorry! You assessment score couldn't be recorded because of some issues. " +
                                            "Please, re-take the assessment later.")
                                    .setNegativeButton(android.R.string.yes, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                            Fragment fragment =  new HomeFragment();
                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.main_fragmentLayout, fragment );
                            transaction.commit();
                        }
                    }
                });
    }

    private void getAssessmentScore() {

        if(option1.isChecked()){
            assessmentScore = assessmentScore + 1;
        }
        else if (option2.isChecked()){
            assessmentScore = assessmentScore + 2;
        }
        else if (option3.isChecked()){
            assessmentScore = assessmentScore + 3;
        }
        else if (option4.isChecked()){
            assessmentScore = assessmentScore + 4;
        }
        else if (option5.isChecked()){
            assessmentScore = assessmentScore + 5;
        }
        else{
            option5.setError("Please Select An Answer");
        }
    }

    private void displayQuestions() {
        question.setText(questionList[questionNo][0]);
        option1.setText(questionList[questionNo][1]);
        option2.setText(questionList[questionNo][2]);
        option3.setText(questionList[questionNo][3]);
        option4.setText(questionList[questionNo][4]);
        option5.setText(questionList[questionNo][5]);

        assessmentOptions.check(choseAnswer[questionNo][1]);
        option5.setError(null);
    }

    private String[][] getQuestions() {
        String[][] questionsAnswers = new String[5][6];
        String[] questionOptionsList = getResources().getStringArray(R.array.assessmentQuestionAnswers);

        for(int index=0; index<5; index++){
            String [] questionsOnly = questionOptionsList[index].split(",");
            for(int newIndex=0; newIndex<6; newIndex++){
                questionsAnswers[index][newIndex] = questionsOnly[newIndex];
            }
        }

        return questionsAnswers;
    }
}