package com.example.shoppinglist.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shoppinglist.R;
import com.example.shoppinglist.ui.sql.ConnectionDataBase;
import com.example.shoppinglist.ui.sql.Produto;
import com.example.shoppinglist.ui.sql.sqlShoppingList;

import java.util.List;

public class AdapterPersProductsOfCategory extends BaseAdapter {
    private List<Produto> listProdutos;
    private Activity act;


    @Override
    public int getCount() {
        return listProdutos.size();
    }

    @Override
    public Object getItem(int position) {
        return listProdutos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view = act.getLayoutInflater().inflate(R.layout.layout_listview_categories02,parent,false);

        final Produto listproductsSelected = listProdutos.get(position);
        final sqlShoppingList sql = new sqlShoppingList(act);
        final ConnectionDataBase db = new ConnectionDataBase();

        TextView quant = (TextView) view.findViewById(R.id.textViewQuantProduct_layoutlistviewategories02);
        TextView price = (TextView) view.findViewById(R.id.textViewPriceProduct_layoutlistviewategories01);
        final CheckBox check = (CheckBox) view.findViewById(R.id.checkBoxProds_layoutlistviewcategories02);
        ImageView urgent = (ImageView) view.findViewById(R.id.imageViewProductsOfCategory);

        check.setText(listproductsSelected.getNome());
        quant.setText(String.valueOf(listproductsSelected.getQuant())+"q");
        price.setText(listproductsSelected.getPrice()+"€");

        if(listproductsSelected.isSelectedToFinalList()){
            check.setChecked(true);
        }

        if(listproductsSelected.isUrgent()){
            urgent.setImageResource(R.drawable.urgent_foreground);
        }else{
            urgent.setImageResource(R.drawable.noturgent_foreground);
        }

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check.isChecked()){
                    listproductsSelected.setSelectedToFinalList(true);
                    sql.UpdateProdoctToUrgentAndListFinal(db.connection(act), listproductsSelected.isUrgent(), listproductsSelected.isSelectedToFinalList(), listproductsSelected.getNome());
                    sql.addFinalList(db.connection(act), listproductsSelected.getPrice(), listproductsSelected.getNome(), listproductsSelected.getQuant());
                }else{
                    listproductsSelected.setSelectedToFinalList(false);
                    sql.UpdateProdoctToUrgentAndListFinal(db.connection(act), listproductsSelected.isUrgent(), listproductsSelected.isSelectedToFinalList(), listproductsSelected.getNome());
                    sql.delListFinal(db.connection(act), listproductsSelected.getNome());
                }
            }
        });

        return view;
    }

    public AdapterPersProductsOfCategory(List<Produto> listProdutos, Activity act){
        this.listProdutos = listProdutos;
        this.act = act;
    }
}
