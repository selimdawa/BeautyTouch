package com.flatcode.beautytouch.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.beautytouch.Adapter.LeaderboardAdapter;
import com.flatcode.beautytouch.Model.Post;
import com.flatcode.beautytouch.Model.Reward;
import com.flatcode.beautytouch.Model.Tools;
import com.flatcode.beautytouch.Model.User;
import com.flatcode.beautytouch.Unit.CLASS;
import com.flatcode.beautytouch.Unit.DATA;
import com.flatcode.beautytouch.Unit.THEME;
import com.flatcode.beautytouch.Unit.VOID;
import com.flatcode.beautytouch.databinding.ActivityLeaderboardBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LeaderboardActivity extends AppCompatActivity {

    private ActivityLeaderboardBinding binding;
    Context context = LeaderboardActivity.this;

    private ArrayList<User> list;
    private LeaderboardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityLeaderboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //binding.recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new LeaderboardAdapter(context, list);
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
                String sessionNumber = tools.getSessionNumber();
                VOID.Glide(false, context, tools.getImageSession(), binding.imageSession);
                VOID.Glide(false, context, tools.getImageLogo(), binding.imageLogo);
                binding.sessionNumber.setText(session);
                String key = year + "_" + sessionNumber;
                getData(key);
                Reward(key);
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
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Reward(String key) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.M_REWARD);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Reward reward = dataSnapshot.getValue(Reward.class);
                assert reward != null;
                if (dataSnapshot.child(key).exists()) {
                    if (dataSnapshot.exists()) {
                        if (reward.getReward() != null)
                            ReadReward(reward.getReward(), binding.reward);
                        if (reward.getReward2() != null)
                            ReadReward(reward.getReward2(), binding.reward2);
                        if (reward.getReward3() != null)
                            ReadReward(reward.getReward3(), binding.reward3);
                        if (reward.getReward4() != null)
                            ReadReward(reward.getReward4(), binding.reward4);
                        if (reward.getReward5() != null)
                            ReadReward(reward.getReward5(), binding.reward5);
                        if (reward.getReward6() != null)
                            ReadReward(reward.getReward6(), binding.reward6);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ReadReward(String R, ImageView Reward) {
        if (!R.equals(DATA.EMPTY)) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.POSTS).child(R);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Post post = dataSnapshot.getValue(Post.class);
                    assert post != null;
                    if (post.getPostid().equals(R)) {
                        VOID.Glide(false, context, post.getPostimage(), Reward);

                        Reward.setOnClickListener(view -> VOID.IntentExtra(context, CLASS.POST_DETAILS, DATA.POST_ID, R));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void onResume() {
        SessionInfo();
        super.onResume();
    }

    @Override
    protected void onRestart() {
        SessionInfo();
        super.onRestart();
    }
}