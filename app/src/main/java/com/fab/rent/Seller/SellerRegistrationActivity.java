package com.fab.rent.Seller;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.fab.rent.MainActivity;
import com.fab.rent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {

    private Button registerButton;
    private EditText nameInput,phoneInput,emailInput,passwordInput,addressInput;
    private FirebaseAuth mAuth;
    ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

        nameInput=findViewById(R.id.seller_name);
        phoneInput=findViewById(R.id.seller_phone);
        emailInput=findViewById(R.id.seller_email);
        passwordInput=findViewById(R.id.seller_password);
        addressInput=findViewById(R.id.seller_address);
        registerButton=findViewById(R.id.seller_register_btn);

        mAuth=FirebaseAuth.getInstance();
        loadingbar=new ProgressDialog(this);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerSeller();
            }
        });

            }

    private void registerSeller()
    {
        String Name=nameInput.getText().toString();
        String Phone=phoneInput.getText().toString();
        String Email=emailInput.getText().toString();
        String Password=passwordInput.getText().toString();
        String Address=addressInput.getText().toString();

        if(Name.isEmpty())
        {
            nameInput.setError("Please Enter the name!");
            nameInput.requestFocus();
            return;
        }
        if(Phone.isEmpty())
        {
            phoneInput.setError("Please Enter the phone no!");
            phoneInput.requestFocus();
            return;
        }
        if(Email.isEmpty())
        {
            emailInput.setError("Please Enter the Email Address!");
            emailInput.requestFocus();
            return;
        }
        if(Password.isEmpty())
        {
            passwordInput.setError("Please Enter the password!");
            passwordInput.requestFocus();
            return;
        }
        if(Address.isEmpty())
        {
            addressInput.setError("Please Enter the Address!");
            addressInput.requestFocus();
            return;
        }
        else
        {
            loadingbar.setTitle("Creating Seller Account");
            loadingbar.setMessage("Please wait,While we are checking the credentials");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful())
                    {
                        final DatabaseReference rootRef;
                        rootRef= FirebaseDatabase.getInstance().getReference();

                        String sid=mAuth.getCurrentUser().getUid();

                        HashMap<String,Object> sellerMap = new HashMap<>();
                        sellerMap.put("sid",sid);
                        sellerMap.put("phone",Phone);
                        sellerMap.put("email",Email);
                        sellerMap.put("address",Address);
                        sellerMap.put("name",Name);

                        rootRef.child("Sellers").child(sid).updateChildren(sellerMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful())
                                {
                                    loadingbar.dismiss();
                                    Toast.makeText(SellerRegistrationActivity.this,"You are now registered Successfully",Toast.LENGTH_SHORT).show();

                                    Intent intent=new Intent(SellerRegistrationActivity.this, SellerHoActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                }
                                else
                                {
                                    loadingbar.dismiss();
                                    Toast.makeText(SellerRegistrationActivity.this,"Something went wrong,Please try again later!",Toast.LENGTH_SHORT).show();

                                    Intent intent=new Intent(SellerRegistrationActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            }
                        });

                    }

                }
            });
        }


    }
}