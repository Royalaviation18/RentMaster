package com.fab.rent.Navigation;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fab.rent.User.UserFeedbackActivity;
import com.fab.rent.Model.AdminOrders;
import com.fab.rent.Prevalent.Prevalent;
import com.fab.rent.R;
import com.fab.rent.User.UserOrderHistoryActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MyRentals extends Fragment {


    private RecyclerView orderhistory;
    private DatabaseReference ordersRef;
    String pid, phone, saveCurrentDate, saveCurrentTime, feedbackdetailsid;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_my_rentals, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("OrderDetails");

        orderhistory = getView().findViewById(R.id.my_order_list);
        orderhistory.setLayoutManager(new LinearLayoutManager(getActivity()));


    }

    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>().setQuery(ordersRef.orderByChild("phone").equalTo(Prevalent.currentOnlineUser.getPhone()), AdminOrders.class).build();
        FirebaseRecyclerAdapter<AdminOrders, UserOrderHistoryActivity.AdminOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders, UserOrderHistoryActivity.AdminOrdersViewHolder>(options) {


            @NonNull
            @Override
            public UserOrderHistoryActivity.AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userhistory_layout, parent, false);
                return new UserOrderHistoryActivity.AdminOrdersViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(@NonNull UserOrderHistoryActivity.AdminOrdersViewHolder adminOrdersViewHolder, int i, @NonNull AdminOrders adminOrders) {

                adminOrdersViewHolder.userName.setText(adminOrders.getName());
                adminOrdersViewHolder.userPhoneNumber.setText(adminOrders.getPhone());
                adminOrdersViewHolder.userShippingAddress.setText(adminOrders.getAddress());
                adminOrdersViewHolder.userDateTime.setText(adminOrders.getDate());
                adminOrdersViewHolder.userSecurityAmt.setText(adminOrders.getSecurityAmt());
                adminOrdersViewHolder.userTotalPrice.setText(adminOrders.getTotalamount());


                adminOrdersViewHolder.feedback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(getActivity(), UserFeedbackActivity.class);
                        intent.putExtra("pid", adminOrders.getPid());
                        intent.putExtra("phone", adminOrders.getPhone());
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
            feedback = itemView.findViewById(R.id.btn_feedback);


        }
    }
}