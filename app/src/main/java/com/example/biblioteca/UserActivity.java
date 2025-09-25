package com.example.biblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;

public class UserActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setTitle("Panel de Usuario");
        drawerLayout = findViewById(R.id.drawer_layout_user);
        navView = findViewById(R.id.user_nav_view);
        navView.setItemTextColor(getResources().getColorStateList(R.color.dark_green));
        navView.setItemIconTintList(getResources().getColorStateList(R.color.dark_green));
        navView.setCheckedItem(R.id.nav_user_home);
        findViewById(R.id.user_menu_icon).setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });
        navView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_user_signout) {
                Intent intent = new Intent(UserActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return true;
            }
            return false;
        });
    }
}
