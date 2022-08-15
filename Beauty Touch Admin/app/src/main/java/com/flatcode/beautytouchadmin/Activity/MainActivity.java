package com.flatcode.beautytouchadmin.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.flatcode.beautytouchadmin.Adapter.MainAdapter;
import com.flatcode.beautytouchadmin.Model.Main;
import com.flatcode.beautytouchadmin.Model.Post;
import com.flatcode.beautytouchadmin.Model.User;
import com.flatcode.beautytouchadmin.R;
import com.flatcode.beautytouchadmin.Unit.CLASS;
import com.flatcode.beautytouchadmin.Unit.DATA;
import com.flatcode.beautytouchadmin.Unit.THEME;
import com.flatcode.beautytouchadmin.Unit.VOID;
import com.flatcode.beautytouchadmin.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ActivityMainBinding binding;

    List<Main> list;
    MainAdapter adapter;

    Context context = MainActivity.this;

    private static final int SETTINGS_CODE = 234;

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
                .replace(R.id.settings, new MainActivity.SettingsFragment())
                .commit();
        // Color Mode -------------------------------- End

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        if (sharedPreferences.getString("color_option", "ONE").equals("ONE") ||
                sharedPreferences.getString("color_option", "TWO").equals("TWO") ||
                sharedPreferences.getString("color_option", "THREE").equals("THREE") ||
                sharedPreferences.getString("color_option", "FOUR").equals("FOUR") ||
                sharedPreferences.getString("color_option", "FIVE").equals("FIVE") ||
                sharedPreferences.getString("color_option", "SIX").equals("SIX") ||
                sharedPreferences.getString("color_option", "SEVEN").equals("SEVEN") ||
                sharedPreferences.getString("color_option", "EIGHT").equals("EIGHT") ||
                sharedPreferences.getString("color_option", "NINE").equals("NINE") ||
                sharedPreferences.getString("color_option", "TEEN").equals("TEEN")) {
            binding.toolbar.mode.setBackgroundResource(R.drawable.sun);
        } else if (sharedPreferences.getString("color_option", "NIGHT_ONE").equals("NIGHT_ONE") ||
                sharedPreferences.getString("color_option", "NIGHT_TWO").equals("NIGHT_TWO") ||
                sharedPreferences.getString("color_option", "NIGHT_THREE").equals("NIGHT_THREE") ||
                sharedPreferences.getString("color_option", "NIGHT_FOUR").equals("NIGHT_FOUR") ||
                sharedPreferences.getString("color_option", "NIGHT_FIVE").equals("NIGHT_FIVE") ||
                sharedPreferences.getString("color_option", "NIGHT_SIX").equals("NIGHT_SIX") ||
                sharedPreferences.getString("color_option", "NIGHT_SEVEN").equals("NIGHT_SEVEN")) {
            binding.toolbar.mode.setBackgroundResource(R.drawable.moon);
        }

        binding.toolbar.image.setOnClickListener(v ->
                VOID.IntentExtra(context, CLASS.PROFILE, DATA.PROFILE_ID, DATA.FirebaseUserUid));

        userInfo();

        binding.recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new MainAdapter(context, list);
        binding.recyclerView.setAdapter(adapter);

        nrItems();
    }

    private void IdeaPosts(int users, int hotProduct, int posts, int shoppingCentres, int sliderShow) {
        list.clear();
        Main item1 = new Main(R.drawable.ic_person_white, "المستخدمين", (users - 1), CLASS.USERS);
        Main item2 = new Main(R.drawable.ic_hot, "الأكثر رواجا", hotProduct, CLASS.HOT_PRODUCTS);
        Main item3 = new Main(R.drawable.ic_post, "منشوراتي", posts, CLASS.POSTS);
        Main item4 = new Main(R.drawable.ic_add, "إضافة منشور", 0, CLASS.POST_ADD);
        Main item5 = new Main(R.drawable.ic_store, "مراكز التسوق", shoppingCentres, CLASS.SHOPPING_CENTRES);
        Main item6 = new Main(R.drawable.ic_add, "إضافة مركز تسوق", 0, CLASS.SHOPPING_CENTRES_ADD);
        Main item7 = new Main(R.drawable.ic_rank, "المرحلة الحالية", 0, CLASS.SESSION_NOW);
        Main item8 = new Main(R.drawable.ic_rank, "المرحلة السابقة", 0, CLASS.SESSION_OLD);
        Main item9 = new Main(R.drawable.ic_slider, "سلايدر", sliderShow, CLASS.SLIDER_SHOW);
        Main item10 = new Main(R.drawable.ic_ad, "مراقب الإعلانات", 0, CLASS.ADS_METER);
        Main item11 = new Main(R.drawable.ic_my, "عني", 0, CLASS.ABOUT_ME);
        Main item12 = new Main(R.drawable.ic_settings, "الأدوات", 0, CLASS.TOOLS);
        list.add(item1);
        list.add(item2);
        list.add(item3);
        list.add(item4);
        list.add(item5);
        list.add(item6);
        list.add(item7);
        list.add(item8);
        list.add(item9);
        list.add(item10);
        list.add(item11);
        list.add(item12);
        adapter.notifyDataSetChanged();
        binding.progress.setVisibility(View.GONE);
        binding.recyclerView.setVisibility(View.VISIBLE);
    }

    private void userInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.USERS).child(DATA.FirebaseUserUid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                VOID.Glide(true, context, user.getImageurl(), binding.toolbar.image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    int U = 0, H = 0, P = 0, SH = 0, S = 0;

    private void nrItems() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.USERS);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User item = data.getValue(User.class);
                    assert item != null;
                    if (item.getId() != null)
                        U++;
                }
                nrHotProduct();
            }

            private void nrHotProduct() {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.HOT_PRODUCT);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        H = (int) dataSnapshot.getChildrenCount();
                        nrPosts();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            private void nrPosts() {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.POSTS);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Post item = data.getValue(Post.class);
                            assert item != null;
                            if (item.getPublisher().equals(DATA.FirebaseUserUid))
                                P++;
                        }
                        nrShoppingCenters();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            private void nrShoppingCenters() {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.SHOPPING_CENTERS);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Post item = data.getValue(Post.class);
                            assert item != null;
                            if (item.getPublisher().equals(DATA.FirebaseUserUid))
                                SH++;
                        }
                        nrSliderShow();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            private void nrSliderShow() {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.SLIDER_SHOW);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        S = (int) dataSnapshot.getChildrenCount();
                        IdeaPosts(U, H, P, SH, S);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Color Mode ----------------------------- Start
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("color_option")) {
            this.recreate();
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
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