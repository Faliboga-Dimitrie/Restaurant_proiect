package org.example.models;

public class WorkDay {
    private int startHour;
    private int endHour;
    private int breakStart;
    private int breakEnd;

    public WorkDay() {
        startHour = 0;
        endHour = 0;
        breakStart = 0;
        breakEnd = 0;
    }

    public WorkDay(int startHour, int endHour, int breakStart, int breakEnd) {
        this.startHour = startHour;
        this.endHour = endHour;
        this.breakStart = breakStart;
        this.breakEnd = breakEnd;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public int getBreakStart() {
        return breakStart;
    }

    public void setBreakStart(int breakStart) {
        this.breakStart = breakStart;
    }

    public int getBreakEnd() {
        return breakEnd;
    }

    public void setBreakEnd(int breakEnd) {
        this.breakEnd = breakEnd;
    }

    @Override
    public String toString() {
        return "WorkDay{" +
                "startHour=" + startHour +
                ", endHour=" + endHour +
                ", breakStart=" + breakStart +
                ", breakEnd=" + breakEnd +
                '}';
    }

    public int getWorkHours() {
        return endHour - startHour - (breakEnd - breakStart);
    }
}
