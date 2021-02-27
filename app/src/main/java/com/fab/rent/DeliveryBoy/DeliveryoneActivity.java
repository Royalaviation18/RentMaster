package com.fab.rent.DeliveryBoy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fab.rent.Model.AdminOrders;
import com.fab.rent.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DeliveryoneActivity extends AppCompatActivity {

    private RecyclerView orderhistory;
    private DatabaseReference deliveryRef;
    private String nextday;
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
    private  String savCurrentDate,savCurrentTime,orderId;
    private Date date1,date2;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliveryone);


        deliveryRef = FirebaseDatabase.getInstance().getReference().child("OrderDetails");

        orderhistory = findViewById(R.id.deliveryOne_phase);
        orderhistory.setLayoutManager(new LinearLayoutManager(this));




        Calendar calfordate=Calendar.getInstance();
        savCurrentDate=simpleDateFormat.format(calfordate.getTime());
        calfordate.add(Calendar.DAY_OF_MONTH,2);
        nextday=simpleDateFormat.format(calfordate.getTime());




    }
    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>().setQuery(deliveryRef.orderByChild("delivery").equalTo("not started"),AdminOrders.class).build();
        FirebaseRecyclerAdapter<AdminOrders, DeliveryoneActivity.AdminOrdersViewHolder> adapter= new FirebaseRecyclerAdapter<AdminOrders, DeliveryoneActivity.AdminOrdersViewHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull DeliveryoneActivity.AdminOrdersViewHolder adminOrdersViewHolder, int i, @NonNull AdminOrders adminOrders) {




                try {
                    date1=new SimpleDateFormat("dd/MM/yyyy").parse(nextday);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    date2=new SimpleDateFormat("dd/MM/yyyy").parse(adminOrders.getStartdate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (date2.before(date1)) {


                    adminOrdersViewHolder.userName.setText(adminOrders.getName());
                    adminOrdersViewHolder.userPhoneNumber.setText(adminOrders.getPhone());
                    adminOrdersViewHolder.userTotalPrice.setText(adminOrders.getTotalamount());
                    adminOrdersViewHolder.date.setText(adminOrders.getStartdate());
                    adminOrdersViewHolder.location.setText(adminOrders.getAddress());
                    adminOrdersViewHolder.sellerName.setText(adminOrders.getSellername());
                    adminOrdersViewHolder.sellerNumber.setText(adminOrders.getSellerphone());
                    adminOrdersViewHolder.sellerAddress.setText(adminOrders.getSaddress());


                }
                else
                    {
                    adminOrdersViewHolder.itemView.setVisibility(View.INVISIBLE);
                    }

                adminOrdersViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        final String orderID = adminOrders.getOrid();
                        CharSequence[] options = new CharSequence[]
                                {
                                        "Yes",
                                        "No"
                                };
                        AlertDialog.Builder builder=new AlertDialog.Builder(DeliveryoneActivity.this);

                        builder.setTitle("Have you delivered this order");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                if(i==0)
                                {
                                         Intent intent=new Intent(DeliveryoneActivity.this,DelliveredItemActivity.class);
                                         intent.putExtra("orid",orderID);
                                         startActivity(intent);

//                                    Toast.makeText(DeliveryoneActivity.this,"Thanks for clicking!",Toast.LENGTH_LONG).show();
//                                    Toast.makeText(DeliveryoneActivity.this,orderID,Toast.LENGTH_LONG).show();
//

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
            public DeliveryoneActivity.AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.deliveryone_layout,parent,false);
                return new DeliveryoneActivity.AdminOrdersViewHolder(view);
            }


        };
        orderhistory.setAdapter(adapter);
        adapter.startListening();




    }


    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, userPhoneNumber, userTotalPrice,location,date,sellerName,sellerAddress,sellerNumber;


        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.username);
            userPhoneNumber = itemView.findViewById(R.id.user_phonenumber);
            userTotalPrice = itemView.findViewById(R.id.totalprice);
            date= itemView.findViewById(R.id.date);
            location = itemView.findViewById(R.id.location);
            sellerAddress = itemView.findViewById(R.id.seller_add);
            sellerNumber=itemView.findViewById(R.id.seller_phonenumber);
            sellerName=itemView.findViewById(R.id.seller_name);


        }
    }
}