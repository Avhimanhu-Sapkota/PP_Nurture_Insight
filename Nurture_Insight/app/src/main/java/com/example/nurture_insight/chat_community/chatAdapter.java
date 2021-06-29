package com.example.nurture_insight.chat_community;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurture_insight.Interface.ItemClickListener;
import com.example.nurture_insight.Model.Chat_Message;
import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.ChatMessageViewHolder> {

    private List<Chat_Message> chatMessageList;

    public chatAdapter(List<Chat_Message> chatMessages) {

        this.chatMessageList = chatMessages;
    }

    @NonNull
    @Override
    public ChatMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_messages,parent,false);

        ChatMessageViewHolder viewHolder = new ChatMessageViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageViewHolder holder, int position) {
        Chat_Message chatMessage = chatMessageList.get(position);

        holder.chatMessageDate.setText(chatMessage.getMessageDate() + " " + chatMessage.getMessageTime());
        holder.chatMessage.setText(chatMessage.getMessage());
        holder.chatMessageUsername.setText(chatMessage.getUsername());

        if(chatMessage.getAnonymousStatus().equals("true")){
            holder.chatMessageUsername.setText("Posted Anonymously");
        }
        else{
            holder.chatMessageUsername.setText(chatMessage.getUsername());
        }

    }

    @Override
    public int getItemCount() {

        return chatMessageList.size();
    }

    public void getDatabaseItem(int position, Context context) {

        Chat_Message chatMessage = chatMessageList.get(position);
        String currentID = chatMessage.getMessageDate()+chatMessage.getMessageTime();
        final DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference().child("Chat_Messages")
                .child(Prevalent.currentOnlineUser.getPhoneNo());

        chatReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot deleteSnapshot : snapshot.getChildren()){
                    String dataID = deleteSnapshot.getKey();

                    if(currentID.equals(dataID)){
                        deleteSnapshot.getRef().removeValue();
                        chatMessageList.clear();

                    }else{

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void saveDatabaseItem(int position, Context context) {
        String saveCurrentDate, saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());
        String currentUserID = Prevalent.currentOnlineUser.getPhoneNo();

        Chat_Message chatMessage = chatMessageList.get(position);

        final DatabaseReference getChatReference = FirebaseDatabase.getInstance().getReference().child("Saved_Chat_Messages").child(currentUserID);

        final HashMap<String, Object> saveChatMap = new HashMap<>();
        saveChatMap.put("messageDate", chatMessage.getMessageDate());
        saveChatMap.put("messageTime", chatMessage.getMessageTime());
        saveChatMap.put("message", chatMessage.getMessage());
        saveChatMap.put("username", chatMessage.getUsername());
        saveChatMap.put("anonymousStatus", chatMessage.getAnonymousStatus());

        getChatReference.child(saveCurrentDate+saveCurrentTime).updateChildren(saveChatMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //messageArea.setText(null);
                            //DO SOMETHING AFTER SUCCESSFUL UPLOAD.-------- CALL RECYCLER VIEW TO DISPLAY OK
                        }
                    }
                });

    }

    public class ChatMessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView chatMessage, chatMessageDate, chatMessageUsername;
        public ItemClickListener listener;

        public ChatMessageViewHolder(@NonNull View itemView) {
            super(itemView);

            chatMessage = (TextView) itemView.findViewById(R.id.chat_message);
            chatMessageDate = (TextView) itemView.findViewById(R.id.chat_message_date);
            chatMessageUsername = (TextView) itemView.findViewById(R.id.chat_username);

        }

        public  void setItemClickListener(ItemClickListener listener){
            this.listener = listener;
        }

        @Override
        public void onClick(View v) {

            listener.onClick(v, getAdapterPosition(), false);

        }
    }
}
