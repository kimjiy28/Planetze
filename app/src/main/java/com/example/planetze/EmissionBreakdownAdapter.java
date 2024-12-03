package com.example.planetze;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class EmissionBreakdownAdapter extends RecyclerView.Adapter<EmissionBreakdownAdapter.ViewHolder> {

    private List<EmissionCategory> emissionList;

    public EmissionBreakdownAdapter(List<EmissionCategory> emissionList) {
        this.emissionList = emissionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_emission_breakdown, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EmissionCategory category = emissionList.get(position);
        holder.categoryTextView.setText(category.getCategory());
        holder.percentageTextView.setText(category.getPercentage() + "%");
    }

    @Override
    public int getItemCount() {
        return emissionList.size();
    }
    public void updateEmissionList(List<EmissionCategory> newEmissionList) {
        this.emissionList.clear();
        this.emissionList.addAll(newEmissionList);
        notifyDataSetChanged(); // Notify RecyclerView to refresh
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTextView, percentageTextView;

        ViewHolder(View itemView) {
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.emissionCategoryTextView);
            percentageTextView = itemView.findViewById(R.id.emissionPercentageTextView);
        }
    }
}
