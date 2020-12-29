package com.example.shoppinglist.ui.List.UrgentList;

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
import android.widget.Toast;

import com.example.shoppinglist.R;
import com.example.shoppinglist.ui.List.EditUrgenList.edit_urgent_list;
import com.example.shoppinglist.ui.adapter.AdapterPersUrgentProducts;
import com.example.shoppinglist.ui.sql.ConnectionDataBase;
import com.example.shoppinglist.ui.sql.sqlShoppingList;

public class UrgentList extends Fragment {

    private UrgentListViewModel mViewModel;
    private sqlShoppingList sql;
    private ConnectionDataBase db;
    private ListView lv;
    private AdapterPersUrgentProducts adapter;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private int idButton;

    public static UrgentList newInstance() {
        return new UrgentList();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.urgent_list_fragment, container, false);

        final TextView text = view.findViewById(R.id.edittex_AllUrgenList);

        lv = view.findViewById(R.id.listproducts_fragmentUrgentProducts);
        sql = new sqlShoppingList(getActivity());
        db = new ConnectionDataBase();
        adapter = new AdapterPersUrgentProducts(sql.UrgentsProducts(),getActivity());

        radioGroup = view.findViewById(R.id.radioGroupUrgentList);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                idButton = radioGroup.getCheckedRadioButtonId();
                radioButton = view.findViewById(idButton);

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

        Button bt = view.findViewById(R.id.bteditlisturgent_fragmenturgenlist);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), edit_urgent_list.class));
            }
        });

        return view;
    }

    public void pesqChecked(String text){
        if(radioButton.getText().equals("A to Z")){
            adapter = new AdapterPersUrgentProducts(sql.OrderAndSearchOrderByProductsUrgents(text, true, false,false, false),getActivity());
            lv.setAdapter(adapter);
        }else if(radioButton.getText().equals("Z to A")){
            adapter = new AdapterPersUrgentProducts(sql.OrderAndSearchOrderByProductsUrgents(text, false, true,false, false),getActivity());
            lv.setAdapter(adapter);
        }else if(radioButton.getText().equals("€ Asc")){
            adapter = new AdapterPersUrgentProducts(sql.OrderAndSearchOrderByProductsUrgents(text, false, false,true, false),getActivity());
            lv.setAdapter(adapter);
        }else if(radioButton.getText().equals("€ Desc")){
            adapter = new AdapterPersUrgentProducts(sql.OrderAndSearchOrderByProductsUrgents(text, false, false,false, true),getActivity());
            lv.setAdapter(adapter);
        }else {
            adapter = new AdapterPersUrgentProducts(sql.OrderAndSearchOrderByProductsUrgents(text, false, false,false, false),getActivity());
            lv.setAdapter(adapter);
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(UrgentListViewModel.class);
        // TODO: Use the ViewModel
    }
}