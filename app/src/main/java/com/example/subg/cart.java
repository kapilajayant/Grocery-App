package com.example.subg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.subg.ViewHolder.CartViewHolder;
import com.example.subg.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseArray;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Model.CartItems;
import Model.Products;
import info.hoang8f.widget.FButton;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class cart extends AppCompatActivity implements PaymentResultListener {

    private static final String TAG = "Payment Error";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference cart_items;

    String mypreferences1 = "mypref";
    SharedPreferences prefs;
    public String Quant;
    String username = "";

    StringBuffer buf_name, buf_price, buf_quantity;

    private RecyclerView recyclerView;
    FirebaseRecyclerAdapter<CartItems, CartViewHolder> adapter;
    RecyclerView.LayoutManager layoutManager;

    FButton btn_placeorder;
    int total_price = 0;
    ImageView btn_del_cart;
    TextView txt_total_price;

    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onStart() {
        super.onStart();

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        adapter.startListening();
//    }

    @Override
    protected void onRestart() {
        super.onRestart();

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    @Override
    protected void onStop() {
        total_price = 0;
        super.onStop();
        adapter.stopListening();
    }

    HashMap<Integer,Integer> hm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        total_price = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Checkout.preload(getApplicationContext());
        buf_name = new StringBuffer();
        buf_price = new StringBuffer();
        buf_quantity = new StringBuffer();
        btn_del_cart = findViewById(R.id.btn_del_cart);
        txt_total_price = findViewById(R.id.txt_total_price);
        prefs = getSharedPreferences(mypreferences1, Context.MODE_MULTI_PROCESS);
        btn_placeorder = findViewById(R.id.btn_placeorder);
        btn_placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment();
            }
        });
        username = prefs.getString("username", NULL);
//        Toast.makeText(getApplicationContext(), username, Toast.LENGTH_LONG).show();

        recyclerView = findViewById(R.id.recycler_menu_cart);
//        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        firebaseAuth = FirebaseAuth.getInstance();

        currentUser = firebaseAuth.getCurrentUser();
        fetch_cart();
    }


    private void fetch_cart() {

        Query query = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid()).child("Cart");

        FirebaseRecyclerOptions<CartItems> options =
                new FirebaseRecyclerOptions.Builder<CartItems>()
                        .setQuery(query, new SnapshotParser<CartItems>() {
                            @NonNull
                            @Override
                            public CartItems parseSnapshot(@NonNull DataSnapshot snapshot) {


                                return new
                                        CartItems(snapshot.child("Name").getValue().toString(),
                                        snapshot.child("Price").getValue().toString(),
                                        snapshot.child("Quantity").getValue().toString(),
                                        snapshot.child("id").getValue().toString());

                            }
                        }
                        )
                        .build();

        adapter = new FirebaseRecyclerAdapter<CartItems, CartViewHolder>(options) {
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_layout, parent, false);
                CartViewHolder productViewHolder = new CartViewHolder(view);
                return productViewHolder;
            }

            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder productViewHolder, final int position, final CartItems products) {

                productViewHolder.cart_name.setText(products.getName());
                productViewHolder.cart_price.setText(products.getPrice());
                productViewHolder.cart_quantity.setText(products.getQuantity());
                productViewHolder.cart_number_button.setNumber(products.getQuantity());
                int price = Integer.parseInt(products.getPrice());
                int quan = Integer.parseInt(products.getQuantity());
                total_price += price*quan;
                txt_total_price.setText(String.valueOf(total_price));
                Toast.makeText(getApplicationContext(),"Total Price is: "+total_price, Toast.LENGTH_LONG).show();
//                txt_total_price.setText(String.valueOf(products.getTotal_price()));
                //update number button on change
//                DatabaseReference cart_ref;
//                cart_ref = FirebaseDatabase.getInstance().getReference("Users");
//                cart_ref.child(username).child("Cart").child(products.getId()).child("Quantity").setValue(Quant);



//                int price = Integer.parseInt(products.getPrice());
//                int quan = Integer.parseInt(products.getQuantity());
//                total_price += price*quan;
//                Toast.makeText(getApplicationContext(),"Total Price is: "+total_price, Toast.LENGTH_LONG).show();

                productViewHolder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(final View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(cart.this);
                        builder.setMessage("This item will be deleted from your cart.")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        DatabaseReference ref =  FirebaseDatabase.getInstance().getReference().child("Users").child(username).child("Cart").child(products.getId());
                                        ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                           Toast.makeText(getApplicationContext(),"Removed From Cart",Toast.LENGTH_LONG).show();
                                                Snackbar.make(view, " Removed From Cart", Snackbar.LENGTH_LONG)
                                                        .setAction("Undo", new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                startActivity(new Intent(cart.this, cart.class));
                                                            }
                                                        }).show();
                                            recreate();
                                            }
                                        });
                                    }
                                });
                        builder.setTitle("Remove From Cart?");
                        AlertDialog alert = builder.create();
                        alert.setCancelable(true);
                        alert.show();
                    }
                });
            }

        };

//        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    public void startPayment() {
        Checkout checkout = new Checkout();

        int logo = R.drawable.ic_artichoke;
        checkout.setImage(logo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;
       try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name", "Jayant Kapila");

            /**
             * Description can be anything
             * eg: Reference No. #123123 - This order number is passed by you for your internal reference. This is not the `razorpay_order_id`.
             *     Invoice Payment
             *     etc.
             */
            options.put("description", "Test Order");
//            options.put("order_id", "order_9A33XWu170gUtm");
            options.put("currency", "INR");

            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */
            options.put("amount", total_price*100);

            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razor pay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
        DatabaseReference ref =  FirebaseDatabase.getInstance().getReference().child("Users").child(username).child("Cart");
        ref.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getApplicationContext(),"Removed From Cart",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }
}