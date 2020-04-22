package com.example.hw1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VenueRecyclerViewAdapter extends RecyclerView.Adapter<VenueViewHolder> {

    private List<VenueEntry> venueList;
    private Context context;

    public VenueRecyclerViewAdapter(List<VenueEntry> venueList, Context context) {
        this.venueList = venueList;
        this.context = context;
    }

    @NonNull
    @Override
    public VenueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.venue_view_item, parent, false);
        return new VenueViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull VenueViewHolder holder, int position) {
        if (venueList != null && position < venueList.size()){
            VenueEntry venue = venueList.get(position);
            holder.venueId.setText(venue.id);
            holder.venueName.setText(venue.name);
        }
    }

    @Override
    public int getItemCount() { return venueList.size(); }
}
