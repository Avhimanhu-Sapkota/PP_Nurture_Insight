package com.example.nurture_insight.Model;

public class Habits {
    Integer habit_title;
    String habits_name;

    public Habits() {
    }

    public Habits(String habits_name) {
        this.habits_name = habits_name;
    }

    public Habits(Integer habit_title) {
        this.habit_title = habit_title;
    }

    public Integer getHabit_title() {
        return habit_title;
    }

    public void setHabit_title(Integer habit_title) {
        this.habit_title = habit_title;
    }

    public String getHabits_name() {
        return habits_name;
    }

    public void setHabits_name(String habits_name) {
        this.habits_name = habits_name;
    }
}
