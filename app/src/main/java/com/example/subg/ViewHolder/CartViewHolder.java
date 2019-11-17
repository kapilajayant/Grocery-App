package com.example.subg.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.subg.Interface.ItemClickListener;
import com.example.subg.R;
import com.example.subg.cart;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView cart_name, cart_price, cart_quantity;
    public ItemClickListener listener;
    public ElegantNumberButton cart_number_button;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        cart_name = itemView.findViewById(R.id.cart_item_name);
        cart_price = itemView.findViewById(R.id.cart_item_price);
        cart_quantity = itemView.findViewById(R.id.cart_item_quantity);
        cart_number_button = itemView.findViewById(R.id.cart_number_button);
//        final cart c = new cart();
//        cart_number_button.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
//            @Override
//            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
//                c.Quant = cart_number_button.getNumber();
//            }
//        });

    }

    public void setItemClickListner(View.OnClickListener itemClickListener)
    {
        this.listener = (ItemClickListener) itemClickListener;
    }

    @Override
    public void onClick(View v) {

        listener.onClick(v, getAdapterPosition(), false);

    }
}
