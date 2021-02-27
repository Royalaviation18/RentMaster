package com.fab.rent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.fab.rent.User.ConfirmFinalOrderActivity;
import com.fab.rent.User.HomeFragment;
import com.fab.rent.User.UserHome;

public class orderConfirmed extends AppCompatActivity {
    LottieAnimationView lottieAnimationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmed);

        lottieAnimationView = findViewById(R.id.confirmed);

        Thread td=new Thread()
        {
            public void run()
            {
                try {
                    sleep(3000);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally {
//
                    Intent intent=new Intent(orderConfirmed.this,UserHome.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); //user can't go back to the this page until he has something in cart
                    startActivity(intent);
                }
            }
        };td.start();
    }


}