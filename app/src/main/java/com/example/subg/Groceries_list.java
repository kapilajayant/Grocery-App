package com.example.subg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.subg.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import Model.Products;

public class Groceries_list extends AppCompatActivity {

    String mypreferences1 = "mypref";
    SharedPreferences pref1;

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onStart()
    {
        super.onStart();


        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groceries_list);
        recyclerView = findViewById(R.id.recycler_menu_groceries);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        fetch_groceries();

    }


    private void fetch_groceries() {

            Query query = FirebaseDatabase.getInstance().getReference().child("Groceries");

            FirebaseRecyclerOptions<Products> options =
                    new FirebaseRecyclerOptions.Builder<Products>()
                            .setQuery(query, new SnapshotParser<Products>() {
                                @NonNull
                                @Override
                                public Products parseSnapshot(@NonNull DataSnapshot snapshot) {

                                    return new
                                            Products(snapshot.child("Name").getValue().toString(),
                                            snapshot.child("Price").getValue().toString(),
                                            snapshot.child("Quantity").getValue().toString());

                                }
                            })
                            .build();

            adapter = new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                @Override
                public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
                    ProductViewHolder productViewHolder = new ProductViewHolder(view);
                    return productViewHolder;
                }


                @Override
                protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, final int position, Products products) {

                    productViewHolder.product_name.setText(products.getName());
                    productViewHolder.product_price.setText("₹"+products.getPrice());
                    productViewHolder.product_quantity.setText(products.getQuantity());
                    if (productViewHolder.product_name.getText().toString().equals("Basmati Rice")) {
                        productViewHolder.product_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_rice));
                    }
                    if (productViewHolder.product_name.getText().toString().equals("Chakki Atta")) {
                        productViewHolder.product_image.setImageDrawable(getResources().getDrawable(R.drawable.ic_wheat));
                    }

                    //Picasso.get().load(products.getImage()).into(productViewHolder.product_image);
                    //Glide.with(getApplicationContext()).load(products.getImage()).into(productViewHolder.product_image);

                    productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Toast.makeText(Groceries_list.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                            Intent productDetail = new Intent(Groceries_list.this, ProductDetails.class);
                            pref1=getSharedPreferences(mypreferences1, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref1.edit();
                            editor.putString("prodId", adapter.getRef(position).getKey());
                            editor.putString("prodType","Groceries");
                            editor.commit();
                            startActivity(productDetail);
                        }
                    });
                }

            };

            recyclerView.setAdapter(adapter);

        }
}