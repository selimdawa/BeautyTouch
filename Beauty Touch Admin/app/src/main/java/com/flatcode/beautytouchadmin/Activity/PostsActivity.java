package com.flatcode.beautytouchadmin.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.beautytouchadmin.Adapter.MyPostsAdapter;
import com.flatcode.beautytouchadmin.Model.Post;
import com.flatcode.beautytouchadmin.Unit.DATA;
import com.flatcode.beautytouchadmin.Unit.THEME;
import com.flatcode.beautytouchadmin.databinding.ActivityPostsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostsActivity extends AppCompatActivity {

    private ActivityPostsBinding binding;
    private Context context = PostsActivity.this;

    MyPostsAdapter adapter;
    List<Post> list;

    String type = DATA.ALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityPostsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.toolbar.nameSpace.setText("My Posts");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        //binding.recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new MyPostsAdapter(context, list);
        binding.recyclerView.setAdapter(adapter);

        binding.all.setOnClickListener(v -> {
            type = DATA.ALL;
            getData(type);
        });
        binding.hair.setOnClickListener(v -> {
            type = DATA.HAIR;
            getData(type);
        });
        binding.skin.setOnClickListener(v -> {
            type = DATA.SKIN;
            getData(type);
        });
    }

    private void getData(String type) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.POSTS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    assert post != null;
                    if (post.getAname().equals(DATA.BEAUTY_TOUCH)) {
                        switch (type) {
                            case DATA.ALL:
                                list.add(post);
                                break;
                            case DATA.SKIN:
                                if (post.getCategory().equals(DATA.SKIN_PRODUCTS))
                                    list.add(post);
                                break;
                            case DATA.HAIR:
                                if (post.getCategory().equals(DATA.HAIR_PRODUCTS))
                                    list.add(post);
                                break;
                        }
                    }
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
        getData(type);
        super.onRestart();
    }

    @Override
    protected void onResume() {
        getData(type);
        super.onResume();
    }
}