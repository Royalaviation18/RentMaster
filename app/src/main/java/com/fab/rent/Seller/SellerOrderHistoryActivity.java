package com.fab.rent.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fab.rent.Model.AdminOrders;
import com.fab.rent.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SellerOrderHistoryActivity extends AppCompatActivity {

    private RecyclerView orderhistory;
    private DatabaseReference ordersRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_order_history);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("OrderDetails");

        orderhistory = findViewById(R.id.seller_home_recycler_view);
        orderhistory.setLayoutManager(new LinearLayoutManager(this));
    }

        @Override
        protected void onStart() {
            super.onStart();


            FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>().setQuery(ordersRef.orderByChild("sid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()),AdminOrders.class).build();
            FirebaseRecyclerAdapter<AdminOrders, SellerOrderHistoryActivity.AdminOrdersViewHolder> adapter= new FirebaseRecyclerAdapter<AdminOrders, SellerOrderHistoryActivity.AdminOrdersViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull AdminOrdersViewHolder adminOrdersViewHolder, int i, @NonNull AdminOrders adminOrders) {

                    adminOrdersViewHolder.userName.setText(adminOrders.getPname());
                    adminOrdersViewHolder.userDateTime.setText(adminOrders.getDate());
                    adminOrdersViewHolder.userPhoneNumber.setText(adminOrders.getPhone());
                    adminOrdersViewHolder.userTotalPrice.setText(adminOrders.getTotalamount());
                    adminOrdersViewHolder.userSecurityAmt.setText(adminOrders.getSecurityAmt());
                    adminOrdersViewHolder.userShippingAddress.setText(adminOrders.getAddress());

                    adminOrdersViewHolder.viewfeedback.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(SellerOrderHistoryActivity.this, SellerViewfeedbackActivity.class);
                            intent.putExtra("pid",adminOrders.getPid());
                            startActivity(intent);
                        }
                    });

                }

                @NonNull
                @Override
                public SellerOrderHistoryActivity.AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sellerhistory,parent,false);
                    return new SellerOrderHistoryActivity.AdminOrdersViewHolder(view);
                }
                };


            orderhistory.setAdapter(adapter);
            adapter.startListening();

        }

        public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder {
            public TextView userName, userPhoneNumber, userTotalPrice, userDateTime, userShippingAddress, userSecurityAmt;
            public Button viewfeedback;

            public AdminOrdersViewHolder(@NonNull View itemView) {
                super(itemView);

                userName = itemView.findViewById(R.id.history_seller_name);
                userPhoneNumber = itemView.findViewById(R.id.seller_phone_number);
                userTotalPrice = itemView.findViewById(R.id.seller_total_price);
                userDateTime = itemView.findViewById(R.id.seller_date_time);
                userShippingAddress = itemView.findViewById(R.id.seller_adress_city);
                userSecurityAmt = itemView.findViewById(R.id.seller_security);
                viewfeedback=itemView.findViewById(R.id.btn_view_feedback);


            }



    }
}