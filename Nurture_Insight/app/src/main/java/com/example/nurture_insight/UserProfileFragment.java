package com.example.nurture_insight;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.login_signup.LoginActivity;
import com.example.nurture_insight.login_signup.SignUpActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class UserProfileFragment extends Fragment {

    TextView username;
    CircleImageView profileImage;
    ImageView logout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
         View rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);

         username = (TextView)rootView.findViewById(R.id.profile_username);
         profileImage = (CircleImageView)rootView.findViewById(R.id.profile_image);
         logout = (ImageView)rootView.findViewById(R.id.profile_imgView_logout);

         logout.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Paper.book().destroy();
                 Intent logoutIntent= new Intent(getActivity(), LoginActivity.class);
                 startActivity(logoutIntent);
             }
         });
         username.setText(Prevalent.currentOnlineUser.getUsername());

        return rootView;

        }
}