package com.example.nurture_insight.chat_community;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ChatCommunityFragment extends Fragment {

    ImageView sendButton;
    EditText messageArea;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String anonymousStatus;
    Button savedPosts, myPosts;
    chatAdapter adapter;
    ArrayList <Chat_Message> chatMessages;

    @SuppressLint("WrongViewCast")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_community, container, false);

        sendButton = (ImageView) rootView.findViewById(R.id.chat_btn_send);
        messageArea = (EditText) rootView.findViewById(R.id.chat_editText_messageArea);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.chat_recyclerView);
        savedPosts = (Button) rootView.findViewById(R.id.chat_savedAreaButton);
        myPosts = (Button) rootView.findViewById(R.id.chat_myPostsButton);

        savedPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new SavedPostsFragment();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        myPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = null;
                fragment = new MyPostsFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.setTitle(getResources().getString(R.string.chat_dialog_title));
        dialog.setMessage(getResources().getString(R.string.chat_dialog_message));
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {
                anonymousStatus = "true";
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int buttonId) {
                anonymousStatus = "false";
            }
        });
        dialog.setIcon(android.R.drawable.ic_dialog_alert);
        dialog.show();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadChatMessage();
                chatMessages.clear();
            }
        });

        chatMessages = new ArrayList<Chat_Message>();
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        getChatData();

        new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
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
                                    int position = viewHolder.getBindingAdapterPosition();
                                    adapter.getDatabaseItem(position, getContext());
                                    adapter.notifyDataSetChanged();
                                }
                            });
                            dialog.setIcon(android.R.drawable.ic_dialog_alert);
                            dialog.show();
                    }
                }).attachToRecyclerView(recyclerView);

        new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                        AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
                        dialog.setTitle(getResources().getString(R.string.chat_dialog_title));
                        dialog.setMessage(getResources().getString(R.string.chat_swipe_save));
                        dialog.setCancelable(false);
                        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int buttonId) {
                                adapter.notifyDataSetChanged();
                            }
                        });
                        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int buttonId) {
                                int position = viewHolder.getBindingAdapterPosition();

                                adapter.saveDatabaseItem(position, getContext());
                                adapter.notifyDataSetChanged();
                            }
                        });
                        dialog.setIcon(android.R.drawable.ic_dialog_alert);
                        dialog.show();
                    }
                }).attachToRecyclerView(recyclerView);

        return rootView;
    }


    private void uploadChatMessage() {
        String inputMessage = messageArea.getText().toString();

        String saveCurrentDate, saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        if(!(TextUtils.isEmpty(inputMessage))){

            final DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference().child("Chat_Messages");

            final HashMap<String, Object> chatMap = new HashMap<>();
            chatMap.put("messageDate", saveCurrentDate);
            chatMap.put("messageTime", saveCurrentTime);
            chatMap.put("message", inputMessage);
            chatMap.put("username", Prevalent.currentOnlineUser.getUsername());
            chatMap.put("anonymousStatus", anonymousStatus);

            chatReference.child(Prevalent.currentOnlineUser.getPhoneNo()).child(saveCurrentDate+saveCurrentTime)
                    .updateChildren(chatMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                messageArea.setText(null);
                                //DO SOMETHING AFTER SUCCESSFUL UPLOAD.-------- CALL RECYCLER VIEW TO DISPLAY OK
                            }
                        }
                    });
            chatMessages.clear();

        }
    }

    public void getChatData(){
        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                .child("Chat_Messages");

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dsID: snapshot.getChildren()){
                    chatMessages.clear();
                    String userID = dsID.getKey();
                    Query chatRef = userRef.child(userID).orderByValue();

                    chatRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot finalSnapshot: snapshot.getChildren()){
                                String chatID = finalSnapshot.getKey();
                                String message = finalSnapshot.child("message").getValue(String.class);
                                String messageDate = finalSnapshot.child("messageDate").getValue(String.class);
                                String messageTime = finalSnapshot.child("messageTime").getValue(String.class);
                                String anonymousStatus = finalSnapshot.child("anonymousStatus").getValue(String.class);
                                String username = finalSnapshot.child("username").getValue(String.class);

                                chatMessages.add(new Chat_Message(chatID, messageDate, messageTime, message, username, anonymousStatus));
                            }
                            adapter = new chatAdapter(chatMessages);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



}