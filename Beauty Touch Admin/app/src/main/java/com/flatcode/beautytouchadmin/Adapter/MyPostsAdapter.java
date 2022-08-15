package com.flatcode.beautytouchadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.flatcode.beautytouchadmin.R;
import com.flatcode.beautytouchadmin.Unit.CLASS;
import com.flatcode.beautytouchadmin.Unit.DATA;
import com.flatcode.beautytouchadmin.Unit.VOID;
import com.flatcode.beautytouchadmin.databinding.ItemMyPostBinding;

import java.text.MessageFormat;
import java.util.List;

public class MyPostsAdapter extends RecyclerView.Adapter<MyPostsAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Post> mPost;

    private static ItemMyPostBinding binding;

    public MyPostsAdapter(Context context, List<Post> mPost) {
        this.mContext = context;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public MyPostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemMyPostBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final MyPostsAdapter.ViewHolder holder, final int position) {

        final Post post = mPost.get(position);

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
        isLiked(post.getPostid(), holder.like);

        holder.like.setOnClickListener(view -> {
            if (holder.like.getTag().equals("like")) {
                FirebaseDatabase.getInstance().getReference().child(DATA.LIKES).child(post.getPostid())
                        .child(DATA.FirebaseUserUid).setValue(true);
            } else {
                FirebaseDatabase.getInstance().getReference().child(DATA.LIKES).child(post.getPostid())
                        .child(DATA.FirebaseUserUid).removeValue();
            }
        });

        holder.more.setOnClickListener(view -> VOID.moreOptionDialog(mContext, post));
        holder.card.setOnClickListener(view -> VOID.IntentExtra(mContext, CLASS.POST_DETAILS, DATA.POST_ID, post.getPostid()));
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_product, more, like;
        public TextView likes, name, price;
        public CardView card;

        public ViewHolder(View view) {
            super(view);
            image_product = binding.imageProduct;
            more = binding.more;
            like = binding.like;
            name = binding.name;
            price = binding.price;
            likes = binding.likes;
            card = binding.card;
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

    private void isLiked(final String postId, final ImageView imageView) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child(DATA.LIKES).child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(DATA.FirebaseUserUid).exists()) {
                    imageView.setImageResource(R.drawable.ic_heart_selected);
                    imageView.setTag("liked");
                } else {
                    imageView.setImageResource(R.drawable.ic_heart_unselected);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}