package com.example.bibiliogo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.bibiliogo.ui.home.HomeFragment;
import com.example.bibiliogo.admin.PedidosAdminFragment;
import com.example.bibiliogo.ui.librosadmin.LibrosAdminFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import com.example.bibiliogo.ui.librosadmin.LibroFormDialog;
import com.example.bibiliogo.model.Libro;
import com.example.bibiliogo.network.ApiClient;
import com.example.bibiliogo.network.LibroService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.view.View;

public class AdminActivity extends AppCompatActivity {
    private LibroService libroService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    // Inicializar libroService para evitar NullPointerException
    libroService = ApiClient.getClient().create(LibroService.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Configurar Toolbar como ActionBar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Mostrar el nombre de usuario en el header
        String username = getSharedPreferences("user_prefs", MODE_PRIVATE).getString("username", "");
        if (getSupportActionBar() != null && !username.isEmpty()) {
            getSupportActionBar().setTitle("Bienvenido, " + username);
        }

        // Inicializar BottomNavigationView antes de usarlo
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // FAB global para agregar libro
        FloatingActionButton fabAddLibro = findViewById(R.id.fabAddLibro);
        fabAddLibro.setOnClickListener(v -> {
            if (bottomNav.getSelectedItemId() == R.id.navigation_libros) {
                mostrarDialogoCrearLibro();
            }
        });
        fabAddLibro.setVisibility(View.GONE); // Ocultar por defecto
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                selectedFragment = new HomeFragment();
                fabAddLibro.setVisibility(View.GONE);
            } else if (id == R.id.navigation_pedidos) {
                selectedFragment = new PedidosAdminFragment();
                fabAddLibro.setVisibility(View.GONE);
            } else if (id == R.id.navigation_libros) {
                selectedFragment = new com.example.bibiliogo.ui.librosadmin.LibrosAdminFragment();
                fabAddLibro.setVisibility(View.VISIBLE);
            }
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, selectedFragment)
                        .commit();
            }
            return true;
        });
        // Mostrar Home por defecto
        bottomNav.setSelectedItemId(R.id.navigation_home);
    }

    private void mostrarDialogoCrearLibro() {
        LibroFormDialog dialog = new LibroFormDialog();
        dialog.setOnLibroFormListener((titulo, autor, categoria, codigo, precio, descripcion, estado) -> {
            Libro libro = new Libro();
            libro.setTitulo(titulo);
            libro.setAutor(autor);
            libro.setCategoria(categoria);
            libro.setCodigo(codigo);
            libro.setPrecio(precio);
            libro.setDescripcion(descripcion);
            libro.setEstado(estado);
            crearLibro(libro);
        });
        dialog.show(getSupportFragmentManager(), "crearLibro");
    }

    private void crearLibro(Libro libro) {
        libroService.createLibro(libro).enqueue(new Callback<Libro>() {
            @Override
            public void onResponse(Call<Libro> call, Response<Libro> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(AdminActivity.this, "Libro creado", Toast.LENGTH_SHORT).show();
                    // Opcional: recargar fragmento de libros
                    getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, new com.example.bibiliogo.ui.librosadmin.LibrosAdminFragment())
                        .commit();
                } else {
                    Toast.makeText(AdminActivity.this, "Error al crear libro", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Libro> call, Throwable t) {
                Toast.makeText(AdminActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            // Borrar usuario guardado y volver a login
            SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
            prefs.edit().clear().apply();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
