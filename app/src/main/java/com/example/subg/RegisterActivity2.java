package com.example.subg;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class RegisterActivity2 extends AppCompatActivity {

    DatabaseReference user_ref;
    SharedPreferences prefs;
    String mypreferences1 = "mypref";
    private String username;
    private EditText et_businessname, et_address, et_pan;
    Button btn_register;
    ProgressDialog progress;
    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        prefs = getSharedPreferences(mypreferences1, Context.MODE_MULTI_PROCESS);
        username = prefs.getString("username", NULL);

        et_businessname = findViewById(R.id.et_businessname);
        et_address = findViewById(R.id.et_address);
        et_pan = findViewById(R.id.et_pan);
        btn_register = findViewById(R.id.btn_register);


        firebaseAuth = FirebaseAuth.getInstance();

        currentUser = firebaseAuth.getCurrentUser();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInputs();
            }
        });
    }

        private void checkInputs(){

            if (!TextUtils.isEmpty(et_businessname.getText().toString())) {
                if (!TextUtils.isEmpty(et_address.getText().toString())) {
                    if (!TextUtils.isEmpty(et_pan.getText().toString())) {
                        CreateAccount();
                    } else {
                        et_pan.setError("PAN number Cannot Be Empty");
//                        Toast.makeText(getApplicationContext(), "Please Confirm Your Password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    et_address.setError("Address Cannot Be Empty");
//                    Toast.makeText(getApplicationContext(), "Please Enter Your Password", Toast.LENGTH_SHORT).show();
                }
            } else {
                et_businessname.setError("Business Name Cannot Be Empty");
//                Toast.makeText(getApplicationContext(), "Please Fill Your Email", Toast.LENGTH_SHORT).show();
            }

    }

    private void CreateAccount() {


        user_ref = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid()).child("profile");
        user_ref.child("business name").setValue(et_businessname.getText().toString());
        user_ref.child("address").setValue(et_address.getText().toString());
        user_ref.child("pan").setValue(et_pan.getText().toString());
        Intent mainIntent = new Intent(RegisterActivity2.this, MainActivity.class);
        startActivity(mainIntent);
        finish();

    }
}
