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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.flatcode.beautytouchadmin.Adapter.LeaderboardOldAdapter;
import com.flatcode.beautytouchadmin.Model.Tools;
import com.flatcode.beautytouchadmin.Model.User;
import com.flatcode.beautytouchadmin.Unit.DATA;
import com.flatcode.beautytouchadmin.Unit.THEME;
import com.flatcode.beautytouchadmin.databinding.ActivitySessionOldInfoBinding;

import java.util.ArrayList;

public class SessionOldInfoActivity extends AppCompatActivity {

    private ActivitySessionOldInfoBinding binding;
    private Context context = SessionOldInfoActivity.this;

    private ArrayList<User> list;
    private LeaderboardOldAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivitySessionOldInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.toolbar.nameSpace.setText("Session Old");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        //binding.recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new LeaderboardOldAdapter(context, list, true);
        binding.recyclerView.setAdapter(adapter);
    }

    private void SessionInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.M_TOOLS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Tools tools = dataSnapshot.getValue(Tools.class);
                assert tools != null;
                String year = tools.getOldYear();
                String session = tools.getOldSession();
                String key = year + "_" + session;
                getData(key);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getData(String orderBy) {
        Query ref = FirebaseDatabase.getInstance().getReference(DATA.USERS);
        ref.orderByChild(orderBy).limitToLast(3).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.child(orderBy).exists()) {
                        User item = data.getValue(User.class);
                        assert item != null;
                        list.add(item);
                    }
                }
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
        SessionInfo();
        super.onRestart();
    }

    @Override
    protected void onResume() {
        SessionInfo();
        super.onResume();
    }
}