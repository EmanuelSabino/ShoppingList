package com.example.shoppinglist.ui.List.EditUrgenList;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.shoppinglist.MainActivity;
import com.example.shoppinglist.R;
import com.example.shoppinglist.ui.adapter.AdapterPersEditUrgentProducts;
import com.example.shoppinglist.ui.adapter.AdapterPersProdutos;
import com.example.shoppinglist.ui.adapter.AdapterPersUrgentProducts;
import com.example.shoppinglist.ui.sql.ConnectionDataBase;
import com.example.shoppinglist.ui.sql.sqlShoppingList;

public class edit_urgent_list extends AppCompatActivity {

    private sqlShoppingList sql;
    private AdapterPersEditUrgentProducts adapter;
    private ListView lv;
    private TextView text;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private int idButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_urgent_list);

        text = findViewById(R.id.editTextTextSearch_activityEitUrgentList);
        lv = findViewById(R.id.listviewediturgenproducts_activityediturgentproducts);
        sql = new sqlShoppingList(this);
        adapter = new AdapterPersEditUrgentProducts(sql.allProdutos(),this);

        lv.setAdapter(adapter);

        radioGroup = findViewById(R.id.radioGroupEditUrgentList);
        idButton = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(idButton);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                idButton = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(idButton);

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
    }

    public void pesqChecked(String text){
        if(radioButton.getText().equals("A to Z")){
            adapter = new AdapterPersEditUrgentProducts(sql.OrderAndSearchOrderByProducts(text, true, false,false, false), this);
            lv.setAdapter(adapter);
        }else if(radioButton.getText().equals("Z to A")){
            adapter = new AdapterPersEditUrgentProducts(sql.OrderAndSearchOrderByProducts(text, false, true,false, false), this);
            lv.setAdapter(adapter);
        }else if(radioButton.getText().equals("€ Asc")){
            adapter = new AdapterPersEditUrgentProducts(sql.OrderAndSearchOrderByProducts(text, false, false,true, false), this);
            lv.setAdapter(adapter);
        }else if(radioButton.getText().equals("€ Desc")){
            adapter = new AdapterPersEditUrgentProducts(sql.OrderAndSearchOrderByProducts(text, false, false,false, true),this);
            lv.setAdapter(adapter);
        }else {
            adapter = new AdapterPersEditUrgentProducts(sql.OrderAndSearchOrderByProducts(text, false, false,false, false), this);
            lv.setAdapter(adapter);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}