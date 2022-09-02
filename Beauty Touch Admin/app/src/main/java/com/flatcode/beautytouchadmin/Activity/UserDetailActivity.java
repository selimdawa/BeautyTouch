package com.flatcode.beautytouchadmin.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.beautytouchadmin.Adapter.FavoritesAdapter;
import com.flatcode.beautytouchadmin.Model.Post;
import com.flatcode.beautytouchadmin.Model.User;
import com.flatcode.beautytouchadmin.Unit.DATA;
import com.flatcode.beautytouchadmin.Unit.THEME;
import com.flatcode.beautytouchadmin.Unit.VOID;
import com.flatcode.beautytouchadmin.databinding.ActivityUserDetailBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserDetailActivity extends AppCompatActivity {

    private ActivityUserDetailBinding binding;
    private Context context = UserDetailActivity.this;

    private List<Post> list;
    private List<String> mySaves;
    private FavoritesAdapter adapter;

    String profileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityUserDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        profileId = intent.getStringExtra(DATA.PROFILE_ID);

        //binding.recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new FavoritesAdapter(context, list);
        binding.recyclerView.setAdapter(adapter);
    }

    private void Saves() {
        mySaves = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.SAVES).child(profileId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mySaves.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    mySaves.add(snapshot.getKey());
                }
                readSaves();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readSaves() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.POSTS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    for (String id : mySaves) {
                        assert post != null;
                        if (post.getPublisher().equals(DATA.PUBLICHER)) {
                            if (post.getAname().equals(DATA.APP_NAME)) {
                                if (post.getPostid().equals(id)) {
                                    list.add(post);
                                }
                            }
                        }
                    }
                }
                Collections.reverse(list);
                adapter.notifyDataSetChanged();
                binding.recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void userInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.USERS).child(profileId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                assert user != null;

                VOID.Glide(false, context, user.getImageurl(), binding.image);
                binding.name.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onRestart() {
        Saves();
        userInfo();
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Saves();
        userInfo();
        super.onResume();
    }
}