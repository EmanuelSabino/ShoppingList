package com.example.shoppinglist.ui.Product.AddNewProduct;

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

public class AddNewProduct extends AppCompatActivity {

    private TextInputLayout textInputName;
    private TextInputLayout textInputQuant;
    private TextInputLayout textInputPrice;
    private TextInputLayout textInputDescription;
    private String name;
    private int quant;
    private double price;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_product);

        Spinner spinnerCategory = findViewById(R.id.spinnerCategory_AddNewProduct);
        sqlShoppingList sql = new sqlShoppingList(this);

        ArrayList<String> arrayList = new ArrayList<>();
        textInputName = findViewById(R.id.textinput_nameProduct_AddNewProduct);
        textInputQuant = findViewById(R.id.textinput_QuantProduct_AddNewProduct);
        textInputPrice = findViewById(R.id.textinput_priceProduct_AddNewProduct);
        textInputDescription = findViewById(R.id.textinput_descritionProduct_AddNewProduct);

        textInputQuant.getEditText().setText("1");
        textInputPrice.getEditText().setText("0.0");

        for (int i = 0; i<sql.allCategory().size(); i++){
            arrayList.add(sql.allCategory().get(i).getNome());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayList);

        spinnerCategory.setAdapter(arrayAdapter);
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

    public void addNewProduct(View view) {
        CheckBox txturgent = findViewById(R.id.checkBox_urgent_addnewproduct);
        Spinner spinner = findViewById(R.id.spinnerCategory_AddNewProduct);
        ConnectionDataBase db = new ConnectionDataBase();
        sqlShoppingList sql = new sqlShoppingList(this);

        boolean urgent = txturgent.isChecked();

        if(validateDescription() && validateName() && validatePrice() && validateQuant()){
            String name = textInputName.getEditText().getText().toString().toLowerCase().trim();
            String[] split = name.split(" ");
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < split.length; i++) {
                if(!split[i].isEmpty()){
                    String word = split[i];
                    word = word.substring(0, 1).toUpperCase() + word.substring(1);
                    builder.append(" ").append(word);
                }
            }

            if(!sql.ThisProductExist(builder.toString().replaceFirst(" ", ""))){

                description = textInputDescription.getEditText().getText().toString().trim();
                price = Double.parseDouble(textInputPrice.getEditText().getText().toString());
                quant = Integer.parseInt(textInputQuant.getEditText().getText().toString());


                sql.addProduct(db.connection(this), builder.toString().replaceFirst(" ", ""), spinner.getSelectedItem().toString(), urgent, description, price, quant);

                if(urgent){
                    sql.UpdateDescriptionUser(db.connection(this), false, false,true, sql.oneUser().getNomeUtilizador(), (sql.oneUser().getNumeroProdutosUrgentes()));
                }else{
                    sql.UpdateDescriptionUser(db.connection(this), true, false,false, sql.oneUser().getNomeUtilizador(), (sql.oneUser().getNumeroProdutos()));
                }

                textInputPrice.getEditText().setText("0.0");
                textInputQuant.getEditText().setText("1");
                textInputDescription.getEditText().setText("");
                textInputName.getEditText().setText("");
                txturgent.setChecked(false);

                if(!sql.skipWarning()){
                    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Success");
                    alertDialog.setMessage("Your product was successfully added");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }else{
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Invalid");
                alertDialog.setMessage("This product already exists");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }

        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}