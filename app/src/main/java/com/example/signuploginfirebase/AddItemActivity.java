package com.example.signuploginfirebase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddItemActivity extends AppCompatActivity {

    private EditText itemNameEditText;
    private EditText itemCategoryEditText;
    private ImageView itemImageView;
    private Button uploadImageButton;
    private Button submitItemButton;
    private Uri selectedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        itemNameEditText = findViewById(R.id.itemNameEditText);
        itemCategoryEditText = findViewById(R.id.itemCategoryEditText);
        itemImageView = findViewById(R.id.itemImageView);
        uploadImageButton = findViewById(R.id.addItemButton);
        submitItemButton = findViewById(R.id.submitItemButton);
        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 100);
            }
        });

        submitItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImageAndSaveItemData();
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

    private void uploadImageAndSaveItemData() {
        if (selectedImage != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                StorageReference imagesRef = storageRef.child("images/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + itemNameEditText.getText().toString());

                UploadTask uploadTask = imagesRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(AddItemActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                saveItemData(uri.toString());
                            }
                        });
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            saveItemData(null);
        }
    }

    private void saveItemData(String imageUrl) {
        Map<String, Object> itemData = new HashMap<>();
        itemData.put("itemName", itemNameEditText.getText().toString());
        itemData.put("itemCategory", itemCategoryEditText.getText().toString());
        itemData.put("imageUrl", imageUrl);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("items").add(itemData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AddItemActivity.this, "Item added", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddItemActivity.this, "Error adding item", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
