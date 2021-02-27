package com.fab.rent.Seller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fab.rent.Model.AdminOrders;
import com.fab.rent.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SellerViewfeedbackActivity extends AppCompatActivity {

    private RecyclerView feedbackhistory;
    private DatabaseReference feedbackRef;

    private String pid="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_viewfeedback);

        feedbackRef= FirebaseDatabase.getInstance().getReference().child("Feedback");

        feedbackhistory = findViewById(R.id.feedback_lists);
        feedbackhistory.setLayoutManager(new LinearLayoutManager(this));
        pid=getIntent().getStringExtra("pid");
        Toast.makeText(SellerViewfeedbackActivity.this,pid,Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<AdminOrders> options = new FirebaseRecyclerOptions.Builder<AdminOrders>().setQuery(feedbackRef.orderByChild("pid").equalTo(pid), AdminOrders.class).build();
        FirebaseRecyclerAdapter<AdminOrders, AdminOrdersViewHolder> adapter = new FirebaseRecyclerAdapter<AdminOrders, SellerViewfeedbackActivity.AdminOrdersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SellerViewfeedbackActivity.AdminOrdersViewHolder adminOrdersViewHolder, int i, @NonNull AdminOrders adminOrders) {

                adminOrdersViewHolder.feedbackdate.setText(adminOrders.getDate());
                adminOrdersViewHolder.feedbackphone.setText(adminOrders.getPhone());
                adminOrdersViewHolder.feedbackratings.setText(adminOrders.getRatings());
                adminOrdersViewHolder.feedbackdescription.setText(adminOrders.getDescription());


        };

            @NonNull
            @Override
            public SellerViewfeedbackActivity.AdminOrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sellerviewfeedback, parent, false);
                return new SellerViewfeedbackActivity.AdminOrdersViewHolder(view);
            }
        };
        feedbackhistory.setAdapter(adapter);
        adapter.startListening();
    }

        public static class AdminOrdersViewHolder extends RecyclerView.ViewHolder {
            public TextView feedbackdate,feedbackphone,feedbackratings,feedbackdescription;


                public AdminOrdersViewHolder(@NonNull View itemView) {
                super(itemView);


                feedbackdate=itemView.findViewById(R.id.feedback_date);
                feedbackphone=itemView.findViewById(R.id.feedback_phone_number);
                feedbackratings=itemView.findViewById(R.id.feedback_ratings);
                feedbackdescription=itemView.findViewById(R.id.feedback_description);

            }
        }



    }






