package com.fab.rent.DeliveryBoy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fab.rent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DeliveryLoginActivity extends AppCompatActivity {

    private Button dbLog,dbReg;
    private EditText emailInput,passwordInput;
    private ProgressDialog loadingbar;
    private FirebaseAuth mAuth;
    private TextView forgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_login);

        emailInput=findViewById(R.id.db_login_email);
        passwordInput=findViewById(R.id.db_login_password);
        dbLog=findViewById(R.id.db_login_btn);
        forgotPass=findViewById(R.id.db_forgot);
        dbReg=findViewById(R.id.db_register);
        mAuth=FirebaseAuth.getInstance();
        loadingbar=new ProgressDialog(this);


        dbLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                logindb();
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(DeliveryLoginActivity.this, DeliveryResetPasswordActivity.class);
                startActivity(intent);
            }
        });

        dbReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DeliveryLoginActivity.this,DeliveryRegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void logindb()
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
            loadingbar.setTitle("Delivery Boy Account Login!");
            loadingbar.setMessage("Please wait,While we are checking the credentials");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        loadingbar.dismiss();
                        Intent intent = new Intent(DeliveryLoginActivity.this, DeliveryHomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else
                    {
                        loadingbar.dismiss();
                        Toast.makeText(DeliveryLoginActivity.this, "Something went wrong,Please try again late!", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

    }
}