package com.flatcode.beautytouch.Fragment;

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
import com.flatcode.beautytouch.Adapter.ImageSliderAdapter;
import com.flatcode.beautytouch.Adapter.PostHotAdapter;
import com.flatcode.beautytouch.Adapter.PostLinearAdapter;
import com.flatcode.beautytouch.Model.Post;
import com.flatcode.beautytouch.Unit.DATA;
import com.flatcode.beautytouch.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    List<String> hotProductList;
    PostHotAdapter hotpostAdapter;
    List<Post> hotpostLists;

    PostLinearAdapter allpostAdapter;
    List<Post> allpostLists;

    int TotalCounts;

    String publisher = DATA.PUBLISHER_NAME , aname = DATA.APP_NAME;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(LayoutInflater.from(getContext()), container, false);

        binding.recyclerView.setHasFixedSize(true);
        hotpostLists = new ArrayList<>();
        hotpostAdapter = new PostHotAdapter(getContext(), hotpostLists);
        binding.recyclerView.setAdapter(hotpostAdapter);

        //binding.recyclerView.setHasFixedSize(true);
        allpostLists = new ArrayList<>();
        allpostAdapter = new PostLinearAdapter(getContext(), allpostLists);
        binding.recyclerView2.setAdapter(allpostAdapter);

        FirebaseDatabase.getInstance().getReference(DATA.IMAGE_LINKS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long counts = snapshot.getChildrenCount();
                TotalCounts = counts.intValue();

                binding.imageSlider.setSliderAdapter(new ImageSliderAdapter(getContext(), TotalCounts));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();
    }

    private void checkHotProduct() {
        hotProductList = new ArrayList();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.HOT_PRODUCT);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hotProductList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    hotProductList.add(snapshot.getKey());
                }
                readPosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.POSTS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                hotpostLists.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    for (String id : hotProductList) {
                        assert post != null;
                        if (post.getPublisher().equals(publisher))
                            if (post.getAname().equals(aname))
                                if (post.getPostid().equals(id))
                                    hotpostLists.add(post);
                    }
                }
                hotpostAdapter.notifyDataSetChanged();
                binding.progressCircular.setVisibility(View.GONE);
                binding.recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getShowMoreProduct() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.POSTS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allpostLists.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    assert post != null;
                    if (post.getPublisher().equals(publisher))
                        if (post.getAname().equals(aname))
                            allpostLists.add(post);
                }
                allpostAdapter.notifyDataSetChanged();
                binding.progressCircular2.setVisibility(View.GONE);
                binding.recyclerView2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        checkHotProduct();
        getShowMoreProduct();
        super.onResume();
    }
}