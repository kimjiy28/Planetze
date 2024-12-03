package com.example.planetze;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BreakdownAdapter extends RecyclerView.Adapter<BreakdownAdapter.BreakdownViewHolder> {

    private final BreakdownViewInterface breakdownViewInterface;
    Context context;
    ArrayList<Breakdown> activities;

    public BreakdownAdapter(Context context, ArrayList<Breakdown> activities,
                            BreakdownViewInterface breakdownViewInterface) {
        this.breakdownViewInterface = breakdownViewInterface;
        this.context = context;
        this.activities = activities;
    }

    @NonNull
    @Override
    public BreakdownAdapter.BreakdownViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.breakdown_component, parent, false);
        return new BreakdownAdapter.BreakdownViewHolder(view, breakdownViewInterface, activities);
    }

    @Override
    public void onBindViewHolder(@NonNull BreakdownAdapter.BreakdownViewHolder holder, int position) {
        String category = activities.get(position).getCategory();
        if (category.equals("Transportation")) {
            holder.category.setImageResource(R.drawable.transportation);
        } else if (category.equals("Food")) {
            holder.category.setImageResource(R.drawable.food);
        } else {
            holder.category.setImageResource(R.drawable.consumption);
        }
        holder.activity.setText(activities.get(position).getActivity());
        holder.emission.setText(Double.toString(activities.get(position).getEmission()));
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }

    public static class BreakdownViewHolder extends RecyclerView.ViewHolder {

        ImageView category;
        TextView activity, emission;

        public BreakdownViewHolder(@NonNull View itemView,
                                   BreakdownViewInterface breakdownViewInterface,
                                   ArrayList<Breakdown> activities) {
            super(itemView);

            category = itemView.findViewById(R.id.rvCategory);
            activity = itemView.findViewById(R.id.rvActivity);
            emission = itemView.findViewById(R.id.rvEmission);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (breakdownViewInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            breakdownViewInterface.onItemClicked(position, activities);
                        }
                    }
                }
            });
        }


    }
}
