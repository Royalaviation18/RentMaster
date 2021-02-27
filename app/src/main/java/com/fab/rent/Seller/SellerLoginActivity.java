package com.fab.rent.Seller;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.fab.rent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SellerLoginActivity extends AppCompatActivity {

    private Button sellerLog,sellerReg;
    private EditText emailInput,passwordInput;
    private ProgressDialog loadingbar;
    private FirebaseAuth mAuth;
    private TextView forgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);

        emailInput=findViewById(R.id.seller_login_email);
        passwordInput=findViewById(R.id.seller_login_password);
        sellerReg=findViewById(R.id.seller_newAccount);

        sellerLog=findViewById(R.id.seller_login_btn);
        forgotPass=findViewById(R.id.seller_forgot);
        mAuth=FirebaseAuth.getInstance();
        loadingbar=new ProgressDialog(this);


        sellerLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginSeller();
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(SellerLoginActivity.this, SellerResetPassword.class);
                startActivity(intent);
            }
        });

        sellerReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SellerLoginActivity.this,SellerRegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginSeller()
    {
        final String Email=emailInput.getText().toString();
        final String Password=passwordInput.getText().toString();

        if(Email.isEmpty())
        {
            emailInput.setError("Please Enter Your Registered Email ID!");
            emailInput.requestFocus();
            return;
        }
        if(Password.isEmpty())
        {
            passwordInput.setError("Please Enter Your Password!");
            passwordInput.requestFocus();
            return;
        }
        else
        {
            loadingbar.setTitle("Seller Account Login!");
            loadingbar.setMessage("Please wait,While we are checking the credentials");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        loadingbar.dismiss();
                        Intent intent = new Intent(SellerLoginActivity.this, SellerHoActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else
                    {
                        loadingbar.dismiss();
                        Toast.makeText(SellerLoginActivity.this, "Something went wrong,Please try again late!", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }


    }
}