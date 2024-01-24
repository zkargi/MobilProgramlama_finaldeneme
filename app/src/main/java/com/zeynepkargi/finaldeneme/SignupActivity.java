package com.zeynepkargi.finaldeneme;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.zeynepkargi.finaldeneme.databinding.ActivityMainBinding;
import com.zeynepkargi.finaldeneme.databinding.ActivitySignupBinding;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private FirebaseAuth auth;
    Button login,signup;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        binding =ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        signup=findViewById(R.id.btn_signup);
        login=findViewById(R.id.btn_login);
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        auth= FirebaseAuth.getInstance();//bu objeyle giriş çıkış işlemlerini yapabiliriz.


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=binding.etIsim.getText().toString();
                String lastname=binding.etSoyisim.getText().toString();
                String email = binding.etEmail2.getText().toString();
                String password = binding.etPassword2.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "Lütfen e-posta ve şifreyi girin", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(SignupActivity.this, "Geçerli bir e-posta adresi girin", Toast.LENGTH_LONG).show();
                    return;
                }

                auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        Toast.makeText(SignupActivity.this, "User Created", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(SignupActivity.this, SignupActivity.class);
                        startActivity(intent);
                        finish();
                        Map<String,Object> user=new HashMap<>();
                        user.put("name: ", name);
                        user.put("lastname: ",lastname);
                        firebaseFirestore.collection("users").document(auth.getUid()).set(user);
                    }
                });
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


}