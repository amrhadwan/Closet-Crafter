package com.example.signuploginfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button; // Make sure to import Button from the correct package
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class WardrobeActivity extends AppCompatActivity {

    private RecyclerView wardrobeRecyclerView;
    private FloatingActionButton addItemButton;
    private Button viewOutfitsButton; // Added button for viewing outfits
    private WardrobeAdapter adapter;
    private List<Item> items;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wardrobe);
        Button viewOutfitsButton = findViewById(R.id.viewOutfitsButton);
        wardrobeRecyclerView = findViewById(R.id.wardrobeRecyclerView);
        addItemButton = findViewById(R.id.addItemFloatingButton);
        viewOutfitsButton = findViewById(R.id.viewOutfitsButton); // Initialize the button
        db = FirebaseFirestore.getInstance();

        items = new ArrayList<>();
        adapter = new WardrobeAdapter((ArrayList<Item>) items, this);
        wardrobeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        wardrobeRecyclerView.setAdapter(adapter);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WardrobeActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });

        viewOutfitsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Replace ViewOutfitsActivity.class with your desired activity
                Intent intent = new Intent(WardrobeActivity.this, ViewOutfitsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateWardrobe();
    }

    private void updateWardrobe() {
        db.collection("wardrobe")
                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            items.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Item item = document.toObject(Item.class);
                                items.add(item);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Log.w("WardrobeActivity", "Error getting documents.", task.getException());
                        }
                    }
                });
        adapter.setOnItemClickListener(new WardrobeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(com.Item position) {

            }


            public void onItemClick(int position) {
                Item clickedItem = items.get(position);
                Intent intent = new Intent(WardrobeActivity.this, UpdateItemActivity.class);
                intent.putExtra("itemId", clickedItem.getItemId());
                startActivity(intent);
            }
        });
    }
}


