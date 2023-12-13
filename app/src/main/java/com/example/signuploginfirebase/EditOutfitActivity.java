// EditOutfitActivity.java
package com.example.signuploginfirebase;

import com.bumptech.glide.Glide;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import java.util.HashMap;
import java.util.Map;

public class EditOutfitActivity extends AppCompatActivity {

    private EditText editOutfitNameEditText;
    private EditText editOutfitDescriptionEditText;
    private ImageView editOutfitImageView;
    private Button changeOutfitImageButton;
    private Button updateOutfitButton;
    private Uri selectedImage;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private String outfitId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_outfit);

        editOutfitNameEditText = findViewById(R.id.editItemNameEditText);
        editOutfitDescriptionEditText = findViewById(R.id.editItemCategoryEditText);
        editOutfitImageView = findViewById(R.id.editOutfitImageView);
        changeOutfitImageButton = findViewById(R.id.changeOutfitImageButton);
        updateOutfitButton = findViewById(R.id.updateItemButton);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        outfitId = getIntent().getStringExtra("outfitId");

        // Fetch outfit details from Firestore using outfitId
        db.collection("outfits").document(outfitId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Populate the EditText fields with existing data
                                editOutfitNameEditText.setText(document.getString("outfitName"));
                                editOutfitDescriptionEditText.setText(document.getString("outfitDescription"));

                                // Load the existing image into ImageView using Glide or any other library
                                Glide.with(EditOutfitActivity.this).load(document.getString("outfitImageUrl")).into(editOutfitImageView);
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

        // Set OnClickListener for editOutfitImageView to open the gallery
        editOutfitImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the gallery to choose an image
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 100);
            }
        });

        // Set OnClickListener for updateOutfitButton to update the outfit
        updateOutfitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateOutfit();
            }
        });
    }

    private void updateOutfit() {
        String outfitName = editOutfitNameEditText.getText().toString();
        String outfitDescription = editOutfitDescriptionEditText.getText().toString();

        if (outfitName.isEmpty() || outfitDescription.isEmpty()) {
            Toast.makeText(EditOutfitActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> updatedOutfit = new HashMap<>();
        updatedOutfit.put("outfitName", outfitName);
        updatedOutfit.put("outfitDescription", outfitDescription);

        if (selectedImage != null) {
            // If a new image is selected, upload it to Firebase Storage and update the outfit
            storage.getReference().child("images/" + outfitId).putFile(selectedImage)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            String imageUrl = task.getResult().toString();
                                            updatedOutfit.put("outfitImageUrl", imageUrl);

                                            // Update the outfit in Firestore
                                            db.collection("outfits").document(outfitId).update(updatedOutfit)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(EditOutfitActivity.this, "Outfit updated successfully", Toast.LENGTH_SHORT).show();
                                                                finish();
                                                            } else {
                                                                Toast.makeText(EditOutfitActivity.this, "Outfit update failed", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(EditOutfitActivity.this, "Failed to get image URL", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(EditOutfitActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            // If no new image is selected, update the outfit without changing the image
            db.collection("outfits").document(outfitId).update(updatedOutfit)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(EditOutfitActivity.this, "Outfit updated successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(EditOutfitActivity.this, "Outfit update failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    // onActivityResult method to handle image selection from the gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            editOutfitImageView.setImageURI(selectedImage);
        }
    }
}
