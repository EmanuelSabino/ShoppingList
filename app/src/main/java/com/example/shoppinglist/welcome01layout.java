package com.example.shoppinglist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shoppinglist.ui.sql.ConnectionDataBase;
import com.example.shoppinglist.ui.sql.sqlShoppingList;
import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

public class welcome01layout extends AppCompatActivity {

    private TextInputLayout textInputName;
    private String name;
    private sqlShoppingList sql;
    private ConnectionDataBase db;
    private final int gallery_image = 1;
    private final int PERMISSAO_REQUEST = 2;
    private ImageButton gallery;
    private Bitmap bit = null;
    private boolean haveImage = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome01layout);

        textInputName = findViewById(R.id.textinput_nameUser_welcome);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){

            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSAO_REQUEST);
            }
        }


        gallery = findViewById(R.id.imageButtonPerfil);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, gallery_image);
                haveImage = true;
            }
        });


        sql = new sqlShoppingList(this);
        db = new ConnectionDataBase();

        if(sql.oneUser() != null){
            startActivity(new Intent(this, MainActivity.class));
        }
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


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        if(requestCode == PERMISSAO_REQUEST){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }else{

            }
            return;
        }
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

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    public void addNewUser(View view) {
     if (validateName()){
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

         sql.addDescricaoUtilizador(db.connection(this), builder.toString().replaceFirst(" ", ""), 0,0,1);
         sql.addCategory(db.connection(this), "Widouth Category");
         sql.addDefApp(db.connection(this), false, false, false);

         if(haveImage){
             Bitmap bmp = bit;
             ByteArrayOutputStream stream = new ByteArrayOutputStream();
             bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
             byte[] byteArray = stream.toByteArray();
             bmp.recycle();
             sql.InserFotoUtilizador(db.connection(this), sql.oneUser().getNomeUtilizador(), byteArray);
         }
         startActivity(new Intent(this, MainActivity.class));
     }

    }

}