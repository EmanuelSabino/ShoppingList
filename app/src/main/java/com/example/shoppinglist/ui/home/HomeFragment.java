package com.example.shoppinglist.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.example.shoppinglist.MainActivity;
import com.example.shoppinglist.R;
import com.example.shoppinglist.ui.sql.ConnectionDataBase;
import com.example.shoppinglist.ui.sql.DescricaoUtilizador;
import com.example.shoppinglist.ui.sql.sqlShoppingList;
import com.example.shoppinglist.welcome01layout;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private sqlShoppingList sql;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        sql = new sqlShoppingList(getActivity());

        if(sql.oneUser() == null){
            startActivity(new Intent(getActivity(), welcome01layout.class));
        }
        else {
            DescricaoUtilizador user = sql.oneUser();
            TextView name = root.findViewById(R.id.nomeUtilizador);
            TextView numberProducts = root.findViewById(R.id.numberProducts);
            TextView numberCategories = root.findViewById(R.id.numberCategories);
            TextView numberOfUrgentList = root.findViewById(R.id.numberUrgentProducts);
            name.setText(user.getNomeUtilizador());
            numberProducts.setText(String.valueOf(user.getNumeroProdutos() + user.getNumeroProdutosUrgentes()));
            numberCategories.setText(String.valueOf(user.getNumeroCategorias()));
            numberOfUrgentList.setText(String.valueOf(user.getNumeroProdutosUrgentes()));

            ImageView image = root.findViewById(R.id.imageViewPerfil);
            ConnectionDataBase db = new ConnectionDataBase();

            if(sql.fotPerfil() != null){
                image.setImageBitmap(sql.fotPerfil());
            }
            return root;
        }

        return root;
    }
}