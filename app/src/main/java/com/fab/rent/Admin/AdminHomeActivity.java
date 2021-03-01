package com.fab.rent.Admin;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fab.rent.MainActivity;
import com.fab.rent.R;
import com.fab.rent.mainHomeAct;

public class AdminHomeActivity extends AppCompatActivity {

    private Button logOutBtn,checkOrderBtn,maintainProductsBtn,checkApproveProductsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);


        logOutBtn=findViewById(R.id.admin_logout_btn);
        checkOrderBtn=findViewById(R.id.check_orders_btn);
        maintainProductsBtn=findViewById(R.id.maintain_btn);
        checkApproveProductsBtn=findViewById(R.id.check_approve_products_btn);




        maintainProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(AdminHomeActivity.this, mainHomeAct.class);
                intent.putExtra("Admin","Admin");
                startActivity(intent);

            }
        });

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AdminHomeActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        checkOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(AdminHomeActivity.this,AdminNewOrdersActivity.class);
                startActivity(intent);


            }
        });
        checkApproveProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(AdminHomeActivity.this, CheckNewProductsActivity.class);
                startActivity(intent);


            }
        });
    }


}