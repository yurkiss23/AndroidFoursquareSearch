package com.example.hw1;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VenueViewHolder extends RecyclerView.ViewHolder {
    private View view;
    public TextView venueId;
    public TextView venueName;

    public VenueViewHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        venueId = itemView.findViewById(R.id.venue_id);
        venueName = itemView.findViewById(R.id.venue_name);
    }
    public View getView(){return view;}
}
