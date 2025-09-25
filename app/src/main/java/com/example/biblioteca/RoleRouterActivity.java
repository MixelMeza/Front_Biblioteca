package com.example.biblioteca;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class RoleRouterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Simulación: obtener el rol del usuario (admin o usuario)
        String userRole = getIntent().getStringExtra("role");
        if (userRole == null) userRole = "usuario"; // Por defecto
        if (userRole.equals("admin")) {
            startActivity(new Intent(this, AdminActivity.class));
        } else {
            startActivity(new Intent(this, UserActivity.class));
        }
        finish();
    }
}
