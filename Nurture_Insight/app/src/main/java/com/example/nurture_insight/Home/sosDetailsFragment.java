package com.example.nurture_insight.Home;

import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.nurture_insight.R;
import com.example.nurture_insight.therapist_dashboard.therapistDashboard;

import org.w3c.dom.Text;

public class sosDetailsFragment extends Fragment {

    ImageView backButton;
    Button addContact;
    EditText phoneNo;

    public sosDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sos_details, container, false);


        backButton = (ImageView) view.findViewById(R.id.addSos_backButton);
        addContact = (Button) view.findViewById(R.id.sos_addContact);
        phoneNo = (EditText) view.findViewById(R.id.addSos_Number);

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadNumber();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment =  new HomeFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment);
                transaction.commit();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void uploadNumber() {

        String inputPhoneNo = phoneNo.getText().toString();

        if(TextUtils.isEmpty(inputPhoneNo)){
            phoneNo.setError(getResources().getString(R.string.signUp_error_1));
        }
        else if(inputPhoneNo.length()!=10){
            phoneNo.setError(getString(R.string.signUp_error_8));
        }
        else{
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("phoneNo", getContext().MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("phoneNo", inputPhoneNo);
            editor.apply();

            Fragment fragment =  new HomeFragment();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.main_fragmentLayout, fragment);
            transaction.commit();
        }
    }

}