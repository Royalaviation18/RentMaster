package com.fab.rent.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fab.rent.Interface.ItemClickListener;
import com.fab.rent.R;

public class ItemViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtproductName,txtProductDescription,txtproductprice,txtproductStatus;
    public ImageView imageView;
    public ItemClickListener listener;

    public ItemViewHolder(@NonNull View itemView)
    {
        super(itemView);

        imageView=(ImageView)itemView.findViewById(R.id.product_seller_image);

        txtproductName=(TextView)itemView.findViewById(R.id.product_seller_name);
        txtProductDescription=(TextView)itemView.findViewById(R.id.product_seller_description);
        txtproductprice=(TextView)itemView.findViewById(R.id.product_seller_price);
        txtproductStatus=(TextView)itemView.findViewById(R.id.seller_product_status);

    }

    public void setItemClickListener(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View view)
    {
        listener.onClick(view, getAdapterPosition(),false);
    }


}
