package com.example.shoppinglist.ui.Categories.AddNewCategories;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppinglist.MainActivity;
import com.example.shoppinglist.R;
import com.example.shoppinglist.ui.Categories.AllCategories.AllCategories;
import com.example.shoppinglist.ui.adapter.AdapterPersCategories;
import com.example.shoppinglist.ui.sql.ConnectionDataBase;
import com.example.shoppinglist.ui.sql.sqlShoppingList;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddNewCategory extends AppCompatActivity {

    private sqlShoppingList sql = new sqlShoppingList(this);
    private ConnectionDataBase db = new ConnectionDataBase();
    private TextInputLayout textInputName;
    private String name;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_category);

        lv = findViewById(R.id.listCategories_activityaddnewcategories);
        textInputName = findViewById(R.id.textinput_nameCategory_AddNewCategory);


        AdapterPersCategories adapter = new AdapterPersCategories(sql.allCategory(), this);

        lv.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
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

    public void AddNewCategory(View view) {
        TextView nameEditText = findViewById(R.id.edittex_activityaddnewcategory);

        String name = nameEditText.getText().toString().toLowerCase().trim();
        String[] split = name.split(" ");
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < split.length; i++) {
            if(!split[i].isEmpty()){
                String word = split[i];
                word = word.substring(0, 1).toUpperCase() + word.substring(1);
                builder.append(" ").append(word);
            }
        }

        if(validateName()){
            if(!sql.ThisCategoryExist(builder.toString().replaceFirst(" ", ""))){
                sql.addCategory(db.connection(this), builder.toString().replaceFirst(" ", ""));
                sql.UpdateDescriptionUser(db.connection(this), false, true, false, sql.oneUser().getNomeUtilizador(), sql.oneUser().getNumeroCategorias());

                AdapterPersCategories adapter = new AdapterPersCategories(sql.allCategory(), this);

                lv.setAdapter(adapter);
                if(!sql.skipWarning()){
                    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setTitle("Success");
                    alertDialog.setMessage("Your Category was successfully added");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    textInputName.getEditText().setText("");
                }
            }else{
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("Invalid");
                alertDialog.setMessage("This category already exists");
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
}