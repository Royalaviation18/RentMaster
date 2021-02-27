package com.fab.rent.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fab.rent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class SellerResetPassword extends AppCompatActivity {

    private EditText resetEmail;
    private Button resetBtn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_reset_password);

        resetEmail=findViewById(R.id.et_email);
        resetBtn=findViewById(R.id.reset_button);

        mAuth=FirebaseAuth.getInstance();

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetEmailfun();
            }
        });
    }

    private void resetEmailfun()
    {
        String email=resetEmail.getText().toString();

        if(email.isEmpty())
        {
            resetEmail.setError("Please Enter Your Registered Email");
            resetEmail.requestFocus();
            return;
        }

        else
        {
               mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                          if(task.isSuccessful())
                          {
                              Toast.makeText(SellerResetPassword.this,"Check Your Email",Toast.LENGTH_LONG).show();
                              Intent intent=new Intent(SellerResetPassword.this, SellerLoginActivity.class);
                              startActivity(intent);
                          }
                          else
                          {
                              Toast.makeText(SellerResetPassword.this,"Something went wrong,Please Try again later!",Toast.LENGTH_LONG).show();
                              Intent intent=new Intent(SellerResetPassword.this,SellerLoginActivity.class);
                              startActivity(intent);
                          }
                   }
               });
        }
    }
}