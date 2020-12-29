package com.example.shoppinglist.ui.FinalPrice;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.shoppinglist.MainActivity;
import com.example.shoppinglist.R;
import com.example.shoppinglist.ui.adapter.AdapterPersEditUrgentProducts;
import com.example.shoppinglist.ui.adapter.AdapterPersProdutosListaFinal;
import com.example.shoppinglist.ui.sql.ConnectionDataBase;
import com.example.shoppinglist.ui.sql.ListaFinal;
import com.example.shoppinglist.ui.sql.Produto;
import com.example.shoppinglist.ui.sql.sqlShoppingList;

import java.util.ArrayList;
import java.util.List;

public class FinalPrice extends Fragment {

    private FinalPriceViewModel mViewModel;
    private sqlShoppingList sql;
    private ConnectionDataBase db;
    private AdapterPersProdutosListaFinal adapter;
    private ListView lv;
    private Button bt;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private int idButton;
    private TextView text;
    private List<ListaFinal> finallist;
    private TextView result;
    private double resulNumProd = 0;

    public static FinalPrice newInstance() {
        return new FinalPrice();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View v =  inflater.inflate(R.layout.final_price_fragment, container, false);

        text = v.findViewById(R.id.editTextTextPersonNameFinalList);
        bt = v.findViewById(R.id.finalize);
        lv = v.findViewById(R.id.Finallistview_fragmentfinallist);
        sql = new sqlShoppingList(getActivity());
        db = new ConnectionDataBase();
        adapter = new AdapterPersProdutosListaFinal(sql.allfinallist(), getActivity());
        lv.setAdapter(adapter);
        result = v.findViewById(R.id.textView7result);
        finallist = sql.allfinallist();


        radioGroup = v.findViewById(R.id.radioGroupEditAllFinalProducts);
        idButton = radioGroup.getCheckedRadioButtonId();
        radioButton = v.findViewById(idButton);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                idButton = radioGroup.getCheckedRadioButtonId();
                radioButton = v.findViewById(idButton);

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

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<ListaFinal> finallist = new ArrayList<>(sql.allfinallist());

                for (int i = 0; i< finallist.size(); i++){
                    List<Produto> produtos = new ArrayList<>(sql.allProdutos());
                    for(int y =0; y<produtos.size(); y++){
                        if(produtos.get(y).getNome().equals(finallist.get(i).getNomeProduto())){
                            sql.delProduct(db.connection(getActivity()), produtos.get(y).getNome());
                            sql.UpdateDescriptionUser(db.connection(getActivity()), true,false,false,sql.oneUser().getNomeUtilizador(), (sql.oneUser().getNumeroProdutos()-2));
                            break;
                        }
                    }
                }

                sql.delAllListFinal(db.connection(getActivity()));
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        resulNumProd = 0;
        finallist = sql.allfinallist();
        for(int z = 0; z < finallist.size(); z++){
            resulNumProd+= finallist.get(z).getPrecoProduto();
        }
        result.setText(String.valueOf(resulNumProd));

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FinalPriceViewModel.class);
        // TODO: Use the ViewModel
    }

    public void pesqChecked(String text){
        if(radioButton.getText().equals("A to Z")){
            adapter = new AdapterPersProdutosListaFinal(sql.OrderAndSearchOrderByProductsFinal(text, true, false,false, false), getActivity());
            lv.setAdapter(adapter);
        }else if(radioButton.getText().equals("Z to A")){
            adapter = new AdapterPersProdutosListaFinal(sql.OrderAndSearchOrderByProductsFinal(text, false, true,false, false), getActivity());
            lv.setAdapter(adapter);
        }else if(radioButton.getText().equals("€ Asc")){
            adapter = new AdapterPersProdutosListaFinal(sql.OrderAndSearchOrderByProductsFinal(text, false, false,true, false), getActivity());
            lv.setAdapter(adapter);
        }else if(radioButton.getText().equals("€ Desc")){
            adapter = new AdapterPersProdutosListaFinal(sql.OrderAndSearchOrderByProductsFinal(text, false, false,false, true),getActivity());
            lv.setAdapter(adapter);
        }else {
            adapter = new AdapterPersProdutosListaFinal(sql.OrderAndSearchOrderByProductsFinal(text, false, false,false, false), getActivity());
            lv.setAdapter(adapter);
        }
    }

}