package com.example.shoppinglist.ui.sql;

public class Produto {

    private int idProd;
    private String nome;
    private String descricao;
    private String category;
    private double price;
    private boolean urgent;
    private int quant;
    private boolean isSelectedToFinalList;

    public Produto(String nome, String descricao, String category, double price, boolean urgent, int quant, boolean isSelectedToFinalList){
        this.nome = nome;
        this.descricao = descricao;
        this.category = category;
        this.price = price;
        this.urgent = urgent;
        this.quant = quant;
        this.isSelectedToFinalList = isSelectedToFinalList;
    }

    public Produto(String nome, String descricao, String category, double price, boolean urgent, int quant, boolean isSelectedToFinalList, int idProd){
        this.nome = nome;
        this.descricao = descricao;
        this.category = category;
        this.price = price;
        this.urgent = urgent;
        this.quant = quant;
        this.isSelectedToFinalList = isSelectedToFinalList;
        this.idProd = idProd;
    }

    public int getIdProd() {
        return idProd;
    }

    public int getQuant() {
        return quant;
    }

    public boolean isSelectedToFinalList() {
        return isSelectedToFinalList;
    }

    public String getNome(){
        return nome;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public void setQuant(int quant) {
        this.quant = quant;
    }

    public void setSelectedToFinalList(boolean selectedToFinalList) {
        isSelectedToFinalList = selectedToFinalList;
    }

    public void setIdProd(int idProd) {
        this.idProd = idProd;
    }
}
