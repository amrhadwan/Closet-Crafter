package com.example.signuploginfirebase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class UpdateItemActivity extends AppCompatActivity {
    private EditText itemNameEditText;
    private EditText itemCategoryEditText;
    private ImageView itemImageView;
    private Button updateItemButton;
    private Uri selectedImage;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private DocumentReference itemRef;
    private String itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);

        itemNameEditText = findViewById(R.id.itemNameEditText);
        itemCategoryEditText = findViewById(R.id.itemCategoryEditText);
        itemImageView = findViewById(R.id.itemImageView);
        updateItemButton = findViewById(R.id.updateItemButton);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        itemId = getIntent().getStringExtra("itemId");
        itemRef = db.collection("items").document(itemId);

        itemRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        String name = document.getString("name");
                        String category = document.getString("category");
                        String imageUrl = document.getString("imageUrl");

                        itemNameEditText.setText(name);
                        itemCategoryEditText.setText(category);
                        Glide.with(UpdateItemActivity.this).load(imageUrl).into(itemImageView);
                    } else {
                        Toast.makeText(UpdateItemActivity.this, "No such document", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UpdateItemActivity.this, "Error getting document: " + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        itemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the gallery to choose an image
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 100);
            }
        });

        updateItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemName = itemNameEditText.getText().toString();
                String itemCategory = itemCategoryEditText.getText().toString();

                if (itemName.isEmpty() || itemCategory.isEmpty()) {
                    Toast.makeText(UpdateItemActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, Object> item = new HashMap<>();
                item.put("name", itemName);
                item.put("category", itemCategory);

                if (selectedImage != null) {
                    // Upload the selected image to Firebase Storage
                    storage.getReference().child("images/" + itemId).putFile(selectedImage)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Get the download URL of the uploaded image
                                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            // Update the item with the new image URL
                                            item.put("imageUrl", uri.toString());
                                            updateItem(item);
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(UpdateItemActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    // If no new image is selected, update the item without changing the image
                    updateItem(item);
                }
            }
        });
    }

    private void updateItem(Map<String, Object> item) {
        // Update the item in Firestore
        itemRef.update(item).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UpdateItemActivity.this, "Item updated successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Finish the activity after a successful update
                } else {
                    Toast.makeText(UpdateItemActivity.this, "Item update failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            itemImageView.setImageURI(selectedImage);
        }
    }
}
