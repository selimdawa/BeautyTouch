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
import com.flatcode.beautytouch.Adapter.ProductsStaggeredAdapter;
import com.flatcode.beautytouch.Model.Post;
import com.flatcode.beautytouch.Unit.DATA;
import com.flatcode.beautytouch.Unit.VOID;
import com.flatcode.beautytouch.databinding.FragmentHairProductsBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HairProductsFragment extends Fragment {

    private FragmentHairProductsBinding binding;

    private List<Post> list;
    private ProductsStaggeredAdapter adapter;

    String publisher = DATA.PUBLISHER_NAME , aname = DATA.APP_NAME;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHairProductsBinding.inflate(LayoutInflater.from(getContext()), container, false);

        VOID.BannerAd(getContext(), binding.adView, DATA.BANNER_HAIR);

        //binding.recyclerView.setHasFixedSize(true);
        list = new ArrayList<>();
        adapter = new ProductsStaggeredAdapter(getContext(), list);
        binding.recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    private void getAllPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.POSTS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    assert post != null;
                    if (post.getCategory().equals(DATA.HAIR_PRODUCTS)) {
                        if (post.getPublisher().equals(publisher)) {
                            if (post.getAname().equals(aname)) {
                                list.add(post);
                            }
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