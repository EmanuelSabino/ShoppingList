package com.example.shoppinglist.ui.sql;

public class ListaFinal {
    private int idProduto;
    private String nomeProduto;
    private double precoProduto;
    private int quant;

    public ListaFinal(int idProduto, String nomeProduto, double precoProduto, int quant){
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.precoProduto = precoProduto;
        this.quant = quant;
    }


    public ListaFinal(String nomeProduto, double precoProduto, int quant){
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.precoProduto = precoProduto;
        this.quant = quant;
    }

    public int getQuant() {
        return quant;
    }

    public String getNomeProduto(){
        return nomeProduto;
    }

    public int getIdProduto(){
        return idProduto;
    }

    public double getPrecoProduto(){
        return precoProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public void setPrecoProduto(double precoProduto) {
        this.precoProduto = precoProduto;
    }

    public void setQuant(int quant) {
        this.quant = quant;
    }
}
