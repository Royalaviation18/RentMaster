package com.fab.rent;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class registerActivity extends AppCompatActivity {

    private Button Createaccountbutton;
    private EditText Inputname,Inputphoneno,Inputemail,InputAadhar,Inputpassword,InputConfirm;
    ProgressDialog loadingbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        Createaccountbutton=(Button)findViewById(R.id.register_btn);
        Inputname=(EditText)findViewById(R.id.register_username_input);
        Inputphoneno=(EditText)findViewById(R.id.register_phone_number);
        Inputemail=(EditText)findViewById(R.id.register_email);
        InputAadhar=(EditText)findViewById(R.id.register_adhaar_input);
        Inputpassword=(EditText)findViewById(R.id.register_password_input);
        InputConfirm=(EditText)findViewById(R.id.register_confirm_password);
        loadingbar=new ProgressDialog(this);

        Createaccountbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Createaccount();
            }
        });

    }

    private  void Createaccount()
    {
        String name=Inputname.getText().toString();
        String phone=Inputphoneno.getText().toString();
        String email=Inputemail.getText().toString();
        String aadhar=InputAadhar.getText().toString();
        String password=Inputpassword.getText().toString();
        String cpassword=InputConfirm.getText().toString();

        if(name.isEmpty())
        {
            Inputname.setError("Please Enter your Name!");
            Inputname.requestFocus();
            return;
        }

        if(phone.isEmpty())
        {
            Inputphoneno.setError("Please Enter your Phone no!");
            Inputphoneno.requestFocus();
            return;
        }
        if(phone.length()!=10)
        {
            Inputphoneno.setError("Phone no should be only of 10 digits");
            Inputphoneno.requestFocus();
            return;
        }
        if(email.isEmpty())
        {
            Inputemail.setError("Please Enter Your Email Id");
            Inputemail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Inputemail.setError("Please Provide a Valid Email Id");
            Inputemail.requestFocus();
            return;
        }
        if(aadhar.isEmpty())
        {
            InputAadhar.setError("Please Provide your Aadhar no!");
            InputAadhar.requestFocus();
            return;
        }
        if(aadhar.length()!=12)
        {
            InputAadhar.setError("Aadhar no consits of only 12 digits,Please check you aadhar no");
            InputAadhar.requestFocus();
            return;
        }

        if(password.isEmpty())
        {
            Inputpassword.setError("Please Enter a password!");
            Inputphoneno.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            Inputpassword.setError("Password should be of minimum 6 characters");
            Inputpassword.requestFocus();
            return;
        }
        if(cpassword.isEmpty())
        {
            InputConfirm.setError("Please Confirm Your password");
            InputConfirm.requestFocus();
            return;
        }
        if(!cpassword.equals(password))
        {
            InputConfirm.setError("Passwords do not match");
            InputConfirm.requestFocus();
            return;
        }
        else
        {
            loadingbar.setTitle("Create Account");
            loadingbar.setMessage("Please wait,While we are checking the credentials");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();

            ValidationNumber(name,phone,password,email,aadhar);
        }
    }

    private void ValidationNumber(final String name, final String phone, final String password,final String email,final String aadhar)
    {

        final DatabaseReference Rooted;
        Rooted= FirebaseDatabase.getInstance().getReference();

        Rooted.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(!(dataSnapshot.child("Users").child(phone).exists()))
                {
                    HashMap<String,Object> userdataMap=new HashMap<>();
                    userdataMap.put("phone",phone);
                    userdataMap.put("password",password);
                    userdataMap.put("name",name);
                    userdataMap.put("email",email);
                    userdataMap.put("aadhar",aadhar);

                    Rooted.child("Users").child(phone).updateChildren(userdataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(registerActivity.this,"Congratulations,Your Account has been created",Toast.LENGTH_SHORT).show();
                                        loadingbar.dismiss();
                                        Intent intent=new Intent(registerActivity.this,loginActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        loadingbar.dismiss();
                                        Toast.makeText(registerActivity.this,"Something went Wrong,Please try again later!",Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }
                else
                {
                    Toast.makeText(registerActivity.this,"This" + phone+"already exists!",Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                    Toast.makeText(registerActivity.this,"Please try again later using another phone number!",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(registerActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}