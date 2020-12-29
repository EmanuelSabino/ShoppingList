package com.example.shoppinglist.ui.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.shoppinglist.R;
import com.example.shoppinglist.ui.sql.ConnectionDataBase;
import com.example.shoppinglist.ui.sql.Produto;
import com.example.shoppinglist.ui.sql.sqlShoppingList;

import java.util.List;

public class AdapterPersUrgentProducts extends BaseAdapter {
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
        final View view = act.getLayoutInflater().inflate(R.layout.layout_listview_produtos04,parent,false);

        final Produto listproductsSelected = listproducts.get(position);
        final sqlShoppingList sql = new sqlShoppingList(act);
        final ConnectionDataBase db = new ConnectionDataBase();

        final CheckBox check = (CheckBox) view.findViewById(R.id.checkBoxProds04);
        ImageButton inf = (ImageButton) view.findViewById(R.id.imageButtonInf04);
        TextView price = (TextView) view.findViewById(R.id.textViewPrice04);
        TextView quant = (TextView) view.findViewById(R.id.textViewQuant04);

        price.setText(String.valueOf(listproductsSelected.getPrice()) + "€");
        quant.setText(String.valueOf(listproductsSelected.getQuant()) + "q");
        check.setText(listproductsSelected.getNome());

        if(listproductsSelected.isSelectedToFinalList()){
            check.setChecked(true);
        }

        inf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button close;
                final Dialog myDialog;

                myDialog = new Dialog(act);
                myDialog.setContentView(R.layout.dialog_of_detail_product);

                close = myDialog.findViewById(R.id.close);

                TextView name = myDialog.findViewById(R.id.textview_name_dialog);
                TextView description = myDialog.findViewById(R.id.textview_description_dialog);
                TextView price = myDialog.findViewById(R.id.textview_price_dialog);
                TextView isurgent = myDialog.findViewById(R.id.textview_urgent_dialog);
                TextView quant = myDialog.findViewById(R.id.textview_quant_dialog);
                TextView category = myDialog.findViewById(R.id.textview_Category_dialog);

                name.setText(listproductsSelected.getNome());
                price.setText(""+ listproductsSelected.getPrice());
                isurgent.setText(""+listproductsSelected.isUrgent());
                quant.setText(""+ listproductsSelected.getQuant());
                category.setText(""+ listproductsSelected.getCategory());

                if(listproductsSelected.getDescricao().isEmpty()){
                    TextView title = myDialog.findViewById(R.id.textviewdescriptionofdialog);
                    title.setText("");
                    description.setText("without description");
                }else {
                    description.setText(listproductsSelected.getDescricao());
                }

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.cancel();
                    }
                });

                myDialog.show();
            }
        });

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

    public AdapterPersUrgentProducts(List<Produto> listproducts, Activity act){
        this.listproducts = listproducts;
        this.act = act;
    }
}
