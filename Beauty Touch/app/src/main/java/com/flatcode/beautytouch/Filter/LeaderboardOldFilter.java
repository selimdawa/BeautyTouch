package com.flatcode.beautytouch.Filter;

import android.widget.Filter;

import com.flatcode.beautytouch.Adapter.LeaderboardOldAdapter;
import com.flatcode.beautytouch.Model.User;

import java.util.ArrayList;

public class LeaderboardOldFilter extends Filter {

    ArrayList<User> list;
    LeaderboardOldAdapter adapter;

    public LeaderboardOldFilter(ArrayList<User> list, LeaderboardOldAdapter adapter) {
        this.list = list;
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toUpperCase();
            ArrayList<User> filter = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getUsername().contains(constraint)) {
                    filter.add(list.get(i));
                }
            }
            results.count = filter.size();
            results.values = filter;
        } else {
            results.count = list.size();
            results.values = list;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.list = (ArrayList<User>) results.values;
        adapter.notifyDataSetChanged();
    }
}