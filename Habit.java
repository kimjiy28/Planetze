import java.util.ArrayList;
import java.util.List;

public class Habit {
    private String name;
    private String description;
    private String category;
    private int impactLevel; // Higher numbers mean greater impact

    // Constructor
    public Habit(String name, String description, String category, int impactLevel) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.impactLevel = impactLevel;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public int getImpactLevel() {
        return impactLevel;
    }

    // Method to get a list of suggested habits
    public static List<Habit> getSuggestedHabits() {
        List<Habit> habits = new ArrayList<>();
        habits.add(new Habit("Walk or bike instead of driving", "Reduce carbon emissions by avoiding car use.", "Transportation", 5));
        habits.add(new Habit("Use a reusable water bottle", "Reduce plastic waste.", "Consumption", 3));
        habits.add(new Habit("Turn off lights when not in use", "Save energy by reducing electricity consumption.", "Energy", 4));
        habits.add(new Habit("Recycle regularly", "Reduce waste by recycling items whenever possible.", "Waste Management", 4));
        habits.add(new Habit("Use public transport", "Reduce emissions by using buses and trains instead of personal vehicles.", "Transportation", 4));
        // Add more habits as needed
        return habits;
    }
}
