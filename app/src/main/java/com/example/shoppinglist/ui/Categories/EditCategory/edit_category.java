package com.example.shoppinglist.ui.Categories.EditCategory;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppinglist.MainActivity;
import com.example.shoppinglist.R;
import com.example.shoppinglist.ui.adapter.AdapterPersCategories;
import com.example.shoppinglist.ui.adapter.AdapterPersProductsOfCategory;
import com.example.shoppinglist.ui.sql.ConnectionDataBase;
import com.example.shoppinglist.ui.sql.sqlShoppingList;

public class edit_category extends AppCompatActivity {

    private ListView lv;
    private sqlShoppingList sql;
    private ConnectionDataBase db;
    private AdapterPersProductsOfCategory adapter;
    private TextView name;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private int idButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        final TextView text = findViewById(R.id.editTextSearchCategoryEditCategory);
        name = findViewById(R.id.CategorySelected_activityeditcategory);
        lv = findViewById(R.id.listviewproductsofcategories_activityeditcategory);
        name.setText(getIntent().getStringExtra("nomeCategoria"));

        sql = new sqlShoppingList(this);
        db = new ConnectionDataBase();

        adapter = new AdapterPersProductsOfCategory(sql.allProductsOfOneCategory(getIntent().getStringExtra("nomeCategoria")),this);

        lv.setAdapter(adapter);

        radioGroup = findViewById(R.id.radioGroupEditCategory);
        idButton = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(idButton);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                idButton = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(idButton);
                pesqChecked(text.getText().toString(), name.getText().toString());
            }
        });

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pesqChecked(text.getText().toString(), name.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        if(name.getText().equals("Widouth Category")){
            ImageButton btedit = findViewById(R.id.imageButtonEditCategory);
            ImageButton btDel = findViewById(R.id.imageButtonDelCategory);

            btDel.setVisibility(View.GONE);
            btedit.setVisibility(View.GONE);
        }
    }

    public void edit(View view) {
        TextView txt = findViewById(R.id.CategorySelected_activityeditcategory);
        Intent myIntenst = new Intent(this , EditOneCategory.class);
        myIntenst.putExtra("nameOfCatgory", txt.getText().toString());
        startActivity(myIntenst);
    }

    public void delete(View view) {
        if(!sql.skipQuestion()){
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setMessage("Are you sure you want to delete '" + getIntent().getStringExtra("nomeCategoria") + "'?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startAct(sql, db, name.getText().toString());
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }else{
            startAct(sql, db, name.getText().toString());
        }

    }
    public void startAct(sqlShoppingList sql, ConnectionDataBase db, String name){
        sql.delCat(db.connection(this), name);
        sql.UpdateCategoryNameOfProducts(db.connection(this), name, "Widouth Category");
        sql.UpdateDescriptionUser(db.connection(this), false, true, false, sql.oneUser().getNomeUtilizador(), (sql.oneUser().getNumeroCategorias()-2));
        startActivity(new Intent(this, MainActivity.class));
    }

    public void pesqChecked(String text, String cat){
        if(radioButton.getText().equals("A to Z")){
            adapter = new AdapterPersProductsOfCategory(sql.OrderAndSearchOrderByProductsofCategory(text, true, false, false, false,cat), this);
            lv.setAdapter(adapter);
        }else if(radioButton.getText().equals("Z to A")){
            adapter = new AdapterPersProductsOfCategory(sql.OrderAndSearchOrderByProductsofCategory(text, false, true, false, false, cat), this);
            lv.setAdapter(adapter);
        }else if(radioButton.getText().equals("€ Asc")){
            adapter = new AdapterPersProductsOfCategory(sql.OrderAndSearchOrderByProductsofCategory(text, false, false, true, false, cat), this);
            lv.setAdapter(adapter);
        }else if(radioButton.getText().equals("€ Desc")){
            adapter = new AdapterPersProductsOfCategory(sql.OrderAndSearchOrderByProductsofCategory(text, false, false, false, true, cat),this);
            lv.setAdapter(adapter);
        }else {
            adapter = new AdapterPersProductsOfCategory(sql.OrderAndSearchOrderByProductsofCategory(text, false, false, false, false, cat), this);
            lv.setAdapter(adapter);
        }
    }
}