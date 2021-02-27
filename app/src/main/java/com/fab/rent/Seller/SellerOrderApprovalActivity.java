package com.fab.rent.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fab.rent.Model.AdminOrders;
import com.fab.rent.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SellerOrderApprovalActivity extends AppCompatActivity {


    private RecyclerView orderList;
    private DatabaseReference ordersRef;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_order_approval);

        ordersRef= FirebaseDatabase.getInstance().getReference().child("Orders");
        orderList=findViewById(R.id.order_list);
        orderList.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    protected void onStart() {
        super.onStart();




        FirebaseRecyclerOptions<AdminOrders> options=new FirebaseRecyclerOptions.Builder<AdminOrders>().setQuery(ordersRef.orderByChild("sid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()),AdminOrders.class ).build();

        FirebaseRecyclerAdapter<AdminOrders, SellerOrderApprovalActivity.AdminOrdersViewHolder> adapter=new FirebaseRecyclerAdapter<AdminOrders, SellerOrderApprovalActivity.AdminOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminOrdersViewHolder adminOrdersViewHolder, int i, @NonNull AdminOrders adminOrders) {


                adminOrdersViewHolder.userName.setText("Name : " + adminOrders.getName());
                adminOrdersViewHolder.userPhoneNumber.setText("Phone : " + adminOrders.getPhone());
                adminOrdersViewHolder.userTotalPrice.setText("Total Amount : " + adminOrders.getTotalamount());
                adminOrdersViewHolder.userDateTime.setText("Order At: " + adminOrders.getDate()+ "  "+ adminOrders.getTime());
                adminOrdersViewHolder.userShippingAddress.setText("Shipping Address : " + adminOrders.getAddress()+ ", "+ adminOrders.getCity());
                adminOrdersViewHolder.showstatus.setText("Status : " + adminOrders.getStatus());
                adminOrdersViewHolder.showOrderBtn.setText("State : " + adminOrders.getState());


                /*adminOrdersViewHolder.showOrderBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String uId=getRef(i).getKey();

                        Intent intent=new Intent(S.this,AdminUserProductsActivity.class);
                        intent.putExtra("uid",uId);
                        startActivity(intent);
                    }
                });*/

                adminOrdersViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        final String orderID = adminOrders.getPhone();
                        CharSequence[] options = new CharSequence[]
                                {
                                        "Yes",
                                        "No"
                                };
                        AlertDialog.Builder builder=new AlertDialog.Builder(SellerOrderApprovalActivity.this);

                        builder.setTitle("Do you want to Confirm this Order?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                if(i==0)
                                {
                                   // String uID=getRef(i).getKey();
                                   // RemoveOrder(uID);
                                    ChangeProductState(orderID);
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
            public SellerOrderApprovalActivity.AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout,parent,false);
                return new SellerOrderApprovalActivity.AdminOrdersViewHolder(view);
            }
        };
        orderList.setAdapter(adapter);
        adapter.startListening();
    }



    public static  class AdminOrdersViewHolder extends  RecyclerView.ViewHolder
    {
        public TextView userName,userPhoneNumber,userTotalPrice,userDateTime,userShippingAddress;
        public Button showOrderBtn,showstatus;
        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.order_user_name);
            userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
            userTotalPrice = itemView.findViewById(R.id.order_total_price);
            userDateTime = itemView.findViewById(R.id.order_date_time);
            userShippingAddress = itemView.findViewById(R.id.order_adress_city);
            showOrderBtn=itemView.findViewById(R.id.show_all_products_btn);
            showstatus=itemView.findViewById(R.id.show_status);

        }
    }

    private  void ChangeProductState(String orderID)
    {
        ordersRef.child(orderID).child("status").setValue("Confirmed").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(SellerOrderApprovalActivity.this,"Item has been approved,the product is live now!",Toast.LENGTH_SHORT).show();

            }
        });
    }
    /*
    private void RemoveOrder(String uID)
    {

        ordersRef.child(uID).removeValue();
    }*/
}