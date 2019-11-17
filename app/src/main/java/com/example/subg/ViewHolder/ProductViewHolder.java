package com.example.subg.ViewHolder;

import android.content.ClipData;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.subg.Interface.ItemClickListener;
import com.example.subg.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    public TextView product_name, product_price, product_quantity;
    public ImageView product_image;
    public ItemClickListener listener;




    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        product_image = (ImageView) itemView.findViewById(R.id.product_image);
        product_name = (TextView) itemView.findViewById(R.id.product_name);
        product_price = (TextView) itemView.findViewById(R.id.product_price);
        product_quantity = (TextView) itemView.findViewById(R.id.product_quantity);


    }

    public void setItemClickListner(ItemClickListener itemClickListener)
    {
        this.listener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

        listener.onClick(v, getAdapterPosition(), false);

    }
}
