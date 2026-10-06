package com.example.drizzle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button loginButton, registerButton;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        auth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(v -> login());

        registerButton.setOnClickListener(v ->
                startActivity(new Intent(
                        LoginActivity.this,
                        RegisterActivity.class
                ))
        );
    }

    private void login() {

        String e = email.getText().toString().trim();
        String p = password.getText().toString().trim();

        if (e.isEmpty() || p.isEmpty()) {
            Toast.makeText(this,
                    "Enter email and password",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(e, p)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        Toast.makeText(this,
                                "Login successful",
                                Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(
                                LoginActivity.this,
                                MainActivity.class
                        ));

                        finish();

                    } else {

                        Toast.makeText(this,
                                "Login failed: " +
                                        task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}