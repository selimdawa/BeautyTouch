package com.flatcode.beautytouchadmin.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.beautytouchadmin.Adapter.PostDetailAdapter;
import com.flatcode.beautytouchadmin.Model.Post;
import com.flatcode.beautytouchadmin.R;
import com.flatcode.beautytouchadmin.Unit.DATA;
import com.flatcode.beautytouchadmin.Unit.THEME;
import com.flatcode.beautytouchadmin.databinding.ActivityPostDetailsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostDetailsActivity extends AppCompatActivity {

    Context context = PostDetailsActivity.this;
    private ActivityPostDetailsBinding binding;
    private PostDetailAdapter adapter;
    private List<Post> list;

    String postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityPostDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        postId = intent.getStringExtra(DATA.POST_ID);

        binding.toolbar.nameSpace.setText(R.string.post_detail);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        //binding.recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new PostDetailAdapter(context, list);
        binding.recyclerView.setAdapter(adapter);
    }

    private void readPost() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.POSTS).child(postId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                Post post = dataSnapshot.getValue(Post.class);
                list.add(post);

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //private void addView() {
    //    FirebaseDatabase.getInstance().getReference(DATA.POSTS).child(postId).child("views")
    //            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(true);
    //}

    @Override
    protected void onRestart() {
        readPost();
        //addView();
        super.onRestart();
    }

    @Override
    protected void onResume() {
        readPost();
        //addView();
        super.onResume();
    }
}