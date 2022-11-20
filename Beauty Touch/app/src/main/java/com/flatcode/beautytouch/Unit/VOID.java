package com.flatcode.beautytouch.Unit;

import static com.flatcode.beautytouch.Activity.MainActivity.mInterstitialAd;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.flatcode.beautytouch.Model.ADs;
import com.flatcode.beautytouch.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Objects;

public class VOID {

    public static void IntentClear(Context context, Class c) {
        Intent intent = new Intent(context, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void Intent(Context context, Class c) {
        Intent intent = new Intent(context, c);
        context.startActivity(intent);
    }

    public static void IntentExtra(Context context, Class c, String key, String value) {
        Intent intent = new Intent(context, c);
        intent.putExtra(key, value);
        context.startActivity(intent);
    }

    public static void IntentExtra2(Context context, Class c, String key, String value, String key2, String value2) {
        Intent intent = new Intent(context, c);
        intent.putExtra(key, value);
        intent.putExtra(key2, value2);
        context.startActivity(intent);
    }

    public static void IntentExtra3(Context context, Class c, String key, String value,
                                    String key2, String value2, String key3, String value3) {
        Intent intent = new Intent(context, c);
        intent.putExtra(key, value);
        intent.putExtra(key2, value2);
        intent.putExtra(key3, value3);
        context.startActivity(intent);
    }

    public static void Glide(Boolean isUser, Context context, String Url, ImageView Image) {
        try {
            if (Url.equals(DATA.BASIC)) {
                if (isUser) {
                    Image.setImageResource(R.drawable.basic_user);
                } else {
                    Image.setImageResource(R.drawable.basic_user);
                }
            } else {
                Glide.with(context).load(Url).centerCrop().placeholder(R.color.image_profile).into(Image);
            }
        } catch (Exception e) {
            Image.setImageResource(R.drawable.basic_user);
        }
    }

    public static void CropImageSquare(Activity activity) {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .setMinCropResultSize(DATA.MIN_SQUARE, DATA.MIN_SQUARE)
                .setAspectRatio(1, 1)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(activity);
    }

    public static void Intro(Context context, ImageView background, ImageView backWhite, ImageView backDark) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (Objects.equals(sharedPreferences.getString("color_option", "ONE"), "ONE") ||
                Objects.equals(sharedPreferences.getString("color_option", "TWO"), "TWO") ||
                Objects.equals(sharedPreferences.getString("color_option", "THREE"), "THREE") ||
                Objects.equals(sharedPreferences.getString("color_option", "FOUR"), "FOUR") ||
                Objects.equals(sharedPreferences.getString("color_option", "FIVE"), "FIVE") ||
                Objects.equals(sharedPreferences.getString("color_option", "SIX"), "SIX") ||
                Objects.equals(sharedPreferences.getString("color_option", "SEVEN"), "SEVEN") ||
                Objects.equals(sharedPreferences.getString("color_option", "EIGHT"), "EIGHT") ||
                Objects.equals(sharedPreferences.getString("color_option", "NINE"), "NINE") ||
                Objects.equals(sharedPreferences.getString("color_option", "TEEN"), "TEEN")) {
            background.setImageResource(R.drawable.background_day);
            backWhite.setVisibility(View.VISIBLE);
            backDark.setVisibility(View.GONE);
        } else if (Objects.equals(sharedPreferences.getString("color_option", "NIGHT_ONE"), "NIGHT_ONE") ||
                Objects.equals(sharedPreferences.getString("color_option", "NIGHT_TWO"), "NIGHT_TWO") ||
                Objects.equals(sharedPreferences.getString("color_option", "NIGHT_THREE"), "NIGHT_THREE") ||
                Objects.equals(sharedPreferences.getString("color_option", "NIGHT_FOUR"), "NIGHT_FOUR") ||
                Objects.equals(sharedPreferences.getString("color_option", "NIGHT_FIVE"), "NIGHT_FIVE") ||
                Objects.equals(sharedPreferences.getString("color_option", "NIGHT_SIX"), "NIGHT_SIX") ||
                Objects.equals(sharedPreferences.getString("color_option", "NIGHT_SEVEN"), "NIGHT_SEVEN")) {
            background.setImageResource(R.drawable.background_night);
            backWhite.setVisibility(View.GONE);
            backDark.setVisibility(View.VISIBLE);
        }
    }

    public static void BannerAd(Context context, AdView adView, String bannerName) {
        MobileAds.initialize(context, initializationStatus -> {
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                AdUserCount(DATA.FirebaseUserUid, DATA.AD_LOAD, 1);
                AdCount(DATA.FirebaseUserUid, bannerName, DATA.ADS_LOADED_COUNT);
            }

            @Override
            public void onAdOpened() {
                AdUserCount(DATA.FirebaseUserUid, DATA.AD_CLICK, 1);
                AdCount(DATA.FirebaseUserUid, bannerName, DATA.ADS_CLICKED_COUNT);
            }
        });
    }

