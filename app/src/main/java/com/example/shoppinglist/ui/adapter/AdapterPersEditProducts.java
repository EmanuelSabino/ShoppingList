package com.example.shoppinglist.ui.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.shoppinglist.MainActivity;
import com.example.shoppinglist.R;
import com.example.shoppinglist.ui.List.EditList.EditOneProduct;
import com.example.shoppinglist.ui.sql.ConnectionDataBase;
import com.example.shoppinglist.ui.sql.Produto;
import com.example.shoppinglist.ui.sql.sqlShoppingList;

import java.util.ArrayList;
import java.util.List;

public class AdapterPersEditProducts extends BaseAdapter {
    private List<Produto> listproducts;
    private Activity act;
    private List<String> deleteList;


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
        final View view = act.getLayoutInflater().inflate(R.layout.layout_listview_produtos02,parent,false);

        final Produto listproductsSelected = listproducts.get(position);
        final sqlShoppingList sql = new sqlShoppingList(act);
        final ConnectionDataBase db = new ConnectionDataBase();

        final TextView name = (TextView) view.findViewById(R.id.textViewProdcts);
        ImageButton inf = (ImageButton) view.findViewById(R.id.imageButtonInf02);
        ImageButton trash = (ImageButton) view.findViewById(R.id.imageButtonTrash);
        ImageButton edit = (ImageButton) view.findViewById(R.id.imageEditProducts);

        name.setText(listproductsSelected.getNome());

        if(deleteList.contains(listproductsSelected.getNome())){
            view.setBackgroundColor(view.getResources().getColor(R.color.colorAccent));
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(deleteList.contains(listproductsSelected.getNome())){
                    view.setBackgroundColor(0);
                    deleteList.remove(listproductsSelected.getNome());
                }else{
                    deleteList.add(listproductsSelected.getNome());
                    view.setBackgroundColor(view.getResources().getColor(R.color.colorAccent));
                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(act, EditOneProduct.class);
                myIntent.putExtra("nomeProduto", listproductsSelected.getNome());
                myIntent.putExtra("Price", String.valueOf(listproductsSelected.getPrice()));
                myIntent.putExtra("Quant", String.valueOf(listproductsSelected.getQuant()));
                myIntent.putExtra("isUrgent", String.valueOf(listproductsSelected.isUrgent()));
                myIntent.putExtra("description", String.valueOf(listproductsSelected.getDescricao()));
                myIntent.putExtra("nomeCategory", String.valueOf(listproductsSelected.getCategory()));
                myIntent.putExtra("isSelectedFinaList", String.valueOf(listproductsSelected.isSelectedToFinalList()));
                act.startActivity(myIntent);
            }
        });

        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sql.skipQuestion()) {
                    AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                    alertDialog.setMessage("Are you sure you want to delete '" + listproductsSelected.getNome() + "'");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    sql.delProduct(db.connection(act), listproductsSelected.getNome());
                                    sql.delListFinal(db.connection(act), listproductsSelected.getNome());
                                    sql.UpdateDescriptionUser(db.connection(act), true, false, false, sql.oneUser().getNomeUtilizador(), (sql.oneUser().getNumeroProdutos() - 2));
                                    act.startActivity(new Intent(act, MainActivity.class));
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    sql.delProduct(db.connection(act), listproductsSelected.getNome());
                    sql.delListFinal(db.connection(act), listproductsSelected.getNome());
                    sql.UpdateDescriptionUser(db.connection(act), true, false, false, sql.oneUser().getNomeUtilizador(), (sql.oneUser().getNumeroProdutos() - 2));
                    act.startActivity(new Intent(act, MainActivity.class));
                }
            }
        });

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

        return view;
    }

    public AdapterPersEditProducts(List<Produto> listproducts, Activity act){
        this.listproducts = listproducts;
        this.act = act;
    }

    public AdapterPersEditProducts(List<Produto> listproducts, Activity act, List<String> deleteList){
        this.listproducts = listproducts;
        this.act = act;
        this.deleteList = deleteList;
    }
}
