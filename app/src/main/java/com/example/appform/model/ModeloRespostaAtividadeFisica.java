package com.example.appform.model;

public class ModeloRespostaAtividadeFisica {
    public Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    // Inner class representing the data
    public static class Data {
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
    }

    // Inner class representing the attributes
    public static class Attributes {
        public int user_id;
        public String name;
        public String intensity;
        public int duration_in_min;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getName() {
            return name;
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
