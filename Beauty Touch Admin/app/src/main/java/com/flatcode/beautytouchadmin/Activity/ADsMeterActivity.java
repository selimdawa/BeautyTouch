package com.flatcode.beautytouchadmin.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.beautytouchadmin.Adapter.ADsUserAdapter;
import com.flatcode.beautytouchadmin.Model.User;
import com.flatcode.beautytouchadmin.R;
import com.flatcode.beautytouchadmin.Unit.DATA;
import com.flatcode.beautytouchadmin.Unit.THEME;
import com.flatcode.beautytouchadmin.databinding.ActivityAdsMeterBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ADsMeterActivity extends AppCompatActivity {

    private ActivityAdsMeterBinding binding;
    private Context context = ADsMeterActivity.this;

    ArrayList<User> list;
    ADsUserAdapter adapter;

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityAdsMeterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        type = DATA.AD_LOAD;

        binding.toolbar.nameSpace.setText(R.string.users_ads);
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        //binding.recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new ADsUserAdapter(context, list, true);
        binding.recyclerView.setAdapter(adapter);

        binding.adLoad.setOnClickListener(v -> {
            type = DATA.AD_LOAD;
            getData(type);
        });
        binding.adClick.setOnClickListener(v -> {
            type = DATA.AD_CLICK;
            getData(type);
        });
        binding.timestamp.setOnClickListener(v -> {
            type = DATA.STARTED;
            getData(type);
        });
    }

    private void getData(String orderBy) {
        Query ref = FirebaseDatabase.getInstance().getReference(DATA.USERS);
        ref.orderByChild(orderBy).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User item = data.getValue(User.class);
                    assert item != null;
                    if (!(item.getAdLoad() == 0 && item.getAdClick() == 0))
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
        getData(type);
        super.onRestart();
    }

    @Override
    protected void onResume() {
        getData(type);
        super.onResume();
    }
}