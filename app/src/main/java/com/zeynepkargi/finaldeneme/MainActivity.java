package com.zeynepkargi.finaldeneme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.OnClickAction;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.zeynepkargi.finaldeneme.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth auth;
    Button login,signup;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        login=findViewById(R.id.btn_giris);
        signup=findViewById(R.id.btn_kayit);


        auth= FirebaseAuth.getInstance();//bu objeyle giriş çıkış işlemlerini yapabiliriz.
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {

            Intent intent = new Intent(MainActivity.this, SignupActivity.class);
            startActivity(intent);
            finish();

        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.etEmail.getText().toString();
                String password = binding.etPassword.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Lütfen e-posta ve şifreyi girin", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(MainActivity.this, "Geçerli bir e-posta adresi girin", Toast.LENGTH_LONG).show();
                    return;
                }


                auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = binding.etEmail.getText().toString();
                String password = binding.etPassword.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Lütfen e-posta ve şifreyi girin", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(MainActivity.this, "Geçerli bir e-posta adresi girin", Toast.LENGTH_LONG).show();
                    return;
                }

                auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        Toast.makeText(MainActivity.this, "User Created", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });



    }






}