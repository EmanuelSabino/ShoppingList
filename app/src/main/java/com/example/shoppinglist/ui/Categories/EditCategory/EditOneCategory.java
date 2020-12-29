package com.example.shoppinglist.ui.Categories.EditCategory;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.shoppinglist.MainActivity;
import com.example.shoppinglist.R;
import com.example.shoppinglist.ui.Categories.AllCategories.AllCategories;
import com.example.shoppinglist.ui.sql.ConnectionDataBase;
import com.example.shoppinglist.ui.sql.sqlShoppingList;
import com.google.android.material.textfield.TextInputLayout;

public class EditOneCategory extends AppCompatActivity {

    private TextInputLayout textInputName;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_one_category);

        TextView title = findViewById(R.id.EditCategoryTextView);
        textInputName = findViewById(R.id.textinput_nameCategory_EditOneCategory);

        title.setText("Edit Category '" + getIntent().getStringExtra("nameOfCatgory") + "'");
    }

    public boolean validateName(){
        String name = textInputName.getEditText().getText().toString().trim();
        if(name.isEmpty()){
            textInputName.setError("Field can't be empty");
            return false;
        }else if(name.length()>20){
            textInputName.setError("Name too long");
            return false;
        }else{
            textInputName.setError(null);
            return true;
        }
    }

    public void save(View view) {

        final sqlShoppingList sql = new sqlShoppingList(this);
        final ConnectionDataBase db = new ConnectionDataBase();

        if(validateName()){
            name = textInputName.getEditText().getText().toString().trim();

            if(!sql.skipQuestion()){
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setMessage("Do you want to rename '" + getIntent().getStringExtra("nameOfCatgory") + "' to '" + name + "'?");
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startAct(sql, db, name);
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }else{
                startAct(sql, db, name);
            }
        }
    }

        public void startAct(sqlShoppingList sql, ConnectionDataBase db, String name){
            sql.UpdateCat(db.connection(this),name, getIntent().getStringExtra("nameOfCatgory"));
            sql.UpdateCategoryNameOfProducts(db.connection(this), getIntent().getStringExtra("nameOfCatgory"), name);
            startActivity(new Intent(this, MainActivity.class));
        }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, AllCategories.class));
    }
    }