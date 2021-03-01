package com.fab.rent.DeliveryBoy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fab.rent.MainActivity;
import com.fab.rent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DeliveryRegistrationActivity extends AppCompatActivity {
    private Button registerButton;
    private EditText nameInput,phoneInput,emailInput,passwordInput;
    private FirebaseAuth mAuth;
    ProgressDialog loadingbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_registration);



        nameInput=findViewById(R.id.db_name);
        phoneInput=findViewById(R.id.db_phone);
        emailInput=findViewById(R.id.db_email);
        passwordInput=findViewById(R.id.db_password);
        registerButton=findViewById(R.id.db_register_btn);

        mAuth=FirebaseAuth.getInstance();
        loadingbar=new ProgressDialog(this);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registerDeliveryboy();
            }
        });

    }

    private void registerDeliveryboy()
    {
        String Name=nameInput.getText().toString();
        String Phone=phoneInput.getText().toString();
        String Email=emailInput.getText().toString();
        String Password=passwordInput.getText().toString();


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
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches())
        {
            emailInput.setError("Please Provide a Valid Email");
            emailInput.requestFocus();
            return;
        }
        if(Password.isEmpty())
        {
            passwordInput.setError("Please Enter the password!");
            passwordInput.requestFocus();
            return;
        }
        if(Password.length()<6)
        {
            passwordInput.setError("Password should be of minimum 6 characters");
            passwordInput.requestFocus();
            return;
        }
        else
        {
            loadingbar.setTitle("Creating Your Account");
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

                        String dbid=Phone;

                        HashMap<String,Object> dbMap = new HashMap<>();
                        dbMap.put("dbid",dbid);
                        dbMap.put("phone",Phone);
                        dbMap.put("email",Email);
                        dbMap.put("name",Name);

                        rootRef.child("Delivery Boys").child(dbid).updateChildren(dbMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful())
                                {
                                    loadingbar.dismiss();
                                    Toast.makeText(DeliveryRegistrationActivity.this,"You are now registered Successfully",Toast.LENGTH_SHORT).show();

                                    Intent intent=new Intent(DeliveryRegistrationActivity.this, DeliveryLoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                }
                                else
                                {
                                    loadingbar.dismiss();
                                    Toast.makeText(DeliveryRegistrationActivity.this,"Something went wrong,Please try again later!",Toast.LENGTH_SHORT).show();

                                    Intent intent=new Intent(DeliveryRegistrationActivity.this, MainActivity.class);
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