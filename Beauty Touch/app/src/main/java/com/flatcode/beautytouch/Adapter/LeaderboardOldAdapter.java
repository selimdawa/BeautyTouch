package com.flatcode.beautytouch.Adapter;

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
import com.flatcode.beautytouch.Filter.LeaderboardOldFilter;
import com.flatcode.beautytouch.Model.Tools;
import com.flatcode.beautytouch.Model.User;
import com.flatcode.beautytouch.Unit.DATA;
import com.flatcode.beautytouch.Unit.VOID;
import com.flatcode.beautytouch.databinding.ItemLeaderboardBinding;

import java.text.MessageFormat;
import java.util.ArrayList;

public class LeaderboardOldAdapter extends RecyclerView.Adapter<LeaderboardOldAdapter.ViewHolder> implements Filterable {

    private final Context mContext;
    public ArrayList<User> list, filterList;
    private LeaderboardOldFilter filter;

    private static ItemLeaderboardBinding binding;

    public LeaderboardOldAdapter(Context context, ArrayList<User> list) {
        this.mContext = context;
        this.list = list;
        this.filterList = list;
    }

    @NonNull
    @Override
    public LeaderboardOldAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemLeaderboardBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull final LeaderboardOldAdapter.ViewHolder holder, final int position) {

        final User user = list.get(position);
        String id = user.getId();
        String username = user.getUsername();
        String image = user.getImageurl();

        int First = holder.getPosition();
        int Final = list.size() - First;

        holder.range.setText(MessageFormat.format("{0}", Final));
        holder.username.setText(username);
        VOID.Glide(true, mContext, image, holder.image_profile);

        SessionInfo(holder.points, id);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new LeaderboardOldFilter(filterList, this);
        }
        return filter;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_profile;
        public TextView username, range, points;
        public LinearLayout linear_one;

        public ViewHolder(View itemView) {
            super(itemView);
            username = binding.username;
            image_profile = binding.imageProfile;
            range = binding.range;
            points = binding.points;
            linear_one = binding.linearOne;
        }
    }

    private void SessionInfo(TextView points, String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(DATA.M_TOOLS);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Tools tools = dataSnapshot.getValue(Tools.class);
                assert tools != null;
                String year = tools.getOldYear();
                String session = tools.getOldSession();
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