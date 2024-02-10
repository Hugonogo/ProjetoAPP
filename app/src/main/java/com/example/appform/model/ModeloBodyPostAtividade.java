package com.example.appform.model;

public class ModeloBodyPostAtividade {
    ModeloAtividadeFisica.Exercise exercise_metric;

    public ModeloBodyPostAtividade(ModeloAtividadeFisica.Exercise exercise_metric) {
        this.exercise_metric = exercise_metric;
    }

    public ModeloAtividadeFisica.Exercise getExercise_metric() {
        return exercise_metric;
    }

    public void setExercise_metric(ModeloAtividadeFisica.Exercise exercise_metric) {
        this.exercise_metric = exercise_metric;
    }
}
