package com.example.nurture_insight;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurture_insight.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ChatCommunityFragment extends Fragment {

    ImageView sendButton;
    EditText messageArea;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

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

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadChatMessage();
            }
        });
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
            final DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference().child("ChatMessages");

            final HashMap<String, Object> chatMap = new HashMap<>();
            chatMap.put("messageDate", saveCurrentDate);
            chatMap.put("messageTime", saveCurrentTime);
            chatMap.put("message", inputMessage);

            chatReference.child(Prevalent.currentOnlineUser.getPhoneNo()).child(saveCurrentDate+saveCurrentTime)
                    .updateChildren(chatMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                messageArea.setText(null);
                                //DO SOMETHING AFTER SUCCESSFUL UPLOAD.-------- CALL RECYCLER VIEW TO DISPLAY
                            }
                        }
                    });

        }

        final HashMap<String, Object> moodMap = new HashMap<>();
    }
}