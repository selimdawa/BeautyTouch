package com.flatcode.beautytouch.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.flatcode.beautytouch.Model.Tools;
import com.flatcode.beautytouch.R;
import com.flatcode.beautytouch.Unit.CLASS;
import com.flatcode.beautytouch.Unit.DATA;
import com.flatcode.beautytouch.Unit.THEME;
import com.flatcode.beautytouch.Unit.VOID;
import com.flatcode.beautytouch.databinding.ActivityRewardBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.MessageFormat;

public class RewardActivity extends AppCompatActivity implements RewardedVideoAdListener {

    private Context context = RewardActivity.this;
    private ActivityRewardBinding binding;

    RewardedVideoAd rewardedVideoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityRewardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.toolbar.nameSpace.setText("Earn Points");

        binding.leaderboardCard.setOnClickListener(v -> VOID.Intent(context, CLASS.LEADERBOARD));
        binding.leaderboardCardOld.setOnClickListener(v -> VOID.Intent(context, CLASS.LEADERBOARD_OLD));

        MobileAds.initialize(context, initializationStatus -> {
        });

        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        rewardedVideoAd.setRewardedVideoAdListener(this);
        loadAd();
    }

    private void getNrPoints(String year, String session) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.USERS).child(DATA.FirebaseUserUid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String key = year + "_" + session;
                String value = DATA.EMPTY + dataSnapshot.child(key).getValue();
                if (dataSnapshot.child(key).exists())
                    binding.myPoints.setText(MessageFormat.format("My Points : {0}", value));
                else
                    binding.myPoints.setText("My Points : 0");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
                String oldYear = tools.getOldYear();
                String oldSession = tools.getOldSession();

                if (!tools.getSession().equals(tools.getOldSession())) {
                    binding.leaderboardCardOld.setVisibility(View.VISIBLE);
                } else {
                    binding.leaderboardCardOld.setVisibility(View.GONE);
                }

                binding.sessionInfo.setText(MessageFormat.format("{0} | {1}", year, session));
                binding.sessionInfoOld.setText(MessageFormat.format("{0} | {1}", oldYear, oldSession));
                getNrPoints(year, session);

                binding.rewardCard.setOnClickListener(v -> {
                    if (rewardedVideoAd.isLoaded()) {
                        rewardedVideoAd.show();
                    }
                    Toast.makeText(RewardActivity.this, "The ad is loaded, click again if it does not appear", Toast.LENGTH_SHORT).show();
                    getNrPoints(year, session);
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Reward() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.M_TOOLS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Tools tools = dataSnapshot.getValue(Tools.class);
                assert tools != null;
                String year = tools.getYear();
                String session = tools.getSession();
                VOID.AdRewardCount(DATA.FirebaseUserUid, DATA.EMPTY + year + "_" + DATA.EMPTY + session);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadAd() {
        rewardedVideoAd.loadAd(getResources().getString(R.string.admob_reward),
                new AdRequest.Builder().build());
    }

    @Override
    public void onRewardedVideoAdLoaded() {
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        loadAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Toast.makeText(this, "Please wait for the ads to load", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoCompleted() {
        Reward();
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