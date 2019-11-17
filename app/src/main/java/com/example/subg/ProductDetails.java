package com.example.subg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.subg.MainActivity;
import com.example.subg.R;
import com.example.subg.cart;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Model.Products;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class ProductDetails extends AppCompatActivity {

    String mypreferences1 = "mypref";
    SharedPreferences prefs;

    TextView prod_name, prod_price, prod_desc;
    ImageView prod_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;
    String prodId = "";
    String prodType = "";
    String username = "";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference prods;
    DatabaseReference cart_ref;
//    FirebaseAuth firebaseAuth;
//    FirebaseUser firebaseUser;
    ImageView img_product;
    String id;
    //String currentUser;
    int cart_total_price = 0;
    CoordinatorLayout coordinatorLayout;

    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        firebaseAuth = FirebaseAuth.getInstance();

        currentUser = firebaseAuth.getCurrentUser();

        //Firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        prod_name = (TextView) findViewById(R.id.product_details_name);
        prod_price = (TextView) findViewById(R.id.product_details_price);
        prod_desc = (TextView) findViewById(R.id.product_details_desc);
        prod_image = (ImageView)findViewById(R.id.product_details_image);
        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        prefs = getSharedPreferences(mypreferences1, Context.MODE_MULTI_PROCESS);
        prodType = prefs.getString("prodType",NULL);
        prodId = prefs.getString("prodId",NULL);
        username = prefs.getString("username",NULL);
        SharedPreferences.Editor editor = prefs.edit();
        Toast.makeText(getApplicationContext(), prodType, Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), prodId, Toast.LENGTH_SHORT).show();
        img_product = (ImageView) findViewById(R.id.product_details_image);
        btnCart = findViewById(R.id.btn_cart);
        numberButton = findViewById(R.id.number_button);
//        firebaseUser = firebaseAuth.getCurrentUser();
//        currentUser = firebaseUser.getDisplayName();

            if(prodType == "Vegetables")
            {
                Toast.makeText(getApplicationContext(), "Inside Vegetable If", Toast.LENGTH_SHORT).show();
                getVegetableDetail();
                editor.remove("prodType");
                editor.commit();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Inside Groceries If", Toast.LENGTH_SHORT).show();
                getGroceryDetail();
                editor.remove("prodType");
                editor.commit();
            }
        }

    private void getGroceryDetail()
    {
        prods = firebaseDatabase.getReference("Groceries");

        prods.child(prodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final Products products = dataSnapshot.getValue(Products.class);
                prod_name.setText(products.getName());
                prod_price.setText("₹"+products.getPrice());
                if (prod_name.getText().toString().equals("Basmati Rice")) {
                    img_product.setImageDrawable(getResources().getDrawable(R.drawable.ic_rice));
                    id = dataSnapshot.child("id").getValue(String.class);
                    Toast.makeText(getApplicationContext(),id,Toast.LENGTH_SHORT).show();
                }
                if (prod_name.getText().toString().equals("Chakki Atta")) {
                    img_product.setImageDrawable(getResources().getDrawable(R.drawable.ic_wheat));
                    id = dataSnapshot.child("id").getValue(String.class);
                    Toast.makeText(getApplicationContext(),id,Toast.LENGTH_SHORT).show();
                }

                btnCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        cart_ref = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
                        String s_id = cart_ref.push().getKey();
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(products.getName().toString(),products.getName().toString());
                        editor.commit();

                        cart_ref.child("Cart").child(id).child("id").setValue(id);
                        cart_ref.child("Cart").child(id).child("Name").setValue(products.getName());
                        cart_ref.child("Cart").child(id).child("Price").setValue(products.getPrice());
                        cart_ref.child("Cart").child(id).child("Quantity").setValue(numberButton.getNumber());
                        Snackbar.make(view, products.getName()+" added to cart", Snackbar.LENGTH_LONG)
                                .setAction("View Cart", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivity(new Intent(ProductDetails.this, cart.class));
                                    }
                                }).show();

//                        cart_total_price += Integer.parseInt(products.getPrice()) * Integer.parseInt(products.getQuantity());
//                        Toast.makeText(getApplicationContext(), cart_total_price, Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getVegetableDetail()
    {
        prods = firebaseDatabase.getReference("Vegetables");
        prods.child(prodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final Products products = dataSnapshot.getValue(Products.class);
                prod_name.setText(products.getName());
                prod_price.setText("₹"+products.getPrice());
                if(prod_name.getText().toString().equals("Cauliflower"))
                {
                    id = dataSnapshot.child("id").getValue(String.class);
                    img_product.setImageDrawable(getResources().getDrawable(R.drawable.ic_cauliflower));
                }
                if(prod_name.getText().toString().equals("Cucumber"))
                {
                    id = dataSnapshot.child("id").getValue(String.class);
                    img_product.setImageDrawable(getResources().getDrawable(R.drawable.ic_cucumber));
                }
                if(prod_name.getText().toString().equals("Onion"))
                {
                    id = dataSnapshot.child("id").getValue(String.class);
                    img_product.setImageDrawable(getResources().getDrawable(R.drawable.ic_onion));
                }
                if(prod_name.getText().toString().equals("Potato"))
                {
                    id = dataSnapshot.child("id").getValue(String.class);
                    img_product.setImageDrawable(getResources().getDrawable(R.drawable.ic_potato));
                }

                btnCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        cart_ref = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());


                        cart_ref.child("Cart").child(id).child("id").setValue(id);
                        cart_ref.child("Cart").child(id).child("Name").setValue(products.getName());
                        cart_ref.child("Cart").child(id).child("Price").setValue(products.getPrice());
                        cart_ref.child("Cart").child(id).child("Quantity").setValue(numberButton.getNumber());
                        Snackbar.make(view, products.getName()+" added to cart", Snackbar.LENGTH_LONG)
                                .setAction("View Cart", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        startActivity(new Intent(ProductDetails.this, cart.class));                                    }
                                }).show();


                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
