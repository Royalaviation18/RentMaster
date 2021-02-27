package com.fab.rent.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.fab.rent.MainActivity;
import com.fab.rent.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class SellerHoActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_ho);


        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_seller);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);



        getSupportFragmentManager().beginTransaction().replace(R.id.seller_frag,new ShomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selected=null;

                    switch (item.getItemId())
                    {
                        case R.id.seller_home:
                            selected=new ShomeFragment();
                            break;

                        case R.id.seller_order_approval:
                            selected=new SorderApFragment();
                            break;

                        case R.id.seller_order_history:
                            selected=new ShistoryFragment();
                            break;

                        case R.id.seller_logout:
                            selected=new SlogFragment();
                            break;

                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.seller_frag,selected).commit();
                    return true;
                }

            };
}