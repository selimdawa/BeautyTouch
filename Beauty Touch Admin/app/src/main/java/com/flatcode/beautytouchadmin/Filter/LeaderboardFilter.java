package com.flatcode.beautytouchadmin.Filter;

import android.widget.Filter;

import com.flatcode.beautytouchadmin.Adapter.LeaderboardAdapter;
import com.flatcode.beautytouchadmin.Model.User;

import java.util.ArrayList;

public class LeaderboardFilter extends Filter {

    ArrayList<User> list;
    LeaderboardAdapter adapter;

    public LeaderboardFilter(ArrayList<User> list, LeaderboardAdapter adapter) {
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