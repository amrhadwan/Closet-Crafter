// AddOutfitActivity.java
package com.example.signuploginfirebase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddOutfitActivity extends AppCompatActivity {

    private EditText outfitNameEditText;
    private ImageView outfitImageView;
    private Button addOutfitButton;
    private Uri selectedImage;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_outfit);

        outfitNameEditText = findViewById(R.id.outfitNameEditText);
        outfitImageView = findViewById(R.id.outfitImageView);
        addOutfitButton = findViewById(R.id.addOutfitButton);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        // Set OnClickListener for outfitImageView to open the gallery
        outfitImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the gallery to choose an image
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 100);
            }
        });

        // Set OnClickListener for addOutfitButton to add the outfit
        addOutfitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOutfit();
            }
        });
    }

    private void addOutfit() {
        String outfitName = outfitNameEditText.getText().toString();

        if (outfitName.isEmpty() || selectedImage == null) {
            Toast.makeText(AddOutfitActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a unique identifier for the outfit
        String outfitId = db.collection("outfits").document().getId();

        // Upload the selected image to Firebase Storage
        storage.getReference().child("outfit_images/" + outfitId).putFile(selectedImage)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        String imageUrl = task.getResult().toString();

                                        // Create a new outfit object with the entered data
                                        Outfit outfit = new Outfit(outfitId, outfitName, imageUrl, FirebaseAuth.getInstance().getCurrentUser().getUid());

                                        // Add the outfit to Firestore
                                        db.collection("outfits").document(outfitId).set(outfit)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(AddOutfitActivity.this, "Outfit added successfully", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        } else {
                                                            Toast.makeText(AddOutfitActivity.this, "Failed to add outfit", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(AddOutfitActivity.this, "Failed to get image URL", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(AddOutfitActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // onActivityResult method to handle image selection from the gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            outfitImageView.setImageURI(selectedImage);
        }
    }
}
