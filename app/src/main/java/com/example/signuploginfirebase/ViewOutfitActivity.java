// ViewOutfitActivity.java
package com.example.signuploginfirebase;

import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewOutfitActivity extends AppCompatActivity {

    private TextView outfitNameTextView;
    private TextView outfitDetailsTextView; // Add a TextView for displaying outfit details
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_outfit);

        outfitNameTextView = findViewById(R.id.outfitNameTextView);
        outfitDetailsTextView = findViewById(R.id.outfitCategoryTextView); // Initialize the TextView
        db = FirebaseFirestore.getInstance();

        // Retrieve outfitId from intent
        String outfitId = getIntent().getStringExtra("outfitId");

        // Fetch outfit details from Firestore using outfitId
        db.collection("outfits").document(outfitId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Retrieve outfit details from the document
                                String outfitName = document.getString("outfitName");
                                String outfitDetails = document.getString("outfitDetails");

                                // Set the fetched details to the respective TextViews
                                outfitNameTextView.setText(outfitName);
                                outfitDetailsTextView.setText(outfitDetails);
                            } else {
                                // Handle the case where the document does not exist
                                // You might want to show an error message or navigate back
                            }
                        } else {
                            // Handle the error
                            // You might want to show an error message or navigate back
                        }
                    }
                });
    }
}
