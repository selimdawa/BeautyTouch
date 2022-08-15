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
import com.flatcode.beautytouchadmin.Unit.DATA;
import com.flatcode.beautytouchadmin.Unit.THEME;
import com.flatcode.beautytouchadmin.Unit.VOID;
import com.flatcode.beautytouchadmin.databinding.ActivityShoppingCentersAddBinding;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class ShoppingCentersAddActivity extends AppCompatActivity {

    private ActivityShoppingCentersAddBinding binding;
    Activity activity;
    Context context = activity = ShoppingCentersAddActivity.this;

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

        binding.toolbar.nameSpace.setText("Add a shopping center");
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
        } else if (imageUri == null) {
            Toast.makeText(context, "There is no center image!", Toast.LENGTH_SHORT).show();
        } else if (imageUri2 == null) {
            Toast.makeText(context, "There is no location picture!", Toast.LENGTH_SHORT).show();
        } else {
            uploadImage();
        }
    }

    private void uploadImage() {
        dialog.setMessage("Center image being created...");
        dialog.show();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.SHOPPING_CENTERS);
        String id = ref.push().getKey();

        String filePathAndName = "Images/ShoppingCentres/image" + id;

        StorageReference reference = FirebaseStorage.getInstance().getReference(filePathAndName + DATA.DOT + VOID.getFileExtension(imageUri, context));
        reference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isSuccessful()) ;
            String image = DATA.EMPTY + uriTask.getResult();
            if (imageUri2 != null) {
                uploadImage2(image, ref, id);
            } else {
                uploadInfoDB(image, null, ref, id);
            }
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Something went wrong! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void uploadImage2(String image, DatabaseReference ref, String id) {
        dialog.setMessage("Location image being created...");
        dialog.show();

        String filePathAndName = "Images/ShoppingCentres/" + id + "2";

        StorageReference reference = FirebaseStorage.getInstance().getReference(filePathAndName + DATA.DOT + VOID.getFileExtension(imageUri, context));
        reference.putFile(imageUri2).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isSuccessful()) ;
            String image2 = DATA.EMPTY + uriTask.getResult();

            uploadInfoDB(image, image2, ref, id);
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Something went wrong! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void uploadInfoDB(String image, String image2, DatabaseReference ref, String id) {
        dialog.setMessage("Shopping center under construction...");
        dialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("aname", DATA.APP_NAME);
        hashMap.put("id", id);
        hashMap.put("imageurl", image);
        hashMap.put("imageurl2", image2);
        hashMap.put("location", binding.location.getText().toString().trim());
        hashMap.put("location2", binding.location2.getText().toString().trim());
        hashMap.put("location3", binding.location3.getText().toString().trim());
        hashMap.put("name", binding.name.getText().toString().trim());
        hashMap.put("numberPhone", binding.numberPhone.getText().toString().trim());
        hashMap.put("timeStamp", DATA.EMPTY + System.currentTimeMillis());
        hashMap.put("publisher", DATA.EMPTY + DATA.FirebaseUserUid);

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