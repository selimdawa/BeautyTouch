package com.flatcode.beautytouch.Activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.flatcode.beautytouch.Unit.CLASS;
import com.flatcode.beautytouch.Unit.THEME;
import com.flatcode.beautytouch.Unit.VOID;
import com.flatcode.beautytouch.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    Context context = SplashActivity.this;
    FirebaseAuth auth;

    int time_per_second = 2;
    final static int time_per_millis = 1000;
    int time_final = time_per_millis * time_per_second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();

        new Handler().postDelayed(this::checkUser, time_final);
    }

    private void checkUser() {
        //get current user, if logged in
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser == null) {
            VOID.Intent(context, CLASS.AUTH);
        } else {
            VOID.Intent(context, CLASS.MAIN);
        }
        finish();
    }
}

 /*   TextView m_version;

    String profileid;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StyleApp.setThemeOfApp(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences.Editor editor = this.getSharedPreferences("PREFS", 0).edit();
        editor.putString("profileid", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        editor.apply();

        profileid = this.getSharedPreferences("PREFS", 0).getString("profileid", "none");

        m_version = findViewById(R.id.m_version);

        new Handler().postDelayed(this::userInfo, 3000);
    }

    private void userInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(profileid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                m_version.setText(user.getMversion());
                if (m_version.getText().equals("1.0")) {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}*/