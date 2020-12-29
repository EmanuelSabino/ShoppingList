package com.example.shoppinglist.ui.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class ConnectionDataBase {
    private SQLiteDatabase db;

    public ConnectionDataBase(){

    }

    public SQLiteDatabase connection(Context ct){

        try{
            return db = ct.openOrCreateDatabase("ShoopingList", Context.MODE_PRIVATE, null);
        }catch (Exception e){
            Toast.makeText(ct.getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
