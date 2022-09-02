package com.flatcode.beautytouch.Fragment;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.flatcode.beautytouch.Adapter.ShoppingCentersAdapter;
import com.flatcode.beautytouch.Model.ShoppingCenter;
import com.flatcode.beautytouch.Unit.DATA;
import com.flatcode.beautytouch.Unit.VOID;
import com.flatcode.beautytouch.databinding.FragmentShoppingCentersBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShoppingCentersFragment extends Fragment {

    private FragmentShoppingCentersBinding binding;

    private List<ShoppingCenter> list;
    private ShoppingCentersAdapter adapter;

    String publisher = DATA.PUBLISHER_NAME , aname = DATA.APP_NAME;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentShoppingCentersBinding.inflate(LayoutInflater.from(getContext()), container, false);

        VOID.BannerAd(getContext(), binding.adView, DATA.BANNER_SHOPPING_CENTRES);

        //binding.recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new ShoppingCentersAdapter(getContext(), list);
        binding.recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    private void getAllPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.SHOPPING_CENTERS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ShoppingCenter shoppingCenter = snapshot.getValue(ShoppingCenter.class);
                    assert shoppingCenter != null;
                    if (shoppingCenter.getPublisher().equals(publisher)) {
                        if (shoppingCenter.getAname().equals(aname)) {
                            list.add(shoppingCenter);
                        }
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
    public void onResume() {
        getAllPosts();
        super.onResume();
    }
}