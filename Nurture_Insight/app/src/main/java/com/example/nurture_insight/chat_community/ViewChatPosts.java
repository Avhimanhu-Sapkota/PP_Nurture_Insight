package com.example.nurture_insight.chat_community;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurture_insight.Model.Chat_Message;
import com.example.nurture_insight.Model.Comments;
import com.example.nurture_insight.Prevalent.Prevalent;
import com.example.nurture_insight.R;
import com.example.nurture_insight.therapist_dashboard.PatientProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ViewChatPosts extends Fragment {

    ImageView backButton, commentSendButton;
    EditText commentTypeArea;
    TextView viewChat_username,viewChat_message, viewChat_date;
    String chatID;
    RecyclerView commentRecyclerView;
    RecyclerView.LayoutManager layoutManager;
    CommentAdapter commentAdapter;
    ArrayList<Comments> commentsArrayList;
    LinearLayout viewChat_commentsLL;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View  view = inflater.inflate(R.layout.fragment_view_chat_posts, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null){
            chatID = bundle.getString("chatID", "");
            Log.d("UNIQUENAME", "loadPost: " + chatID);
        }

        backButton = (ImageView) view.findViewById(R.id.viewChatPost_backButton);
        commentSendButton = (ImageView) view.findViewById(R.id.comment_btn_send);
        commentTypeArea = (EditText) view.findViewById(R.id.comment_typeArea);
        commentRecyclerView = (RecyclerView) view.findViewById(R.id.chatCommentRecyclerView);
        viewChat_username = (TextView) view.findViewById(R.id.viewChat_username);
        viewChat_message = (TextView) view.findViewById(R.id.viewChat_message);
        viewChat_date = (TextView) view.findViewById(R.id.viewChat_date);
        viewChat_commentsLL = (LinearLayout) view.findViewById(R.id.viewChat_commentsLL);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ChatCommunityFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.main_fragmentLayout, fragment );
                transaction.commit();
            }
        });

        loadPost();

        if (Prevalent.currentOnlineUser.getType().equals("therapist")){
            commentSendButton.setVisibility(View.VISIBLE);
            commentTypeArea.setVisibility(View.VISIBLE);
        }
        else{
            commentSendButton.setVisibility(View.GONE);
            commentTypeArea.setVisibility(View.GONE);
        }

        commentsArrayList = new ArrayList<>();
        getComments();

        commentSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadComment();
            }
        });

        return view;
    }

    private void loadPost() {
        final DatabaseReference chatRootRef = FirebaseDatabase.getInstance().getReference().child("Chat_Messages");

        chatRootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot newSnap: snapshot.getChildren()){
                    String userID = newSnap.getKey();
                    DatabaseReference userIDRef = snapshot.child(userID).getRef();

                    if(newSnap.hasChild(chatID)) {
                        userIDRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot newSnapshot) {
                                for (DataSnapshot chatSnapshot : newSnapshot.getChildren()) {


                                    Chat_Message chat_message_obj = chatSnapshot.getValue(Chat_Message.class);

                                    if (chat_message_obj.getAnonymousStatus().equals("true")) {
                                        viewChat_username.setText("Posted Anonymously");
                                    } else {
                                        viewChat_username.setText(chat_message_obj.getUsername());
                                    }
                                    viewChat_message.setText(chat_message_obj.getMessage());
                                    String postDate = chat_message_obj.getMessageDate() + " at " + chat_message_obj.getMessageTime();
                                    viewChat_date.setText(postDate);


                                }

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

    }

    private void getComments() {

        final DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference().child("Comments").child(chatID);

        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentsArrayList.clear();
                for(DataSnapshot eachSnapshot: snapshot.getChildren()){
                    DatabaseReference commentID = eachSnapshot.getRef();
                    Comments model = new Comments(commentID);
                    commentsArrayList.add(model);

                }

                layoutManager = new LinearLayoutManager(getContext());
                commentRecyclerView.setLayoutManager(layoutManager);
                commentAdapter = new CommentAdapter(getContext(), commentsArrayList);
                commentRecyclerView.setAdapter(commentAdapter);

                if (commentsArrayList.isEmpty()){
                    viewChat_commentsLL.setVisibility(View.GONE);
                }
                else{
                    viewChat_commentsLL.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void uploadComment() {
        String saveCurrentDate, saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        String inputComment = commentTypeArea.getText().toString();

        if (!TextUtils.isEmpty(inputComment)){

            final DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference().child("Comments").child(chatID);
            HashMap<String, Object> commentMap = new HashMap<>();
            commentMap.put("comment", inputComment);
            commentMap.put("postedBy", Prevalent.currentOnlineUser.getUsername());
            commentMap.put("postedDate", saveCurrentDate);
            commentMap.put("postedTime", saveCurrentTime);

            commentRef.child(saveCurrentDate+saveCurrentTime).updateChildren(commentMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        commentTypeArea.setText(null);
                    }
                }
            });
        }
    }
}