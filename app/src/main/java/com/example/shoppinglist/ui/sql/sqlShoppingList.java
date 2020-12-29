package com.example.shoppinglist.ui.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

public class sqlShoppingList extends SQLiteOpenHelper {
    private static final String DB_name = "ShoopingList";//não esquecer de reeescrever nome
    private static final int DB_version = 1;

    public sqlShoppingList(Context context){
        super(context, DB_name, null, DB_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Produtos(_id INTEGER PRIMARY KEY AUTOINCREMENT, Nome TEXT NOT NULL, Categoria TEXT NOT NULL, Urgente BOOLEAN NOT NULL, Descricao TEXT, Price MONEY NOT NULL, " +
                "Quantidade INTEGER NOT NULL, SelecionadoParaListaFinal BOOLEAN NOT NULL)");
        db.execSQL("CREATE TABLE Categorias(_id INTEGER PRIMARY KEY AUTOINCREMENT, Nome TEXT NOT NULL)");
        db.execSQL("CREATE TABLE DescricaoUtilizador(_id INTEGER PRIMARY KEY AUTOINCREMENT, NomeUtilizador TEXT NOT NULL, NumeroProdutos INTEGER NOT NULL, NumeroProdutosUrgentes INTEGER NOT NULL," +
                " NumeroCategorias INTEGER NOT NULL, fotoPerfil BLOB)");
        db.execSQL("CREATE TABLE ListaFinal(_id INTEGER PRIMARY KEY AUTOINCREMENT, _idProduto INTEGER NOT NULL, PriceProduct MONEY NOT NULL, NomeProduto TEXT NOT NULL, Quantidade INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE DefinicoesApp(_id INTEGER PRIMARY KEY AUTOINCREMENT, ignorarPerguntas BOOLEAN NOT NULL, ignorarAvisos BOOLEAN NOT NULL,  ignorarProdutosSelect BOOLEAN NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    //add to sql meter letras maiusculas no inicio
    public void addProduct(SQLiteDatabase sql,String nome, String categoria, boolean urgente, String desc, double price, int quant){
        ContentValues cv = new ContentValues();

        cv.put("Nome", nome);
        cv.put("Categoria", categoria);
        cv.put("Urgente", urgente);
        cv.put("Descricao", desc);
        cv.put("Price", price);
        cv.put("Quantidade", quant);
        cv.put("SelecionadoParaListaFinal", false);

        sql.insert("Produtos", null, cv);
        sql.close();
    }
    public void addCategory(SQLiteDatabase sql, String nome){
        ContentValues cv = new ContentValues();

        cv.put("Nome", nome);

        sql.insert("Categorias", null, cv);
        sql.close();
    }
    public void addDescricaoUtilizador(SQLiteDatabase sql, String nomeUtilizador, int numProdut, int numProdutosUrgent,int numCat){
        ContentValues cv = new ContentValues();

        cv.put("NomeUtilizador", nomeUtilizador);
        cv.put("NumeroProdutos", numProdut);
        cv.put("NumeroProdutosUrgentes", numProdutosUrgent);
        cv.put("NumeroCategorias", numCat);

        sql.insert("DescricaoUtilizador", null, cv);
        sql.close();
    }
    public void addFinalList(SQLiteDatabase sql, double price, String nomeProduct, int quant){
        ContentValues cv = new ContentValues();

        int id_prod = OneProcuct(nomeProduct).getIdProd();

        cv.put("_idProduto", id_prod);
        cv.put("PriceProduct", price);
        cv.put("NomeProduto", nomeProduct);
        cv.put("Quantidade", quant);

        sql.insert("ListaFinal", null, cv);
        sql.close();
    }
    public void addDefApp(SQLiteDatabase sql, boolean ignorQuestion, boolean ignorWarning, boolean ignorSelectedProducts){
        ContentValues cv = new ContentValues();

        cv.put("ignorarPerguntas", ignorQuestion);
        cv.put("ignorarAvisos", ignorWarning);
        cv.put("ignorarProdutosSelect", ignorSelectedProducts);

        sql.insert("DefinicoesApp", null, cv);
        sql.close();
    }
    //del sql
    public void delProduct(SQLiteDatabase sql, String nome){
        sql.delete("Produtos", "Nome = '" + nome +"'" , null);
        sql.close();
    }
    public void delCat(SQLiteDatabase sql, String nome){
        sql.delete("Categorias", "Nome = '" + nome +"'", null);
        sql.close();
    }
    public void delListFinal(SQLiteDatabase sql, String nome){
        sql.delete("ListaFinal", "NomeProduto = '" + nome+"'", null);
        sql.close();
    }
    public void delAllListFinal(SQLiteDatabase sql){
        sql.delete("ListaFinal", null, null);
        sql.close();
    }
    public void delAllCategory(SQLiteDatabase sql){
        sql.delete("Categorias", null, null);
        sql.close();
    }
    public void deldef(SQLiteDatabase sql){
        sql.delete("DefinicoesApp", null, null);
        sql.close();
    }
    public void delUser(SQLiteDatabase sql){
        sql.delete("DescricaoUtilizador", null, null);
        sql.close();
    }
    public void delAllProducts(SQLiteDatabase sql){
        sql.delete("Produtos", null, null);
        sql.close();
    }
    //update sql
    public void UpdateProduct(SQLiteDatabase sql, String nome, String categoria, boolean urgente, String desc, double price, int quant, boolean selecionadoListaFial,String pesqNome){
        ContentValues cv = new ContentValues();

        cv.put("Nome", nome);
        cv.put("Categoria", categoria);
        cv.put("Urgente", urgente);
        cv.put("Descricao", desc);
        cv.put("Price", price);
        cv.put("Quantidade", quant);

        cv.put("SelecionadoParaListaFinal", selecionadoListaFial);

        sql.update("Produtos", cv, "Nome = ?", new String[]{pesqNome});
        sql.close();
    }
    public void UpdateProdoctToUrgentAndListFinal(SQLiteDatabase sql, boolean urgente, boolean selecionadoListaFial, String pesqNome){
        ContentValues cv = new ContentValues();

        cv.put("Urgente", urgente);
        cv.put("SelecionadoParaListaFinal", selecionadoListaFial);

        sql.update("Produtos", cv, "Nome = ?", new String[]{pesqNome});
        sql.close();
    }
    public void UpdateProductSelectListFinal(SQLiteDatabase sql, boolean selecionadoListaFial,String pesqNome){
        ContentValues cv = new ContentValues();

        cv.put("SelecionadoParaListaFinal", selecionadoListaFial);

        sql.update("Produtos", cv, "Nome = ?", new String[]{pesqNome});
        sql.close();
    }
    public void UpdateCat(SQLiteDatabase sql, String nome, String pesqNome){
        ContentValues cv = new ContentValues();

        cv.put("Nome", nome);

        sql.update("Categorias", cv, "Nome = ?", new String[]{pesqNome});
        sql.close();
    }
    public void UpdateFinalList(SQLiteDatabase sql, double price, String nomeProduto, int quant,int pesqId){
        ContentValues cv = new ContentValues();

        cv.put("_idProduto", pesqId);
        cv.put("PriceProduct", price);
        cv.put("NomeProduto", nomeProduto);
        cv.put("Quantidade", quant);

        sql.update("ListaFinal", cv, "NomeProduto = ?", new String[]{String.valueOf(pesqId)});
        sql.close();
    }
    public void UpdateDefApp(SQLiteDatabase sql, int id, int skipWarning, int skipQuestion, int skipproductsselected){
        ContentValues cv = new ContentValues();

        cv.put("ignorarPerguntas", skipQuestion);
        cv.put("ignorarAvisos", skipWarning);
        cv.put("ignorarProdutosSelect", skipproductsselected);

        sql.update("DefinicoesApp", cv, "_id = ?", new String[]{String.valueOf(id)});
        sql.close();
    }
    public void UpdateDescricaoUtlizaodores(SQLiteDatabase sql, String nome, int numProduct, int numProductUregent, int numeroCat){
        ContentValues cv = new ContentValues();

        cv.put("NomeUtilizador", numProduct);
        cv.put("NumeroProdutosUrgentes", numProductUregent);
        cv.put("NumeroCategorias", numeroCat);

        sql.update("DescricaoUtilizador", cv, "NomeUtilizador = ?", new String[]{nome});
        sql.close();
    }
    public void UpdateNameUser(SQLiteDatabase sql, String newName,String nome){
        ContentValues cv = new ContentValues();

        cv.put("NomeUtilizador", newName);

        sql.update("DescricaoUtilizador", cv, "NomeUtilizador = ?", new String[]{nome});
        sql.close();
    }

    public void UpdateDescriptionUser(SQLiteDatabase sql, boolean newProduct, boolean newCategory, boolean newProductUrgent, String nome, int number){
        ContentValues cv = new ContentValues();

        if(newProduct){
            cv.put("NumeroProdutos", (number +1));
        }else if(newProductUrgent){
            cv.put("NumeroProdutosUrgentes", (number +1));
        }else  if(newCategory){
            cv.put("NumeroCategorias", (number +1));
        }

        sql.update("DescricaoUtilizador", cv, "NomeUtilizador = ?", new String[]{nome});
        sql.close();
    }
    public void InserFotoUtilizador(SQLiteDatabase sql,String nomeUser, byte[] foto){
        ContentValues cv = new ContentValues();

        cv.put("fotoPerfil", foto);

        sql.update("DescricaoUtilizador", cv, "NomeUtilizador = ?", new String[]{nomeUser});
        sql.close();
    }

    public Bitmap fotPerfil(){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM DescricaoUtilizador";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            byte[] img =cursor.getBlob(cursor.getColumnIndex("fotoPerfil"));
            if(img == null){
                return null;
            }
            Bitmap bitmap;
            bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            return bitmap;
        }
        return null;
    }

    public void resetCategories(SQLiteDatabase sql){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM Produtos WHERE Categoria != 'Widouth Category'";
        List<Produto> produtos = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                ContentValues cv = new ContentValues();

                cv.put("Categoria", "Widouth Category");

                sql.update("Produtos", cv, "Nome = ?", new String[]{cursor.getString(cursor.getColumnIndex("Nome"))});
            }while (cursor.moveToNext());

        }
        db.close();
        cursor.close();
        sql.close();
    }
    public List<Produto> UpdateCategoryNameOfProducts(SQLiteDatabase sql, String category, String newCategory){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM Produtos WHERE Categoria = '" + category + "'";
        List<Produto> produtos = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                ContentValues cv = new ContentValues();

                cv.put("Categoria", newCategory);

                sql.update("Produtos", cv, "Nome = ?", new String[]{cursor.getString(cursor.getColumnIndex("Nome"))});
            }while (cursor.moveToNext());

        }
        db.close();
        cursor.close();
        sql.close();
        return produtos;
    }
    //pesq sql
    public List<Produto> allProdutos(){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM Produtos";
        List<Produto> produtos = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                String nome = cursor.getString(cursor.getColumnIndex("Nome"));
                String descricao = cursor.getString(cursor.getColumnIndex("Descricao"));
                String categoria = cursor.getString(cursor.getColumnIndex("Categoria"));
                double price = Double.parseDouble(cursor.getString(cursor.getColumnIndex("Price")));
                boolean urgente;
                if(cursor.getInt(cursor.getColumnIndex("Urgente")) > 0){
                    urgente = true;
                }else{
                    urgente = false;
                }

                boolean listfinal;
                if(cursor.getInt(cursor.getColumnIndex("SelecionadoParaListaFinal")) > 0){
                    listfinal = true;
                }else {
                    listfinal = false;
                }
                int quant = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Quantidade")));

                produtos.add(new Produto(nome, descricao, categoria, price, urgente, quant, listfinal));
            }while(cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return produtos;
    }
    public boolean ThisProductExist(String nameSearch){

        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM Produtos WHERE Nome = '" +  nameSearch + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            return true;
        }
        db.close();
        cursor.close();
        return false;
    }
    public boolean ThisCategoryExist(String nameSearch){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM Categorias WHERE Nome = '" +  nameSearch + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            return true;
        }
        db.close();
        cursor.close();
        return false;
    }
    public List<Produto> UrgentsProducts(){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM Produtos";
        List<Produto> produtos = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                if(cursor.getInt(cursor.getColumnIndex("Urgente")) > 0){
                    String nome = cursor.getString(cursor.getColumnIndex("Nome"));
                    String descricao = cursor.getString(cursor.getColumnIndex("Descricao"));
                    String categoria = cursor.getString(cursor.getColumnIndex("Categoria"));
                    double price = Double.parseDouble(cursor.getString(cursor.getColumnIndex("Price")));
                    boolean urgente;
                    if(cursor.getInt(cursor.getColumnIndex("Urgente")) > 0){
                        urgente = true;
                    }else{
                        urgente = false;
                    }
                    boolean listfinal;
                    if(cursor.getInt(cursor.getColumnIndex("SelecionadoParaListaFinal")) > 0){
                        listfinal = true;
                    }else {
                        listfinal = false;
                    }
                    int quant = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Quantidade")));

                    produtos.add(new Produto(nome, descricao, categoria, price, urgente, quant, listfinal));
                }
            }while(cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return produtos;
    }
    public List<Categoria> allCategory(){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM Categorias";
        List<Categoria> categorias = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                String nome = cursor.getString(cursor.getColumnIndex("Nome"));
                categorias.add(new Categoria(nome));
            }while(cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return categorias;
    }
    public DescricaoUtilizador oneUser(){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM DescricaoUtilizador";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            String nome = cursor.getString(cursor.getColumnIndex("NomeUtilizador"));
            int numeroProdutos = Integer.parseInt(cursor.getString(cursor.getColumnIndex("NumeroProdutos")));
            int numeroProdutosUrgentes = Integer.parseInt(cursor.getString(cursor.getColumnIndex("NumeroProdutosUrgentes")));
            int numeroCategorias = Integer.parseInt(cursor.getString(cursor.getColumnIndex("NumeroCategorias")));
            DescricaoUtilizador utilizador = new DescricaoUtilizador(nome, numeroProdutos, numeroProdutosUrgentes, numeroCategorias);
            return utilizador;
        }
        db.close();
        cursor.close();
        return null;
    }
    public int oneDefApp(){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM DefinicoesApp";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            return cursor.getInt(cursor.getColumnIndex("_id"));
        }
        db.close();
        cursor.close();
        return -1;
    }
    public String skipQuestionkjf(){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM DefinicoesApp";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            return " " + cursor.getInt(cursor.getColumnIndex("ignorarPerguntas")) + " " + cursor.getString(cursor.getColumnIndex("ignorarPerguntas"));
            /*if(cursor.getInt(cursor.getColumnIndex("ignorarPerguntas")) > 0){
                //return true;
            }else{
                //return false;
            }*/
        }
        db.close();
        cursor.close();
        return "";
        //return false;
    }
    public Boolean skipQuestion(){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM DefinicoesApp";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            if(cursor.getInt(cursor.getColumnIndex("ignorarPerguntas")) > 0){
                return true;
            }else{
                return false;
            }
        }
        db.close();
        cursor.close();
        return false;
    }
    public boolean skipWarning(){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM DefinicoesApp";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            if(cursor.getInt(cursor.getColumnIndex("ignorarAvisos")) > 0){
                return true;
            }else{
                return false;
            }
        }
        db.close();
        cursor.close();
        return false;
    }
    public boolean skipSelectedProduct(){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM DefinicoesApp";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            if(cursor.getInt(cursor.getColumnIndex("ignorarProdutosSelect")) > 0){
                return true;
            }else{
                return false;
            }
        }
        db.close();
        cursor.close();
        return false;
    }
    public Produto OneProcuct(String pesq){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM Produtos WHERE Nome = '" + pesq + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            int id_prod = Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id")));
            String nome = cursor.getString(cursor.getColumnIndex("Nome"));
            String descricao = cursor.getString(cursor.getColumnIndex("Descricao"));
            String categoria = cursor.getString(cursor.getColumnIndex("Categoria"));
            double price = Double.parseDouble(cursor.getString(cursor.getColumnIndex("Price")));
            boolean urgente = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("Urgente")));
            int quant = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Quantidade")));
            boolean listfinal = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("SelecionadoParaListaFinal")));
            Produto produto = new Produto(nome, descricao, categoria, price, urgente, quant, listfinal, id_prod);
            return produto;
        }
        db.close();
        cursor.close();
        return null;
    }
    public List<Produto> allProductsOfOneCategory(String category){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM Produtos WHERE Categoria = '" + category + "'";
        List<Produto> produtos = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                int id_prod = Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id")));
                String nome = cursor.getString(cursor.getColumnIndex("Nome"));
                String descricao = cursor.getString(cursor.getColumnIndex("Descricao"));
                String categoria = cursor.getString(cursor.getColumnIndex("Categoria"));
                double price = Double.parseDouble(cursor.getString(cursor.getColumnIndex("Price")));
                int quant = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Quantidade")));

                boolean urgente;
                if(cursor.getInt(cursor.getColumnIndex("Urgente")) > 0){
                    urgente = true;
                }else{
                    urgente = false;
                }

                boolean listfinal;
                if(cursor.getInt(cursor.getColumnIndex("SelecionadoParaListaFinal")) > 0){
                    listfinal = true;
                }else {
                    listfinal = false;
                }
                produtos.add(new Produto(nome, descricao, categoria, price, urgente, quant, listfinal, id_prod));
            }while (cursor.moveToNext());

        }
        db.close();
        cursor.close();
        return produtos;
    }
    public List<ListaFinal> allfinallist(){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM ListaFinal";
        List<ListaFinal> finalpriceproducts = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                int idProd = Integer.parseInt(cursor.getString(cursor.getColumnIndex("_idProduto")));
                String name = cursor.getString(cursor.getColumnIndex("NomeProduto"));
                double price = Double.parseDouble(cursor.getString(cursor.getColumnIndex("PriceProduct")));
                int quant = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Quantidade")));

                finalpriceproducts.add(new ListaFinal(idProd, name, price, quant));
            }while(cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return finalpriceproducts;
    }
    public int numberProductsOfCategories(String pesq){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM Produtos WHERE Categoria = '" + pesq + "'";

        int totalProducts = 0;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                totalProducts+=1;

            }while(cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return totalProducts;
    }
    public List<Produto> UrgentsProductsSearch(String pesq){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM Produtos WHERE SUBSTR(UPPER(Nome), 0," + (pesq.length()+1) +") = '" + pesq.toUpperCase() + "'";
        List<Produto> produtos = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                if(cursor.getInt(cursor.getColumnIndex("Urgente")) > 0){
                    String nome = cursor.getString(cursor.getColumnIndex("Nome"));
                    String descricao = cursor.getString(cursor.getColumnIndex("Descricao"));
                    String categoria = cursor.getString(cursor.getColumnIndex("Categoria"));
                    double price = Double.parseDouble(cursor.getString(cursor.getColumnIndex("Price")));
                    boolean urgente;
                    if(cursor.getInt(cursor.getColumnIndex("Urgente")) > 0){
                        urgente = true;
                    }else{
                        urgente = false;
                    }
                    boolean listfinal;
                    if(cursor.getInt(cursor.getColumnIndex("SelecionadoParaListaFinal")) > 0){
                        listfinal = true;
                    }else {
                        listfinal = false;
                    }
                    int quant = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Quantidade")));

                    produtos.add(new Produto(nome, descricao, categoria, price, urgente, quant, listfinal));
                }
            }while(cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return produtos;
    }
    public List<Produto> ProductsSearch(String pesq){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM Produtos WHERE SUBSTR(UPPER(Nome), 0," + (pesq.length()+1) +") = '" + pesq.toUpperCase() + "'";
        List<Produto> produtos = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                String nome = cursor.getString(cursor.getColumnIndex("Nome"));
                String descricao = cursor.getString(cursor.getColumnIndex("Descricao"));
                String categoria = cursor.getString(cursor.getColumnIndex("Categoria"));
                double price = Double.parseDouble(cursor.getString(cursor.getColumnIndex("Price")));
                boolean urgente;
                if(cursor.getInt(cursor.getColumnIndex("Urgente")) > 0){
                    urgente = true;
                }else{
                    urgente = false;
                }
                boolean listfinal;
                if(cursor.getInt(cursor.getColumnIndex("SelecionadoParaListaFinal")) > 0){
                    listfinal = true;
                }else {
                    listfinal = false;
                }
                int quant = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Quantidade")));

                produtos.add(new Produto(nome, descricao, categoria, price, urgente, quant, listfinal));
            }while(cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return produtos;
    }
    public List<Categoria> CategoriesSearch(String pesq){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM Categorias WHERE SUBSTR(UPPER(Nome), 0," + (pesq.length()+1) +") = '" + pesq.toUpperCase() + "'";
        List<Categoria> categorias = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do{
                String nome = cursor.getString(cursor.getColumnIndex("Nome"));
                categorias.add(new Categoria(nome));
            }while(cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return categorias;
    }
    public List<Produto> OrderAndSearchOrderByProducts(String pesq, boolean orderAtoZ, boolean orderZtoA, boolean orderPriceAsc, boolean orderPriceDesc){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery;
        List<Produto> produtos = new ArrayList<>();
        Cursor cursor = null;
        if(pesq.isEmpty()){
            if(orderAtoZ){
                selectQuery = "SELECT * FROM Produtos ORDER BY Nome ASC";
                cursor = db.rawQuery(selectQuery, null);
            }else if(orderZtoA){
                selectQuery = "SELECT * FROM Produtos ORDER BY Nome DESC";
                cursor = db.rawQuery(selectQuery, null);
            }else if(orderPriceAsc){
                selectQuery = "SELECT * FROM Produtos ORDER BY Price ASC";
                cursor = db.rawQuery(selectQuery, null);
            }else if(orderPriceDesc){
                selectQuery = "SELECT * FROM Produtos ORDER BY Price DESC";
                cursor = db.rawQuery(selectQuery, null);
            }else{
                selectQuery = "SELECT * FROM Produtos";
                cursor = db.rawQuery(selectQuery, null);
            }
        }else{
            if(orderAtoZ){
                selectQuery = "SELECT * FROM Produtos WHERE SUBSTR(UPPER(Nome), 0," + (pesq.length()+1) +") = '" + pesq.toUpperCase() + "' ORDER BY Nome ASC";
                cursor = db.rawQuery(selectQuery, null);
            }else if(orderZtoA){
                selectQuery = "SELECT * FROM Produtos WHERE SUBSTR(UPPER(Nome), 0," + (pesq.length()+1) +") = '" + pesq.toUpperCase() + "'  ORDER BY Nome DESC";
                cursor = db.rawQuery(selectQuery, null);
            }else if(orderPriceAsc){
                selectQuery = "SELECT * FROM Produtos WHERE SUBSTR(UPPER(Nome), 0," + (pesq.length()+1) +") = '" + pesq.toUpperCase() + "'  ORDER BY Price ASC";
                cursor = db.rawQuery(selectQuery, null);
            }else if(orderPriceDesc){
                selectQuery = "SELECT * FROM Produtos WHERE SUBSTR(UPPER(Nome), 0," + (pesq.length()+1) +") = '" + pesq.toUpperCase() + "' ORDER BY Price DESC";
                cursor = db.rawQuery(selectQuery, null);
            }else{
                selectQuery = "SELECT * FROM Produtos WHERE SUBSTR(UPPER(Nome), 0," + (pesq.length()+1) +") = '" + pesq.toUpperCase() + "'";
                cursor = db.rawQuery(selectQuery, null);
            }
        }

        if(cursor.moveToFirst()){
            do{
                String nome = cursor.getString(cursor.getColumnIndex("Nome"));
                String descricao = cursor.getString(cursor.getColumnIndex("Descricao"));
                String categoria = cursor.getString(cursor.getColumnIndex("Categoria"));
                double price = Double.parseDouble(cursor.getString(cursor.getColumnIndex("Price")));
                boolean urgente;
                if(cursor.getInt(cursor.getColumnIndex("Urgente")) > 0){
                    urgente = true;
                }else{
                    urgente = false;
                }
                boolean listfinal;
                if(cursor.getInt(cursor.getColumnIndex("SelecionadoParaListaFinal")) > 0){
                    listfinal = true;
                }else {
                    listfinal = false;
                }
                int quant = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Quantidade")));

                produtos.add(new Produto(nome, descricao, categoria, price, urgente, quant, listfinal));
            }while(cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return produtos;
    }
    public List<Produto> OrderAndSearchOrderByProductsUrgents(String pesq, boolean orderAtoZ, boolean orderZtoA, boolean orderPriceAsc, boolean orderPriceDesc){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery;
        List<Produto> produtos = new ArrayList<>();
        Cursor cursor = null;
        if(pesq.isEmpty()){
            if(orderAtoZ){
                selectQuery = "SELECT * FROM Produtos ORDER BY Nome ASC";
                cursor = db.rawQuery(selectQuery, null);
            }else if(orderZtoA){
                selectQuery = "SELECT * FROM Produtos ORDER BY Nome DESC";
                cursor = db.rawQuery(selectQuery, null);
            }else if(orderPriceAsc){
                selectQuery = "SELECT * FROM Produtos ORDER BY Price ASC";
                cursor = db.rawQuery(selectQuery, null);
            }else if(orderPriceDesc){
                selectQuery = "SELECT * FROM Produtos ORDER BY Price DESC";
                cursor = db.rawQuery(selectQuery, null);
            }else{
                selectQuery = "SELECT * FROM Produtos";
                cursor = db.rawQuery(selectQuery, null);
            }
        }else{
            if(orderAtoZ){
                selectQuery = "SELECT * FROM Produtos WHERE SUBSTR(UPPER(Nome), 0," + (pesq.length()+1) +") = '" + pesq.toUpperCase() + "' ORDER BY Nome ASC";
                cursor = db.rawQuery(selectQuery, null);
            }else if(orderZtoA){
                selectQuery = "SELECT * FROM Produtos WHERE SUBSTR(UPPER(Nome), 0," + (pesq.length()+1) +") = '" + pesq.toUpperCase() + "'  ORDER BY Nome DESC";
                cursor = db.rawQuery(selectQuery, null);
            }else if(orderPriceAsc){
                selectQuery = "SELECT * FROM Produtos WHERE SUBSTR(UPPER(Nome), 0," + (pesq.length()+1) +") = '" + pesq.toUpperCase() + "'  ORDER BY Price ASC";
                cursor = db.rawQuery(selectQuery, null);
            }else if(orderPriceDesc){
                selectQuery = "SELECT * FROM Produtos WHERE SUBSTR(UPPER(Nome), 0," + (pesq.length()+1) +") = '" + pesq.toUpperCase() + "' ORDER BY Price DESC";
                cursor = db.rawQuery(selectQuery, null);
            }else{
                selectQuery = "SELECT * FROM Produtos WHERE SUBSTR(UPPER(Nome), 0," + (pesq.length()+1) +") = '" + pesq.toUpperCase() + "'";
                cursor = db.rawQuery(selectQuery, null);
            }
        }

        if(cursor.moveToFirst()){
            do{
                if(cursor.getInt(cursor.getColumnIndex("Urgente")) > 0){
                    String nome = cursor.getString(cursor.getColumnIndex("Nome"));
                    String descricao = cursor.getString(cursor.getColumnIndex("Descricao"));
                    String categoria = cursor.getString(cursor.getColumnIndex("Categoria"));
                    double price = Double.parseDouble(cursor.getString(cursor.getColumnIndex("Price")));
                    boolean urgente;
                    if(cursor.getInt(cursor.getColumnIndex("Urgente")) > 0){
                        urgente = true;
                    }else{
                        urgente = false;
                    }
                    boolean listfinal;
                    if(cursor.getInt(cursor.getColumnIndex("SelecionadoParaListaFinal")) > 0){
                        listfinal = true;
                    }else {
                        listfinal = false;
                    }
                    int quant = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Quantidade")));

                    produtos.add(new Produto(nome, descricao, categoria, price, urgente, quant, listfinal));
                }
            }while(cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return produtos;
    }
    public List<Categoria> OrderAndSearchOrderByCategory(String pesq, boolean orderAtoZ, boolean orderZtoA){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery;
        List<Categoria> categorias = new ArrayList<>();
        Cursor cursor = null;
        if(pesq.isEmpty()){
            if(orderAtoZ){
                selectQuery = "SELECT * FROM Categorias ORDER BY Nome ASC";
                cursor = db.rawQuery(selectQuery, null);
            }else if(orderZtoA){
                selectQuery = "SELECT * FROM Categorias ORDER BY Nome DESC";
                cursor = db.rawQuery(selectQuery, null);
            }else{
                selectQuery = "SELECT * FROM Categorias";
                cursor = db.rawQuery(selectQuery, null);
            }
        }else{
            if(orderAtoZ){
                selectQuery = "SELECT * FROM Categorias WHERE SUBSTR(UPPER(Nome), 0," + (pesq.length()+1) +") = '" + pesq.toUpperCase() + "' ORDER BY Nome ASC";
                cursor = db.rawQuery(selectQuery, null);
            }else if(orderZtoA){
                selectQuery = "SELECT * FROM Categorias WHERE SUBSTR(UPPER(Nome), 0," + (pesq.length()+1) +") = '" + pesq.toUpperCase() + "'  ORDER BY Nome DESC";
                cursor = db.rawQuery(selectQuery, null);
                cursor = db.rawQuery(selectQuery, null);
            }else{
                selectQuery = "SELECT * FROM Categorias WHERE SUBSTR(UPPER(Nome), 0," + (pesq.length()+1) +") = '" + pesq.toUpperCase() + "'";
                cursor = db.rawQuery(selectQuery, null);
            }
        }

        if(cursor.moveToFirst()){
            do{
                String nome = cursor.getString(cursor.getColumnIndex("Nome"));
                categorias.add(new Categoria(nome));
            }while(cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return categorias;
    }
    public List<Produto> OrderAndSearchOrderByProductsofCategory(String pesq, boolean orderAtoZ, boolean orderZtoA, boolean orderPriceAsc, boolean orderPriceDesc, String cat){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery;
        List<Produto> produtos = new ArrayList<>();
        Cursor cursor = null;
        if(pesq.isEmpty()){
            if(orderAtoZ){
                selectQuery = "SELECT * FROM Produtos ORDER BY Nome ASC";
                cursor = db.rawQuery(selectQuery, null);
            }else if(orderZtoA){
                selectQuery = "SELECT * FROM Produtos ORDER BY Nome DESC";
                cursor = db.rawQuery(selectQuery, null);
            }else if(orderPriceAsc){
                selectQuery = "SELECT * FROM Produtos ORDER BY Price ASC";
                cursor = db.rawQuery(selectQuery, null);
            }else if(orderPriceDesc){
                selectQuery = "SELECT * FROM Produtos ORDER BY Price DESC";
                cursor = db.rawQuery(selectQuery, null);
            }else{
                selectQuery = "SELECT * FROM Produtos";
                cursor = db.rawQuery(selectQuery, null);
            }
        }else{
            if(orderAtoZ){
                selectQuery = "SELECT * FROM Produtos WHERE SUBSTR(UPPER(Nome), 0," + (pesq.length()+1) +") = '" + pesq.toUpperCase() + "' ORDER BY Nome ASC";
                cursor = db.rawQuery(selectQuery, null);
            }else if(orderZtoA){
                selectQuery = "SELECT * FROM Produtos WHERE SUBSTR(UPPER(Nome), 0," + (pesq.length()+1) +") = '" + pesq.toUpperCase() + "'  ORDER BY Nome DESC";
                cursor = db.rawQuery(selectQuery, null);
            }else if(orderPriceAsc){
                selectQuery = "SELECT * FROM Produtos WHERE SUBSTR(UPPER(Nome), 0," + (pesq.length()+1) +") = '" + pesq.toUpperCase() + "'  ORDER BY Price ASC";
                cursor = db.rawQuery(selectQuery, null);
            }else if(orderPriceDesc){
                selectQuery = "SELECT * FROM Produtos WHERE SUBSTR(UPPER(Nome), 0," + (pesq.length()+1) +") = '" + pesq.toUpperCase() + "' ORDER BY Price DESC";
                cursor = db.rawQuery(selectQuery, null);
            }else{
                selectQuery = "SELECT * FROM Produtos WHERE SUBSTR(UPPER(Nome), 0," + (pesq.length()+1) +") = '" + pesq.toUpperCase() + "'";
                cursor = db.rawQuery(selectQuery, null);
            }
        }

        if(cursor.moveToFirst()){
            do{
                if(cursor.getString(cursor.getColumnIndex("Categoria")).equals(cat)){
                    String nome = cursor.getString(cursor.getColumnIndex("Nome"));
                    String descricao = cursor.getString(cursor.getColumnIndex("Descricao"));
                    String categoria = cursor.getString(cursor.getColumnIndex("Categoria"));
                    double price = Double.parseDouble(cursor.getString(cursor.getColumnIndex("Price")));
                    boolean urgente;
                    if(cursor.getInt(cursor.getColumnIndex("Urgente")) > 0){
                        urgente = true;
                    }else{
                        urgente = false;
                    }
                    boolean listfinal;
                    if(cursor.getInt(cursor.getColumnIndex("SelecionadoParaListaFinal")) > 0){
                        listfinal = true;
                    }else {
                        listfinal = false;
                    }
                    int quant = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Quantidade")));

                    produtos.add(new Produto(nome, descricao, categoria, price, urgente, quant, listfinal));
                }
            }while(cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return produtos;
    }
    public List<ListaFinal> OrderAndSearchOrderByProductsFinal(String pesq, boolean orderAtoZ, boolean orderZtoA, boolean orderPriceAsc, boolean orderPriceDesc){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery;
        List<ListaFinal> finallist = new ArrayList<>();
        Cursor cursor = null;
        if(pesq.isEmpty()){
            if(orderAtoZ){
                selectQuery = "SELECT * FROM ListaFinal ORDER BY NomeProduto ASC";
                cursor = db.rawQuery(selectQuery, null);
            }else if(orderZtoA){
                selectQuery = "SELECT * FROM ListaFinal ORDER BY NomeProduto DESC";
                cursor = db.rawQuery(selectQuery, null);
            }else if(orderPriceAsc){
                selectQuery = "SELECT * FROM ListaFinal ORDER BY PriceProduct ASC";
                cursor = db.rawQuery(selectQuery, null);
            }else if(orderPriceDesc){
                selectQuery = "SELECT * FROM ListaFinal ORDER BY PriceProduct DESC";
                cursor = db.rawQuery(selectQuery, null);
            }else{
                selectQuery = "SELECT * FROM ListaFinal";
                cursor = db.rawQuery(selectQuery, null);
            }
        }else{
            if(orderAtoZ){
                selectQuery = "SELECT * FROM ListaFinal WHERE SUBSTR(UPPER(NomeProduto), 0," + (pesq.length()+1) +") = '" + pesq.toUpperCase() + "' ORDER BY NomeProduto ASC";
                cursor = db.rawQuery(selectQuery, null);
            }else if(orderZtoA){
                selectQuery = "SELECT * FROM ListaFinal WHERE SUBSTR(UPPER(NomeProduto), 0," + (pesq.length()+1) +") = '" + pesq.toUpperCase() + "'  ORDER BY NomeProduto DESC";
                cursor = db.rawQuery(selectQuery, null);
            }else if(orderPriceAsc){
                selectQuery = "SELECT * FROM ListaFinal WHERE SUBSTR(UPPER(NomeProduto), 0," + (pesq.length()+1) +") = '" + pesq.toUpperCase() + "'  ORDER BY PriceProduct ASC";
                cursor = db.rawQuery(selectQuery, null);
            }else if(orderPriceDesc){
                selectQuery = "SELECT * FROM ListaFinal WHERE SUBSTR(UPPER(NomeProduto), 0," + (pesq.length()+1) +") = '" + pesq.toUpperCase() + "' ORDER BY PriceProduct DESC";
                cursor = db.rawQuery(selectQuery, null);
            }else{
                selectQuery = "SELECT * FROM ListaFinal WHERE SUBSTR(UPPER(NomeProduto), 0," + (pesq.length()+1) +") = '" + pesq.toUpperCase() + "'";
                cursor = db.rawQuery(selectQuery, null);
            }
        }

        if(cursor.moveToFirst()){
            do{
                String nome = cursor.getString(cursor.getColumnIndex("NomeProduto"));
                double price = Double.parseDouble(cursor.getString(cursor.getColumnIndex("PriceProduct")));
                int quant = Integer.parseInt(cursor.getString(cursor.getColumnIndex("Quantidade")));

                finallist.add(new ListaFinal(nome, price, quant));
            }while(cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return finallist;
    }
}
