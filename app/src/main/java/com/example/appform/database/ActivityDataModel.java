package com.example.appform.database;

public class ActivityDataModel {
    private int id;
    private String activityType;
    private int activityTimeMinutes;
    private String activityLevel;

    public ActivityDataModel(int id, String activityType, int activityTimeMinutes, String activityLevel) {
        this.id = id;
        this.activityType = activityType;
        this.activityTimeMinutes = activityTimeMinutes;
        this.activityLevel = activityLevel;
    }

    public int getId() {
        return id;
    }

    public String getActivityType() {
        return activityType;
    }

    public int getActivityTimeMinutes() {
        return activityTimeMinutes;
    }

    public String getActivityLevel() {
        return activityLevel;
    }
}

