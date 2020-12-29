package com.example.shoppinglist.ui.List.EditList;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppinglist.MainActivity;
import com.example.shoppinglist.R;
import com.example.shoppinglist.ui.sql.ConnectionDataBase;
import com.example.shoppinglist.ui.sql.sqlShoppingList;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class EditOneProduct extends AppCompatActivity {

    private sqlShoppingList sql;
    private ConnectionDataBase db;
    private TextView title;
    private TextInputLayout textInputName;
    private TextInputLayout textInputQuant;
    private TextInputLayout textInputPrice;
    private TextInputLayout textInputDescription;
    private String name;
    private int quant;
    private double price;
    private String description;
    private CheckBox isUrgent;
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_one_product);

        sql = new sqlShoppingList(this);
        db = new ConnectionDataBase();
        title = findViewById(R.id.editproduct);
        isUrgent = findViewById(R.id.checkBox_urgent_editproduct);
        spinner = findViewById(R.id.spinnerProduct_editproduct);
        textInputName = findViewById(R.id.textinput_nameProduct_EditProduct);
        textInputQuant = findViewById(R.id.textinput_quant_EditProduct);
        textInputPrice = findViewById(R.id.textinput_price_EditProduct);
        textInputDescription = findViewById(R.id.textinput_description_EditProduct);

        title.setText("Edit Product '" + getIntent().getStringExtra("nomeProduto") + "'");
        textInputName.getEditText().setText(getIntent().getStringExtra("nomeProduto"));
        textInputQuant.getEditText().setText(getIntent().getStringExtra("Quant"));
        textInputPrice.getEditText().setText(getIntent().getStringExtra("Price"));
        textInputDescription.getEditText().setText(getIntent().getStringExtra("description"));
        isUrgent.setChecked(Boolean.parseBoolean(getIntent().getStringExtra("isUrgent")));

        ArrayList<String> arrayList = new ArrayList<>();

        for (int i = 0; i<sql.allCategory().size(); i++){
            arrayList.add(sql.allCategory().get(i).getNome());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);
        spinner.setAdapter(arrayAdapter);

        for(int i = 0; i<arrayAdapter.getCount(); i++){
            if(arrayAdapter.getItem(i).equals(getIntent().getStringExtra("nomeCategory"))){
                spinner.setSelection(i);
                break;
            }
        }
    }

    public boolean validateName(){
        String name = textInputName.getEditText().getText().toString().trim();
        if(name.isEmpty()){
            textInputName.setError("Field can't be empty");
            return false;
        }else if(name.length()>16){
            textInputName.setError("Name too long");
            return false;
        }else{
            textInputName.setError(null);
            return true;
        }
    }

    public boolean validateDescription(){
        String descrition = textInputDescription.getEditText().getText().toString().trim();

        if(descrition.length()>150){
            textInputDescription.setError("Description too long");
            return false;
        }else{
            textInputDescription.setError(null);
            return true;
        }
    }

    public boolean validateQuant(){
        int quant = Integer.parseInt(textInputQuant.getEditText().getText().toString());
        if(quant == 0){
            textInputQuant.setError("Error, min: 1");
            return false;
        }else if(quant>100){
            textInputQuant.setError("min: 0 | max: 100");
            return false;
        }else{
            textInputQuant.setError(null);
            return true;
        }
    }

    public boolean validatePrice(){
        String pric = textInputPrice.getEditText().getText().toString();
        if(pric.isEmpty()){
            textInputPrice.getEditText().setText("0.0");
            return true;
        }else {
            double price = Double.parseDouble(textInputPrice.getEditText().getText().toString());
            if(price > 10000000){
                textInputPrice.setError("max price is 10000000");
                return false;
            }else{
                textInputPrice.setError(null);
                return true;
            }
        }
    }

    public void save(View view) {
        if(validateDescription() && validateName() && validatePrice() && validateQuant()){

            name = textInputName.getEditText().getText().toString().trim();
            description = textInputDescription.getEditText().getText().toString().trim();
            price = Double.parseDouble(textInputPrice.getEditText().getText().toString());
            quant = Integer.parseInt(textInputQuant.getEditText().getText().toString());

            if(!sql.skipWarning()){
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Success");
                alertDialog.setMessage("Your product was successfully saved");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startAct(sql, db,  name, quant,price, spinner.getSelectedItem().toString(),
                                        isUrgent.isChecked(), description, getIntent().getStringExtra("nomeProduto"), Boolean.parseBoolean(getIntent().getStringExtra("isSelectedFinaList")));
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }else{
                startAct(sql, db,  name, quant,price, spinner.getSelectedItem().toString(),
                        isUrgent.isChecked(), description, getIntent().getStringExtra("nomeProduto"), Boolean.parseBoolean(getIntent().getStringExtra("isSelectedFinaList")));
            }

        }

    }


    public void startAct(sqlShoppingList sql, ConnectionDataBase db, String nome, int quant, double price, String category, boolean isurgent, String description, String nomeAntigo, boolean selecionadoListFinal){
        sql.UpdateProduct(db.connection(this), nome, category, isurgent, description, price, quant, selecionadoListFinal, nomeAntigo);
        startActivity(new Intent(this, MainActivity.class));
    }
}