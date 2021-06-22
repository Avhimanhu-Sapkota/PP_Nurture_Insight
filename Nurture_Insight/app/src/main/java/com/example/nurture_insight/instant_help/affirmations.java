package com.example.nurture_insight.instant_help;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.nurture_insight.HomeFragment;
import com.example.nurture_insight.R;

import java.util.Random;

public class affirmations extends Fragment {

    TextView textArea;
    Button nextButton, goBackButton;
    String[] affirmations;

    public affirmations() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_affirmations, container, false);

        textArea = (TextView) view.findViewById(R.id.affirmation_textArea);
        nextButton = (Button) view.findViewById(R.id.affirmation_next_button);
        goBackButton = (Button) view.findViewById(R.id.affirmation_goback_button);
        affirmations = getResources().getStringArray(R.array.affirmations);

        displayAffirmations();

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new HomeFragment();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAffirmations();
            }
        });


        // Inflate the layout for this fragment
        return view; }

    private void displayAffirmations() {
        final int random = new Random().nextInt((10 - 1) + 1) + 1;
        textArea.setText(affirmations[random-1]);
    }
}