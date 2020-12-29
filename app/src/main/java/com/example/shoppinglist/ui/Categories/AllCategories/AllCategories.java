package com.example.shoppinglist.ui.Categories.AllCategories;

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
import com.example.shoppinglist.ui.Categories.AddNewCategories.AddNewCategory;
import com.example.shoppinglist.ui.adapter.AdapterPersCategories;
import com.example.shoppinglist.ui.adapter.AdapterPersProdutos;
import com.example.shoppinglist.ui.sql.ConnectionDataBase;
import com.example.shoppinglist.ui.sql.sqlShoppingList;


public class AllCategories extends Fragment {

    private AllCategoriesViewModel mViewModel;
    private sqlShoppingList sql;
    private ConnectionDataBase db;
    private AdapterPersCategories adapter;
    private ListView lv;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private int idButton;


    public static AllCategories newInstance() {
        return new AllCategories();
    }
    Button btAddCategory;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View v =  inflater.inflate(R.layout.all_categories_fragment, container, false);

        btAddCategory = v.findViewById(R.id.btAddCategory);
        lv = v.findViewById(R.id.listviewCategory_fragmentallcategories);

        btAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddNewCategory.class));
            }
        });

        sql = new sqlShoppingList(getActivity());
        db = new ConnectionDataBase();
        adapter = new AdapterPersCategories(sql.allCategory(), getActivity());

        lv.setAdapter(adapter);

        final TextView text = v.findViewById(R.id.editTextSearchCategory);


        radioGroup = v.findViewById(R.id.radioGroupAllCategories);
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

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AllCategoriesViewModel.class);
        // TODO: Use the ViewModel
    }

    public void pesqChecked(String text){
        if(radioButton.getText().equals("A to Z")){
            adapter = new AdapterPersCategories(sql.OrderAndSearchOrderByCategory(text, true, false), getActivity());
            lv.setAdapter(adapter);
        }else if(radioButton.getText().equals("Z to A")){
            adapter = new AdapterPersCategories(sql.OrderAndSearchOrderByCategory(text, false, true), getActivity());
            lv.setAdapter(adapter);
        }else {
            adapter = new AdapterPersCategories(sql.OrderAndSearchOrderByCategory(text, false, false), getActivity());
            lv.setAdapter(adapter);
        }
    }
}