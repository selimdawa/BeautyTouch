package com.flatcode.beautytouch.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.flatcode.beautytouch.Model.Post;
import com.flatcode.beautytouch.R;
import com.flatcode.beautytouch.Unit.CLASS;
import com.flatcode.beautytouch.Unit.DATA;
import com.flatcode.beautytouch.Unit.VOID;
import com.flatcode.beautytouch.databinding.ItemProductLinearBinding;

import java.text.MessageFormat;
import java.util.List;

public class PostLinearAdapter extends RecyclerView.Adapter<PostLinearAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Post> mPost;

    private static ItemProductLinearBinding binding;

    public PostLinearAdapter(Context context, List<Post> posts) {
        this.mContext = context;
        this.mPost = posts;
    }

    @NonNull
    @Override
    public PostLinearAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemProductLinearBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final PostLinearAdapter.ViewHolder holder, final int position) {

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

        isLiked(post.getPostid(), holder.like);
        isSaved(post.getPostid(), holder.save);
        nrLikes(holder.likes, post.getPostid());

        holder.like.setOnClickListener(view -> {
            if (holder.like.getTag().equals("like")) {
                FirebaseDatabase.getInstance().getReference().child(DATA.LIKES).child(post.getPostid())
                        .child(DATA.FirebaseUserUid).setValue(true);
            } else {
                FirebaseDatabase.getInstance().getReference().child(DATA.LIKES).child(post.getPostid())
                        .child(DATA.FirebaseUserUid).removeValue();
            }
        });

        holder.save.setOnClickListener(view -> {
            if (holder.save.getTag().equals("save")) {
                FirebaseDatabase.getInstance().getReference().child(DATA.SAVES).child(DATA.FirebaseUserUid)
                        .child(post.getPostid()).setValue(true);
            } else {
                FirebaseDatabase.getInstance().getReference().child(DATA.SAVES).child(DATA.FirebaseUserUid)
                        .child(post.getPostid()).removeValue();
            }
        });

        holder.card.setOnClickListener(view -> VOID.IntentExtra(mContext, CLASS.POST_DETAILS, DATA.POST_ID, post.getPostid()));
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CardView card;
        public ImageView image_product, like, save;
        public TextView likes, name, price;
        public LinearLayout color;

        public ViewHolder(View itemView) {
            super(itemView);

            card = binding.card;
            image_product = binding.imageProduct;
            like = binding.like;
            name = binding.name;
            save = binding.save;
            likes = binding.likes;
            price = binding.price;
            color = binding.color;
        }
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

    private void isSaved(final String postId, final ImageView imageView) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child(DATA.SAVES).child(DATA.FirebaseUserUid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(postId).exists()) {
                    imageView.setImageResource(R.drawable.ic_favorites_selected);
                    imageView.setTag("saved");
                } else {
                    imageView.setImageResource(R.drawable.ic_favorites_unselected);
                    imageView.setTag("save");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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