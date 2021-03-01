package com.fab.rent.Admin;

import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fab.rent.Model.AdminOrders;
import com.fab.rent.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminNewOrdersActivity extends AppCompatActivity {

    private RecyclerView orderList;
    private DatabaseReference ordersRef;
    private String OrderDetailsId,saveCurrentDate,saveCurrentTime;
    private String sID="",pid="",pname="",price="",email="",holdingdays="",paymentid="",securityAmt="",totalAmt="",stdate,eddate,sellername,sellerphone,saddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders");

        orderList=findViewById(R.id.orders_list);
        orderList.setLayoutManager(new LinearLayoutManager(this));



    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>().setQuery(ordersRef.orderByChild("status").equalTo("Confirmed"),AdminOrders.class).build();

        FirebaseRecyclerAdapter<AdminOrders,AdminOrdersViewHolder> adapter=new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrdersViewHolder adminOrdersViewHolder, int i, @NonNull AdminOrders adminOrders) {

                adminOrdersViewHolder.userName.setText("Name : " + adminOrders.getName());
                adminOrdersViewHolder.userPhoneNumber.setText("Phone : " + adminOrders.getPhone());
                adminOrdersViewHolder.userTotalPrice.setText("Total Amount : " + adminOrders.getTotalamount());
                adminOrdersViewHolder.userDateTime.setText("Order At: " + adminOrders.getDate()+ "  "+ adminOrders.getTime());
                adminOrdersViewHolder.userShippingAddress.setText("Shipping Address : " + adminOrders.getAddress()+ ", "+ adminOrders.getCity());

                ordersRef.child(adminOrders.getPhone()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists())
                        {
                            sID=snapshot.child("sid").getValue().toString();
                            pid=snapshot.child("pid").getValue().toString();
                            pname=snapshot.child("pname").getValue().toString();
                            price=snapshot.child("price").getValue().toString();
                            email=snapshot.child("email").getValue().toString();
                            holdingdays=snapshot.child("holdingdays").getValue().toString();
                            paymentid=snapshot.child("paymentid").getValue().toString();
                            securityAmt=snapshot.child("securityAmt").getValue().toString();
                            totalAmt=snapshot.child("totalamount").getValue().toString();
                            stdate=snapshot.child("startdate").getValue().toString();
                            eddate=snapshot.child("enddate").getValue().toString();
                            sellername=snapshot.child("sname").getValue().toString();
                            sellerphone=snapshot.child("sphone").getValue().toString();
                            saddress=snapshot.child("saddress").getValue().toString();



                        }
                        else
                        {

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



                adminOrdersViewHolder.showOrderBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String uId=getRef(i).getKey();

                        Intent intent=new Intent(AdminNewOrdersActivity.this,AdminUserProductsActivity.class);
                        intent.putExtra("uid",uId);
                        startActivity(intent);
                    }
                });
                adminOrdersViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        CharSequence[] options = new CharSequence[]
                                {
                                        "Yes",
                                        "No"
                                };
                        AlertDialog.Builder builder=new AlertDialog.Builder(AdminNewOrdersActivity.this);

                        builder.setTitle("Have you shipped the order's products?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                if(i==0)
                                {
                                    Calendar calendar=Calendar.getInstance();
                                    SimpleDateFormat currentDate=new SimpleDateFormat(" MM dd,yyyy ");
                                    saveCurrentDate=currentDate.format(calendar.getTime());

                                    SimpleDateFormat currentTime=new SimpleDateFormat(" HH:mm:ss a ");
                                    saveCurrentTime=currentTime.format(calendar.getTime());

                                    OrderDetailsId=saveCurrentDate+saveCurrentTime;

                                    DatabaseReference finalref= FirebaseDatabase.getInstance().getReference().child("OrderDetails");

                                    finalref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                        {


                                                HashMap<String,Object> orderDetailsMap=new HashMap<>();
                                                orderDetailsMap.put("name",adminOrders.getName());
                                                orderDetailsMap.put("phone",adminOrders.getPhone());
                                                orderDetailsMap.put("address",adminOrders.getAddress());
                                                orderDetailsMap.put("city",adminOrders.getCity());
                                                orderDetailsMap.put("date",adminOrders.getDate());
                                                orderDetailsMap.put("state", "Shipped");
                                                orderDetailsMap.put("sid",sID);
                                                orderDetailsMap.put("pid",pid);
                                                orderDetailsMap.put("pname",pname);
                                                orderDetailsMap.put("price",price);
                                                orderDetailsMap.put("email",email);
                                                orderDetailsMap.put("holdingdays",holdingdays);
                                                orderDetailsMap.put("paymentid",paymentid);
                                                orderDetailsMap.put("securityAmt",securityAmt);
                                                orderDetailsMap.put("totalamount",totalAmt);
                                                orderDetailsMap.put("startdate",stdate);
                                                orderDetailsMap.put("enddate",eddate);
                                                orderDetailsMap.put("delivery","not started");
                                                orderDetailsMap.put("sellername",sellername);
                                                orderDetailsMap.put("sellerphone",sellerphone);
                                                orderDetailsMap.put("saddress",saddress);
                                                orderDetailsMap.put("orid",OrderDetailsId);

//


                                                finalref.child(OrderDetailsId).updateChildren(orderDetailsMap)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful())
                                                                {
                                                                   Intent intent=new Intent(AdminNewOrdersActivity.this,AdminHomeActivity.class);
                                                                   startActivity(intent);
                                                                }
                                                                else
                                                                {

                                                                    Toast.makeText(AdminNewOrdersActivity.this,"Something went Wrong,Please try again later!",Toast.LENGTH_SHORT).show();
                                                                }

                                                            }
                                                        });

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                    String uID=getRef(i).getKey();
                                    RemoveOrder(uID);
                                }
                                else
                                {
                                    finish();
                                }
                            }
                        });
                        builder.show();

                    }
                });

            }

            @NonNull
            @Override
            public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                return new AdminOrdersViewHolder(view);
            }
        };
        orderList.setAdapter(adapter);
        adapter.startListening();
    }

    public static  class AdminOrdersViewHolder extends  RecyclerView.ViewHolder
    {
        public TextView userName,userPhoneNumber,userTotalPrice,userDateTime,userShippingAddress;
        public Button showOrderBtn;
        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.order_user_name);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userShippingAddress = itemView.findViewById(R.id.order_adress_city);
            showOrderBtn=itemView.findViewById(R.id.show_all_products_btn);

        }
    }
    private void RemoveOrder(String uID)
    {

        ordersRef.child(uID).removeValue();
    }
}
