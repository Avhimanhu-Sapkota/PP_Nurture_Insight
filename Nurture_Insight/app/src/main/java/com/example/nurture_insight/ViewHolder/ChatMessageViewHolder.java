package com.example.nurture_insight.ViewHolder;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurture_insight.Interface.ItemClickListener;
import com.example.nurture_insight.R;

public class ChatMessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView chatMessage, chatMessageDate, chatMessageUsername;
    public ItemClickListener listener;
    public CardView chatMessageCard;

    public ChatMessageViewHolder(@NonNull View itemView) {
        super(itemView);

        chatMessage = (TextView) itemView.findViewById(R.id.chat_message);
        chatMessageDate = (TextView) itemView.findViewById(R.id.chat_message_date);
        chatMessageUsername = (TextView) itemView.findViewById(R.id.chat_username);
        chatMessageCard = (CardView) itemView.findViewById(R.id.each_chat_card);



    }

    public  void setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {

        listener.onClick(v, getAdapterPosition(), false);

    }
}
