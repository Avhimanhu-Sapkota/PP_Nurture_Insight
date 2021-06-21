package com.example.nurture_insight.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurture_insight.Interface.ItemClickListener;
import com.example.nurture_insight.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class eachTherapistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView eachTherapistName, eachTherapistExpertise, eachTherapistWorksAt, eachTherapistBio, eachTherapistContact;
    public CircleImageView eachTherapistProfileImage;
    public ItemClickListener listener;

    public eachTherapistViewHolder(@NonNull View itemView) {
        super(itemView);

        eachTherapistName = (TextView) itemView.findViewById(R.id.eachTherapist_Name);
        eachTherapistExpertise = (TextView) itemView.findViewById(R.id.eachTherapist_expertise);
        eachTherapistWorksAt = (TextView) itemView.findViewById(R.id.eachTherapist_worksAt);
        eachTherapistBio = (TextView) itemView.findViewById(R.id.eachTherapist_bio);
        eachTherapistContact = (TextView) itemView.findViewById(R.id.eachTherapist_contact);
        eachTherapistProfileImage = (CircleImageView) itemView.findViewById(R.id.eachTherapist_profileImage);
    }

    public  void setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
