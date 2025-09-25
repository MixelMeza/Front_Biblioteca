package com.example.biblioteca.ui.pedidos;
import com.example.biblioteca.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;
import com.example.biblioteca.PedidosAdapter;
import com.example.biblioteca.Pedido;
import com.example.biblioteca.ui.interfaces.PedidoService;
import com.example.biblioteca.ui.utils.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class MisPedidosFragment extends Fragment {
    private RecyclerView recyclerView;
    private PedidosAdapter adapter;
    private List<Pedido> pedidosOriginal;
    private SwipeRefreshLayout swipeRefreshLayout;
    private androidx.appcompat.widget.SearchView searchView;
    private android.widget.Spinner spinnerEstado;
    private android.widget.LinearLayout layoutFiltroAdmin;

    // Filtrar por estado para el spinner admin
    private void filtrarPorEstado(String estado) {
        if (pedidosOriginal == null || adapter == null) return;
        adapter.setPedidos(pedidosOriginal);
    }

    // Filtrar por texto
    private void filtrarPedidos(String texto) {
        if (pedidosOriginal == null || adapter == null) return;
        if (texto == null || texto.trim().isEmpty()) {
            adapter.setPedidos(pedidosOriginal);
            return;
        }
        String lower = texto.toLowerCase();
        java.util.List<Pedido> filtrados = new java.util.ArrayList<>();
        for (Pedido p : pedidosOriginal) {
            boolean match = false;
            if (p.libro != null) {
                if ((p.libro.titulo != null && p.libro.titulo.toLowerCase().contains(lower)) ||
                    (p.libro.autor != null && p.libro.autor.toLowerCase().contains(lower))) {
                    match = true;
                }
            }
            if ((p.usuario != null && p.usuario.toLowerCase().contains(lower)) ||
                (p.estado != null && p.estado.toLowerCase().contains(lower))) {
                match = true;
            }
            if (match) {
                filtrados.add(p);
            }
        }
        adapter.setPedidos(filtrados);
    }

    // Verifica si el usuario es admin
    private boolean esAdmin() {
        android.content.SharedPreferences prefs = requireActivity().getSharedPreferences("session", android.content.Context.MODE_PRIVATE);
        String rol = prefs.getString("rol", "usuario");
        return rol.equalsIgnoreCase("admin");
    }

    // Obtiene el usuario actual
    private String obtenerUsuarioActual() {
        android.content.SharedPreferences prefs = requireActivity().getSharedPreferences("session", android.content.Context.MODE_PRIVATE);
        return prefs.getString("usuario", "");
    }

    // Carga los pedidos desde el backend
    public void cargarPedidos() {
        PedidoService api = ApiClient.getClient().create(PedidoService.class);
        if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(true);
        Call<List<Pedido>> call = api.getPedidos(); // Obtener todos los pedidos
        call.enqueue(new Callback<List<Pedido>>() {
            @Override
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Pedido> pedidosResponse = response.body();
                    pedidosOriginal = pedidosResponse;
                    if (adapter == null) {
                        adapter = new PedidosAdapter(pedidosOriginal);
                        adapter.setModoAdmin(esAdmin());
                        adapter.setOnPedidosRefreshListener(MisPedidosFragment.this::cargarPedidos);
                        recyclerView.setAdapter(adapter);
                    } else {
                        adapter.setPedidos(pedidosOriginal);
                        adapter.setModoAdmin(esAdmin());
                        adapter.setOnPedidosRefreshListener(MisPedidosFragment.this::cargarPedidos);
                    }
                    android.widget.Toast.makeText(requireContext(), "Lista de pedidos actualizada", android.widget.Toast.LENGTH_SHORT).show();
                }
                if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<List<Pedido>> call, Throwable t) {
                if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mis_pedidos, container, false);
        recyclerView = view.findViewById(R.id.rvMisPedidos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchView = view.findViewById(R.id.searchViewPedidos);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        spinnerEstado = view.findViewById(R.id.spinnerEstado);
        layoutFiltroAdmin = view.findViewById(R.id.layoutFiltroAdmin);

        // Inicializar filtro admin solo si el usuario es admin
        if (esAdmin()) {
            layoutFiltroAdmin.setVisibility(View.VISIBLE);
            String[] estados = {"Todos", "Pendiente", "Entregado", "Cancelado"};
            android.widget.ArrayAdapter<String> adapterSpinner = new android.widget.ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, estados);
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerEstado.setAdapter(adapterSpinner);
            spinnerEstado.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                    filtrarPorEstado(estados[position]);
                }
                @Override
                public void onNothingSelected(android.widget.AdapterView<?> parent) {}
            });
        } else {
            layoutFiltroAdmin.setVisibility(View.GONE);
        }

        // SwipeRefreshLayout
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setOnRefreshListener(this::cargarPedidos);
        }

        // Configurar el callback para refrescar pedidos
        cargarPedidos();

        // Lógica de búsqueda
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filtrarPedidos(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filtrarPedidos(newText);
                return true;
            }
        });
        return view;
    }
}
