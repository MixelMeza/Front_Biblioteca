package com.example.bibiliogo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bibiliogo.adapters.LibroUsuarioAdapter;
import com.example.bibiliogo.model.Libro;
import com.example.bibiliogo.model.Pedido;
import com.example.bibiliogo.network.ApiClient;
import com.example.bibiliogo.network.LibroService;
import com.example.bibiliogo.network.PedidoService;
import com.example.bibiliogo.ui.pedidos.PedidoFormDialog;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibrosUsuarioFragment extends Fragment {
    private RecyclerView rvLibrosUsuario;
    private LibroUsuarioAdapter adapter;
    private LibroService libroService;
    private PedidoService pedidoService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_libros_usuario, container, false);
        rvLibrosUsuario = view.findViewById(R.id.rvLibrosUsuario);
        rvLibrosUsuario.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LibroUsuarioAdapter(new ArrayList<>());
        rvLibrosUsuario.setAdapter(adapter);

        libroService = ApiClient.getClient().create(LibroService.class);
        pedidoService = ApiClient.getClient().create(PedidoService.class);
        adapter.setOnPedirClickListener(libro -> mostrarDialogoPedido(libro));
        cargarLibros();

        return view;
    }

    private void mostrarDialogoPedido(Libro libro) {
        PedidoFormDialog dialog = new PedidoFormDialog();
        dialog.setTituloLibro(libro.getTitulo());
        dialog.setOnPedidoFormListener((nombre, direccion, telefono, descripcion) -> {
            Pedido pedido = new Pedido();
            pedido.setLibro(libro);
            pedido.setNombre(nombre);
            pedido.setDireccion(direccion);
            pedido.setTelefono(telefono);
            pedido.setDescripcion(descripcion);
            pedido.setEstado("PENDIENTE");
            // Asignar fecha actual en formato yyyy-MM-dd
            String fechaActual = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(new java.util.Date());
            pedido.setFecha(fechaActual);
            // Obtener usuario logueado
            String usuario = requireActivity().getSharedPreferences("user_prefs", 0).getString("username", "");
            pedido.setUsuario(usuario);
            crearPedido(pedido);
        });
        dialog.show(getParentFragmentManager(), "crearPedido");
    }

    private void crearPedido(Pedido pedido) {
        pedidoService.createPedido(pedido).enqueue(new retrofit2.Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Ir a MisPedidosFragment
                    requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, new MisPedidosFragment())
                        .commit();
                }
            }

            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {
                // Manejar error (puedes mostrar un Toast si quieres)
            }
        });
    }

    private void cargarLibros() {
        libroService.getLibros().enqueue(new Callback<List<Libro>>() {
            @Override
            public void onResponse(Call<List<Libro>> call, Response<List<Libro>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setLibros(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Libro>> call, Throwable t) {
                // Manejar error (puedes mostrar un Toast si quieres)
            }
        });
    }
}
