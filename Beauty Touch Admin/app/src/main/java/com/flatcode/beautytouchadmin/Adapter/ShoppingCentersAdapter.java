package com.flatcode.beautytouchadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flatcode.beautytouchadmin.Model.ShoppingCenter;
import com.flatcode.beautytouchadmin.Unit.DATA;
import com.flatcode.beautytouchadmin.Unit.VOID;
import com.flatcode.beautytouchadmin.databinding.ItemShoppingCenterBinding;

import java.text.MessageFormat;
import java.util.List;

public class ShoppingCentersAdapter extends RecyclerView.Adapter<ShoppingCentersAdapter.ViewHolder> {

    private final Context mContext;
    private final List<ShoppingCenter> mShoppingCenters;

    private static ItemShoppingCenterBinding binding;

    public ShoppingCentersAdapter(Context context, List<ShoppingCenter> shoppingCenters) {
        this.mContext = context;
        this.mShoppingCenters = shoppingCenters;
    }

    @NonNull
    @Override
    public ShoppingCentersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemShoppingCenterBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final ShoppingCentersAdapter.ViewHolder holder, final int position) {

        final ShoppingCenter shoppingCenter = mShoppingCenters.get(position);
        String id = shoppingCenter.getId();

        VOID.Glide(false, mContext, shoppingCenter.getImageurl(), holder.image_product);
        VOID.Glide(false, mContext, shoppingCenter.getImageurl2(), holder.image_product2);

        if (shoppingCenter.getName().equals(DATA.EMPTY)) {
            holder.linearName.setVisibility(View.GONE);
        } else {
            holder.linearName.setVisibility(View.VISIBLE);
            holder.name.setText(shoppingCenter.getName());
        }

        if (shoppingCenter.getLocation().equals(DATA.EMPTY) && shoppingCenter.getLocation2().equals(DATA.EMPTY)) {
            holder.linearLocation.setVisibility(View.GONE);
            holder.view.setVisibility(View.GONE);
        } else {
            holder.linearLocation.setVisibility(View.VISIBLE);
            holder.view.setVisibility(View.VISIBLE);
            holder.location.setText(MessageFormat.format("{0} - {1} - {2}"
                    , shoppingCenter.getLocation(), shoppingCenter.getLocation2()
                    , shoppingCenter.getLocation3()));
        }

        if (shoppingCenter.getNumberPhone().equals(DATA.EMPTY)) {
            holder.linearNumberPhone.setVisibility(View.GONE);
            holder.view2.setVisibility(View.GONE);
        } else {
            holder.linearNumberPhone.setVisibility(View.VISIBLE);
            holder.view2.setVisibility(View.VISIBLE);
            holder.numberPhone.setText(shoppingCenter.getNumberPhone());
        }

        holder.more.setOnClickListener(view -> VOID.moreShoppingCenters(mContext, shoppingCenter));
    }

    public int getItemCount() {
        return mShoppingCenters.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_product, image_product2, more;
        public TextView name, location, numberPhone;
        public LinearLayout linearName, linearLocation, linearNumberPhone;
        public View view, view2;

        public ViewHolder(View itemView) {
            super(itemView);

            image_product = binding.imageProduct;
            image_product2 = binding.imageProduct2;
            name = binding.name;
            location = binding.location;
            numberPhone = binding.numberPhone;
            linearName = binding.linearName;
            linearLocation = binding.linearLocation;
            linearNumberPhone = binding.linearNumberPhone;
            view = binding.view;
            view2 = binding.view2;
            more = binding.more;
        }
    }
}