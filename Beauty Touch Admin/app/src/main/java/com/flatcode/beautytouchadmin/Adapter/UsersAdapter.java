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
import com.flatcode.beautytouchadmin.Model.User;
import com.flatcode.beautytouchadmin.Unit.CLASS;
import com.flatcode.beautytouchadmin.Unit.DATA;
import com.flatcode.beautytouchadmin.Unit.VOID;
import com.flatcode.beautytouchadmin.databinding.ItemUserBinding;

import java.text.MessageFormat;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private final Context mContext;
    private final List<User> mUser;

    private static ItemUserBinding binding;

    public UsersAdapter(Context context, List<User> mUser) {
        this.mContext = context;
        this.mUser = mUser;
    }

    @NonNull
    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemUserBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final UsersAdapter.ViewHolder holder, final int position) {

        final User user = mUser.get(position);
        String id = DATA.EMPTY + user.getId();

        VOID.Glide(true, mContext, user.getImageurl(), holder.image);

        if (user.getUsername().equals(DATA.EMPTY)) {
            holder.name.setVisibility(View.GONE);
        } else {
            holder.name.setVisibility(View.VISIBLE);
            holder.name.setText(user.getUsername());
        }

        nrFavorites(holder.favorites, id);

        holder.card.setOnClickListener(view -> VOID.IntentExtra(mContext, CLASS.USER_DETAILS, DATA.PROFILE_ID, id));
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView name, favorites;
        public CardView card;

        public ViewHolder(View view) {
            super(view);
            image = binding.image;
            name = binding.name;
            favorites = binding.favorites;
            card = binding.card;
        }
    }

    private void nrFavorites(final TextView favorites, String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.SAVES).child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favorites.setText(MessageFormat.format("{0}", dataSnapshot.getChildrenCount()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}