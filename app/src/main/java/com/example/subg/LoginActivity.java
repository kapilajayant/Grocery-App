package com.example.subg;

import android.app.ProgressDialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText et_login_email, et_login_password;
    Button btn_login;
    ProgressDialog progress;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;


    @Override
    protected void onStart() {
        super.onStart();

        currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null)
        {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_login_email = findViewById(R.id.et_login_email);
        et_login_password = findViewById(R.id.et_login_password);
        btn_login = findViewById(R.id.btn_login);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });


    }

    private void check() {
        if(!TextUtils.isEmpty(et_login_email.getText().toString())) {
            if (!TextUtils.isEmpty(et_login_password.getText().toString())) {
                signIn();
            }
            else
            {
                et_login_password.setError("Password Cannot Be Empty");
            }
        }
        else
        {
            et_login_email.setError("Email Cannot Be Empty");
        }
    }

    private void signIn() {

        progress = new ProgressDialog(this);
        progress.setTitle("Signing In...");
        progress.setMessage("Please Wait!");
        progress.setCancelable(true);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        progress.closeOptionsMenu();
        firebaseAuth.signInWithEmailAndPassword(et_login_email.getText().toString(),et_login_password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else
                        {
//                            progress.cancel();
                            String error = task.getException().getMessage();
                            Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void RegisterMethod(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }
}
