package com.example.biblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        TextView tvAppName = findViewById(R.id.tvAppName);
        tvAppName.setAlpha(0f);
        tvAppName.animate().alpha(1f).setDuration(1200).setStartDelay(300);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvAppName.animate().alpha(0f).setDuration(600);
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        }, 3500);
    }
}
