package com.example.signuploginfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ItemDetailActivity1 extends AppCompatActivity {

    private ImageView itemImage;
    private TextView itemName;
    private TextView itemCategory;
    private Button deleteItemButton;
    private Button updateItemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail1);

        itemImage = findViewById(R.id.image);
        itemName = findViewById(R.id.itemName);
        itemCategory = findViewById(R.id.itemCategory);
        deleteItemButton = findViewById(R.id.deleteItemButton);
        updateItemButton = findViewById(R.id.updateItemButton);

        final String itemId = getIntent().getStringExtra("itemId");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference itemRef = db.collection("items").document(itemId);

        itemRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name = document.getString("name");
                        String category = document.getString("category");
                        String imageUrl = document.getString("imageUrl");

                        itemName.setText(name);
                        itemCategory.setText(category);

                        Glide.with(ItemDetailActivity1.this)
                                .load(imageUrl)
                                .into(itemImage);
                    } else {
                        Log.d("ItemDetailActivity", "No such document");
                    }
                } else {
                    Log.d("ItemDetailActivity", "get failed with ", task.getException());
                }
            }
        });

        deleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemRef.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("ItemDetailActivity", "Error deleting document", e);
                            }
                        });
            }
        });

        updateItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemDetailActivity1.this, UpdateItemActivity.class);
                intent.putExtra("itemId", itemId);
                startActivity(intent);
            }
        });
    }
}
