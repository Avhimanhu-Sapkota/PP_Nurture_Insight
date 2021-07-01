package com.example.nurture_insight.ViewHolder;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurture_insight.Model.Assessment;
import com.example.nurture_insight.Model.events;
import com.example.nurture_insight.R;
import com.example.nurture_insight.therapist_dashboard.EventDisplayAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.ViewHolder> {

    ArrayList<Assessment> assessments;
    Context context;

    public AssessmentAdapter(Context context, ArrayList<Assessment> assessments) {
        this.assessments = assessments;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_assessment_display, parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Drawable progressDrawable = holder.assessmentProgressBar.getProgressDrawable().mutate();
        progressDrawable.setColorFilter(context.getResources().getColor(R.color.ni_blue), PorterDuff.Mode.SRC_IN);
        holder.assessmentProgressBar.setProgressDrawable(progressDrawable);

        DatabaseReference assessmentRef = assessments.get(position).getAssessmentRef();
        assessmentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String displayDate = snapshot.getKey();
                     holder.assessmentProgressDate.setText(displayDate);

                     double scoreDouble = Double.parseDouble(snapshot.child("score").getValue().toString());
                     double scorePercent = (scoreDouble / 25.0f) * 100;
                     double scorePercentDouble = (scorePercent / 100.0f) * 25;
                     int scoreInt = (int) scorePercent;
                     int scorePercentDoubleInt = (int) scorePercentDouble;
                     holder.assessmentProgressBar.setProgress(scoreInt);
                     holder.assessmentProgressScore.setText(scorePercentDoubleInt + "/25");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return assessments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView assessmentProgressDate, assessmentProgressScore;
        ProgressBar assessmentProgressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            assessmentProgressDate = itemView.findViewById(R.id.assessmentProgressDate);
            assessmentProgressBar = itemView.findViewById(R.id.assessmentProgressBar);
            assessmentProgressScore = itemView.findViewById(R.id.assessmentProgressScore);
        }
    }
}
