package com.flatcode.beautytouch.Auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.flatcode.beautytouch.Unit.CLASS;
import com.flatcode.beautytouch.Unit.DATA;
import com.flatcode.beautytouch.Unit.THEME;
import com.flatcode.beautytouch.Unit.VOID;
import com.flatcode.beautytouch.databinding.ActivityRegisterBinding;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    Context context = RegisterActivity.this;

    private FirebaseAuth auth;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //VOID.Logo(getBaseContext(), binding.logo);
        VOID.Intro(getBaseContext(), binding.background, binding.backWhite, binding.backBlack);

        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setTitle("Please wait...");
        dialog.setCanceledOnTouchOutside(false);

        binding.login.setOnClickListener(v -> {
            VOID.Intent(context, CLASS.LOGIN);
            finish();
        });
        binding.forget.setOnClickListener(v -> VOID.Intent(context, CLASS.FORGET_PASSWORD));
        binding.go.setOnClickListener(v -> validateData());
    }

    private String name = "", email = "", password = "", number = "0111111111";

    private void validateData() {

        //get data
        name = binding.nameEt.getText().toString().trim();
        number = binding.emailEt.getText().toString().trim();
        email = binding.emailEt.getText().toString().trim() + "@flatcodetest.com";
        password = binding.passwordEt.getText().toString().trim();
        String cPassword = binding.cPasswordEt.getText().toString().trim();

        //validate data
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(context, "Enter the username!", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.PHONE.matcher(number).matches()) {
            Toast.makeText(context, "Enter the Phone number!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, "Enter the password!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(cPassword)) {
            Toast.makeText(context, "Confirm password!", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(cPassword)) {
            Toast.makeText(context, "Password does not match!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(number)) {
            Toast.makeText(context, "Error entering the number!", Toast.LENGTH_SHORT).show();
        } else if (number.length() != 10) {
            Toast.makeText(context, "Please enter a 10 digit number!", Toast.LENGTH_SHORT).show();
        } else if (number.equals("0111111111")) {
            Toast.makeText(context, "Please enter a valid number!", Toast.LENGTH_SHORT).show();
        } else {
            int digit = Integer.parseInt(String.valueOf(number.charAt(0)));
            int digit2 = Integer.parseInt(String.valueOf(number.charAt(1)));
            int digit3 = Integer.parseInt(String.valueOf(number.charAt(2)));
            if (digit == 0 && digit2 == 9 && (digit3 == 3 || digit3 == 4 || digit3 == 5 ||
                    digit3 == 6 || digit3 == 8 || digit3 == 9)) {
                createUserAccount();
            } else {
                Toast.makeText(context, "Please enter valid data!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createUserAccount() {
        //show progress
        dialog.setMessage("The account is created");
        dialog.show();

        //create user in firebase auth
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(authResult -> updateUserinfo())
                .addOnFailureListener(e -> {
                    dialog.dismiss();
                    Toast.makeText(context, DATA.EMPTY + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateUserinfo() {
        dialog.setMessage("User data is saved");
        //get current user uid, since user is registered so we can get now
        String id = auth.getUid();

        //setup data to add in db
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(DATA.ID, id);
        hashMap.put("started", "" + System.currentTimeMillis());
        hashMap.put("phonenumber", number);
        hashMap.put("password", password);
        hashMap.put("mversion", "1.0");
        hashMap.put("imageurl", DATA.BASIC);
        hashMap.put("username", name);

        //set data to db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.USERS);
        assert id != null;
        ref.child(id).setValue(hashMap).addOnSuccessListener(unused -> {
            //data added to db
            dialog.dismiss();
            Toast.makeText(context, "Account created", Toast.LENGTH_SHORT).show();
            VOID.IntentClear(context, CLASS.MAIN);
            finish();
        }).addOnFailureListener(e -> {
            //data failed adding to db
            Toast.makeText(context, DATA.EMPTY + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}