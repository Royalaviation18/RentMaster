package com.fab.rent.Seller;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fab.rent.Model.Products;
import com.fab.rent.R;
import com.fab.rent.Seller.SellerMaintainProductActivity;
import com.fab.rent.ViewHolder.ItemViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ShomeFragment extends Fragment {

    private TextView mTextMessage;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unverifiedProductsRef;
    private String pid="";
    private FloatingActionButton add;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_shome,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unverifiedProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView=getView().findViewById(R.id.seller_home_recycler_view);
        add=getView().findViewById(R.id.seller_add_btn);
        recyclerView.setHasFixedSize(true);
        layoutManager =new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),SellerCategoryActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options=new FirebaseRecyclerOptions.Builder<Products>().setQuery(unverifiedProductsRef.orderByChild("sid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()),Products.class).build();

        FirebaseRecyclerAdapter<Products, ItemViewHolder> adapter=new FirebaseRecyclerAdapter<Products, ItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i, @NonNull Products products) {
                itemViewHolder.txtproductName.setText(products.getPname());
                itemViewHolder.txtProductDescription.setText(products.getDescription());
                itemViewHolder.txtproductprice.setText("Price =" + products.getPrice()+ " Rs");
                itemViewHolder.txtproductStatus.setText("State : "+ products.getProductState());
                Picasso.get().load(products.getImage()).into(itemViewHolder.imageView);

                pid=products.getPid();

                itemViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public
                    void onClick(View view) {



                        Intent intent=new Intent(getActivity(), SellerMaintainProductActivity.class);
                        intent.putExtra("pid",products.getPid());
                        startActivity(intent);




                        //
//                        final String productID = products.getPid();
//                        CharSequence [] options=new CharSequence[]
//                                {
//
//                                        "Yes",
//                                        "No"
//                                };
//                        AlertDialog.Builder builder=new AlertDialog.Builder(SellerHomeActivity.this);
//                        builder.setTitle("Do you want to delete this product?");
//                        builder.setItems(options, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i)
//                            {
//                                if(i==0)
//                                {
//                                    deleteProduct(productID);
//                                }
//                                if(i==1)
//                                {
//
//                                }
//                            }
//
//                        });
//                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_item_view,parent,false);
                ItemViewHolder holder=new ItemViewHolder(view);
                return  holder;

            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void deleteProduct(String productID) {

        unverifiedProductsRef.child(productID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getActivity(),"Item has been removed Successfully!",Toast.LENGTH_SHORT).show();

            }
        });


    }
}
