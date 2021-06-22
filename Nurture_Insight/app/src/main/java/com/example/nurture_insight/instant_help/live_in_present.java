package com.example.nurture_insight.instant_help;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.example.nurture_insight.BrowseTherapistFragment;
import com.example.nurture_insight.HomeFragment;
import com.example.nurture_insight.R;

public class live_in_present extends Fragment {

    TextView textArea;
    Button nextButton, goBackButton;
    String[] liveInPresent;
    int index=0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_live_in_present, container, false);

        textArea = (TextView) view.findViewById(R.id.lip_textArea);
        nextButton = (Button) view.findViewById(R.id.lip_next_button);
        goBackButton = (Button) view.findViewById(R.id.lip_goback_button);
        liveInPresent = getResources().getStringArray(R.array.lip_messages);

        displayMessage();

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
                index++;
                displayMessage();
            }
        });

        return view;
    }

    private void displayMessage() {
        if (index==4){
            nextButton.setVisibility(View.GONE);
        }

        if (index<5){
            textArea.setText(liveInPresent[index]);
        }
    }
}