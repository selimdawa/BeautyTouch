package com.flatcode.beautytouchadmin.Activity;

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

import com.flatcode.beautytouchadmin.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.flatcode.beautytouchadmin.Model.Post;
import com.flatcode.beautytouchadmin.Unit.DATA;
import com.flatcode.beautytouchadmin.Unit.THEME;
import com.flatcode.beautytouchadmin.Unit.VOID;
import com.flatcode.beautytouchadmin.databinding.ActivityPostAddBinding;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class PostEditActivity extends AppCompatActivity {

    private ActivityPostAddBinding binding;
    Activity activity;
    Context context = activity = PostEditActivity.this;

    String id;

    private Uri imageUri = null;
    private ProgressDialog dialog;

    String typePost = DATA.EMPTY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityPostAddBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        id = intent.getStringExtra(DATA.POST_ID);

        dialog = new ProgressDialog(context);
        dialog.setTitle("Please wait...");
        dialog.setCanceledOnTouchOutside(false);

        binding.toolbar.nameSpace.setText(R.string.add_post);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        binding.typeOne.setOnClickListener(v -> {
            typePost = "Skin Products";
            binding.typeOne.setText("Skin Products ✓");
            binding.typeTwo.setText("Hair Products");
        });

        binding.typeTwo.setOnClickListener(v -> {
            typePost = "Hair Products";
            binding.typeOne.setText("Skin Products");
            binding.typeTwo.setText("Hair Products ✓");
        });

        loadPostInfo();

        binding.toolbar.nameSpace.setText("Edit post");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.go.setOnClickListener(v -> validateData());
        binding.layoutImageProfile.setOnClickListener(v -> VOID.CropImageSquare(activity));
    }

    private String name = DATA.EMPTY, indications = DATA.EMPTY, howToUse = DATA.EMPTY, price = DATA.EMPTY;

    private void validateData() {
        //get data
        name = binding.name.getText().toString().trim();
        indications = binding.indications.getText().toString().trim();
        howToUse = binding.howToUse.getText().toString().trim();
        price = binding.price.getText().toString().trim();

        //validate data
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(context, "Enter a name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(indications)) {
            Toast.makeText(context, "Enter the indications", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(howToUse)) {
            Toast.makeText(context, "Enter how to use", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(price)) {
            Toast.makeText(context, "Enter a price", Toast.LENGTH_SHORT).show();
        } else {
            if (imageUri == null) {
                updatePost(DATA.EMPTY);
            } else {
                uploadImage();
            }
        }
    }

    private void uploadImage() {
        dialog.setMessage("Post is updating");
        dialog.show();

        String filePathAndName = "Images/Posts/" + id;

        StorageReference reference = FirebaseStorage.getInstance().getReference(filePathAndName + DATA.DOT + VOID.getFileExtension(imageUri, context));
        reference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isSuccessful()) ;
            String uploadedImageUrl = DATA.EMPTY + uriTask.getResult();

            updatePost(uploadedImageUrl);
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Something went wrong! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void updatePost(String imageUrl) {
        dialog.setMessage("Editing....");
        dialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(DATA.NAME, DATA.EMPTY + name);
        hashMap.put("price", DATA.EMPTY + price);
        hashMap.put("indications", DATA.EMPTY + indications);
        hashMap.put("use", DATA.EMPTY + howToUse);
        hashMap.put("category", DATA.EMPTY + typePost);
        if (imageUri != null) {
            hashMap.put("postimage", DATA.EMPTY + imageUrl);
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.POSTS);
        reference.child(id).updateChildren(hashMap).addOnSuccessListener(unused -> {
            dialog.dismiss();
            Toast.makeText(context, "Modified", Toast.LENGTH_SHORT).show();
            //finish();
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Something went wrong! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void loadPostInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.POSTS);
        reference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post item = snapshot.getValue(Post.class);
                assert item != null;
                String name = item.getName();
                String price = item.getPrice();
                String indications = item.getIndications();
                String howToUse = item.getUse();
                String category = item.getCategory();
                String image = item.getPostimage();
                typePost = category;

                VOID.Glide(true, context, image, binding.image);
                binding.name.setText(name);
                binding.price.setText(price);
                binding.indications.setText(indications);
                binding.howToUse.setText(howToUse);

                if (category.equals("Skin Products")) {
                    binding.typeOne.setText("Skin Products ✓");
                    binding.typeTwo.setText("Hair Products");
                    typePost = category;
                } else if (category.equals("Hair Products")) {
                    binding.typeOne.setText("Skin Products");
                    binding.typeTwo.setText("Hair Products ✓");
                    typePost = category;
                }
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
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Something went wrong! " + error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}