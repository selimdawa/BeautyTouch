package com.flatcode.beautytouchadmin.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.beautytouchadmin.Adapter.ShoppingCentersAdapter;
import com.flatcode.beautytouchadmin.Model.ShoppingCenter;
import com.flatcode.beautytouchadmin.R;
import com.flatcode.beautytouchadmin.Unit.DATA;
import com.flatcode.beautytouchadmin.Unit.THEME;
import com.flatcode.beautytouchadmin.databinding.ActivityShoppingCentersBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShoppingCentersActivity extends AppCompatActivity {

    private ActivityShoppingCentersBinding binding;
    private Context context = ShoppingCentersActivity.this;

    private List<ShoppingCenter> list;
    private ShoppingCentersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityShoppingCentersBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.toolbar.nameSpace.setText(R.string.shopping_centers);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        //binding.recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new ShoppingCentersAdapter(context, list);
        binding.recyclerView.setAdapter(adapter);
    }

    private void getAllPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.SHOPPING_CENTERS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ShoppingCenter shoppingCenter = snapshot.getValue(ShoppingCenter.class);
                    assert shoppingCenter != null;
                    if (shoppingCenter.getPublisher().equals(DATA.PUBLICHER)) {
                        if (shoppingCenter.getAname().equals(DATA.APP_NAME)) {
                            list.add(shoppingCenter);
                        }
                    }
                }
                Collections.reverse(list);
                binding.bar.setVisibility(GONE);
                if (!(list.isEmpty())) {
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

    @Override
    protected void onRestart() {
        getAllPosts();
        super.onRestart();
    }

    @Override
    protected void onResume() {
        getAllPosts();
        super.onResume();
    }
}