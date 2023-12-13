package com.example.signuploginfirebase;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Item;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class WardrobeAdapter extends RecyclerView.Adapter<WardrobeAdapter.ViewHolder> {
    private ArrayList<Item> items;
    private Context context;
    private List<Item> selectedItems;
    private OnItemClickListener mListener;

    public WardrobeAdapter(ArrayList<Item> items, Context context) {
        this.items = items;
        this.context = context;
        selectedItems = new ArrayList<>();
    }
    public List<Item> getSelectedItems() {
        return selectedItems;
    }

    public WardrobeAdapter(ArrayList<com.example.signuploginfirebase.Item> items, OutfitCreatorActivity context) {

    }
    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
         mListener = (OnItemClickListener) listener;
    }

    public WardrobeAdapter(List<com.example.signuploginfirebase.Item> items, WardrobeActivity wardrobeActivity) {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wardrobe, parent, false);
        selectedItems = new ArrayList<>();
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int p) {
        Item item = items.get(holder.getAdapterPosition());
        holder.itemNameTextView.setText(item.getItemName());
        holder.itemCategoryTextView.setText(item.getItemCategory());
        Glide.with(context).load(item.getItemImageUrl()).into(holder.itemImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(item);
                }

                int adapterPosition = holder.getBindingAdapterPosition();
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    Item item = items.get(adapterPosition);

                    // Your existing code for starting UpdateItemActivity
                    Intent intent = new Intent(context, UpdateItemActivity.class);
                    intent.putExtra("itemId", item.getItemId());
                    context.startActivity(intent);

                    // Your existing code for managing selectedItems
                    if (selectedItems.contains(item)) {
                        selectedItems.remove(item);
                    } else {
                        selectedItems.add(item);
                    }
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return 0;
    }
    public void setItems(List<Item> items) {
        this.items = (ArrayList<Item>) items;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {

        mListener = listener;

    }

    public interface OnItemClickListener {
        void onItemClick(Item position);
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImageView;
        TextView itemNameTextView;
        TextView itemCategoryTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImageView = itemView.findViewById(R.id.itemImageView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            itemCategoryTextView = itemView.findViewById(R.id.itemCategoryTextView);
        }
    }
}
