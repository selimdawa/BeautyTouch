package com.flatcode.beautytouch.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.flatcode.beautytouch.Adapter.ProductsStaggeredAdapter;
import com.flatcode.beautytouch.Model.Post;
import com.flatcode.beautytouch.R;
import com.flatcode.beautytouch.Unit.DATA;
import com.flatcode.beautytouch.Unit.THEME;
import com.flatcode.beautytouch.Unit.VOID;
import com.flatcode.beautytouch.databinding.ActivityFavoritesBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class FavoritesActivity extends AppCompatActivity {

    private Context context = FavoritesActivity.this;
    private ActivityFavoritesBinding binding;

    private List<Post> postList;
    private List<String> mySaves;
    private ProductsStaggeredAdapter adapter;

    String publisher = DATA.PUBLISHER_NAME , aname = DATA.APP_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityFavoritesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.toolbar.nameSpace.setText(R.string.favorites);

        VOID.BannerAd(getApplicationContext(), binding.adView, DATA.BANNER_FAVORITES);

        //binding.recyclerView.setHasFixedSize(true);
        postList = new ArrayList<>();
        adapter = new ProductsStaggeredAdapter(context, postList);
        binding.recyclerView.setAdapter(adapter);

        mySaves();
    }

    private void mySaves() {
        mySaves = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.SAVES).child(DATA.FirebaseUserUid);
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
                postList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    for (String id : mySaves) {
                        assert post != null;
                        if (post.getPublisher().equals(publisher)) {
                            if (post.getAname().equals(aname)) {
                                if (post.getPostid().equals(id)) {
                                    postList.add(post);
                                }
                            }
                        }
                    }
                }
                Collections.reverse(postList);
                binding.bar.setVisibility(GONE);
                if (!(postList.isEmpty())) {
                    binding.recyclerView.setVisibility(VISIBLE);
                    binding.emptyText.setVisibility(GONE);
                } else {
                    binding.recyclerView.setVisibility(GONE);
                    binding.emptyText.setVisibility(VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}