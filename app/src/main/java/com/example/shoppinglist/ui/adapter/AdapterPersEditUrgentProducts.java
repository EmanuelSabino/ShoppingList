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

public class AdapterPersEditUrgentProducts extends BaseAdapter {
    private List<Produto> listproducts;
    private Activity act;


    @Override
    public int getCount() {
        return listproducts.size();
    }

    @Override
    public Object getItem(int position) {
        return listproducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater().inflate(R.layout.layout_listview_produtos05,parent,false);

        final Produto listproductsSelected = listproducts.get(position);
        final sqlShoppingList sql = new sqlShoppingList(act);
        final ConnectionDataBase db = new ConnectionDataBase();

        final CheckBox check = (CheckBox) view.findViewById(R.id.checkBoxProds05);
        final ImageView urgent = (ImageView) view.findViewById(R.id.imageUrgen05);
        TextView price = (TextView) view.findViewById(R.id.textViewPrice05);
        TextView quant = (TextView) view.findViewById(R.id.textViewQuant05);

        price.setText(String.valueOf(listproductsSelected.getPrice()) + "€");
        quant.setText(String.valueOf(listproductsSelected.getQuant()) + "q");
        check.setText(listproductsSelected.getNome());

        if(listproductsSelected.isUrgent()){
            urgent.setImageResource(R.drawable.urgent_foreground);
            check.setChecked(true);
        }else {
            urgent.setImageResource(R.drawable.noturgent_foreground);
        }

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check.isChecked()){
                    urgent.setImageResource(R.drawable.urgent_foreground);
                    listproductsSelected.setUrgent(true);
                    sql.UpdateProdoctToUrgentAndListFinal(db.connection(act), listproductsSelected.isUrgent(), listproductsSelected.isSelectedToFinalList(), listproductsSelected.getNome());
                }else{
                    urgent.setImageResource(R.drawable.noturgent_foreground);
                    listproductsSelected.setUrgent(false);
                    sql.UpdateProdoctToUrgentAndListFinal(db.connection(act), listproductsSelected.isUrgent(), listproductsSelected.isSelectedToFinalList(), listproductsSelected.getNome());
                }
            }
        });
        return view;
    }

    public AdapterPersEditUrgentProducts(List<Produto> listproducts, Activity act){
        this.listproducts = listproducts;
        this.act = act;
    }
}
