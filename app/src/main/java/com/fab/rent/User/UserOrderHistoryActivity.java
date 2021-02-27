package com.fab.rent.User;

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
import com.fab.rent.Prevalent.Prevalent;
import com.fab.rent.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserOrderHistoryActivity extends AppCompatActivity {


    private RecyclerView orderhistory;
    private DatabaseReference ordersRef;
    String pid,phone,saveCurrentDate,saveCurrentTime,feedbackdetailsid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_history);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("OrderDetails");

        orderhistory = findViewById(R.id.my_order_list);
        orderhistory.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>().setQuery(ordersRef.orderByChild("phone").equalTo(Prevalent.currentOnlineUser.getPhone()),AdminOrders.class).build();
        FirebaseRecyclerAdapter<AdminOrders, UserOrderHistoryActivity.AdminOrdersViewHolder> adapter= new FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder>(options) {


            @NonNull
            @Override
            public AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.userhistory_layout,parent,false);
                return new UserOrderHistoryActivity.AdminOrdersViewHolder(view);
            }



            @Override
            protected void onBindViewHolder(@NonNull AdminOrdersViewHolder adminOrdersViewHolder, int i, @NonNull AdminOrders adminOrders) {

                adminOrdersViewHolder.userName.setText(adminOrders.getName());
                adminOrdersViewHolder.userPhoneNumber.setText(adminOrders.getPhone());
                adminOrdersViewHolder.userShippingAddress.setText(adminOrders.getAddress());
                adminOrdersViewHolder.userDateTime.setText(adminOrders.getDate());
                adminOrdersViewHolder.userSecurityAmt.setText(adminOrders.getSecurityAmt());
                adminOrdersViewHolder.userTotalPrice.setText(adminOrders.getTotalamount());


                adminOrdersViewHolder.feedback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent=new Intent(UserOrderHistoryActivity.this,UserFeedbackActivity.class);
                        intent.putExtra("pid",adminOrders.getPid());
                        intent.putExtra("phone",adminOrders.getPhone());
                        startActivity(intent);
                    }
                });



            }


        };
        orderhistory.setAdapter(adapter);
        adapter.startListening();



    }


    public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder {
        public TextView userName, userPhoneNumber, userTotalPrice, userDateTime, userShippingAddress, userSecurityAmt;
        public Button feedback;

        public AdminOrdersViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.history_user_name);
            userPhoneNumber = itemView.findViewById(R.id.history_phone_number);
            userTotalPrice = itemView.findViewById(R.id.history_total_price);
            userDateTime = itemView.findViewById(R.id.history_date_time);
            userShippingAddress = itemView.findViewById(R.id.history_adress_city);
           userSecurityAmt = itemView.findViewById(R.id.user_security);
           feedback=itemView.findViewById(R.id.btn_feedback);


        }
    }
}
