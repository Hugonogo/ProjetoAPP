package com.example.appform.model;

import java.util.List;

public class ModeloRespsotaListaAtividades {
    public List<ModeloRespostaAtividadeFisica.Data> data;

    public List<ModeloRespostaAtividadeFisica.Data> getData() {
        return data;
    }

    public void setData(List<ModeloRespostaAtividadeFisica.Data> data) {
        this.data = data;
    }

    public ModeloRespsotaListaAtividades(List<ModeloRespostaAtividadeFisica.Data> data) {
        this.data = data;
    }
}
