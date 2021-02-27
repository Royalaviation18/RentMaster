package com.fab.rent.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fab.rent.Admin.AdminMaintainProductsActivity;
import com.fab.rent.Model.Products;
import com.fab.rent.R;
import com.fab.rent.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {

    DatabaseReference ProductRef;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String type ="";






    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        Intent intent=getActivity().getIntent();
        Bundle bundle=intent.getExtras();
        if(bundle!=null)
        {
            type=getActivity().getIntent().getExtras().getString("Admin");
        }

        ProductRef= FirebaseDatabase.getInstance().getReference().child("Products");
        recyclerView=getView().findViewById(R.id.recycle_menu);

        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);




    }
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>().setQuery(ProductRef.orderByChild("productState").equalTo("Approved"), Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Products products)
            {

                productViewHolder.txtproductName.setText(products.getPname());
                productViewHolder.txtProductDescription.setText(products.getDescription());
                productViewHolder.txtproductprice.setText("Price =" + products.getPrice() + " Rs");
                Picasso.get().load(products.getImage()).into(productViewHolder.imageView);

                   productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override


                        public void onClick(View view)
                        {


                            if (type.equals("Admin"))
                            {
                                Intent intent = new Intent(getActivity(), AdminMaintainProductsActivity.class);
                                intent.putExtra("pid", products.getPid());

                                startActivity(intent);
                            }
                            else {


                                Intent intent = new Intent(getActivity(), UserViewProductActivity.class);
                                intent.putExtra("pid", products.getPid());
                                intent.putExtra("sid",products.getSid());
                                intent.putExtra("sname",products.getSellerName());
                                intent.putExtra("sphone",products.getSellerPhone());
                                startActivity(intent);

                            }
                    }
                });

            }

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_items_layout, parent, false);
                ProductViewHolder holder = new ProductViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
