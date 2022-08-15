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
import com.flatcode.beautytouchadmin.Model.Tools;
import com.flatcode.beautytouchadmin.Unit.DATA;
import com.flatcode.beautytouchadmin.Unit.THEME;
import com.flatcode.beautytouchadmin.Unit.VOID;
import com.flatcode.beautytouchadmin.databinding.ActivityToolsBinding;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class ToolsActivity extends AppCompatActivity {

    private ActivityToolsBinding binding;
    Activity activity;
    Context context = activity = ToolsActivity.this;

    private Uri imageUri = null, imageUri2 = null, imageUri3 = null, imageUri4 = null;

    private ProgressDialog dialog;

    private int IMAGE_NOW = 1;
    private int IMAGE_OLD = 2;
    private int LOGO_NOW = 3;
    private int LOGO_OLD = 4;

    private int IMAGE_NUMBER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityToolsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        dialog = new ProgressDialog(context);
        dialog.setTitle("Please wait...");
        dialog.setCanceledOnTouchOutside(false);

        Data();

        binding.editImageSessionNow.setOnClickListener(v -> {
            VOID.CropImageSession(activity);
            IMAGE_NUMBER = IMAGE_NOW;
        });
        binding.editImageSessionOld.setOnClickListener(v -> {
            VOID.CropImageSession(activity);
            IMAGE_NUMBER = IMAGE_OLD;
        });
        binding.editLogoSessionNow.setOnClickListener(v -> {
            VOID.CropImageSession(activity);
            IMAGE_NUMBER = LOGO_NOW;
        });
        binding.editLogoSessionOld.setOnClickListener(v -> {
            VOID.CropImageSession(activity);
            IMAGE_NUMBER = LOGO_OLD;
        });
        binding.toolbar.nameSpace.setText("Tools");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());
        binding.go.setOnClickListener(v -> validateData());
    }

    private String sessionNow = DATA.EMPTY, sessionOld = DATA.EMPTY, sessionNumberNow = DATA.EMPTY,
            sessionNumberOld = DATA.EMPTY, yearNow = DATA.EMPTY, yearOld = DATA.EMPTY;

    private void validateData() {
        //get data
        sessionNow = binding.sessionNow.getText().toString().trim();
        sessionOld = binding.sessionOld.getText().toString().trim();
        sessionNumberNow = binding.sessionNumberNow.getText().toString().trim();
        sessionNumberOld = binding.sessionNumberOld.getText().toString().trim();
        yearNow = binding.yearNow.getText().toString().trim();
        yearOld = binding.yearOld.getText().toString().trim();

        //validate data
        if (TextUtils.isEmpty(sessionNow)) {
            Toast.makeText(context, "Enter the Session number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(sessionNumberNow)) {
            Toast.makeText(context, "Enter the Session name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(yearNow)) {
            Toast.makeText(context, "Enter the year", Toast.LENGTH_SHORT).show();
        } else {
            if (imageUri == null && imageUri2 == null && imageUri3 == null && imageUri4 == null) {
                updatePost(DATA.EMPTY, DATA.EMPTY, DATA.EMPTY, DATA.EMPTY);
            } else {
                if (imageUri != null)
                    uploadImage();
                else if (imageUri2 != null)
                    uploadImage2(null);
                else if (imageUri3 != null)
                    uploadImage3(null, null);
                else if (imageUri4 != null)
                    uploadImage4(null, null, null);
            }
        }
    }

    private void uploadImage() {
        dialog.setMessage("The current session image is being updated...");
        dialog.show();

        String filePathAndName = "Images/Session/sessionNow";

        StorageReference reference = FirebaseStorage.getInstance().getReference(filePathAndName + DATA.DOT + VOID.getFileExtension(imageUri, context));
        reference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isSuccessful()) ;
            String image = DATA.EMPTY + uriTask.getResult();
            if (imageUri2 != null) {
                uploadImage2(image);
            } else if (imageUri3 != null) {
                uploadImage3(image, null);
            } else if (imageUri4 != null) {
                uploadImage4(image, null, null);
            } else {
                updatePost(image, null, null, null);
            }
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Something went wrong! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void uploadImage2(String image) {
        dialog.setMessage("The previous session image is being updated...");
        dialog.show();

        String filePathAndName = "Images/Session/sessionOld";

        StorageReference reference = FirebaseStorage.getInstance().getReference(filePathAndName + DATA.DOT + VOID.getFileExtension(imageUri, context));
        reference.putFile(imageUri2).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isSuccessful()) ;
            String image2 = DATA.EMPTY + uriTask.getResult();
            if (imageUri3 != null) {
                uploadImage3(image, image2);
            } else if (imageUri4 != null) {
                uploadImage4(image, image2, null);
            } else {
                updatePost(image, image2, null, null);
            }
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Something went wrong! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void uploadImage3(String image, String image2) {
        dialog.setMessage("The current session logo is being updated...");
        dialog.show();

        String filePathAndName = "Images/Logo/logoNow";

        StorageReference reference = FirebaseStorage.getInstance().getReference(filePathAndName + DATA.DOT + VOID.getFileExtension(imageUri, context));
        reference.putFile(imageUri3).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isSuccessful()) ;
            String image3 = DATA.EMPTY + uriTask.getResult();
            if (imageUri4 != null) {
                uploadImage4(image, image2, image3);
            } else {
                updatePost(image, image2, image3, null);
            }
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Something went wrong! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void uploadImage4(String image, String image2, String image3) {
        dialog.setMessage("The logo of the previous session is being updated...");
        dialog.show();

        String filePathAndName = "Images/Logo/logoOld";

        StorageReference reference = FirebaseStorage.getInstance().getReference(filePathAndName + DATA.DOT + VOID.getFileExtension(imageUri, context));
        reference.putFile(imageUri4).addOnSuccessListener(taskSnapshot -> {
            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
            while (!uriTask.isSuccessful()) ;
            String image4 = DATA.EMPTY + uriTask.getResult();

            updatePost(image, image2, image3, image4);
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Something went wrong! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void updatePost(String image, String image2, String image3, String image4) {
        dialog.setMessage("Editing....");
        dialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("session", DATA.EMPTY + sessionNow);
        hashMap.put("sessionNumber", DATA.EMPTY + sessionNumberNow);
        hashMap.put("year", DATA.EMPTY + yearNow);
        hashMap.put("oldSession", DATA.EMPTY + sessionOld);
        hashMap.put("oldSessionNumber", DATA.EMPTY + sessionNumberOld);
        hashMap.put("oldYear", DATA.EMPTY + yearOld);
        if (imageUri != null)
            hashMap.put("imageSession", DATA.EMPTY + image);
        if (imageUri2 != null)
            hashMap.put("oldImageSession", DATA.EMPTY + image2);
        if (imageUri3 != null)
            hashMap.put("imageLogo", DATA.EMPTY + image3);
        if (imageUri4 != null)
            hashMap.put("oldImageLogo", DATA.EMPTY + image4);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.M_TOOLS);
        reference.updateChildren(hashMap).addOnSuccessListener(unused -> {
            dialog.dismiss();
            Toast.makeText(context, "Modified", Toast.LENGTH_SHORT).show();
            //finish();
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Something went wrong! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void Data() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.M_TOOLS);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Tools tools = dataSnapshot.getValue(Tools.class);
                assert tools != null;
                String sessionNow = tools.getSession();
                String sessionOld = tools.getOldSession();
                String sessionNumberNow = tools.getSessionNumber();
                String sessionNumberOld = tools.getOldSessionNumber();
                String yearNow = tools.getYear();
                String yearOld = tools.getOldYear();
                String ImageNow = tools.getImageSession();
                String ImageOld = tools.getOldImageSession();
                String logoNow = tools.getImageLogo();
                String logoOld = tools.getOldImageLogo();

                VOID.Glide(false, context, ImageNow, binding.imageSessionNow);
                VOID.Glide(false, context, ImageOld, binding.imageSessionOld);
                VOID.Glide(false, context, logoNow, binding.logoSessionNow);
                VOID.Glide(false, context, logoOld, binding.logoSessionOld);
                binding.sessionNow.setText(sessionNow);
                binding.sessionOld.setText(sessionOld);
                binding.sessionNumberNow.setText(sessionNumberNow);
                binding.sessionNumberOld.setText(sessionNumberOld);
                binding.yearNow.setText(yearNow);
                binding.yearOld.setText(yearOld);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = CropImage.getPickImageResultUri(context, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(context, uri)) {
                if (IMAGE_NUMBER == IMAGE_NOW) {
                    imageUri = uri;
                } else if (IMAGE_NUMBER == IMAGE_OLD) {
                    imageUri2 = uri;
                } else if (IMAGE_NUMBER == LOGO_NOW) {
                    imageUri3 = uri;
                } else if (IMAGE_NUMBER == LOGO_OLD) {
                    imageUri4 = uri;
                }
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                VOID.CropImageSquare(activity);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                if (IMAGE_NUMBER == IMAGE_NOW) {
                    imageUri = result.getUri();
                    binding.imageSessionNow.setImageURI(imageUri);
                } else if (IMAGE_NUMBER == IMAGE_OLD) {
                    imageUri2 = result.getUri();
                    binding.imageSessionOld.setImageURI(imageUri2);
                } else if (IMAGE_NUMBER == LOGO_NOW) {
                    imageUri3 = result.getUri();
                    binding.logoSessionNow.setImageURI(imageUri3);
                } else if (IMAGE_NUMBER == LOGO_OLD) {
                    imageUri4 = result.getUri();
                    binding.logoSessionOld.setImageURI(imageUri4);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Something went wrong! " + error, Toast.LENGTH_SHORT).show();
            }
        }
    }
}