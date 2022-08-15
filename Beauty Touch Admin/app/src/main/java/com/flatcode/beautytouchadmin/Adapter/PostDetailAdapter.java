package com.flatcode.beautytouchadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.flatcode.beautytouchadmin.Model.Post;
import com.flatcode.beautytouchadmin.R;
import com.flatcode.beautytouchadmin.Unit.DATA;
import com.flatcode.beautytouchadmin.Unit.VOID;
import com.flatcode.beautytouchadmin.databinding.ItemPostDetailBinding;

import java.text.MessageFormat;
import java.util.List;

public class PostDetailAdapter extends RecyclerView.Adapter<PostDetailAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Post> mPost;

    private static ItemPostDetailBinding binding;

    public PostDetailAdapter(Context context, List<Post> mPost) {
        this.mContext = context;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public PostDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemPostDetailBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final PostDetailAdapter.ViewHolder holder, final int position) {

        final Post post = mPost.get(position);

        VOID.Glide(false, mContext, post.getPostimage(), holder.image_product);
        VOID.Glide(false, mContext, post.getPostimage(), holder.image_product_1);
        VOID.Glide(false, mContext, post.getPostimage2(), holder.image_product_2);
        VOID.Glide(false, mContext, post.getPostimage3(), holder.image_product_3);
        VOID.Glide(false, mContext, post.getPostimage4(), holder.image_product_4);
        VOID.Glide(false, mContext, post.getPostimage5(), holder.image_product_5);
        VOID.Glide(false, mContext, post.getPostimage6(), holder.image_product_6);
        VOID.Glide(false, mContext, post.getPostimage7(), holder.image_product_7);
        VOID.Glide(false, mContext, post.getPostimage8(), holder.image_product_8);
        VOID.Glide(false, mContext, post.getPostimage9(), holder.image_product_9);
        VOID.Glide(false, mContext, post.getPostimage10(), holder.image_product_10);

        if (post.getPostimage2().equals(DATA.EMPTY)) {
            holder.image_product_2.setVisibility(View.GONE);
        } else {
            holder.image_product_2.setVisibility(View.VISIBLE);
        }

        if (post.getPostimage3().equals(DATA.EMPTY)) {
            holder.image_product_3.setVisibility(View.GONE);
        } else {
            holder.image_product_3.setVisibility(View.VISIBLE);
        }

        if (post.getPostimage4().equals(DATA.EMPTY)) {
            holder.image_product_4.setVisibility(View.GONE);
        } else {
            holder.image_product_4.setVisibility(View.VISIBLE);
        }

        if (post.getPostimage5().equals(DATA.EMPTY)) {
            holder.image_product_5.setVisibility(View.GONE);
        } else {
            holder.image_product_5.setVisibility(View.VISIBLE);
        }

        if (post.getPostimage6().equals(DATA.EMPTY)) {
            holder.image_product_6.setVisibility(View.GONE);
        } else {
            holder.image_product_6.setVisibility(View.VISIBLE);
        }

        if (post.getPostimage7().equals(DATA.EMPTY)) {
            holder.image_product_7.setVisibility(View.GONE);
        } else {
            holder.image_product_7.setVisibility(View.VISIBLE);
        }

        if (post.getPostimage8().equals(DATA.EMPTY)) {
            holder.image_product_8.setVisibility(View.GONE);
        } else {
            holder.image_product_8.setVisibility(View.VISIBLE);
        }

        if (post.getPostimage9().equals(DATA.EMPTY)) {
            holder.image_product_9.setVisibility(View.GONE);
        } else {
            holder.image_product_9.setVisibility(View.VISIBLE);
        }

        if (post.getPostimage10().equals(DATA.EMPTY)) {
            holder.image_product_10.setVisibility(View.GONE);
        } else {
            holder.image_product_10.setVisibility(View.VISIBLE);
        }

        if (post.getPostimage2().equals(DATA.EMPTY) && post.getPostimage3().equals(DATA.EMPTY) &&
                post.getPostimage4().equals(DATA.EMPTY) && post.getPostimage5().equals(DATA.EMPTY) &&
                post.getPostimage6().equals(DATA.EMPTY) && post.getPostimage7().equals(DATA.EMPTY) &&
                post.getPostimage8().equals(DATA.EMPTY) && post.getPostimage9().equals(DATA.EMPTY) &&
                post.getPostimage10().equals(DATA.EMPTY)) {
            holder.scroll_image.setVisibility(View.GONE);
        } else {
            holder.scroll_image.setVisibility(View.VISIBLE);
        }

        if (post.getName().equals(DATA.EMPTY)) {
            holder.product_name.setVisibility(View.GONE);
        } else {
            holder.product_name.setVisibility(View.VISIBLE);
            holder.product_name.setText(post.getName());
        }

        if (post.getPrice().equals(DATA.EMPTY)) {
            holder.price_product.setVisibility(View.GONE);
        } else {
            holder.price_product.setVisibility(View.VISIBLE);
            holder.price_product.setText(MessageFormat.format("{0} $", post.getPrice()));
        }

        if (post.getIndications().equals(DATA.EMPTY)) {
            holder.linear_indications.setVisibility(View.GONE);
            holder.linear_indications2.setVisibility(View.GONE);
        } else {
            holder.linear_indications.setVisibility(View.VISIBLE);
            holder.text_indications.setVisibility(View.VISIBLE);
            holder.linear_indications2.setVisibility(View.VISIBLE);
            holder.indications.setVisibility(View.VISIBLE);
            holder.indications.setText(post.getIndications());
        }

        if (post.getUse().equals(DATA.EMPTY)) {
            holder.linear_how_to_use.setVisibility(View.GONE);
            holder.linear_how_to_use2.setVisibility(View.GONE);
        } else {
            holder.linear_how_to_use.setVisibility(View.VISIBLE);
            holder.text_how_to_use.setVisibility(View.VISIBLE);
            holder.linear_how_to_use2.setVisibility(View.VISIBLE);
            holder.how_to_use.setVisibility(View.VISIBLE);
            holder.how_to_use.setText(post.getUse());
        }

        isLiked(post.getPostid(), holder.like);
        isSaved(post.getPostid(), holder.save);
        nrLikes(holder.like_number, post.getPostid());

        holder.save.setOnClickListener(view -> {
            if (holder.save.getTag().equals("save")) {
                FirebaseDatabase.getInstance().getReference().child(DATA.SAVES).child(DATA.FirebaseUserUid)
                        .child(post.getPostid()).setValue(true);
            } else {
                FirebaseDatabase.getInstance().getReference().child(DATA.SAVES).child(DATA.FirebaseUserUid)
                        .child(post.getPostid()).removeValue();
            }
        });

        holder.like.setOnClickListener(view -> {
            if (holder.like.getTag().equals("like")) {
                FirebaseDatabase.getInstance().getReference().child(DATA.LIKES).child(post.getPostid())
                        .child(DATA.FirebaseUserUid).setValue(true);
            } else {
                FirebaseDatabase.getInstance().getReference().child(DATA.LIKES).child(post.getPostid())
                        .child(DATA.FirebaseUserUid).removeValue();
            }
        });

        holder.image_product_1.setOnClickListener(v ->
                VOID.Glide(false, mContext, post.getPostimage(), holder.image_product));

        holder.image_product_2.setOnClickListener(v ->
                VOID.Glide(false, mContext, post.getPostimage2(), holder.image_product));

        holder.image_product_3.setOnClickListener(v ->
                VOID.Glide(false, mContext, post.getPostimage3(), holder.image_product));

        holder.image_product_4.setOnClickListener(v ->
                VOID.Glide(false, mContext, post.getPostimage4(), holder.image_product));

        holder.image_product_5.setOnClickListener(v ->
                VOID.Glide(false, mContext, post.getPostimage5(), holder.image_product));

        holder.image_product_6.setOnClickListener(v ->
                VOID.Glide(false, mContext, post.getPostimage6(), holder.image_product));

        holder.image_product_7.setOnClickListener(v ->
                VOID.Glide(false, mContext, post.getPostimage7(), holder.image_product));

        holder.image_product_8.setOnClickListener(v ->
                VOID.Glide(false, mContext, post.getPostimage8(), holder.image_product));

        holder.image_product_9.setOnClickListener(v ->
                VOID.Glide(false, mContext, post.getPostimage9(), holder.image_product));

        holder.image_product_10.setOnClickListener(v ->
                VOID.Glide(false, mContext, post.getPostimage10(), holder.image_product));
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_product, save, like, image_product_1, image_product_2, image_product_3,
                image_product_4, image_product_5, image_product_6, image_product_7, image_product_8,
                image_product_9, image_product_10;
        public TextView product_name, price_product, like_number, text_indications, indications,
                text_how_to_use, how_to_use;
        public LinearLayout linear_indications, linear_indications2, linear_how_to_use, linear_how_to_use2;
        public HorizontalScrollView scroll_image;

        public ViewHolder(View view) {
            super(view);

            image_product = binding.imageProduct;
            product_name = binding.productName;
            save = binding.save;
            price_product = binding.priceProduct;
            like_number = binding.likeNumber;
            like = binding.like;
            linear_indications = binding.linearIndications;
            text_indications = binding.textIndications;
            linear_indications2 = binding.linearIndications2;
            indications = binding.indications;
            linear_how_to_use = binding.linearHowToUse;
            text_how_to_use = binding.textHowToUse;
            linear_how_to_use2 = binding.linearHowToUse2;
            how_to_use = binding.howToUse;
            image_product_1 = binding.imageProduct1;
            image_product_2 = binding.imageProduct2;
            image_product_3 = binding.imageProduct3;
            image_product_4 = binding.imageProduct4;
            image_product_5 = binding.imageProduct5;
            image_product_6 = binding.imageProduct6;
            image_product_7 = binding.imageProduct7;
            image_product_8 = binding.imageProduct8;
            image_product_9 = binding.imageProduct9;
            image_product_10 = binding.imageProduct10;
            scroll_image = binding.scrollImage;

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