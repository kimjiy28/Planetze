package com.example.planetze.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planetze.R;
import java.util.List;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {

    private final List<Habit> habitList;
    private final OnHabitIncrementListener incrementListener;
    private final OnHabitDeleteListener deleteListener;
    private final boolean showDeleteButton;

    // Constructor with deleteListener
    public HabitAdapter(List<Habit> habitList, OnHabitIncrementListener incrementListener, OnHabitDeleteListener deleteListener, boolean showDeleteButton) {
        this.habitList = habitList;
        this.incrementListener = incrementListener;
        this.deleteListener = deleteListener;
        this.showDeleteButton = showDeleteButton;
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.habit_item, parent, false);
        return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        Habit habit = habitList.get(position);
        holder.nameTextView.setText(habit.getName());
        holder.daysTextView.setText(String.valueOf(habit.getDaysCompleted()));

        // Handle increment button click
        holder.incrementButton.setOnClickListener(v -> incrementListener.onIncrement(habit));

        //Hide delete button in AddHabitActivity
        if (showDeleteButton) {
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setOnClickListener(v -> deleteListener.onDelete(habit));
        } else {
            holder.deleteButton.setVisibility(View.GONE);
        }

        // Handle delete button click
        holder.deleteButton.setOnClickListener(v -> deleteListener.onDelete(habit));
    }

    @Override
    public int getItemCount() {
        return habitList.size();
    }

    public void updateHabitList(List<Habit> newHabitList) {
        this.habitList.clear();
        this.habitList.addAll(newHabitList);
        notifyDataSetChanged(); // Notify the RecyclerView to refresh
    }

    static class HabitViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView, daysTextView;
        Button incrementButton, deleteButton;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.habitName);
            daysTextView = itemView.findViewById(R.id.habitDays);
            incrementButton = itemView.findViewById(R.id.incrementDaysButton);
            deleteButton = itemView.findViewById(R.id.deleteHabitButton);
        }
    }

    // Interface for handling increment button clicks
    public interface OnHabitIncrementListener {
        void onIncrement(Habit habit);
    }

    // Interface for handling delete button clicks
    public interface OnHabitDeleteListener {
        void onDelete(Habit habit);
    }
}
