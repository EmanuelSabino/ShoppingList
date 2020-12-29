package com.example.shoppinglist.ui.List.EditList;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppinglist.R;
import com.example.shoppinglist.ui.adapter.AdapterPersEditProducts;
import com.example.shoppinglist.ui.adapter.AdapterPersProdutos;
import com.example.shoppinglist.ui.sql.ConnectionDataBase;
import com.example.shoppinglist.ui.sql.ListaFinal;
import com.example.shoppinglist.ui.sql.Produto;
import com.example.shoppinglist.ui.sql.sqlShoppingList;

import java.util.ArrayList;
import java.util.List;

public class EditList extends Fragment {

    private EditListViewModel mViewModel;
    private ListView lv;
    private sqlShoppingList sql;
    private  ConnectionDataBase db;
    private AdapterPersEditProducts adapter;
    private List<String> delList = new ArrayList<>();
    private ImageButton deleteList;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private int idButton;

    public static EditList newInstance() {
        return new EditList();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View v =  inflater.inflate(R.layout.edit_list_fragment, container, false);

        lv = v.findViewById(R.id.EditListViewAllListProducts);
        sql = new sqlShoppingList(getActivity());
        db = new ConnectionDataBase();

        adapter = new AdapterPersEditProducts(sql.allProdutos(), getActivity(), delList);

        lv.setAdapter(adapter);

        final TextView text = v.findViewById(R.id.editTextSearchProduct_fragmentEditList_test);

        radioGroup = v.findViewById(R.id.radioGroupEditAllProducts);
        idButton = radioGroup.getCheckedRadioButtonId();
        radioButton = v.findViewById(idButton);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                idButton = radioGroup.getCheckedRadioButtonId();
                radioButton = v.findViewById(idButton);

                pesqChecked(text.getText().toString());
            }
        });

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pesqChecked(text.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        deleteList = v.findViewById(R.id.imageButtonDeleteList);
        if(!sql.skipQuestion()){
            deleteList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setMessage("Are you sure you want to delete " + delList.size() + " items");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    for(int i = 0; i < delList.size(); i++){
                                        sql.delProduct(db.connection(getActivity()), delList.get(i));
                                    }
                                    pesqChecked(text.getText().toString());
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            });
        }else{
            for(int i = 0; i < delList.size(); i++){
                sql.delProduct(db.connection(getActivity()), delList.get(i));
            }
            pesqChecked(text.getText().toString());
        }


        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(EditListViewModel.class);
        // TODO: Use the ViewModel
    }

    public void pesqChecked(String text){
        if(radioButton.getText().equals("A to Z")){
            adapter = new AdapterPersEditProducts(sql.OrderAndSearchOrderByProducts(text, true, false,false, false), getActivity(), delList);
            lv.setAdapter(adapter);
        }else if(radioButton.getText().equals("Z to A")){
            adapter = new AdapterPersEditProducts(sql.OrderAndSearchOrderByProducts(text, false, true,false, false), getActivity(), delList);
            lv.setAdapter(adapter);
        }else if(radioButton.getText().equals("€ Asc")){
            adapter = new AdapterPersEditProducts(sql.OrderAndSearchOrderByProducts(text, false, false,true, false), getActivity(), delList);
            lv.setAdapter(adapter);
        }else if(radioButton.getText().equals("€ Desc")){
            adapter = new AdapterPersEditProducts(sql.OrderAndSearchOrderByProducts(text, false, false,false, true),getActivity(), delList);
            lv.setAdapter(adapter);
        }else {
            adapter = new AdapterPersEditProducts(sql.OrderAndSearchOrderByProducts(text, false, false,false, false), getActivity(), delList);
            lv.setAdapter(adapter);
        }
    }
}