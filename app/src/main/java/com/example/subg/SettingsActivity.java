package com.example.subg;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class SettingsActivity extends AppCompatActivity {

    FButton btn_edit;

    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;

    private DatabaseReference d;

    private TextView profile_et_name, profile_et_email, profile_et_phone, profile_et_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btn_edit = findViewById(R.id.btn_edit);
        profile_et_name = findViewById(R.id.profile_et_name);
        profile_et_email = findViewById(R.id.profile_et_email);
        profile_et_phone = findViewById(R.id.profile_et_phone);
        profile_et_address = findViewById(R.id.profile_et_address);

        firebaseAuth = FirebaseAuth.getInstance();

        currentUser = firebaseAuth.getCurrentUser();

        d = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid()).child("profile");

        d.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                profile_et_name.setHint(dataSnapshot.child("name").getValue(String.class).toString());
                profile_et_email.setHint(dataSnapshot.child("email").getValue(String.class).toString());
                profile_et_phone.setHint(dataSnapshot.child("phone").getValue(Long.class).toString());
                profile_et_address.setHint(dataSnapshot.child("address").getValue(String.class).toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Couldn't Fetch Data (Check Your Internet Connection)", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
