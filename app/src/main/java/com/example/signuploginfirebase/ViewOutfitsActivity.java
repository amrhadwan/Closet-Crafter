package com.example.signuploginfirebase;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class ViewOutfitsActivity extends AppCompatActivity {

    private RecyclerView outfitsRecyclerView;
    private OutfitAdapter adapter;
    private List<Outfit> outfits;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_outfits);

        outfitsRecyclerView = findViewById(R.id.outfitsRecyclerView);
        db = FirebaseFirestore.getInstance();

        outfits = new ArrayList<>();
        adapter = new OutfitAdapter(outfits, this);
        outfitsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        outfitsRecyclerView.setAdapter(adapter);

        // Load outfits from Firestore
        loadOutfits();
    }

    private void loadOutfits() {
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
                            adapter.notifyDataSetChanged();
                        } else {
                            // Handle the error
                        }
                    }
                });
    }

}
