package com.example.appform.model;
import java.util.Map;

public class ModeloAtividadeFisica {
    public Exercise exercise_metric;

    public Exercise getExercise_metric() {
        return exercise_metric;
    }

    public void setExercise_metric(Exercise exercise_metric) {
        this.exercise_metric = exercise_metric;
    }

    // Inner class representing the exercise metric
    public static class Exercise {
        public String name;
        public int user_id;
        public String intensity;
        public int duration_in_min;

        public int steps;
        public int distance_in_m;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getName() {
            return name;
        }

        public int getSteps() {
            return steps;
        }

        public void setSteps(int steps) {
            this.steps = steps;
        }

        public int getDistance_in_m() {
            return distance_in_m;
        }

        public void setDistance_in_m(int distance_in_m) {
            this.distance_in_m = distance_in_m;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getIntensity() {
            return intensity;
        }

        public void setIntensity(String intensity) {
            this.intensity = intensity;
        }

        public int getDuration_in_min() {
            return duration_in_min;
        }

        public void setDuration_in_min(int duration_in_min) {
            this.duration_in_min = duration_in_min;
        }
    }
}