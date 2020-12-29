package com.example.shoppinglist.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shoppinglist.R;
import com.example.shoppinglist.ui.Categories.EditCategory.edit_category;
import com.example.shoppinglist.ui.sql.Categoria;
import com.example.shoppinglist.ui.sql.ConnectionDataBase;
import com.example.shoppinglist.ui.sql.sqlShoppingList;

import java.util.List;

public class AdapterPersCategories extends BaseAdapter {
    private List<Categoria> listCategories;
    private Activity act;


    @Override
    public int getCount() {
        return listCategories.size();
    }

    @Override
    public Object getItem(int position) {
        return listCategories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = act.getLayoutInflater().inflate(R.layout.layout_listview_categories01,parent,false);
        final Categoria listcategoriesSelected = listCategories.get(position);
        final sqlShoppingList sql = new sqlShoppingList(act);
        final ConnectionDataBase db = new ConnectionDataBase();

        TextView name = (TextView) view.findViewById(R.id.textViewnamecategorie_layoutlistviewcategorie01);
        TextView numberproducts = (TextView) view.findViewById(R.id.textViewnamecategorie_layoutlistviewcategorie02);

        name.setText(listcategoriesSelected.getNome());
        numberproducts.setText("" + sql.numberProductsOfCategories(listcategoriesSelected.getNome())+ " Products");

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(act, edit_category.class);
                myIntent.putExtra("nomeCategoria", listcategoriesSelected.getNome());
                act.startActivity(myIntent);
            }
        });

        return view;
    }

    public AdapterPersCategories(List<Categoria> listCategories, Activity act){
        this.listCategories = listCategories;
        this.act = act;
    }
}
