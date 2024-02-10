package com.example.appform.model;

import java.util.ArrayList;
import java.util.List;

public class ModelResponse {

    List<ModeloUsuarioKarine> data = new ArrayList<>();

    public ModelResponse() {
    }

    public ModelResponse(List<ModeloUsuarioKarine> data) {
        this.data = data;
    }

    public List<ModeloUsuarioKarine> getData() {
        return data;
    }

    public void setData(List<ModeloUsuarioKarine> data) {
        this.data = data;
    }
}
