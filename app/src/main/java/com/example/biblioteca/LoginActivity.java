package com.example.biblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvLoginError = findViewById(R.id.tvLoginError);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if (username.equals("admin") && password.equals("admin")) {
                    tvLoginError.setText("");
                    Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                    startActivity(intent);
                    finish();
                } else if (username.equals("usuario") && password.equals("usuario")) {
                    tvLoginError.setText("");
                    Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    tvLoginError.setText("Usuario o contraseña incorrectos");
                }
            }
        });

        TextView tvGoRegister = findViewById(R.id.tvGoRegister);
        tvGoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
