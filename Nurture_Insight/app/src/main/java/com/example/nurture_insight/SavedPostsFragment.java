package com.example.nurture_insight;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurture_insight.Model.Chat_Message;
import com.example.nurture_insight.Model.Save_Chat_Message;
import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.ViewHolder.ChatMessageViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SavedPostsFragment extends Fragment {

    ImageView backButton;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_saved_posts, container, false);

        backButton = (ImageView) rootView.findViewById(R.id.save_backButton);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.savedPost_recyclerView);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new ChatCommunityFragment();

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

    public void onStart(){
        super.onStart();

        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("Saved_Chat_Messages").child(Prevalent.currentOnlineUser.getPhoneNo());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dsObj: snapshot.getChildren()){
                    FirebaseRecyclerOptions<Save_Chat_Message> options =
                            new FirebaseRecyclerOptions.Builder<Save_Chat_Message>()
                                    .setQuery(userRef, Save_Chat_Message.class)
                                    .build();

                    FirebaseRecyclerAdapter<Save_Chat_Message, ChatMessageViewHolder> adapter =
                            new FirebaseRecyclerAdapter<Save_Chat_Message, ChatMessageViewHolder>(options) {

                                @NonNull
                                @Override
                                public ChatMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                    View view = LayoutInflater.from(parent.getContext())
                                            .inflate(R.layout.chat_messages, parent, false);

                                    ChatMessageViewHolder holder = new ChatMessageViewHolder(view);
                                    return holder;
                                }

                                @Override
                                protected void onBindViewHolder(@NonNull ChatMessageViewHolder holder, int position, @NonNull Save_Chat_Message model) {

                                    String chatDate = model.getMessageTime() +" "+ model.getMessageDate();
                                    holder.chatMessage.setText(model.getMessage());
                                    holder.chatMessageDate.setText(chatDate);


                                    if(model.getAnonymousStatus().equals("true")){
                                        holder.chatMessageUsername.setText(getResources().getString(R.string.anonymous));
                                    }
                                    else{
                                        holder.chatMessageUsername.setText(model.getUsername());

                                    }
                                }
                            };

                    new ItemTouchHelper(
                            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                                @Override
                                public boolean onMove(@NonNull RecyclerView recyclerView,
                                                      @NonNull RecyclerView.ViewHolder viewHolder,
                                                      @NonNull RecyclerView.ViewHolder target) {
                                    adapter.notifyDataSetChanged();
                                    return false;
                                }

                                @Override
                                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                                    AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                                    dialog.setTitle(getResources().getString(R.string.chat_dialog_title));
                                    dialog.setMessage(getResources().getString(R.string.chat_swipe_delete));
                                    dialog.setCancelable(false);
                                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, "No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int buttonId) {
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                    dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int buttonId) {
                                            adapter.getRef(viewHolder.getLayoutPosition()).removeValue();
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                    dialog.setIcon(android.R.drawable.ic_dialog_alert);
                                    dialog.show();
                                }
                            }).attachToRecyclerView(recyclerView);

                    recyclerView.setAdapter(adapter);
                    adapter.startListening();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}