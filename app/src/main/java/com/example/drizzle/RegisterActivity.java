package com.example.drizzle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    EditText email, password, confirmPassword;
    Button registerButton;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        confirmPassword = findViewById(R.id.registerConfirmPassword);
        registerButton = findViewById(R.id.createAccountButton);

        auth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(v -> register());
    }

    private void register() {

        String e = email.getText().toString().trim();
        String p = password.getText().toString().trim();
        String cp = confirmPassword.getText().toString().trim();

        if (e.isEmpty() || p.isEmpty() || cp.isEmpty()) {
            Toast.makeText(this,
                    "Fill all fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!p.equals(cp)) {
            Toast.makeText(this,
                    "Passwords do not match",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (p.length() < 6) {
            Toast.makeText(this,
                    "Password must have at least 6 characters",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(e, p)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        Toast.makeText(this,
                                "Account created!",
                                Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(
                                RegisterActivity.this,
                                MainActivity.class
                        ));

                        finish();

                    } else {

                        Toast.makeText(this,
                                "Registration failed: " +
                                        task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}