package com.flatcode.beautytouch.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
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
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.MessageFormat;

public class RewardActivity extends AppCompatActivity {

    private Activity activity;
    private Context context = activity = RewardActivity.this;
    private ActivityRewardBinding binding;

    RewardedAd mRewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityRewardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.toolbar.nameSpace.setText(R.string.earn_points);

        binding.leaderboardCard.setOnClickListener(v -> VOID.Intent(context, CLASS.LEADERBOARD));
        binding.leaderboardCardOld.setOnClickListener(v -> VOID.Intent(context, CLASS.LEADERBOARD_OLD));

        MobileAds.initialize(context, initializationStatus -> {
        });

        loadRewardedAd();
    }

    private void loadRewardedAd() {
        RewardedAd.load(context, getResources().getString(R.string.admob_reward), new AdRequest.Builder().build(), new RewardedAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                mRewardedAd = null;
            }

            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                super.onAdLoaded(rewardedAd);
                mRewardedAd = rewardedAd;
            }
        });
    }

    private void showRewardedAd() {
        if (mRewardedAd != null) {
            mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    mRewardedAd = null;
                    Reward();
                    loadRewardedAd();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    mRewardedAd = null;
                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                }
            });

            mRewardedAd.show(activity, rewardItem -> {
            });
        } else {
        }
    }

    private void loadAndShowRewardedAd() {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle("Please wait");
        dialog.setMessage("Loading Rewarded Ad");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        RewardedAd.load(context, getResources().getString(R.string.admob_reward), new AdRequest.Builder().build(), new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                super.onAdLoaded(rewardedAd);
                mRewardedAd = rewardedAd;
                dialog.dismiss();
                showRewardedAd();
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                mRewardedAd = null;
                dialog.dismiss();
            }
        });
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
                String session = tools.getSessionNumber();
                String oldYear = tools.getOldYear();
                String oldSession = tools.getOldSessionNumber();

                if ((!session.equals(oldSession)) || (!year.equals(oldYear)))
                    binding.leaderboardCardOld.setVisibility(View.VISIBLE);
                else
                    binding.leaderboardCardOld.setVisibility(View.GONE);

                binding.sessionInfo.setText(MessageFormat.format("{0} | {1}", year, session));
                binding.sessionInfoOld.setText(MessageFormat.format("{0} | {1}", oldYear, oldSession));
                getNrPoints(year, session);

                binding.rewardCard.setOnClickListener(v -> {
                    loadAndShowRewardedAd();
                    Toast.makeText(context, "The ad is loaded, click again if it does not appear", Toast.LENGTH_SHORT).show();
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
                String session = tools.getSessionNumber();
                VOID.AdRewardCount(DATA.FirebaseUserUid, DATA.EMPTY + year + "_" + DATA.EMPTY + session);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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