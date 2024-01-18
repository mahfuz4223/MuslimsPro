package com.dark.muslimspro;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DistrictAdapter extends RecyclerView.Adapter<DistrictAdapter.ViewHolder> implements Filterable {

    private List<District> districts;
    private final List<District> filteredList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private District selectedDistrict;


    public District getSelectedDistrict() {
        return selectedDistrict;
    }

    // Add this method to set the selected district
    public void setSelectedDistrict(District district) {
        selectedDistrict = district;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(District district);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView districtNameTextView;

        ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            districtNameTextView = itemView.findViewById(R.id.location_list_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(filteredList.get(position)); // Use filteredList instead of districts
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_list, parent, false);

        // Add this log statement to check if the layout is correctly inflated
        Log.d("ViewHolderCreation", "View created: " + view);

        return new ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        District district = filteredList.get(position); // Use filteredList instead of districts
        if (holder.districtNameTextView != null) {
            holder.districtNameTextView.setText(district.getName());
        } else {
            Log.e("ViewHolderBinding", "districtNameTextView is null");
        }
    }

    @Override
    public int getItemCount() {
        return filteredList != null ? filteredList.size() : 0; // Use filteredList instead of districts
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                FilterResults results = new FilterResults();
                List<District> filteredDistricts = new ArrayList<>();

                if (filterPattern.isEmpty()) {
                    // If the search query is empty, show all districts
                    filteredDistricts.addAll(districts);
                } else {
                    // Filter districts based on the search query
                    for (District district : districts) {
                        if (district.getName().toLowerCase().contains(filterPattern)) {
                            filteredDistricts.add(district);
                        }
                    }
                }

                results.values = filteredDistricts;
                results.count = filteredDistricts.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if (filterResults.values != null) {
                    filteredList.clear();
                    filteredList.addAll((List<District>) filterResults.values);
                    notifyDataSetChanged();
                }
            }
        };
    }
}
