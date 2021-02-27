package com.fab.rent.Seller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.fab.rent.R;

import java.util.ArrayList;
import java.util.List;

public class SellerCategoryActivity extends Activity  implements AdapterView.OnItemSelectedListener {

    Button category_btn;
    Spinner s1,s2;
    int securityAmt;
    TextView Amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_category);
        s1 = (Spinner)findViewById(R.id.spinner1);
        s2 = (Spinner)findViewById(R.id.spinner2);
        s1.setOnItemSelectedListener(this);
        category_btn=(Button)findViewById(R.id.category_btn);
        Amount=findViewById(R.id.security_amt);

        category_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sp1= String.valueOf(s1.getSelectedItem());
                String sp2=String.valueOf(s2.getSelectedItem());

                if(sp2.equals("Novels"))
                {
                          securityAmt=300;
                }
                if(sp2.equals("Comics"))
                {
                          securityAmt=150;
                }
                if(sp2.equals("Manga"))
                {
                          securityAmt=500;
                }
                if(sp2.equals("Others"))
                {
                    securityAmt=400;
                }
                if(sp2.equals("Mouse"))
                {
                    securityAmt=600;
                }
                if(sp2.equals("Keyboard"))
                {
                    securityAmt=700;
                }
                if(sp2.equals("Headphones"))
                {
                    securityAmt=1000;
                }
                if(sp2.equals("Utensils"))
                {
                    securityAmt=500;
                }
                if(sp2.equals("Cutlery"))
                {
                    securityAmt=100;
                }
                if(sp2.equals("Crockery"))
                {
                    securityAmt=250;
                }
                if(sp2.equals("Cleaning Equipment"))
                {
                    securityAmt=300;
                }
                if(sp2.equals("Printers"))
                {
                    securityAmt=1000;
                }
                if(sp2.equals("Stationary"))
                {
                    securityAmt=50;
                }
                if(sp2.equals("Sports"))
                {
                    securityAmt=350;
                }
                if(sp2.equals("Fitness"))
                {
                    securityAmt=900;
                }
                if(sp2.equals("Outdoor Recreation"))
                {
                    securityAmt=300;
                }
                if(sp2.equals("Table"))
                {
                    securityAmt=500;
                }
                if(sp2.equals("Chair"))
                {
                    securityAmt=350;
                }

               // Amount.setText(securityAmt);

                Intent intent=new Intent(SellerCategoryActivity.this,SellerAddNewProductActivity.class);
                intent.putExtra("Category",sp1);
                intent.putExtra("SubCategory",sp2);
                intent.putExtra("securityAmt",securityAmt);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String sp1= String.valueOf(s1.getSelectedItem());


        if(sp1.contentEquals("Books")) {
            List<String> list = new ArrayList<String>();
            list.add("Novels");
            list.add("Comics");
            list.add("Manga");
            list.add("Others");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter.notifyDataSetChanged();
            s2.setAdapter(dataAdapter);

        }

        if(sp1.contentEquals("Computer and Peripherals")) {
            List<String> list = new ArrayList<String>();
            list.add("Mouse");
            list.add("Keyboard");
            list.add("Headphones");
            list.add("Others");
            ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter2.notifyDataSetChanged();
            s2.setAdapter(dataAdapter2);
        }

        if(sp1.contentEquals("Home and Kitchen")) {
            List<String> list = new ArrayList<String>();
            list.add("Utensils");
            list.add("Cutlery");
            list.add("Crockery");
            list.add("Cleaning Equipment");
            list.add("Others");
            ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter3.notifyDataSetChanged();
            s2.setAdapter(dataAdapter3);
        }

        if(sp1.contentEquals("Office Supplies")) {
            List<String> list = new ArrayList<String>();
            list.add("Printers");
            list.add("Stationary");
            list.add("Others");
            ArrayAdapter<String> dataAdapter4 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter4.notifyDataSetChanged();
            s2.setAdapter(dataAdapter4);
        }

        if(sp1.contentEquals("Sports and Outdoors")) {
            List<String> list = new ArrayList<String>();
            list.add("Sports");
            list.add("Fitness");
            list.add("Outdoor Recreation");
            list.add("Others");
            ArrayAdapter<String> dataAdapter5 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter5.notifyDataSetChanged();
            s2.setAdapter(dataAdapter5);
        }

        if(sp1.contentEquals("Furniture")) {
            List<String> list = new ArrayList<String>();
            list.add("Table");
            list.add("Chair");
            list.add("Others");
            ArrayAdapter<String> dataAdapter6 = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dataAdapter6.notifyDataSetChanged();
            s2.setAdapter(dataAdapter6);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}