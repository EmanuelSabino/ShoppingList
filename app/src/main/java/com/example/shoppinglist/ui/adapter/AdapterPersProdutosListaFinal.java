package com.example.shoppinglist.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.shoppinglist.R;
import com.example.shoppinglist.ui.sql.ConnectionDataBase;
import com.example.shoppinglist.ui.sql.ListaFinal;
import com.example.shoppinglist.ui.sql.sqlShoppingList;

import java.util.List;

public class AdapterPersProdutosListaFinal extends BaseAdapter {
    private List<ListaFinal> finallist;
    private Activity act;


    @Override
    public int getCount() {
        return finallist.size();
    }

    @Override
    public Object getItem(int position) {
        return finallist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater().inflate(R.layout.layout_listview_produtos03,parent,false);

        final ListaFinal finallistselected = finallist.get(position);
        final sqlShoppingList sql = new sqlShoppingList(act);
        final ConnectionDataBase db = new ConnectionDataBase();

        final CheckBox check = (CheckBox) view.findViewById(R.id.checkBoxProds03);
        TextView price = (TextView) view.findViewById(R.id.textViewPrice03);
        TextView quant = (TextView) view.findViewById(R.id.textViewQuant03);

        quant.setText(String.valueOf(finallistselected.getQuant())+ "q");
        price.setText(String.valueOf(finallistselected.getPrecoProduto())+"€");
        check.setText(finallistselected.getNomeProduto());
        check.setChecked(true);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check.isChecked()){
                    sql.addFinalList(db.connection(act), finallistselected.getPrecoProduto(), finallistselected.getNomeProduto(), finallistselected.getQuant());
                    sql.UpdateProductSelectListFinal(db.connection(act), true, finallistselected.getNomeProduto());
                }else{
                    sql.delListFinal(db.connection(act), finallistselected.getNomeProduto());
                    sql.UpdateProductSelectListFinal(db.connection(act), false, finallistselected.getNomeProduto());
                }
            }
        });


        return view;
    }

    public AdapterPersProdutosListaFinal(List<ListaFinal> finallist, Activity act){
        this.finallist = finallist;
        this.act = act;
    }
}
