package com.example.signuploginfirebase;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.signuploginfirebase.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.BreakIterator;
import java.util.List;

// In OutfitAdapter.java
public class OutfitAdapter extends RecyclerView.Adapter<OutfitAdapter.ViewHolder> {

    private List<Outfit> outfits;
    private Context context;

    public OutfitAdapter(List<Outfit> outfits, Context context) {
        this.outfits = outfits;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_outfit, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public int getItemCount() {
        return outfits.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public BreakIterator outfitNameTextView;

        // Declare your outfit item views here (e.g., TextViews, ImageViews)

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize your outfit item views here
        }
    }
    // In OutfitAdapter.java
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Outfit outfit = outfits.get(position);
        holder.outfitNameTextView.setText(outfit.getOutfitName());

        // Populate the view with outfit details
        // You can use Glide for image loading, similar to what you did in WardrobeAdapter

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open ViewOutfitActivity and pass the outfitId
                Intent intent = new Intent(context, ViewOutfitActivity.class);
                intent.putExtra("outfitId", outfit.getOutfitId());
                context.startActivity(intent);
            }
        });
    }

}
