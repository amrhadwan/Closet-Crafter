package com.example.signuploginfirebase;

import android.os.Bundle;
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

public class OutfitCreatorActivity extends AppCompatActivity {
    private RecyclerView wardrobeRecyclerView;
    private ArrayList<Item> items;
    private WardrobeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfit_creator);

        wardrobeRecyclerView = findViewById(R.id.wardrobeRecyclerView);
        items = new ArrayList<>();
        adapter = new WardrobeAdapter(items, this);

        wardrobeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        wardrobeRecyclerView.setAdapter(adapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("wardrobe")
                .whereEqualTo("userId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Item item = document.toObject(Item.class);
                                items.add(item);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            // Handle the error
                        }
                    }
                });
    }
}
