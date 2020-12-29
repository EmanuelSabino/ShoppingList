package com.example.shoppinglist.ui.List.AllList;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
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
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.shoppinglist.R;
import com.example.shoppinglist.ui.Product.AddNewProduct.AddNewProduct;
import com.example.shoppinglist.ui.adapter.AdapterPersEditUrgentProducts;
import com.example.shoppinglist.ui.adapter.AdapterPersProdutos;
import com.example.shoppinglist.ui.sql.ConnectionDataBase;
import com.example.shoppinglist.ui.sql.sqlShoppingList;

public class AllList extends Fragment {

    private AllListViewModel mViewModel;
    private Button bt;
    private ListView lv;
    private sqlShoppingList sql;
    private ConnectionDataBase db;
    private AdapterPersProdutos adapter;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private int idButton;


    public static AllList newInstance() {
        return new AllList();
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View v  = inflater.inflate(R.layout.all_list_fragment, container, false);

        final TextView text = v.findViewById(R.id.edittex_AllProducts);
        bt = v.findViewById(R.id.btAddProduct);
        lv = v.findViewById(R.id.listProducts_fragmentAllList);
        sql = new sqlShoppingList(getActivity());
        db = new ConnectionDataBase();
        adapter = new AdapterPersProdutos(sql.allProdutos(), getActivity());

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddNewProduct.class));
            }
        });

        radioGroup = v.findViewById(R.id.radioGroupAllProducts);
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

        lv.setAdapter(adapter);

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


        return  v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AllListViewModel.class);
        // TODO: Use the ViewModel
    }

    public void pesqChecked(String text){
        if(radioButton.getText().equals("A to Z")){
            adapter = new AdapterPersProdutos(sql.OrderAndSearchOrderByProducts(text, true, false,false, false), getActivity());
            lv.setAdapter(adapter);
        }else if(radioButton.getText().equals("Z to A")){
            adapter = new AdapterPersProdutos(sql.OrderAndSearchOrderByProducts(text, false, true,false, false), getActivity());
            lv.setAdapter(adapter);
        }else if(radioButton.getText().equals("€ Asc")){
            adapter = new AdapterPersProdutos(sql.OrderAndSearchOrderByProducts(text, false, false,true, false), getActivity());
            lv.setAdapter(adapter);
        }else if(radioButton.getText().equals("€ Desc")){
            adapter = new AdapterPersProdutos(sql.OrderAndSearchOrderByProducts(text, false, false,false, true),getActivity());
            lv.setAdapter(adapter);
        }else {
            adapter = new AdapterPersProdutos(sql.OrderAndSearchOrderByProducts(text, false, false,false, false), getActivity());
            lv.setAdapter(adapter);
        }
    }

}