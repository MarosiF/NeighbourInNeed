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

    // constructor
    public AdapterClass(ArrayList<Advertisement> list, OnAdvertisementListener onAdvertisementListener) {
        this.list = list;
        this.onAdvertisementListener = onAdvertisementListener;
    }

    /**
     * This method is called, when the RecyclerView needs a new ViewHolder in order to represent an item.
     * @param parent
     * @param viewType
     * @return MyViewHolder
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // inflate the View from given XML-file
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_holder, parent, false);

        return new MyViewHolder(view, onAdvertisementListener);
    }

    /**
     * Binds data at a given position (from the given list) to the ViewHolder.itemView, in order to display the
     * advertisements attributes (name, description, city). Is called by the RecyclerView.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.id.setText("Title: " + list.get(position).getName());
        holder.desc.setText("Description: " + list.get(position).getDescription());
        holder.city.setText("City: " + list.get(position).getCity());
    }

    /**
     * This method returns the size of the list, which is given to the adapter, when initializing it.
     * @return size of the list
     */
    @Override
    public int getItemCount() {
        return this.list.size();
    }

    // View holder for the advertisements
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView id, desc, city;
        // interface (see below)
        OnAdvertisementListener onAdvertisementListener;

        // constructor
        public MyViewHolder(@NonNull View itemView, OnAdvertisementListener onAdvertisementListener) {
            super(itemView);
            id = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.description);
            city = itemView.findViewById(R.id.city);
            this.onAdvertisementListener = onAdvertisementListener;

            // ClickListener
            itemView.setOnClickListener(this);
        }

        /**
         * ClickListener for the View (in this case our advertisement representation)
         * @param v
         */
        @Override
        public void onClick(View v) {
            Advertisement ad = list.get(getAdapterPosition());
            // position and name of clicked advertisement
            onAdvertisementListener.onAdvertisementClick(getAdapterPosition(), list.get(getAdapterPosition()).getName());
        }
    }

    public interface OnAdvertisementListener {
        void onAdvertisementClick(int position, String nameAd);
    }
}
