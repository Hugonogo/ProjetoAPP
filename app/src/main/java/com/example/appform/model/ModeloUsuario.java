package com.example.appform.model;

import java.io.Serializable;

public class ModeloUsuario implements Serializable {
    String nome,email,telefone;
    int idade = 0;

    int passos = 0;
    String sexo = "";
    Double altura = 0.0;
    Double peso = 0.0;

    public ModeloUsuario() {
    }

    public ModeloUsuario(String nome, String email, String telefone, int idade, int passos, String sexo, Double altura, Double peso) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.idade = idade;
        this.passos = passos;
        this.sexo = sexo;
        this.altura = altura;
        this.peso = peso;
    }

    public int getPassos() {
        return passos;
    }

    public void setPassos(int passos) {
        this.passos = passos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Double getAltura() {
        return altura;
    }

    public void setAltura(Double altura) {
        this.altura = altura;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }
}
