package com.flatcode.beautytouchadmin.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.beautytouchadmin.Adapter.ADsInfoAdapter;
import com.flatcode.beautytouchadmin.Model.ADs;
import com.flatcode.beautytouchadmin.R;
import com.flatcode.beautytouchadmin.Unit.DATA;
import com.flatcode.beautytouchadmin.Unit.THEME;
import com.flatcode.beautytouchadmin.Unit.VOID;
import com.flatcode.beautytouchadmin.databinding.ActivityAdsInfoBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ADsInfoActivity extends AppCompatActivity {

    private ActivityAdsInfoBinding binding;
    Context context = ADsInfoActivity.this;

    ArrayList<ADs> list;
    ADsInfoAdapter adapter;

    private String profileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityAdsInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        profileId = intent.getStringExtra(DATA.PROFILE_ID);

        binding.toolbar.nameSpace.setText(R.string.info_ads);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        //binding.recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new ADsInfoAdapter(context, list, true);
        binding.recyclerView.setAdapter(adapter);
    }

    private void loadUserInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.USERS);
        reference.child(profileId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = DATA.EMPTY + snapshot.child(DATA.USER_NAME).getValue();
                String profileImage = DATA.EMPTY + snapshot.child(DATA.IMAGE_URL).getValue();

                binding.username.setText(username);

                VOID.Glide(true, context, profileImage, binding.profileImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadAds(String orderBy) {
        Query ref = FirebaseDatabase.getInstance().getReference(DATA.M_AD).child(profileId);
        ref.orderByChild(orderBy).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    ADs item = data.getValue(ADs.class);
                    assert item != null;
                    list.add(item);
                }
                adapter.notifyDataSetChanged();
                binding.progress.setVisibility(View.GONE);
                if (!list.isEmpty()) {
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    binding.emptyText.setVisibility(View.GONE);
                } else {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.emptyText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onRestart() {
        loadUserInfo();
        loadAds(DATA.NAME);
        super.onRestart();
    }

    @Override
    protected void onResume() {
        loadUserInfo();
        loadAds(DATA.NAME);
        super.onResume();
    }
}