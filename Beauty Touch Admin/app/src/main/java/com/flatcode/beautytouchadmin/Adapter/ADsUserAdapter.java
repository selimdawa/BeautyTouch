package com.flatcode.beautytouchadmin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.flatcode.beautytouchadmin.Filter.ADsUserFilter;
import com.flatcode.beautytouchadmin.Model.User;
import com.flatcode.beautytouchadmin.MyApplication;
import com.flatcode.beautytouchadmin.Unit.CLASS;
import com.flatcode.beautytouchadmin.Unit.DATA;
import com.flatcode.beautytouchadmin.Unit.VOID;
import com.flatcode.beautytouchadmin.databinding.ItemAdsUserBinding;

import java.text.MessageFormat;
import java.util.ArrayList;

public class ADsUserAdapter extends RecyclerView.Adapter<ADsUserAdapter.ViewHolder> implements Filterable {

    private ItemAdsUserBinding binding;
    private final Context context;

    public ArrayList<User> list, filterList;
    private ADsUserFilter filter;
    Boolean isUser;

    public ADsUserAdapter(Context context, ArrayList<User> list, Boolean isUser) {
        this.context = context;
        this.list = list;
        this.filterList = list;
        this.isUser = isUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemAdsUserBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final User item = list.get(position);
        String userId = DATA.EMPTY + item.getId();
        String username = DATA.EMPTY + item.getUsername();
        String profileImage = DATA.EMPTY + item.getImageurl();
        String timestamp = DATA.EMPTY + item.getStarted();
        String adLoaded = DATA.EMPTY + item.getAdLoad();
        String adClicked = DATA.EMPTY + item.getAdClick();

        String formattedDate = MyApplication.formatTimestamp(Long.parseLong(timestamp));

        VOID.Glide(true, context, profileImage, holder.profileImage);

        if (username.equals(DATA.EMPTY)) {
            holder.username.setVisibility(View.GONE);
        } else {
            holder.username.setVisibility(View.VISIBLE);
            holder.username.setText(username);
        }

        int First = holder.getPosition();
        int Final = list.size() - First;

        holder.time.setText(formattedDate);
        holder.rank.setText(MessageFormat.format("{0}", Final));
        holder.numberADsLoad.setText(MessageFormat.format("{0}{1}", DATA.EMPTY, adLoaded));
        holder.numberADsClick.setText(MessageFormat.format("{0}{1}", DATA.EMPTY, adClicked));

        holder.item.setOnClickListener(view ->
                VOID.IntentExtra(context, CLASS.ADS_INFO, DATA.PROFILE_ID, userId));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new ADsUserFilter(filterList, this);
        }
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage;
        TextView username, rank, numberADsLoad, numberADsClick, time;
        LinearLayout item;

        public ViewHolder(View view) {
            super(view);
            profileImage = binding.profileImage;
            username = binding.username;
            rank = binding.rank;
            numberADsLoad = binding.numberADsLoad;
            numberADsClick = binding.numberADsClick;
            time = binding.time;
            item = binding.item;
        }
    }
}