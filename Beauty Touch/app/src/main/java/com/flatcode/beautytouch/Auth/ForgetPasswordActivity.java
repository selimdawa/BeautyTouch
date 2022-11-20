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
import com.flatcode.beautytouch.Unit.CLASS;
import com.flatcode.beautytouch.Unit.THEME;
import com.flatcode.beautytouch.Unit.VOID;
import com.flatcode.beautytouch.databinding.ActivityForgetPasswordBinding;

public class ForgetPasswordActivity extends AppCompatActivity {

    private ActivityForgetPasswordBinding binding;
    private Context context = ForgetPasswordActivity.this;

    private FirebaseAuth auth;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        VOID.Intro(getBaseContext(), binding.background, binding.backWhite, binding.backBlack);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setTitle("Please wait...");
        dialog.setCanceledOnTouchOutside(false);

        binding.noAccount.setOnClickListener(v -> {
            VOID.Intent(context, CLASS.REGISTER);
            finish();
        });
        binding.login.setOnClickListener(v -> {
            VOID.Intent(context, CLASS.LOGIN);
            finish();
        });
        binding.go.setOnClickListener(v -> validateDate());
    }

    private String number = "0111111111", email = "";

    private void validateDate() {

        //get data
        email = binding.emailEt.getText().toString().trim() + "@flatcodetest.com";
        number = binding.emailEt.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(context, "Enter the Phone number!", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.PHONE.matcher(email).matches()) {
            Toast.makeText(context, "Phone number is incorrect!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(number)) {
            Toast.makeText(context, "Phone number is incorrect!", Toast.LENGTH_SHORT).show();
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
                recoverPassword();
            } else {
                Toast.makeText(context, "Please enter a valid phone number and password! ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void recoverPassword() {
        dialog.setMessage("Password recovery is sent to " + email);
        dialog.show();

        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            dialog.dismiss();
            Toast.makeText(context, "Password reset has been sent to " + email, Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            dialog.dismiss();
            Toast.makeText(context, "Something went wrong! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}