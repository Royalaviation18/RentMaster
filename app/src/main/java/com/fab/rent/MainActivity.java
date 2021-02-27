package com.fab.rent;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.fab.rent.DeliveryBoy.DeliveryLoginActivity;
import com.fab.rent.DeliveryBoy.DeliveryRegistrationActivity;
import com.fab.rent.Model.Users;
import com.fab.rent.Prevalent.Prevalent;

import com.fab.rent.Seller.SellerLoginActivity;
import com.fab.rent.Seller.SellerHoActivity;
import com.fab.rent.User.UserHome;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private Button join,login;
    private ProgressDialog Loading;
    private TextView sellerBegin,dbBegin;
    private String type ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        join=(Button)findViewById(R.id.main_join_now_btn);
        login=(Button)findViewById(R.id.main_login_btn);
        sellerBegin=findViewById(R.id.seller_begin);
        dbBegin=findViewById(R.id.db_begin);

        Loading=new ProgressDialog(this);
        Paper.init(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,loginActivity.class);
                startActivity(intent);
            }
        });


        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(MainActivity.this,registerActivity.class);
                startActivity(intent);
            }
        });

        sellerBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this, SellerLoginActivity.class);
                startActivity(intent);
            }
        });

        dbBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, DeliveryLoginActivity.class);
                startActivity(intent);
            }
        });

        String UserPhoneKey=Paper.book().read(Prevalent.UserPhoneKey);
        String UserPasswordKey=Paper.book().read(Prevalent.UserPasswordKey);

        if(UserPhoneKey!="" && UserPasswordKey!="" )
        {
            if(!TextUtils.isEmpty(UserPhoneKey)  && !TextUtils.isEmpty(UserPasswordKey))
            {
                AllowAccess(UserPhoneKey,UserPasswordKey);
                Loading.setTitle("Already Logged In");
                Loading.setMessage("Please wait,while we are redirecting you to the home page");
                Loading.setCanceledOnTouchOutside(false);
                Loading.show();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser!=null)
        {
            Intent intent=new Intent(MainActivity.this, SellerHoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
    }

    private void AllowAccess(final String phone, final String password) {

        final DatabaseReference Rooted;
        Rooted= FirebaseDatabase.getInstance().getReference();

        Rooted.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child("Users").child(phone).exists())
                {
                    Users usersData=snapshot.child("Users").child(phone).getValue(Users.class);

                    if(usersData.getPhone().equals(phone))
                    {
                        if(usersData.getPassword().equals(password))
                        {
                            Toast.makeText(MainActivity.this, "Logged In Successfully!", Toast.LENGTH_SHORT).show();
                            Loading.dismiss();


                            Intent intent=new Intent(MainActivity.this, UserHome.class);
                            Prevalent.currentOnlineUser=usersData;
                            startActivity(intent);
                        }
                    }
                }
                else
                {
                    Loading.dismiss();
                    Toast.makeText(MainActivity.this,"Account with" +phone+ "number does not exists!",Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}