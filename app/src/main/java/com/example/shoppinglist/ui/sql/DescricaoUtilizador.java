package com.example.shoppinglist.ui.sql;

public class DescricaoUtilizador {
    private String nomeUtilizador;
    private int numeroProdutos;
    private int numeroProdutosUrgentes;
    private int numeroCategorias;

    public DescricaoUtilizador(String nomeUtilizador, int numeroProdutos, int numeroProdutosUrgentes, int numeroCategorias){
        this.nomeUtilizador = nomeUtilizador;
        this.numeroProdutos = numeroProdutos;
        this.numeroProdutosUrgentes = numeroProdutosUrgentes;
        this.numeroCategorias =  numeroCategorias;
    }

    public String getNomeUtilizador(){
        return nomeUtilizador;
    }

    public int getNumeroProdutos(){
        return numeroProdutos;
    }

    public int getNumeroProdutosUrgentes(){
        return numeroProdutosUrgentes;
    }

    public int getNumeroCategorias() {
        return numeroCategorias;
    }

    public void setNumeroCategorias(int numeroCategorias) {
        this.numeroCategorias = numeroCategorias;
    }

    public void setNomeUtilizador(String nomeUtilizador){
         this.nomeUtilizador = nomeUtilizador;
    }

    public void setNumeroProdutos(int numeroProdutos){
         this.numeroProdutos = numeroProdutos;
    }

    public void setNumeroProdutosUrgentes(int numeroProdutosUrgentes){
         this.numeroProdutosUrgentes = numeroProdutosUrgentes;
    }

}
