package com.example.nurture_insight;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurture_insight.Model.Articles;
import com.example.nurture_insight.articles.article_anger;
import com.example.nurture_insight.articles.article_anxiety;
import com.example.nurture_insight.articles.article_depression;
import com.example.nurture_insight.articles.article_selfEsteem;
import com.example.nurture_insight.articles.article_stress;
import com.example.nurture_insight.instant_help.live_in_present;

import java.util.ArrayList;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    ArrayList<Articles> articles;
    Context context;

    public ArticlesAdapter(Context context, ArrayList<Articles> articles){
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.imageView.setImageResource(articles.get(position).getArticlesToDisplay());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Fragment myFragment = null;

                switch (position) {
                    case 0:
                        myFragment = new article_anger();
                        break;
                    case 1:
                        myFragment = new article_anxiety();
                        break;
                    case 2:
                        myFragment = new article_depression();
                        break;
                    case 3:
                        myFragment = new article_selfEsteem();
                        break;
                    case 4:
                        myFragment = new article_stress();
                        break;
                }

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragmentLayout, myFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.articles_imageView);

        }
    }
}
