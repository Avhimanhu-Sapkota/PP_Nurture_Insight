package com.example.nurture_insight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurture_insight.Model.Articles;

import java.util.ArrayList;

public class ScrollingFragment extends Fragment {

    RecyclerView recyclerViewArticles;
    ArrayList<Articles> articles;
    ArticlesAdapter articlesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_scrolling, container, false);

        recyclerViewArticles = (RecyclerView) view.findViewById(R.id.recyclerViewScrolling);
        Integer[] articlesToDisplay = {R.drawable.affirmations, R.drawable.uplifting_quotes, R.drawable.chat_community, R.drawable.calm_down};


        articles = new ArrayList<>();
        for (int index=0; index<articlesToDisplay.length; index++){
            Articles model = new Articles(articlesToDisplay[index]);
            articles.add(model);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false
        );
        recyclerViewArticles.setLayoutManager((layoutManager));
        recyclerViewArticles.setItemAnimator(new DefaultItemAnimator());

        articlesAdapter = new ArticlesAdapter(getContext(), articles);
        recyclerViewArticles.setAdapter(articlesAdapter);

        return view;
    }
}