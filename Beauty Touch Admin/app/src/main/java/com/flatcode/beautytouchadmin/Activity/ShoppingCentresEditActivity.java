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

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.flatcode.beautytouchadmin.Model.ShoppingCenter;
import com.flatcode.beautytouchadmin.Unit.DATA;
import com.flatcode.beautytouchadmin.Unit.THEME;
import com.flatcode.beautytouchadmin.Unit.VOID;
import com.flatcode.beautytouchadmin.databinding.ActivityShoppingCentersAddBinding;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class ShoppingCentresEditActivity extends AppCompatActivity {

    private ActivityShoppingCentersAddBinding binding;
    Activity activity;
    Context context = activity = ShoppingCentresEditActivity.this;

    String id;

    private Uri imageUri = null, imageUri2 = null;

    private ProgressDialog dialog;

    private int IMAGE_PIC = 1;
    private int IMAGE_MAP = 2;

    private int IMAGE_NUMBER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityShoppingCentersAddBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        id = intent.getStringExtra(DATA.SHOPPING_CENTER_ID);

        dialog = new ProgressDialog(context);
        dialog.setTitle("Please wait...");
        dialog.setCanceledOnTouchOutside(false);

        binding.addImage.setOnClickListener(v -> {
            VOID.CropImageShoppingCenter(activity);
            IMAGE_NUMBER = IMAGE_PIC;
        });
        binding.addImageTwo.setOnClickListener(v -> {
            VOID.CropImageShoppingCenter(activity);
            IMAGE_NUMBER = IMAGE_MAP;
        });

        Data();

        binding.toolbar.nameSpace.setText("Edit the shopping center");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.go.setOnClickListener(v -> validateData());
    }

    private String name = DATA.EMPTY, locationOne = DATA.EMPTY, locationTwo = DATA.EMPTY,
            locationThree = DATA.EMPTY, numberPhone = DATA.EMPTY;

    private void validateData() {
        //get data
        name = binding.name.getText().toString().trim();
        locationOne = binding.location.getText().toString().trim();
        locationTwo = binding.location2.getText().toString().trim();
        locationThree = binding.location3.getText().toString().trim();
        numberPhone = binding.numberPhone.getText().toString().trim();

        //validate data
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(context, "Enter the name of the center", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(locationOne)) {
            Toast.makeText(context, "Enter the city name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(locationTwo)) {
            Toast.makeText(context, "Enter the name of the neighborhood", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(locationThree)) {
            Toast.makeText(context, "Enter the street name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(numberPhone)) {
            Toast.makeText(context, "Enter a phone number", Toast.LENGTH_SHORT).show();
        } else {
            if (imageUri == null && imageUri2 == null) {
                updatePost(DATA.EMPTY, DATA.EMPTY);
            } else {
                if (imageUri != null)
                    uploadImage();
                else if (imageUri2 != null)
                    uploadImage2(null);
            }
        }
    }

    private void uploadImage() {
        dialog.setMessage("Center image being created...");
        dialog.show();

        String filePathAndName = "Images/ImageSessionNow/" + DATA.FirebaseUserUid;

        StorageReference reference = FirebaseStorage.getInstance().getReference(filePathAndName + DATA.DOT + VOID.getFileExtension(imageUri, context));
        reference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isSuccessful()) ;
            String image = DATA.EMPTY + uriTask.getResult();
            if (imageUri2 != null) {
                uploadImage2(image);
            } else {
                updatePost(image, null);
            }
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Something went wrong! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void uploadImage2(String image) {
        dialog.setMessage("Location image being created...");
        dialog.show();

        String filePathAndName = "Images/imageSessionOld/" + DATA.FirebaseUserUid;

        StorageReference reference = FirebaseStorage.getInstance().getReference(filePathAndName + DATA.DOT + VOID.getFileExtension(imageUri, context));
        reference.putFile(imageUri2).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isSuccessful()) ;
            String image2 = DATA.EMPTY + uriTask.getResult();

            updatePost(image, image2);
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Something went wrong! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void updatePost(String image, String image2) {
        dialog.setMessage("Editing....");
        dialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(DATA.NAME, DATA.EMPTY + name);
        hashMap.put("location", DATA.EMPTY + locationOne);
        hashMap.put("location2", DATA.EMPTY + locationTwo);
        hashMap.put("location3", DATA.EMPTY + locationThree);
        hashMap.put("numberPhone", DATA.EMPTY + numberPhone);
        if (imageUri != null)
            hashMap.put("imageurl", DATA.EMPTY + image);
        if (imageUri2 != null)
            hashMap.put("imageurl2", DATA.EMPTY + image2);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.SHOPPING_CENTERS);
        reference.child(id).updateChildren(hashMap).addOnSuccessListener(unused -> {
            dialog.dismiss();
            Toast.makeText(context, "Modified", Toast.LENGTH_SHORT).show();
            //finish();
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Something went wrong! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void Data() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.SHOPPING_CENTERS);
        reference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ShoppingCenter item = snapshot.getValue(ShoppingCenter.class);
                assert item != null;
                String name = item.getName();
                String locationOne = item.getLocation();
                String locationTwo = item.getLocation2();
                String locationThree = item.getLocation3();
                String numberPhone = item.getNumberPhone();
                String image = item.getImageurl();
                String image2 = item.getImageurl2();

                binding.name.setText(name);
                binding.location.setText(locationOne);
                binding.location2.setText(locationTwo);
                binding.location3.setText(locationThree);
                binding.numberPhone.setText(numberPhone);
                VOID.Glide(false, context, image, binding.imageOne);
                VOID.Glide(false, context, image2, binding.imageTwo);
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
                if (IMAGE_NUMBER == IMAGE_PIC) {
                    imageUri = uri;
                } else if (IMAGE_NUMBER == IMAGE_MAP) {
                    imageUri2 = uri;
                }
                //imageUri = uri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                VOID.CropImageShoppingCenter(activity);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                if (IMAGE_NUMBER == IMAGE_PIC) {
                    imageUri = result.getUri();
                    binding.imageOne.setImageURI(imageUri);
                } else if (IMAGE_NUMBER == IMAGE_MAP) {
                    imageUri2 = result.getUri();
                    binding.imageTwo.setImageURI(imageUri2);
                }
                //imageUri = result.getUri();
                //binding.image.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Something went wrong! " + error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}