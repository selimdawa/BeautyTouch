package com.flatcode.beautytouch.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.flatcode.beautytouch.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.flatcode.beautytouch.Model.User;
import com.flatcode.beautytouch.Unit.DATA;
import com.flatcode.beautytouch.Unit.THEME;
import com.flatcode.beautytouch.Unit.VOID;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    Activity activity;
    Context context = activity = ProfileActivity.this;

    private Uri imageUri = null;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        dialog = new ProgressDialog(context);
        dialog.setTitle("Please wait...");
        dialog.setCanceledOnTouchOutside(false);

        loadUserInfo();

        binding.back.setOnClickListener(v -> onBackPressed());
        binding.editImageIcon.setOnClickListener(v -> VOID.CropImageSquare(activity));

        binding.imageEdit.setOnClickListener(v -> {
            binding.imageEdit.setVisibility(View.GONE);
            binding.imageClose.setVisibility(View.VISIBLE);
            binding.imageTrue.setVisibility(View.VISIBLE);
            binding.name.setVisibility(View.GONE);
            binding.nameEdit.setVisibility(View.VISIBLE);
        });
        binding.imageClose.setOnClickListener(v -> {
            binding.imageEdit.setVisibility(View.VISIBLE);
            binding.imageClose.setVisibility(View.GONE);
            if (imageUri != null) {
                binding.imageTrue.setVisibility(View.VISIBLE);
            } else {
                binding.imageTrue.setVisibility(View.GONE);
            }
            binding.name.setVisibility(View.VISIBLE);
            binding.nameEdit.setVisibility(View.GONE);
        });
        binding.imageTrue.setOnClickListener(v -> {
            binding.imageClose.setVisibility(View.GONE);
            binding.imageEdit.setVisibility(View.VISIBLE);
            binding.imageTrue.setVisibility(View.GONE);
            binding.name.setVisibility(View.VISIBLE);
            binding.name.setText(binding.nameEdit.getText().toString());
            binding.nameEdit.setVisibility(View.GONE);
            validateData();
        });
    }

    private String username = DATA.EMPTY;

    private void validateData() {
        username = binding.nameEdit.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(context, "Please enter the name", Toast.LENGTH_SHORT).show();
        } else {
            if (imageUri == null) {
                updateProfile(DATA.EMPTY);
            } else {
                uploadImage();
            }
        }
    }

    private void uploadImage() {
        dialog.setMessage("The image is loading...");
        dialog.show();

        String filePathAndName = "Images/Profile/" + DATA.FirebaseUserUid;

        StorageReference reference = FirebaseStorage.getInstance().getReference(filePathAndName + DATA.DOT + VOID.getFileExtension(imageUri, context));

        reference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isSuccessful()) ;
            String uploadedImageUrl = DATA.EMPTY + uriTask.getResult();

            updateProfile(uploadedImageUrl);
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Something went wrong! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void updateProfile(String imageUrl) {
        dialog.setMessage("Modifications are loaded...");
        dialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(DATA.USER_NAME, DATA.EMPTY + username);
        if (imageUri != null) {
            hashMap.put(DATA.IMAGE_URL, DATA.EMPTY + imageUrl);
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.USERS);
        reference.child(Objects.requireNonNull(DATA.FirebaseUserUid)).updateChildren(hashMap).addOnSuccessListener(unused -> {
            dialog.dismiss();
            Toast.makeText(context, "Error loading image...", Toast.LENGTH_SHORT).show();
            //finish();
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Something went wrong! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void loadUserInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.USERS);
        reference.child(Objects.requireNonNull(DATA.FirebaseUserUid)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                assert user != null;

                Glide.with(ProfileActivity.this).load(user.getImageurl()).into(binding.image);
                binding.name.setText(user.getUsername());
                binding.nameEdit.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = CropImage.getPickImageResultUri(context, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(context, uri)) {
                imageUri = uri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                VOID.CropImageSquare(activity);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                binding.image.setImageURI(imageUri);
                binding.imageTrue.setVisibility(View.VISIBLE);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Something went wrong! " + error, Toast.LENGTH_SHORT).show();
                binding.imageTrue.setVisibility(View.GONE);
            }
        }
    }
}