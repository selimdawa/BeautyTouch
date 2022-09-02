package com.flatcode.beautytouchadmin.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.beautytouchadmin.Adapter.HotProductAddAdapter;
import com.flatcode.beautytouchadmin.Adapter.HotProductRemoveAdapter;
import com.flatcode.beautytouchadmin.Model.Post;
import com.flatcode.beautytouchadmin.R;
import com.flatcode.beautytouchadmin.Unit.DATA;
import com.flatcode.beautytouchadmin.Unit.THEME;
import com.flatcode.beautytouchadmin.databinding.ActivityHotProductBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HotProductActivity extends AppCompatActivity {

    private ActivityHotProductBinding binding;
    private Context context = HotProductActivity.this;

    List<String> hotProductList;
    HotProductRemoveAdapter hotpostAdapter;
    List<Post> hotpostLists;

    HotProductAddAdapter allpostAdapter;
    List<Post> allpostLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityHotProductBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.toolbar.nameSpace.setText(R.string.hot_product);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        //binding.recyclerView.setHasFixedSize(true);
        hotpostLists = new ArrayList<>();
        hotpostAdapter = new HotProductRemoveAdapter(context, hotpostLists);
        binding.recyclerView.setAdapter(hotpostAdapter);

        //binding.recyclerView.setHasFixedSize(true);
        allpostLists = new ArrayList<>();
        allpostAdapter = new HotProductAddAdapter(context, allpostLists);
        binding.recyclerView2.setAdapter(allpostAdapter);
    }

    private void checkHotProduct() {
        hotProductList = new ArrayList();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.HOT_PRODUCT);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hotProductList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    hotProductList.add(snapshot.getKey());
                }
                readPosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.POSTS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hotpostLists.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    for (String id : hotProductList) {
                        assert post != null;
                        if (post.getPublisher().equals(DATA.PUBLICHER))
                            if (post.getAname().equals(DATA.APP_NAME))
                                if (post.getPostid().equals(id))
                                    hotpostLists.add(post);
                    }
                }
                Collections.reverse(hotpostLists);
                hotpostAdapter.notifyDataSetChanged();
                binding.progressBar.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getMoreProduct() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.POSTS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allpostLists.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    assert post != null;
                    if (post.getPublisher().equals(DATA.PUBLICHER)) {
                        if (post.getAname().equals(DATA.APP_NAME)) {
                            allpostLists.add(post);
                            for (String id : hotProductList) {
                                if (post.getPostid().equals(id)) {
                                    allpostLists.remove(post);
                                }
                            }
                        }
                    }
                }
                Collections.reverse(allpostLists);
                allpostAdapter.notifyDataSetChanged();
                binding.progressBar2.setVisibility(View.GONE);
                binding.recyclerView2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onRestart() {
        checkHotProduct();
        getMoreProduct();
        super.onRestart();
    }

    @Override
    protected void onResume() {
        checkHotProduct();
        getMoreProduct();
        super.onResume();
    }
}