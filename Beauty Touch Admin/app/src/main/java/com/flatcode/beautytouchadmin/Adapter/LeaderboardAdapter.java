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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.flatcode.beautytouchadmin.Filter.LeaderboardFilter;
import com.flatcode.beautytouchadmin.Model.Tools;
import com.flatcode.beautytouchadmin.Model.User;
import com.flatcode.beautytouchadmin.Unit.CLASS;
import com.flatcode.beautytouchadmin.Unit.DATA;
import com.flatcode.beautytouchadmin.Unit.VOID;
import com.flatcode.beautytouchadmin.databinding.ItemLeaderboradBinding;

import java.text.MessageFormat;
import java.util.ArrayList;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> implements Filterable {

    private ItemLeaderboradBinding binding;
    private final Context context;

    public ArrayList<User> list, filterList;
    private LeaderboardFilter filter;
    Boolean isUser;

    public LeaderboardAdapter(Context context, ArrayList<User> list, Boolean isUser) {
        this.context = context;
        this.list = list;
        this.filterList = list;
        this.isUser = isUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemLeaderboradBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final User item = list.get(position);
        String id = DATA.EMPTY + item.getId();
        String username = DATA.EMPTY + item.getUsername();
        String image = DATA.EMPTY + item.getImageurl();

        int First = holder.getPosition();
        int Final = list.size() - First;

        holder.rank.setText(MessageFormat.format("{0}", Final));
        VOID.Glide(true, context, image, holder.profileImage);

        if (username.equals(DATA.EMPTY)) {
            holder.username.setVisibility(View.GONE);
        } else {
            holder.username.setVisibility(View.VISIBLE);
            holder.username.setText(username);
        }

        SessionInfo(holder.numberADsLoad, id);

        holder.item.setOnClickListener(view -> VOID.IntentExtra(context, CLASS.ADS_INFO, DATA.PROFILE_ID, id));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new LeaderboardFilter(filterList, this);
        }
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage;
        TextView username, numberADsLoad, rank;
        LinearLayout item;

        public ViewHolder(View view) {
            super(view);
            profileImage = binding.profileImage;
            username = binding.username;
            numberADsLoad = binding.numberADsLoad;
            rank = binding.rank;
            item = binding.item;
        }
    }

    private void SessionInfo(TextView points, String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.M_TOOLS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Tools tools = dataSnapshot.getValue(Tools.class);
                assert tools != null;
                String year = tools.getYear();
                String session = tools.getSession();
                Points(year, session, points, id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Points(final String year, final String session, TextView points, String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.USERS).child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String key = year + "_" + session;
                String value = DATA.EMPTY + dataSnapshot.child(key).getValue();
                if (dataSnapshot.child(key).exists())
                    points.setText(MessageFormat.format("{0}", value));
                else
                    points.setText("0");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}