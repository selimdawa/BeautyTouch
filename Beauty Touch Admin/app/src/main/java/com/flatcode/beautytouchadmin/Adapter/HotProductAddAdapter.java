package com.flatcode.beautytouchadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.flatcode.beautytouchadmin.Model.Post;
import com.flatcode.beautytouchadmin.Unit.CLASS;
import com.flatcode.beautytouchadmin.Unit.DATA;
import com.flatcode.beautytouchadmin.Unit.VOID;
import com.flatcode.beautytouchadmin.databinding.ItemProductAddBinding;

import java.text.MessageFormat;
import java.util.List;

public class HotProductAddAdapter extends RecyclerView.Adapter<HotProductAddAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Post> mPost;

    private static ItemProductAddBinding binding;

    public HotProductAddAdapter(Context context, List<Post> mPost) {
        this.mContext = context;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public HotProductAddAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemProductAddBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final HotProductAddAdapter.ViewHolder holder, final int position) {

        final Post post = mPost.get(position);
        String id = post.getPostid();

        VOID.Glide(false, mContext, post.getPostimage(), holder.image_product);

        if (post.getName().equals(DATA.EMPTY)) {
            holder.name.setVisibility(View.GONE);
        } else {
            holder.name.setVisibility(View.VISIBLE);
            holder.name.setText(post.getName());
        }

        if (post.getPrice().equals(DATA.EMPTY)) {
            holder.price.setVisibility(View.GONE);
        } else {
            holder.price.setVisibility(View.VISIBLE);
            holder.price.setText(MessageFormat.format("{0} $", post.getPrice()));
        }

        nrLikes(holder.likes, post.getPostid());

        holder.add.setOnClickListener(view -> FirebaseDatabase.getInstance().getReference().child(DATA.HOT_PRODUCT).child(post.getPostid()).setValue(true));
        holder.card.setOnClickListener(view -> VOID.IntentExtra(mContext, CLASS.POST_DETAILS, DATA.POST_ID, id));
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView card;
        public ImageView image_product;
        public TextView likes, name, price;
        public ImageButton add;

        public ViewHolder(View itemView) {
            super(itemView);

            card = binding.card;
            image_product = binding.imageProduct;
            likes = binding.likes;
            name = binding.name;
            price = binding.price;
            add = binding.add;
        }
    }

    private void nrLikes(final TextView likes, String postId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(DATA.LIKES).child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                likes.setText(MessageFormat.format("{0}", dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}