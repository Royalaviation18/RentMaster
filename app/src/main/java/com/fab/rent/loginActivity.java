package com.fab.rent;

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

import com.fab.rent.Admin.AdminHomeActivity;
import com.fab.rent.Model.Users;
import com.fab.rent.Prevalent.Prevalent;
import com.fab.rent.User.ResetPasswordActivity;
import com.fab.rent.User.UserHome;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class loginActivity extends AppCompatActivity
{


    private EditText InputNumber,InputPassword;
    private Button LoginButton;
    private ProgressDialog Loading;
    private TextView AdminLink,NotAdminLink,forgotPassword;
    private String parentDbName= "Users";
    private CheckBox chkBoxRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        InputNumber=(EditText)findViewById(R.id.login_phone_number);
        InputPassword=(EditText)findViewById(R.id.login_password_input);
        LoginButton=(Button)findViewById(R.id.login_btn);
        AdminLink=(TextView) findViewById(R.id.admin_panel_link);
        NotAdminLink=(TextView)findViewById(R.id.not_admin_panel);
        Loading =new ProgressDialog(this);
        chkBoxRememberMe=(CheckBox)findViewById(R.id.remember_me_chk);
        forgotPassword=(TextView)findViewById(R.id.forgot_password);

        Paper.init(this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginUser();
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(loginActivity.this, ResetPasswordActivity.class);
                intent.putExtra("check","login");
                startActivity(intent);
            }
        });

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginButton.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName="Admins";
            }
        });

        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginButton.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName="Users";

            }
        });
    }

    private void LoginUser() {
        String phone=InputNumber.getText().toString();
        String password=InputPassword.getText().toString();

        if(phone.isEmpty())
        {
            InputNumber.setError("Please enter the registered phone no!");
            InputNumber.requestFocus();
        }
        if(password.isEmpty())
        {
            InputPassword.setError("Please enter your password!");
            InputPassword.requestFocus();
        }
        else
        {
            Loading.setTitle("Login Account");
            Loading.setMessage("Please wait,while we are checking the login credentials");
            Loading.setCanceledOnTouchOutside(false);
            Loading.show();


            AllowAccessToAccount(phone,password);
        }
    }

    private void AllowAccessToAccount(final String phone,final String password) {


        if(chkBoxRememberMe.isChecked())
        {
            Paper.book().write(Prevalent.UserPhoneKey,phone);
            Paper.book().write(Prevalent.UserPasswordKey,password);
        }

        final DatabaseReference Rooted;
        Rooted= FirebaseDatabase.getInstance().getReference();

        Rooted.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child(parentDbName).child(phone).exists())
                {
                    Users usersData=snapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if(usersData.getPhone().equals(phone))
                    {
                        if(usersData.getPassword().equals(password))
                        {
                            if(parentDbName.equals("Admins"))
                            {
                                Toast.makeText(loginActivity.this, "Entering Admin Panel!", Toast.LENGTH_SHORT).show();
                                Loading.dismiss();


                                Intent intent=new Intent(loginActivity.this, AdminHomeActivity.class);
                                startActivity(intent);
                            }
                            else if(parentDbName.equals("Users"))
                            {
                                Toast.makeText(loginActivity.this, "Logged In Successfully!", Toast.LENGTH_SHORT).show();
                                Loading.dismiss();


                                Intent intent=new Intent(loginActivity.this, UserHome.class);
                                Prevalent.currentOnlineUser=usersData;
                                startActivity(intent);
                            }

                        }
                        else
                        {
                            Loading.dismiss();
                            Toast.makeText(loginActivity.this, "Password is Incorrect!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    Loading.dismiss();
                    Toast.makeText(loginActivity.this,"Account with" +phone+ "number does not exists!",Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}