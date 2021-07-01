package com.example.nurture_insight.Self_Care_Packages;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nurture_insight.Home.ArticlesAdapter;
import com.example.nurture_insight.Model.Self_Care_Packages;
import com.example.nurture_insight.R;
import com.example.nurture_insight.therapist_dashboard.EventDetailsFragment;

import java.util.ArrayList;

public class Self_Care_Packages_Adapter extends RecyclerView.Adapter<Self_Care_Packages_Adapter.ViewHolder> {

    ArrayList<Self_Care_Packages> self_care_packages;
    Context context;
    String packageType = " ";

    public Self_Care_Packages_Adapter(Context context, ArrayList<Self_Care_Packages> self_care_packages){
        this.context = context;
        this.self_care_packages = self_care_packages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_self_care_package, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.package_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.package_image.setImageResource(self_care_packages.get(position).getSelfCarePackages());
        holder.package_title.setText(self_care_packages.get(position).getSelfCarePackagesTitle());
        holder.package_desc.setText(self_care_packages.get(position).getSelfCarePackagesDesc());
        holder.eachPackageCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch(position){
                    case 0:
                        packageType = "Guided Meditation";
                        break;
                    case 1:
                        packageType = "Relaxation Music";
                        break;
                    case 2:
                        packageType = "Body and Mind Yoga";
                        break;
                    case 3:
                        packageType = "Uplifting Stories";
                        break;
                    case 4:
                        packageType = "Breathwork Exercises";
                        break;
                }

                Fragment fragment = new self_care_exercise_list();
                Bundle bundle = new Bundle();
                bundle.putString("type", packageType);
                fragment.setArguments(bundle);
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_fragmentLayout, fragment).addToBackStack(null).commit();

            }
        });
    }

    @Override
    public int getItemCount() {
        return self_care_packages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView package_image;
        TextView package_title, package_desc;
        CardView eachPackageCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            package_image = itemView.findViewById(R.id.package_image);
            package_title = itemView.findViewById(R.id.package_title);
            package_desc = itemView.findViewById(R.id.package_description);
            eachPackageCard = itemView.findViewById(R.id.self_care_package_cardView);
        }
    }
}
