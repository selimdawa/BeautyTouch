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

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.flatcode.beautytouchadmin.R;
import com.flatcode.beautytouchadmin.Unit.DATA;
import com.flatcode.beautytouchadmin.Unit.THEME;
import com.flatcode.beautytouchadmin.Unit.VOID;
import com.flatcode.beautytouchadmin.databinding.ActivityPostAddBinding;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class PostAddActivity extends AppCompatActivity {

    private ActivityPostAddBinding binding;
    Activity activity;
    Context context = activity = PostAddActivity.this;

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
        } else if (typePost.equals(DATA.EMPTY)) {
            Toast.makeText(context, "Enter a product category", Toast.LENGTH_SHORT).show();
        } else if (imageUri == null) {
            Toast.makeText(context, "There's no picture!", Toast.LENGTH_SHORT).show();
        } else {
            uploadToStorage();
        }
    }

    private void uploadToStorage() {
        dialog.setMessage("Post being created...");
        dialog.show();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.POSTS);
        String id = ref.push().getKey();

        String filePathAndName = "Images/Posts/" + id;

        StorageReference reference = FirebaseStorage.getInstance().getReference(filePathAndName + DATA.DOT + VOID.getFileExtension(imageUri, context));
        reference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isSuccessful()) ;
            String uploadedImageUrl = DATA.EMPTY + uriTask.getResult();

            uploadInfoDB(uploadedImageUrl, id, ref);
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Something went wrong! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void uploadInfoDB(String uploadedImageUrl, String id, DatabaseReference ref) {
        dialog.setMessage("Publishing...");
        dialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("aname", DATA.APP_NAME);
        hashMap.put("category", DATA.EMPTY + typePost);
        hashMap.put("indications", binding.indications.getText().toString().trim());
        hashMap.put("name", binding.name.getText().toString().trim());
        hashMap.put("postid", id);
        hashMap.put("postimage", uploadedImageUrl);
        hashMap.put("postimage2", DATA.EMPTY);
        hashMap.put("postimage3", DATA.EMPTY);
        hashMap.put("postimage4", DATA.EMPTY);
        hashMap.put("postimage5", DATA.EMPTY);
        hashMap.put("postimage6", DATA.EMPTY);
        hashMap.put("postimage7", DATA.EMPTY);
        hashMap.put("postimage8", DATA.EMPTY);
        hashMap.put("postimage9", DATA.EMPTY);
        hashMap.put("postimage10", DATA.EMPTY);
        hashMap.put("timeStamp", DATA.EMPTY + System.currentTimeMillis());
        hashMap.put("price", binding.price.getText().toString().trim());
        hashMap.put("publisher", DATA.EMPTY + DATA.FirebaseUserUid);
        hashMap.put("use", binding.howToUse.getText().toString().trim());

        assert id != null;
        ref.child(id).setValue(hashMap).addOnSuccessListener(unused -> {
            dialog.dismiss();
            Toast.makeText(context, "uploaded", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Something went wrong! " + e.getMessage(), Toast.LENGTH_SHORT).show();
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