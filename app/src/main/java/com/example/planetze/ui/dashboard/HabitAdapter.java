package com.example.planetze.ui.dashboard;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.planetze.R;


public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.HabitViewHolder> {

    private List<Habit> habitList;

    public HabitAdapter(List<Habit> habitList) {
        this.habitList = habitList;
    }

    @NonNull
    @Override
    public HabitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_habit, parent, false);
        return new HabitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HabitViewHolder holder, int position) {
        Habit habit = habitList.get(position);
        holder.habitName.setText(habit.getName());
        holder.habitCategory.setText(habit.getCategory());
        holder.habitImpact.setText(String.valueOf(habit.getImpactLevel()));
    }

    @Override
    public int getItemCount() {
        return habitList.size();
    }

    public static class HabitViewHolder extends RecyclerView.ViewHolder {
        TextView habitName, habitCategory, habitImpact;

        public HabitViewHolder(@NonNull View itemView) {
            super(itemView);
            habitName = itemView.findViewById(R.id.habit_name);
            habitCategory = itemView.findViewById(R.id.habit_category);
            habitImpact = itemView.findViewById(R.id.habit_impact);
        }
    }
}
