package com.flatcode.beautytouchadmin.Activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.beautytouchadmin.Adapter.LeaderboardAdapter;
import com.flatcode.beautytouchadmin.Model.Tools;
import com.flatcode.beautytouchadmin.Model.User;
import com.flatcode.beautytouchadmin.Unit.DATA;
import com.flatcode.beautytouchadmin.Unit.THEME;
import com.flatcode.beautytouchadmin.databinding.ActivitySessionNowInfoBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SessionNowInfoActivity extends AppCompatActivity {

    private ActivitySessionNowInfoBinding binding;
    private Context context = SessionNowInfoActivity.this;

    private ArrayList<User> list;
    private LeaderboardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivitySessionNowInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.toolbar.nameSpace.setText("Session Now");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        //binding.recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new LeaderboardAdapter(context, list, true);
        binding.recyclerView.setAdapter(adapter);
    }

    private void SessionInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.M_TOOLS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Tools tools = dataSnapshot.getValue(Tools.class);
                assert tools != null;
                String year = tools.getYear();
                String session = tools.getSession();
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