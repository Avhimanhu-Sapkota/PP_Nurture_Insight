package com.example.nurture_insight.journal;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.example.nurture_insight.ChatCommunityFragment;
import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class journal_display extends Fragment {

    ImageView backButton;
    TextView saved_journal_answer1, saved_journal_answer2, saved_journal_answer3, saved_journal_answer4, saved_journal_answer5, saved_journal_title;
    String loadDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_journal_display, container, false);

        backButton = (ImageView) view.findViewById(R.id.saved_journal_backButton);
        saved_journal_answer1 = (TextView) view.findViewById(R.id.saved_journal_answer1);
        saved_journal_answer2 = (TextView) view.findViewById(R.id.saved_journal_answer2);
        saved_journal_answer3 = (TextView) view.findViewById(R.id.saved_journal_answer3);
        saved_journal_answer4 = (TextView) view.findViewById(R.id.saved_journal_answer4);
        saved_journal_answer5 = (TextView) view.findViewById(R.id.saved_journal_answer5);
        saved_journal_title = (TextView) view.findViewById(R.id.saved_journal_title);

        Bundle bundle = this.getArguments();
        if (bundle != null){
            loadDate = bundle.getString("clickedDate", "No Data Selected");
        }

        String title = "Journal Memory: " + loadDate;
        saved_journal_title.setText(title);

        final DatabaseReference savedJournalRef = FirebaseDatabase.getInstance().getReference()
                .child("Journals").child(Prevalent.currentOnlineUser.getPhoneNo());

        savedJournalRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.hasChild(loadDate)){
                    savedJournalRef.child(loadDate).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            saved_journal_answer1.setText(snapshot.child("answer_1").getValue().toString());
                            saved_journal_answer2.setText(snapshot.child("answer_2").getValue().toString());
                            saved_journal_answer3.setText(snapshot.child("answer_3").getValue().toString());
                            saved_journal_answer4.setText(snapshot.child("answer_4").getValue().toString());
                            saved_journal_answer5.setText(snapshot.child("answer_5").getValue().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    saved_journal_answer1.setText(getResources().getString(R.string.journal_answer_notFound));
                    saved_journal_answer2.setText(getResources().getString(R.string.journal_answer_notFound));
                    saved_journal_answer3.setText(getResources().getString(R.string.journal_answer_notFound));
                    saved_journal_answer4.setText(getResources().getString(R.string.journal_answer_notFound));
                    saved_journal_answer5.setText(getResources().getString(R.string.journal_answer_notFound));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new JournalFragment();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        return view;
    }
}