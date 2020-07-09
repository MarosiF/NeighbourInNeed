package com.example.neighbourinneed;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterClass extends  RecyclerView.Adapter<AdapterClass.MyViewHolder> {
    private ArrayList<Advertisement> list;
    private OnAdvertisementListener onAdvertisementListener;

    public AdapterClass(ArrayList<Advertisement> list, OnAdvertisementListener onAdvertisementListener) {
        this.list = list;
        this.onAdvertisementListener = onAdvertisementListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_holder, parent, false);

        return new MyViewHolder(view, onAdvertisementListener);
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

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView id, desc, city;
        OnAdvertisementListener onAdvertisementListener;

        public MyViewHolder(@NonNull View itemView, OnAdvertisementListener onAdvertisementListener) {
            super(itemView);
            id = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.description);
            city = itemView.findViewById(R.id.city);
            this.onAdvertisementListener = onAdvertisementListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Advertisement ad = list.get(getAdapterPosition());
            onAdvertisementListener.onAdvertisementClick(getAdapterPosition(), list.get(getAdapterPosition()).getName());
        }
    }

    public interface OnAdvertisementListener {
        void onAdvertisementClick(int position, String nameAd);
    }
}
