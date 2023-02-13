package com.flatcode.beautytouch.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.bumptech.glide.Glide;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.flatcode.beautytouch.BuildConfig;
import com.flatcode.beautytouch.Fragment.HairProductsFragment;
import com.flatcode.beautytouch.Fragment.HomeFragment;
import com.flatcode.beautytouch.Fragment.ShoppingCentersFragment;
import com.flatcode.beautytouch.Fragment.SkinProductsFragment;
import com.flatcode.beautytouch.Model.Post;
import com.flatcode.beautytouch.Model.Tools;
import com.flatcode.beautytouch.Model.User;
import com.flatcode.beautytouch.R;
import com.flatcode.beautytouch.Unit.CLASS;
import com.flatcode.beautytouch.Unit.DATA;
import com.flatcode.beautytouch.Unit.THEME;
import com.flatcode.beautytouch.Unit.VOID;
import com.flatcode.beautytouch.databinding.ActivityMainBinding;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.MessageFormat;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private ActivityMainBinding binding;
    Activity activity;
    Context context = activity = MainActivity.this;

    public String home = "Home Page", skin_product = "Skin Products", hair_product = "Hair Products",
            shopping_center = "Shopping Centers", number_product = DATA.EMPTY;

    MeowBottomNavigation meowBottomNavigation;
    String publisher = DATA.PUBLISHER_NAME, aname = DATA.APP_NAME;
    private static final int SETTINGS_CODE = 234;

    public static InterstitialAd mInterstitialAd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        PreferenceManager.getDefaultSharedPreferences(getBaseContext())
                .registerOnSharedPreferenceChangeListener(this);
        THEME.setThemeOfApp(context);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Color Mode ----------------------------- Start
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new MainActivity.SettingFragment())
                .commit();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_night, new MainActivity.Setting2Fragment())
                .commit();
        // Color Mode -------------------------------- End

        binding.toolbar.image.setOnClickListener(v -> VOID.Intent(context, CLASS.PROFILE));
        binding.toolbar.drawer.setOnClickListener(v -> binding.drawerLayout.openDrawer(GravityCompat.START));

        MobileAds.initialize(this, initializationStatus -> {
        });

        VOID.InterstitialAd(activity);

        meowBottomNavigation = binding.bottomNavigation;

        meowBottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_skin));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_home));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_hair));
        meowBottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_shopping_centers));

        meowBottomNavigation.setOnShowListener(item -> {
            Fragment fragment = null;
            switch (item.getId()) {
                case 1:
                    fragment = new SkinProductsFragment();
                    break;
                case 2:
                    fragment = new HomeFragment();
                    VOID.InterstitialShow(activity, DATA.INTERSTITIAL_HOME);
                    break;
                case 3:
                    fragment = new HairProductsFragment();
                    break;
                case 4:
                    fragment = new ShoppingCentersFragment();
                    break;
            }
            loadFragment(fragment);
        });

        meowBottomNavigation.setCount(1, number_product);
        meowBottomNavigation.setCount(3, number_product);
        meowBottomNavigation.setCount(4, number_product);

        meowBottomNavigation.show(2, true);

        meowBottomNavigation.setOnClickMenuListener(item -> {
            switch (item.getId()) {
                case 1:
                    Toast.makeText(getApplicationContext(), skin_product, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), home, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), hair_product, Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(), shopping_center, Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        meowBottomNavigation.setOnReselectListener(item -> {
            switch (item.getId()) {
                case 1:
                    Toast.makeText(getApplicationContext(), skin_product, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), home, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), hair_product, Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    Toast.makeText(getApplicationContext(), shopping_center, Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, binding.drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.imageDrawer.setOnClickListener(v -> VOID.Intent(context, CLASS.PROFILE));

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
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
            binding.imageNight.setBackgroundResource(R.drawable.sun);
        } else if (Objects.equals(sharedPreferences.getString("color_option", "NIGHT_ONE"), "NIGHT_ONE") ||
                Objects.equals(sharedPreferences.getString("color_option", "NIGHT_TWO"), "NIGHT_TWO") ||
                Objects.equals(sharedPreferences.getString("color_option", "NIGHT_THREE"), "NIGHT_THREE") ||
                Objects.equals(sharedPreferences.getString("color_option", "NIGHT_FOUR"), "NIGHT_FOUR") ||
                Objects.equals(sharedPreferences.getString("color_option", "NIGHT_FIVE"), "NIGHT_FIVE") ||
                Objects.equals(sharedPreferences.getString("color_option", "NIGHT_SIX"), "NIGHT_SIX") ||
                Objects.equals(sharedPreferences.getString("color_option", "NIGHT_SEVEN"), "NIGHT_SEVEN")) {
            binding.imageNight.setBackgroundResource(R.drawable.moon);
        }

        getNrSkin();
        getNrHair();
        getNrShoppingCenters();
        userInfo();
    }

    private void getNrSkin() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.POSTS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    assert post != null;
                    if (post.getCategory().equals(DATA.SKIN_PRODUCTS))
                        if (post.getPublisher().equals(publisher))
                            if (post.getAname().equals(aname))
                                i++;
                    binding.numberProductSkin.setText(MessageFormat.format("{0}", i));
                    meowBottomNavigation.setCount(1, (String) binding.numberProductSkin.getText());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getNrHair() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.POSTS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    assert post != null;
                    if (post.getCategory().equals(DATA.HAIR_PRODUCTS))
                        if (post.getPublisher().equals(publisher))
                            if (post.getAname().equals(aname))
                                i++;
                    binding.numberProductHair.setText(MessageFormat.format("{0}", i));
                    meowBottomNavigation.setCount(3, (String) binding.numberProductHair.getText());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getNrShoppingCenters() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.SHOPPING_CENTERS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                binding.numberShoppingCenters.setText(MessageFormat.format("{0}", dataSnapshot.getChildrenCount()));
                meowBottomNavigation.setCount(4, (String) binding.numberShoppingCenters.getText());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragment).commit();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_closeapp);
            dialog.setCancelable(true);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            dialog.findViewById(R.id.yes).setOnClickListener(v -> finish());
            dialog.findViewById(R.id.no).setOnClickListener(v -> dialog.cancel());

            dialog.show();
            Objects.requireNonNull(dialog.getWindow()).setAttributes(lp);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        binding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void My_Profile(View view) {
        VOID.Intent(context, CLASS.PROFILE);
        binding.drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void Messenger(View view) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(DATA.WHATSAPP));
        startActivity(i);
    }

    public void Default_Color(View view) {
    }

    public void Dark_Mode(View view) {
    }

    public void About_App(View view) {
        showDialogAboutApp();
    }

    public void About_My(View view) {
        showDialogAboutMy();
    }

    public void Share_App(View view) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "share app");
        shareIntent.putExtra(Intent.EXTRA_TEXT, " Beauty Touch beauty care application, download it now from Google Play " + " https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        startActivity(Intent.createChooser(shareIntent, "Choose how to share"));
    }

    public void Reward(View view) {
        VOID.Intent(context, CLASS.REWARD);
        binding.drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void Logout(View view) {
        showDialogLogout();
    }

    private void userInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.USERS).child(DATA.FirebaseUserUid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                Glide.with(context).load(user.getImageurl()).into(binding.imageDrawer);
                Glide.with(context).load(user.getImageurl()).into(binding.toolbar.image);
                binding.name.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showDialogAboutMy() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_about);
        dialog.setCancelable(true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ImageView image = dialog.findViewById(R.id.image);
        TextView text = dialog.findViewById(R.id.text);

        AboutMe(image, text);

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void AboutMe(ImageView image, TextView text) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.M_TOOLS);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Tools tools = dataSnapshot.getValue(Tools.class);
                assert tools != null;
                String aboutMe = tools.getAboutMe();
                String imageMe = tools.getImageMe();

                VOID.Glide(true, context, imageMe, image);
                text.setText(aboutMe);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showDialogAboutApp() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_app);
        dialog.setCancelable(true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.findViewById(R.id.linear_rate).setOnClickListener(v -> VOID.RateUs(activity));
        dialog.findViewById(R.id.facebook_design).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getOpenFacebookIntent());
            }

            public Intent getOpenFacebookIntent() {
                try {
                    getPackageManager().getPackageInfo("com.facebook.katana", 0);
                    return new Intent(Intent.ACTION_VIEW, Uri.parse(DATA.FB_DESINGER));
                } catch (Exception e) {
                    return new Intent(Intent.ACTION_VIEW, Uri.parse(DATA.FB_DESINGER_2));
                }
            }
        });

        dialog.findViewById(R.id.facebook_programmer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getOpenFacebookIntent());
            }

            public Intent getOpenFacebookIntent() {
                try {
                    getPackageManager().getPackageInfo("com.facebook.katana", 0);
                    return new Intent(Intent.ACTION_VIEW, Uri.parse(DATA.FB_PROGRAMMER));
                } catch (Exception e) {
                    return new Intent(Intent.ACTION_VIEW, Uri.parse(DATA.FB_PROGRAMMER_2));
                }
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void showDialogLogout() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        dialog.setCancelable(true);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.findViewById(R.id.yes).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            VOID.IntentClear(context, CLASS.LOGIN);
            finish();
        });

        dialog.findViewById(R.id.no).setOnClickListener(v -> dialog.cancel());

        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setAttributes(lp);
    }

    public void Favorites(View view) {
        VOID.Intent(context, CLASS.FAVORITES);
    }

    // Color Mode ----------------------------- Start
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(DATA.COLOR_OPTION)) {
            this.recreate();
        }
    }

    public static class SettingFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }

    public static class Setting2Fragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences_night, rootKey);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_CODE) {
            this.recreate();
        }
    }
    // Color Mode -------------------------------- End
}