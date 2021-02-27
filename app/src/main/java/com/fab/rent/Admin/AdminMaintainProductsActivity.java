package com.fab.rent.Admin;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.fab.rent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {

    private Button applyChanges,deleteBtn;
    private EditText name,price,description;
    private ImageView imageView;

    private String productId="";
    private DatabaseReference productRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        productId=getIntent().getStringExtra("pid");
        productRef= FirebaseDatabase.getInstance().getReference().child("Products").child(productId);


        applyChanges=findViewById(R.id.apply_btn);
        deleteBtn=findViewById(R.id.delete_product_btn);
        name=findViewById(R.id.product_name_maintain);
        price=findViewById(R.id.product_price_maintain);
        description=findViewById(R.id.product_description_maintain);
        imageView=findViewById(R.id.product_image_maintain);


        displaySpecificProductInfo();


        applyChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                applyChanges();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteThisProduct();
            }
        });

    }

    private void deleteThisProduct()
    {
        productRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminHomeActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(AdminMaintainProductsActivity.this,"The Product has been Deleted Successfully",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void applyChanges() {

        String pName= name.getText().toString();
        String pPrice=price.getText().toString();
        String pDescription= description.getText().toString();

        if(pName.isEmpty())
        {
            name.setError("Please enter the name of the product!");
            name.requestFocus();
            return;
        }
        if(pPrice.isEmpty())
        {
            price.setError("Please enter the price of the product!");
            price.requestFocus();
            return;
        }
        if(pDescription.isEmpty())
        {
            description.setError("Please enter the description for the product!");
        }
        else
        {
            HashMap<String,Object> productMap=new HashMap<>();
            productMap.put("pid",productId);
            productMap.put("description",pDescription);
            productMap.put("price",pPrice);
            productMap.put("pname",pName);

            productRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful())
                    {
                        Toast.makeText(AdminMaintainProductsActivity.this,"Changes have been applied Successfully",Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(AdminMaintainProductsActivity.this, AdminHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }
            });
        }

    }

    private void displaySpecificProductInfo() {

        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    String pname=snapshot.child("pname").getValue().toString();
                    String pprice=snapshot.child("price").getValue().toString();
                    String pdescription=snapshot.child("description").getValue().toString();
                    String pimage=snapshot.child("image").getValue().toString();

                    name.setText(pname);
                    price.setText(pprice);
                    description.setText(pdescription);
                    Picasso.get().load(pimage).into(imageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


}