package com.example.bibiliogo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvLoginError;
    private CheckBox cbRememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Oculta el ActionBar para que no aparezca el banner
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvLoginError = findViewById(R.id.tvLoginError);
        cbRememberMe = findViewById(R.id.cbRememberMe);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                // Demo: usuarios hardcodeados
                if (username.equals("admin") && password.equals("admin123")) {
                    // Guardar usuario localmente
                    getSharedPreferences("user_prefs", MODE_PRIVATE)
                        .edit().putString("username", username).apply();
                    // Ir a la vista de admin
                    Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                    startActivity(intent);
                    finish();
                } else if (username.equals("usuario") && password.equals("usuario123")) {
                    // Guardar usuario localmente
                    getSharedPreferences("user_prefs", MODE_PRIVATE)
                        .edit().putString("username", username).apply();
                    // Ir a la vista de usuario
                    Intent intent = new Intent(LoginActivity.this, UserActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    tvLoginError.setText("Credenciales incorrectas");
                }
            }
        });
    }
}
