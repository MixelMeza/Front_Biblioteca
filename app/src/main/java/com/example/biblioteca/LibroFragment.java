package com.example.biblioteca;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.biblioteca.ui.adapter.LibroAdapter;
import com.example.biblioteca.ui.interfaces.LibroService;
import com.example.biblioteca.ui.model.Libro;
import com.example.biblioteca.ui.utils.ApiClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class LibroFragment extends Fragment {
    private RecyclerView recyclerView;
    private LibroAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_libro, container, false);
        recyclerView = view.findViewById(R.id.recyclerLibros);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        view.findViewById(R.id.btnAgregar).setOnClickListener(v -> mostrarDialogAgregarLibro());

        cargarLibros();
        return view;
    }

    private void mostrarDialogAgregarLibro() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_agregar_libro, null);

    androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(requireContext(), R.style.TransparentDialog)
        .setView(dialogView)
        .setNegativeButton("", null) // Oculta el botón cancelar
        .create();

        dialog.setOnShowListener(dlg -> {
            // Botón X para cerrar
            dialogView.findViewById(R.id.btnCerrarDialog).setOnClickListener(v -> dialog.dismiss());

            dialogView.findViewById(R.id.btnCrearLibro).setOnClickListener(v -> {
                String titulo = ((android.widget.EditText) dialogView.findViewById(R.id.etTitulo)).getText().toString().trim();
                String autor = ((android.widget.EditText) dialogView.findViewById(R.id.etAutor)).getText().toString().trim();
                String codigo = ((android.widget.EditText) dialogView.findViewById(R.id.etCodigo)).getText().toString().trim();
                String categoria = ((android.widget.EditText) dialogView.findViewById(R.id.etCategoria)).getText().toString().trim();
                String descripcion = ((android.widget.EditText) dialogView.findViewById(R.id.etDescripcion)).getText().toString().trim();
                String stockStr = ((android.widget.EditText) dialogView.findViewById(R.id.etStock)).getText().toString().trim();
                int stock = stockStr.isEmpty() ? 0 : Integer.parseInt(stockStr);

                if (titulo.isEmpty() || autor.isEmpty() || codigo.isEmpty()) {
                    Toast.makeText(getContext(), "Título, autor y código son obligatorios", Toast.LENGTH_SHORT).show();
                    return;
                }

                Libro nuevoLibro = new Libro();
                nuevoLibro.setTitulo(titulo);
                nuevoLibro.setAutor(autor);
                nuevoLibro.setCodigo(codigo);
                nuevoLibro.setCategoria(categoria);
                nuevoLibro.setDescripcion(descripcion);
                nuevoLibro.setStock(stock);
                nuevoLibro.setEstado("DISPONIBLE");

                LibroService api = ApiClient.getClient().create(LibroService.class);
                api.createLibro(nuevoLibro).enqueue(new retrofit2.Callback<Libro>() {
                    @Override
                    public void onResponse(Call<Libro> call, Response<Libro> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(getContext(), "Libro agregado", Toast.LENGTH_SHORT).show();
                            cargarLibros();
                            dialog.dismiss();
                        } else {
                            String errorMsg = "Error al agregar libro";
                            if (response.errorBody() != null) {
                                try {
                                    errorMsg += ": " + response.errorBody().string();
                                } catch (Exception e) {}
                            }
                            Toast.makeText(getContext(), errorMsg, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Libro> call, Throwable t) {
                        Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        dialog.show();
    }

    private void cargarLibros() {
        LibroService api = ApiClient.getClient().create(LibroService.class);
        Call<List<Libro>> call = api.getLibros();

        call.enqueue(new Callback<List<Libro>>() {
            @Override
            public void onResponse(Call<List<Libro>> call, Response<List<Libro>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Libro> libros = response.body();
                    Toast.makeText(getContext(), "Libros recibidos: " + libros.size(), Toast.LENGTH_SHORT).show();
                    adapter = new LibroAdapter(libros);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Error al cargar libros", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Libro>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
