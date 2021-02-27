package com.fab.rent.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fab.rent.Model.Products;
import com.fab.rent.Prevalent.Prevalent;
import com.fab.rent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class UserViewProductActivity extends AppCompatActivity {


    private Button addToCartButton;
    private ImageView productImage;
    private TextView productprice, productDescription, productName,weekprice,monthprice;
    private String productId = "", state = "Normal",sellerid,sellername,sellerphone,saddress;
    private String s1;
    private String s2,stdate,eddate;


    //date picker
    private DatePicker datePicker;
    private Calendar calendar1, calendar2,dif;
    private TextView dateView, dateView2,datediff;
    private int year, month, day;
    private int year2, month2, day2;
    private String sdate, edate;
    private  String dayDifference;
    private  Date date1,date2,date3,date4;

    //database reference
    DatabaseReference productRef;

    private String securityAmt="",weeklyprice="",monthlyprice="",nextday;
    private int totalamt;
    //    private String totalAmt;
    private int x,y,z,weektemp,monthtemp;
    private int temp;
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_product);

        productId = getIntent().getStringExtra("pid");
        sellerid = getIntent().getStringExtra("sid");
//        sellername=getIntent().getStringExtra("sname");
//        sellerphone=getIntent().getStringExtra("sphone");


        addToCartButton=(Button)findViewById(R.id.p_add_to_cart_btn);

        productImage=(ImageView)findViewById(R.id.p_image_details);
        productprice=(TextView)findViewById(R.id.p_price_details);
        productDescription=(TextView)findViewById(R.id.p_description_details);
        productName=(TextView)findViewById(R.id.p_name_details);
        weekprice=(TextView)findViewById(R.id.p_week_price);
        monthprice=(TextView)findViewById(R.id.p_month_price);
        datediff=(TextView)findViewById(R.id.date_diff);

        productRef=FirebaseDatabase.getInstance().getReference().child("Products");



        dateView = (TextView) findViewById(R.id.tv_start_date);
        dateView2 = (TextView) findViewById(R.id.tv_end_date);


        calendar1 = Calendar.getInstance();
        calendar2 = Calendar.getInstance();
        year = calendar1.get(Calendar.YEAR);

        month = calendar1.get(Calendar.MONTH);
        day = calendar1.get(Calendar.DAY_OF_MONTH);

        year2 = calendar2.get(Calendar.YEAR);

        month2 = calendar2.get(Calendar.MONTH);
        day2 = calendar2.get(Calendar.DAY_OF_MONTH);

        showDate(year, month + 1, day);
        showEndDate(year2, month2 + 1, day2);

        getProductDetails(productId);

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(state.equals("Order Placed") || state.equals("Order Shipped"))
                {
                    Toast.makeText(UserViewProductActivity.this,"You can purchase more items,once your order is shipped/Confirmed",Toast.LENGTH_LONG).show();
                }
                else
                {

                    try {
                        date3=new SimpleDateFormat("dd/MM/yyyy").parse(sdate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        date4=new SimpleDateFormat("dd/MM/yyyy").parse(nextday);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(date3.before(date4))
                    {
                        Toast.makeText(UserViewProductActivity.this, "This order can't be placed prior to today's date ", Toast.LENGTH_SHORT).show();


                    }
                    else
                    {

                        if(dayDifference.equals("0"))
                        {
                            Toast.makeText(UserViewProductActivity.this,"There should be minimum one day gap",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            addToCartList();
                        }

                    }




                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();





        productRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists())
                {
                    securityAmt=snapshot.child("securityAmt").getValue().toString();
                    weeklyprice=snapshot.child("weeklyprice").getValue().toString();
                    monthlyprice=snapshot.child("monthlyprice").getValue().toString();
                    sellername=snapshot.child("sellerName").getValue().toString();
                    sellerphone=snapshot.child("sellerPhone").getValue().toString();
                    saddress=snapshot.child("sellerAddress").getValue().toString();
//
                }
                else
                {

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        CheckOrderState();
    }

    private void addToCartList() {

        String savCurrentDate,savCurrentTime;
        Calendar calfordate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("MMM  dd, yyyy");
        savCurrentDate=currentDate.format(calfordate.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        savCurrentTime=currentDate.format(calfordate.getTime());

        final DatabaseReference cartListRef= FirebaseDatabase.getInstance().getReference().child("temp");

        final HashMap<String,Object> cartMap=new HashMap<>();
        cartMap.put("pid",productId);
        cartMap.put("pname",productName.getText().toString());
        cartMap.put("price",productprice.getText().toString());
        cartMap.put("date",savCurrentDate);
        cartMap.put("time",savCurrentTime);
        cartMap.put("sid",sellerid);






        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone()).child("Products").child(productId)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {

                            cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone()).child("Products").child(productId)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            s1=productName.getText().toString();
                                            s2=productprice.getText().toString();
//                                            temp=Integer.parseInt(s2);








                                            if(task.isSuccessful())
                                            {


                                                Intent intent=new Intent(UserViewProductActivity.this, ConfirmFinalOrderActivity.class);
                                                intent.putExtra("sid",sellerid);
                                                intent.putExtra("pid",productId);
                                                intent.putExtra("pname",s1);
                                                intent.putExtra("price",s2);
                                                intent.putExtra("weeklyprice",weeklyprice);
                                                intent.putExtra("monthlyprice",monthlyprice);
                                                intent.putExtra("securityAmt",securityAmt);
                                                intent.putExtra("phone",Prevalent.currentOnlineUser.getPhone());
                                                intent.putExtra("daydifference",dayDifference);
                                                intent.putExtra("startdate",stdate);
                                                intent.putExtra("enddate",eddate);
                                                intent.putExtra("sname",sellername);
                                                intent.putExtra("sphone",sellerphone);
                                                intent.putExtra("saddress",saddress);


                                                startActivity(intent);
                                            }

                                        }
                                    });
                        }
                    }
                });


    }

    public void datediff(String sdate,String edate) {
        try
        {

            SimpleDateFormat dates = new SimpleDateFormat("dd/MM/yyyy");
            date1 = dates.parse(sdate);
            date2 = dates.parse(edate);

            long d1=date1.getTime()/ (24 * 60 * 60 * 1000);
            long d2=date2.getTime()/ (24 * 60 * 60 * 1000);



            stdate=simpleDateFormat.format(date1).toString();
            eddate=simpleDateFormat.format(date2).toString();

            long difference = date2.getTime() - date1.getTime();
            long differenceDates = difference / (24 * 60 * 60 * 1000);
            dayDifference = Long.toString(differenceDates);

            datediff.setText("The difference between two dates is "+ dayDifference +" days");




        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }




    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(1);

    }

    @SuppressWarnings("deprecation")
    public void setEndingDate(View view)
    {
        showDialog(2);

    }


    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 1)
        {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        if(id==2)
        {
            return  new DatePickerDialog(this,myEndDateListener,year2,month2,day2);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int arg1, int arg2, int arg3)
                {
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private DatePickerDialog.OnDateSetListener myEndDateListener =new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            showEndDate(i,i1+1,i2);
        }
    };

    private void showEndDate(int year2,int month2,int day2)
    {
        dateView2.setText(new StringBuilder().append(day2).append("/").append(month2).append("/").append(year2));
        edate=dateView2.getText().toString();

        String savCurrentDate,savCurrentTime;
        Calendar calfordate=Calendar.getInstance();
        calfordate.add(Calendar.DAY_OF_MONTH,1);
        nextday=simpleDateFormat.format(calfordate.getTime());

        datediff(sdate,edate);

    }





    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
        sdate=dateView.getText().toString();

    }

    private void getProductDetails(String productId)
    {


        DatabaseReference productRef= FirebaseDatabase.getInstance().getReference().child("Products");

        productRef.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists())
                {
                    Products products=snapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    productprice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    weekprice.setText("Week Price: "+ products.getWeeklyprice());
                    monthprice.setText("Month Price: "+ products.getMonthlyprice());
                    Picasso.get().load(products.getImage()).into(productImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void CheckOrderState()
    {
        DatabaseReference ordersRef=FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists())
                {
                    String shippingState=snapshot.child("state").getValue().toString();

                    if(shippingState.equals("shipped"))
                    {
                        state ="Order Shipped";
                    }
                    else if(shippingState.equals("not Shipped"))
                    {
                        state="Order Placed";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}