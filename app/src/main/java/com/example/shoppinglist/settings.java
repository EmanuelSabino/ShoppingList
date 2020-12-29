package com.example.shoppinglist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppinglist.ui.sql.ConnectionDataBase;
import com.example.shoppinglist.ui.sql.sqlShoppingList;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

public class settings extends AppCompatActivity {

    private sqlShoppingList sql;
    private ConnectionDataBase db;
    private TextView name;
    private final int gallery_image = 1;
    private final int PERMISSAO_REQUEST = 2;
    private ImageButton gallery;
    private Bitmap bit = null;
    private boolean haveImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        name = findViewById(R.id.usernamesettings);
        sql = new sqlShoppingList(this);
        db = new ConnectionDataBase();
        Switch skipWarning = findViewById(R.id.switchIgnorWarning);
        Switch skipQuestion = findViewById(R.id.switchQuestion);
        Switch skipprodSelect = findViewById(R.id.switchProdSelect);

        skipWarning.setChecked(sql.skipWarning());
        skipQuestion.setChecked(sql.skipQuestion());
        skipprodSelect.setChecked(sql.skipSelectedProduct());



        gallery = findViewById(R.id.imageButtonUserUpdate);

        if(sql.fotPerfil() != null){
            gallery.setImageBitmap(sql.fotPerfil());
            haveImage = true;
        }

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, gallery_image);
                haveImage = true;
            }
        });


        name.setText(String.valueOf(sql.oneUser().getNomeUtilizador()));
    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 1){

            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            gallery.setImageBitmap(bitmap);
            bit = bitmap;
        }
    }

    public void save(View view) {
        Switch skipWarning = findViewById(R.id.switchIgnorWarning);
        Switch skipQuestion = findViewById(R.id.switchQuestion);
        Switch skipprodSelect = findViewById(R.id.switchProdSelect);

        TextView txt = findViewById(R.id.usernamesettings);
        String name = txt.getText().toString().toLowerCase().trim();
        String[] split = name.split(" ");
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < split.length; i++) {
            if(!split[i].isEmpty()){
                String word = split[i];
                word = word.substring(0, 1).toUpperCase() + word.substring(1);
                builder.append(" ").append(word);
            }
        }

        int skipWarningNum = 0;
        int skipQuestionNum = 0;
        int skipProductSelected = 0;

        if(skipWarning.isChecked()){
            skipWarningNum = 1;
        }
        if(skipprodSelect.isChecked()){
            skipProductSelected = 1;
        }
        if(skipQuestion.isChecked()){
            skipQuestionNum = 1;
        }

        if(haveImage){
            Bitmap bmp = bit;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            bmp.recycle();
            sql.InserFotoUtilizador(db.connection(this), sql.oneUser().getNomeUtilizador(), byteArray);
        }else{
            sql.InserFotoUtilizador(db.connection(this), sql.oneUser().getNomeUtilizador(), null);
        }

        sql.UpdateNameUser(db.connection(this),  builder.toString().replaceFirst(" ", ""), sql.oneUser().getNomeUtilizador());
        sql.UpdateDefApp(db.connection(this), sql.oneDefApp(), skipWarningNum, skipQuestionNum, skipProductSelected);

        startActivity(new Intent(this, MainActivity.class));

    }

    public void clearAllCat(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("WARNING - CLEAR ALL CATEGORIES");
        alertDialog.setMessage("Are You Sure?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        exitResetCategory();
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

    public void clearAllProduct(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("WARNING - CLEAR ALL PRODUCTS");
        alertDialog.setMessage("Are You Sure?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        exitResetProducts();
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

    public void reset(View view) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("WARNING - RESET DATA");
        alertDialog.setMessage("Are You Sure?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        exitResetMemory();
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

    public void exitResetMemory(){
        sql.delAllListFinal(db.connection(this));
        sql.delAllProducts(db.connection(this));
        sql.delAllCategory(db.connection(this));
        sql.deldef(db.connection(this));
        sql.delUser(db.connection(this));
        startActivity(new Intent(this, MainActivity.class));
    }
    public void exitResetProducts(){
        sql.delAllListFinal(db.connection(this));
        sql.delAllProducts(db.connection(this));
        sql.UpdateDescriptionUser(db.connection(this), true, false, false, sql.oneUser().getNomeUtilizador(), -1);
        sql.UpdateDescriptionUser(db.connection(this), false, false, true, sql.oneUser().getNomeUtilizador(), -1);
        startActivity(new Intent(this, MainActivity.class));
    }
    public void exitResetCategory(){
        sql.resetCategories(db.connection(this));
        sql.delAllCategory(db.connection(this));
        startActivity(new Intent(this, MainActivity.class));
    }

    public void removeimg(View view) {
        if(haveImage){
            haveImage=false;
            gallery.setImageResource(R.drawable.personuserhome_foreground);
        }
    }
}