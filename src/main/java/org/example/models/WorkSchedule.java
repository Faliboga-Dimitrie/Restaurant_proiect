package org.example.models;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

public class WorkSchedule {
    private Map<DayOfWeek, WorkDay> schedule;

    public WorkSchedule() {
        schedule = new HashMap<>();
    }

    public void addWorkDay(DayOfWeek day, int startHour, int endHour, int breakStart, int breakEnd) {
        schedule.put(day, new WorkDay(startHour, endHour, breakStart, breakEnd));
    }

    public WorkDay getWorkDay(DayOfWeek day) {
        return schedule.get(day);
    }

    public void removeWorkDay(DayOfWeek day) {
        schedule.remove(day);
    }

    public int weeklyWorkHours() {
        int totalHours = 0;
        for (WorkDay workDay : schedule.values()) {
            totalHours += workDay.getWorkHours();
        }
        return totalHours;
    }
}
