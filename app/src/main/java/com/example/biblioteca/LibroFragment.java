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

public class LibroFragment extends Fragment{
    private RecyclerView recyclerView;
    private LibroAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_libro, container, false);
        recyclerView = view.findViewById(R.id.recyclerLibros);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cargarLibros();
        return view;
    }

    private void cargarLibros() {
        LibroService api = ApiClient.getClient().create(LibroService.class);
        Call<List<Libro>> call = api.getLibros();

        call.enqueue(new Callback<List<Libro>>() {
            @Override
            public void onResponse(Call<List<Libro>> call, Response<List<Libro>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new LibroAdapter(response.body());
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
