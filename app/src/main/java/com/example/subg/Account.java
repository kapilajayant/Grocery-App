package com.example.subg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import info.hoang8f.widget.FButton;

public class Account extends AppCompatActivity {

    FButton btn_edit;

    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;

    private DatabaseReference d;

    private TextView profile_tv_name, profile_tv_email, profile_tv_phone, profile_tv_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        btn_edit = findViewById(R.id.btn_edit);
        profile_tv_name = findViewById(R.id.profile_tv_name);
        profile_tv_email = findViewById(R.id.profile_tv_email);
        profile_tv_phone = findViewById(R.id.profile_tv_phone);
        profile_tv_address = findViewById(R.id.profile_tv_address);

        firebaseAuth = FirebaseAuth.getInstance();

        currentUser = firebaseAuth.getCurrentUser();

        d = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid()).child("profile");

        d.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profile_tv_name.setText(dataSnapshot.child("name").getValue(String.class).toString());
                profile_tv_email.setText(dataSnapshot.child("email").getValue(String.class).toString());
                profile_tv_phone.setText(dataSnapshot.child("phone").getValue(Long.class).toString());
                profile_tv_address.setText(dataSnapshot.child("address").getValue(String.class).toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Couldn't Fetch Data (Check Your Internet Connection)", Toast.LENGTH_SHORT).show();
            }
        });


        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Account.this, SettingsActivity.class));
            }
        });

    }
}
