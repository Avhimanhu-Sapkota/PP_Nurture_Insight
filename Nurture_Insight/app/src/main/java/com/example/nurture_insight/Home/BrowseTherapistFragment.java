package com.example.nurture_insight.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurture_insight.Model.Therapist;
import com.example.nurture_insight.Model.Users;
import com.example.nurture_insight.R;
import com.example.nurture_insight.ViewHolder.eachTherapistViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BrowseTherapistFragment extends Fragment {

    ImageView backButton;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_browse_therapist, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.browseTherapist_recyclerView);
        backButton = (ImageView) rootView.findViewById(R.id.browseTherapist_backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new HomeFragment();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        return rootView;
    }


    public void onStart() {
        super.onStart();

        final DatabaseReference therapistRef = FirebaseDatabase.getInstance().getReference().child("Therapist");

        therapistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot eachTherapist: snapshot.getChildren()){

                    FirebaseRecyclerOptions<Therapist> options =
                            new FirebaseRecyclerOptions.Builder<Therapist>()
                                    .setQuery(therapistRef, Therapist.class)
                                    .build();

                    FirebaseRecyclerAdapter<Therapist, eachTherapistViewHolder> adapter =
                            new FirebaseRecyclerAdapter<Therapist, eachTherapistViewHolder>(options){

                                @NonNull
                                @Override
                                public eachTherapistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                    View view = LayoutInflater.from(parent.getContext())
                                            .inflate(R.layout.each_therapist_layout, parent, false);

                                    eachTherapistViewHolder holder = new eachTherapistViewHolder(view);
                                    return holder;
                                }

                                @Override
                                protected void onBindViewHolder(@NonNull eachTherapistViewHolder holder, int position, @NonNull Therapist model) {

                                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(model.getPhoneNo());
                                    userRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot newSnapshot) {
                                            Users userObj = newSnapshot.getValue(Users.class);
                                            String eachTherapistName = userObj.getUsername();
                                            String eachTherapistEmail = userObj.getEmail();

                                            String eachTherapistContactDetails = model.getPhoneNo() +"\n" +eachTherapistEmail;
                                            String therapistWorksAt = "Works at: " + model.getWorksAt();

                                            holder.eachTherapistName.setText(eachTherapistName);
                                            holder.eachTherapistContact.setText(eachTherapistContactDetails);
                                            holder.eachTherapistExpertise.setText(model.getExpertise());
                                            holder.eachTherapistWorksAt.setText(therapistWorksAt);
                                            holder.eachTherapistBio.setText(model.getBio());

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            };
                    recyclerView.setAdapter(adapter);
                    adapter.startListening();
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}