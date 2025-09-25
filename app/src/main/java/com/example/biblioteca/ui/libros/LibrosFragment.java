
package com.example.biblioteca.ui.libros;
import com.example.biblioteca.R;
import com.example.biblioteca.ui.pedidos.CrearPedidoFragment;
import com.example.biblioteca.ui.libros.LibrosAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.biblioteca.ui.interfaces.LibroService;
import com.example.biblioteca.ui.utils.ApiClient;
import com.example.biblioteca.Pedido;
import com.example.biblioteca.ui.model.Libro;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibrosFragment extends Fragment {
    private RecyclerView recyclerView;
    private LibrosAdapter adapter;
    private List<Libro> libros;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_libros, container, false);
        recyclerView = view.findViewById(R.id.rvLibros);
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
                    libros = response.body();
                    adapter = new LibrosAdapter(libros, libro -> abrirCrearPedido(libro));
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<Libro>> call, Throwable t) {}
        });
    }

    private void abrirCrearPedido(Libro libro) {
        CrearPedidoFragment fragment = new CrearPedidoFragment();
        Bundle args = new Bundle();
        args.putSerializable("libro", libro);
        fragment.setArguments(args);
        requireActivity().getSupportFragmentManager()
            .beginTransaction()
            .replace(android.R.id.content, fragment)
            .addToBackStack(null)
            .commit();
    }
}
