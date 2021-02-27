package com.fab.rent.User;

import android.app.Activity;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.fab.rent.Prevalent.Prevalent;
import com.fab.rent.R;
import com.fab.rent.orderConfirmed;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity implements PaymentResultListener {

    private EditText nameEditText,phoneEditText,addressEditText,cityEditText,emailId,totalamount,securityamt;
    private Button confirmOrderBtn;

    private String sellerid="";
    private String pid="",price="",pname="";
    String TAG = "Payment Error";

    private int finalsecurity;
    private String  totalamt="",stdate,eddate,sdate,edate;

    private TextView smt;

    private String securityAmt="",weeklyprice="",monthlyprice="",userPhone="",daydifference="",sellername="",sellerphone="",saddress="";
    private int x,y,z,v;

    String Name, Phone,Address, City,Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);



        confirmOrderBtn=(Button)findViewById(R.id.confirm_final_order_btn);
        nameEditText=(EditText)findViewById(R.id.shipment_name);
        phoneEditText=(EditText)findViewById(R.id.shipment_phone_no);
        addressEditText=(EditText)findViewById(R.id.shipment_address);
        cityEditText=(EditText)findViewById(R.id.shipment_city);
        emailId=(EditText)findViewById(R.id.shipment_email);
        securityamt=(EditText)findViewById(R.id.et_security_amount);
        totalamount=(EditText)findViewById(R.id.et_total_amount);
        sellerid=getIntent().getStringExtra("sid");
        pid =getIntent().getStringExtra("pid");
        price =getIntent().getStringExtra("price");
        pname =getIntent().getStringExtra("pname");
        securityAmt =getIntent().getStringExtra("securityAmt");
        weeklyprice =getIntent().getStringExtra("weeklyprice");
        monthlyprice =getIntent().getStringExtra("monthlyprice");
        userPhone=getIntent().getStringExtra("phone");
        daydifference=getIntent().getStringExtra("daydifference");
        stdate=getIntent().getStringExtra("startdate");
        eddate=getIntent().getStringExtra("enddate");
        sellername=getIntent().getStringExtra("sname");
        sellerphone=getIntent().getStringExtra("sphone");
        saddress=getIntent().getStringExtra("saddress");






        int daydiff=Integer.parseInt(daydifference);
        String pprice=price.toString();
        int prc=Integer.parseInt(pprice);
        int week=prc*7;
        int weektemp = (int) (week-0.1*week);
        String wk=String.valueOf(weektemp);
        int month=prc*30;
        int monthtemp=(int) (month-0.2*month);
        String mt=String.valueOf(monthtemp);




        if(daydiff<7)
        {
            totalamt=String.valueOf(prc*daydiff);
        }

        if(daydiff<30 && daydiff>=7)
        {
            x=daydiff/7;
            y=daydiff%7;
            totalamt=String.valueOf(x*weektemp+y*prc);
        }

        if(daydiff>=30 && daydiff<365)
        {
            z=daydiff%30 ;      //no.of days left
            y=daydiff/30;        //no. of moths left
            v=z/7;                   //no of weeks left
            if(v==0)
            {
                totalamt=String.valueOf(y*monthtemp+z*prc);
            }
            else{
                x=z%7;
                totalamt=String.valueOf(y*monthtemp+v*weektemp+x*prc);
            }

        }






        phoneEditText.setText(userPhone);
        totalamount.setText(totalamt);
        securityamt.setText(securityAmt);




        finalsecurity=Integer.parseInt(securityAmt)*100;



        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Check();
            }
        });

    }


    private void Check() {

         Name=nameEditText.getText().toString();
         Email=emailId.getText().toString();
         Address=addressEditText.getText().toString();
         City=cityEditText.getText().toString();

        if(Name.isEmpty())
        {
            nameEditText.setError("Please Enter Your Name!");
            nameEditText.requestFocus();
            return;
        }
        if(Email.isEmpty())
        {
            phoneEditText.setError("Please Enter Your Email Id!");
            phoneEditText.requestFocus();
            return;
        }
        if(Address.isEmpty())
        {
            addressEditText.setError("Please Enter Your Address!");
            addressEditText.requestFocus();
            return;
        }
        if(City.isEmpty())
        {
            cityEditText.setError("Please Enter Your City");
            cityEditText.requestFocus();
            return;
        }


        startPayment();
    }


    private void startPayment() {

        Checkout checkout = new Checkout();
        final Activity activity = ConfirmFinalOrderActivity.this;


        try {
            JSONObject options = new JSONObject();

            options.put("name", Name);
            options.put("Test Payment", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            //options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount",finalsecurity);//pass amount in currency subunits
            options.put("prefill.email", Email);
            options.put("prefill.contact",userPhone);
            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }
        @Override
        public void onPaymentSuccess(String s)
        {
            Toast.makeText(this, "Payment successfully done! " +s, Toast.LENGTH_SHORT).show();
            ConfirmOrder(s);

        }

        @Override
        public void onPaymentError(int i, String s) {
            Toast.makeText(this, "Payment Error! " +s, Toast.LENGTH_SHORT).show();


        }




    private void ConfirmOrder(String payId) {


        final String savCurrentDate,savCurrentTime;
        Calendar calfordate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM  dd, yyyy");
        savCurrentDate=currentDate.format(calfordate.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        savCurrentTime=currentTime.format(calfordate.getTime());

        DatabaseReference ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());
        HashMap<String,Object> ordersMap=new HashMap<>();
        //ordersMap.put("totalAmount",totalAmount);
        ordersMap.put("name",nameEditText.getText().toString());
        ordersMap.put("phone",phoneEditText.getText().toString());
        ordersMap.put("address",addressEditText.getText().toString());
        ordersMap.put("city",cityEditText.getText().toString());
        ordersMap.put("date",savCurrentDate);
        ordersMap.put("time",savCurrentTime);
        ordersMap.put("state","not Shipped");
        ordersMap.put("status","not Confirmed");
        ordersMap.put("sid",sellerid);
        ordersMap.put("pid",pid);
        ordersMap.put("pname",pname);
        ordersMap.put("price",price);
        ordersMap.put("paymentid",payId);
        ordersMap.put("paymentstatus","Paid");
        ordersMap.put("securityAmt",securityAmt);
        ordersMap.put("holdingdays",daydifference);
        ordersMap.put("email",Email);
        ordersMap.put("totalamount",totalamt);
        ordersMap.put("startdate",stdate);
        ordersMap.put("enddate",eddate);
        ordersMap.put("sname",sellername);
        ordersMap.put("sphone",sellerphone);
        ordersMap.put("saddress",saddress);


        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference().child("temp")
                            .child("User View").child(Prevalent.currentOnlineUser.getPhone()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {

                                Intent intent=new Intent(ConfirmFinalOrderActivity.this, orderConfirmed.class);
                                startActivity(intent);
                                finish();

                            }
                        }
                    });
                }

            }
        });



    }
}