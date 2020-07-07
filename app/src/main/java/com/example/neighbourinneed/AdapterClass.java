package com.example.neighbourinneed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class AdapterClass extends  RecyclerView.Adapter<AdapterClass.MyViewHolder> {
    ArrayList<Advertisement> list;

    public AdapterClass(ArrayList<Advertisement> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_holder, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.id.setText("Title: " + list.get(position).getName());
        holder.desc.setText("Description: " + list.get(position).getDescription());
        holder.city.setText("City: " + list.get(position).getCity());
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id, desc, city;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.description);
            city = itemView.findViewById(R.id.city);
        }
    }
}
