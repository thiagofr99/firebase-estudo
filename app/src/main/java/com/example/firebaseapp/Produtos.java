package com.example.firebaseapp;

public class Produtos {
    private String descrição;

    public String getDescrição() {
        return descrição;
    }

    public void setDescrição(String descrição) {
        this.descrição = descrição;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public double getPreço() {
        return preço;
    }

    public void setPreço(double preço) {
        this.preço = preço;
    }

    private String marca;
    private double preço;


    public Produtos(String descrição, String marca, double preço) {
        this.descrição = descrição;
        this.marca = marca;
        this.preço = preço;
    }





}
