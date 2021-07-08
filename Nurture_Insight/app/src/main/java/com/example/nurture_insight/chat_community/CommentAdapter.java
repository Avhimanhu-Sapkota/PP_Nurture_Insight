package com.example.nurture_insight.chat_community;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurture_insight.Model.Comments;
import com.example.nurture_insight.R;
import com.example.nurture_insight.therapist_dashboard.PatientDisplayAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    ArrayList<Comments> comments;
    Context context;

    public CommentAdapter(Context context, ArrayList<Comments> comments) {
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_comment, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        comments.get(position).getChatRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Comments commentsObj = snapshot.getValue(Comments.class);
                    String commentDateTime = commentsObj.getPostedDate() + " at " + commentsObj.getPostedTime();
                    holder.comment_message.setText(commentsObj.getComment());
                    holder.comment_username.setText(commentsObj.getPostedBy());
                    holder.comment_message_date.setText(commentDateTime);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView comment_message, comment_message_date, comment_username;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            comment_message = itemView.findViewById(R.id.comment_message);
            comment_message_date = itemView.findViewById(R.id.comment_message_date);
            comment_username = itemView.findViewById(R.id.comment_username);

        }
    }
}
