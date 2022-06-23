package com.example.whoru;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {


    private ArrayList<Find>arrayList;
    private Context context;

    public CustomAdapter(ArrayList<Find> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detection_list, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getDetection_img())
                .into(holder.detection_img);
        holder.detection_time.setText(arrayList.get(position).getTime());
        holder.detection_day.setText(arrayList.get(position).getDay());

    }

    @Override
    public int getItemCount() {
        return (arrayList !=null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView detection_img;
        TextView detection_time;
        TextView detection_day;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.detection_img =itemView.findViewById(R.id.detection_img);
            this.detection_time = itemView.findViewById(R.id.detection_time);
            this.detection_day = itemView.findViewById(R.id.detection_day);
        }
    }
}
