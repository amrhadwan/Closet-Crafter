package com.example.signuploginfirebase;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

// In OutfitActivity.java
public class OutfitActivity extends AppCompatActivity {

    private RecyclerView outfitRecyclerView;
    private OutfitAdapter outfitAdapter;
    private List<Outfit> outfits;


    // In OutfitActivity.java
    private void fetchOutfits() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("outfits")
                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            outfits.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Outfit outfit = document.toObject(Outfit.class);
                                outfits.add(outfit);
                            }
                            outfitAdapter.notifyDataSetChanged();
                        } else {
                            Log.w("OutfitActivity", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
    // In OutfitActivity.java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfit);

        outfitRecyclerView = findViewById(R.id.outfitRecyclerView);
        outfits = new ArrayList<>();
        outfitAdapter = new OutfitAdapter(outfits, this);

        outfitRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        outfitRecyclerView.setAdapter(outfitAdapter);

        // Call the method to fetch outfits
        fetchOutfits();
    }

    // Add code to fetch outfits from Firestore and update the adapter
}
