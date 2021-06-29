package com.example.nurture_insight.Self_Care_Packages;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nurture_insight.Home.HomeFragment;
import com.example.nurture_insight.Model.Self_care;
import com.example.nurture_insight.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;


public class self_care_exercise extends Fragment {
    ImageView backButton;
    String id;
    TextView videoTitle, videoCategory, videoDuration, videoDesc, videoHost;
    YouTubePlayerView youTubePlayerView;

    public self_care_exercise() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_self_care_exercise, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null){
            id = bundle.getString("id", "");
        }

        videoTitle = (TextView) view.findViewById(R.id.sce_video_title);
        videoCategory = (TextView) view.findViewById(R.id.sce_video_category);
        videoDuration = (TextView) view.findViewById(R.id.sce_video_duration);
        videoDesc = (TextView) view.findViewById(R.id.sce_video_description);
        videoHost = (TextView) view.findViewById(R.id.sce_video_host);
        backButton = (ImageView) view.findViewById(R.id.sce_backButton);
        youTubePlayerView = view.findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment =  new self_care_package_home();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Self_care");
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot eachSnap: snapshot.getChildren()){
                    if(eachSnap.hasChild(id)){
                        DatabaseReference finalRef = eachSnap.child(id).getRef();
                        finalRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot finalSnapshot) {
                                Self_care object = finalSnapshot.getValue(Self_care.class);
                                videoTitle.setText(object.getName());
                                videoCategory.setText("Category: " + object.getCategory());
                                videoDuration.setText("Duration: " + object.getDuration());
                                videoDesc.setText(object.getDescription());
                                videoHost.setText("Video By: " + object.getVideo_by());


                                youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                                    @Override
                                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                                        String videoId = object.getUrl();
                                        youTubePlayer.cueVideo(videoId, 0);
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

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        return view;
    }
}