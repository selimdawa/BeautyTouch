package com.flatcode.beautytouchadmin.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

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
import com.flatcode.beautytouchadmin.Adapter.UsersAdapter;
import com.flatcode.beautytouchadmin.Model.User;
import com.flatcode.beautytouchadmin.R;
import com.flatcode.beautytouchadmin.Unit.DATA;
import com.flatcode.beautytouchadmin.Unit.THEME;
import com.flatcode.beautytouchadmin.Unit.VOID;
import com.flatcode.beautytouchadmin.databinding.ActivityUsersBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsersActivity extends AppCompatActivity {

    private ActivityUsersBinding binding;
    private Context context = UsersActivity.this;

    UsersAdapter adapter;
    List<User> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        //binding.recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new UsersAdapter(context, list);
        binding.recyclerView.setAdapter(adapter);
    }

    private void Users() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.USERS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    if (!user.getId().equals(DATA.FirebaseUserUid)) {
                        list.add(user);
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
    protected void onResume() {
        binding.toolbar.nameSpace.setText(R.string.users);
        VOID.getNrFromServer(DATA.USERS, binding.toolbar.nameSpace);
        Users();
        super.onResume();
    }
}