    public static void InterstitialAd(Activity activity) {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(activity, activity.getResources().getString(R.string.admob_interstitial), adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                super.onAdLoaded(interstitialAd);
                mInterstitialAd = interstitialAd;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                mInterstitialAd = null;
            }
        });
    }

    public static void InterstitialShow(Activity activity, String interstitialName) {
        if (mInterstitialAd != null) {
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    super.onAdFailedToShowFullScreenContent(adError);
                    mInterstitialAd = null;
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent();
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    mInterstitialAd = null;
                    InterstitialAd(activity);
                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                    AdUserCount(DATA.FirebaseUserUid, DATA.AD_LOAD, 1);
                    AdCount(DATA.FirebaseUserUid, interstitialName, DATA.ADS_LOADED_COUNT);
                }

                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                    AdUserCount(DATA.FirebaseUserUid, DATA.AD_CLICK, 1);
                    AdCount(DATA.FirebaseUserUid, interstitialName, DATA.ADS_CLICKED_COUNT);
                }
            });

            mInterstitialAd.show(activity);
        } else {
        }
    }

    public static void AdCount(String userId, String bannerName, String key) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.M_AD).child(userId);
        ref.child(bannerName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get views count
                String adCount = DATA.EMPTY + snapshot.child(key).getValue();
                if (adCount.equals(DATA.EMPTY) || adCount.equals(DATA.NULL)) {
                    adCount = "0";
                }

                long newAdCount = Long.parseLong(adCount) + 1;
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(key, newAdCount);

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.M_AD).child(userId);
                reference.child(bannerName).updateChildren(hashMap).addOnCompleteListener(
                        task -> AdName(DATA.FirebaseUserUid, bannerName)
                );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void AdRewardCount(String userId, String key) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.USERS)
                .child(userId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get views count
                String adCount = DATA.EMPTY + snapshot.child(key).getValue();
                if (adCount.equals(DATA.EMPTY) || adCount.equals(DATA.NULL)) {
                    adCount = "0";
                }

                long newAdCount = Long.parseLong(adCount) + 1;
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(key, newAdCount);

                ref.updateChildren(hashMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void AdUserCount(String userId, String key, int number) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.USERS).child(userId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get views count
                String adCount = DATA.EMPTY + snapshot.child(key).getValue();
                if (adCount.equals(DATA.EMPTY) || adCount.equals(DATA.NULL)) {
                    adCount = "0";
                }

                long newAdCount = Long.parseLong(adCount) + number;
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put(key, newAdCount);

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.USERS).child(userId);
                reference.updateChildren(hashMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void AdName(String userId, String bannerName) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DATA.M_AD).child(userId);
        ref.child(bannerName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //get views count
                ADs item = snapshot.getValue(ADs.class);
                assert item != null;
                if (item.getName() == null) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put(DATA.NAME, bannerName);

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.M_AD).child(userId);
                    reference.child(bannerName).updateChildren(hashMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static void RateUs(Activity activity) {
        Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            activity.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + activity.getPackageName())));
        }
    }

    public static String getFileExtension(Uri uri, Context context) {
        ContentResolver cR = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}