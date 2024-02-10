package com.example.appform.model;
import java.util.Map;

public class ModeloUsuarioKarine {
    public String id;
    public String type;
    public Attributes attributes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    // Inner class representing the attributes
    public static class Attributes {
        public int id;
        public String email;
        public String name;
        public String phone_number;
        public Profile profile;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }

        public Profile getProfile() {
            return profile;
        }

        public void setProfile(Profile profile) {
            this.profile = profile;
        }
    }

    // Inner class representing the profile
    public static class Profile {
        public int id;
        public int user_id;
        public int weight;
        public int height_in_cm;
        public int workout_in_min;
        public int workout_days_frequency;
        public boolean active_lifestyle;
        public String gender;
        public Map<String, Object> physical_activities;
        public String created_at;
        public String updated_at;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public int getHeight_in_cm() {
            return height_in_cm;
        }

        public void setHeight_in_cm(int height_in_cm) {
            this.height_in_cm = height_in_cm;
        }

        public int getWorkout_in_min() {
            return workout_in_min;
        }

        public void setWorkout_in_min(int workout_in_min) {
            this.workout_in_min = workout_in_min;
        }

        public int getWorkout_days_frequency() {
            return workout_days_frequency;
        }

        public void setWorkout_days_frequency(int workout_days_frequency) {
            this.workout_days_frequency = workout_days_frequency;
        }

        public boolean isActive_lifestyle() {
            return active_lifestyle;
        }

        public void setActive_lifestyle(boolean active_lifestyle) {
            this.active_lifestyle = active_lifestyle;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public Map<String, Object> getPhysical_activities() {
            return physical_activities;
        }

        public void setPhysical_activities(Map<String, Object> physical_activities) {
            this.physical_activities = physical_activities;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }
}
