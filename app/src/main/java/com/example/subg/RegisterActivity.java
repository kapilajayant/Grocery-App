package com.example.subg;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;



import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    DatabaseReference user_ref;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private EditText et_name, et_phone, et_email, et_password, et_confirm;
    Button btn_next;
    ProgressDialog progress;
    String mypreferences1 = "mypref";
    SharedPreferences pref1;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        et_confirm = (EditText) findViewById(R.id.et_confirm);
        et_name = (EditText) findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkinputs();
            }
        });
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    private void checkinputs() {

        if (!TextUtils.isEmpty(et_email.getText().toString())) {
            if (!TextUtils.isEmpty(et_password.getText().toString())) {
                if (!TextUtils.isEmpty(et_confirm.getText().toString())) {
                    if ((et_confirm.getText().toString().equals(et_password.getText().toString()))) {
                        CreateAccount();
                    } else {
                        et_confirm.setError("Doesn't Match with Password");
                        Toast.makeText(getApplicationContext(), "Enter same password in both fields", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    et_confirm.setError("Empty Field!");
                    Toast.makeText(getApplicationContext(), "Please Confirm Your Password", Toast.LENGTH_SHORT).show();
                }
            } else {
                et_password.setError("Password Cannot Be Empty");
                Toast.makeText(getApplicationContext(), "Please Enter Your Password", Toast.LENGTH_SHORT).show();
            }
        } else {
            et_email.setError("Email Cannot Be Empty");
            Toast.makeText(getApplicationContext(), "Please Fill Your Email", Toast.LENGTH_SHORT).show();
        }

    }

    private void CreateAccount() {
        final String email = et_email.getText().toString();
        String password = et_password.getText().toString();
//        String name = et_name.getText().toString();
//        String phone = et_phone.getText().toString();
        progress = new ProgressDialog(RegisterActivity.this);
        progress.setTitle("Creating Account...");
        progress.setMessage("Taking you to next page...");

        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    firebaseAuth = FirebaseAuth.getInstance();

                    currentUser = firebaseAuth.getCurrentUser();

                    user_ref = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid()).child("profile");
                    user_ref.child("name").setValue(et_name.getText().toString());
                    user_ref.child("phone").setValue(et_phone.getText().toString());
                    user_ref.child("email").setValue(email);

                    pref1 = getSharedPreferences(mypreferences1, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref1.edit();
                    editor.putString("username", et_name.getText().toString());
                    editor.commit();



                    Intent mainIntent = new Intent(RegisterActivity.this, RegisterActivity2.class);
                    startActivity(mainIntent);
                    finish();

                }

                else {
                    String error = task.getException().getMessage();
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
        public void SignInActivity(View view){
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        }

}
