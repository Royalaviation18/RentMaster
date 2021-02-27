package com.fab.rent.DeliveryBoy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

public class DeliveryResetPasswordActivity extends AppCompatActivity {

    private EditText resetEmail;
    private Button resetBtn;
    FirebaseAuth mAuth;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_reset_password);

        resetEmail=findViewById(R.id.et_dbemail);
        resetBtn=findViewById(R.id.reset_dbbutton);
        loadingbar=new ProgressDialog(this);

        mAuth=FirebaseAuth.getInstance();

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPasswordfun();
            }
        });
    }

    private void resetPasswordfun()
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
            loadingbar.setTitle("Password Reset");
            loadingbar.setMessage("Please wait,While we are looking for your mail ");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        loadingbar.dismiss();
                        Toast.makeText(DeliveryResetPasswordActivity.this,"Password Reset link has been Sent to Your Mail!",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(DeliveryResetPasswordActivity.this, DeliveryLoginActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        loadingbar.dismiss();
                        Toast.makeText(DeliveryResetPasswordActivity.this,"Something went wrong,Please Try again later!",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(DeliveryResetPasswordActivity.this,DeliveryLoginActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }
}