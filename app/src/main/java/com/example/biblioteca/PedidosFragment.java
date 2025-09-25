package com.example.biblioteca;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ArrayAdapter;
import java.util.List;
import android.widget.Toast;
import com.example.biblioteca.ui.interfaces.PedidoService;
import com.example.biblioteca.ui.utils.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PedidosFragment extends Fragment {
    private RecyclerView recyclerView;
    private PedidosAdapter adapter;
    private List<Pedido> pedidosOriginal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pedidos, container, false);
        recyclerView = view.findViewById(R.id.rvPedidos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        android.widget.EditText etBuscar = view.findViewById(R.id.etBuscarPedido);
        android.widget.EditText etPrecio = view.findViewById(R.id.etFiltroPrecio);
        android.widget.EditText etFecha = view.findViewById(R.id.etFiltroFecha);
        android.widget.Spinner spinnerEstado = view.findViewById(R.id.spinnerFiltroEstado);
        ArrayAdapter<CharSequence> estadoAdapter = ArrayAdapter.createFromResource(getContext(), R.array.estados_pedido_filtro, android.R.layout.simple_spinner_item);
        estadoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(estadoAdapter);

        cargarPedidos();

        // Filtros y búsqueda
        android.text.TextWatcher watcher = new android.text.TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) { filtrar(); }
            public void afterTextChanged(android.text.Editable s) { filtrar(); }
        };
        etBuscar.addTextChangedListener(watcher);
        etPrecio.addTextChangedListener(watcher);
        etFecha.addTextChangedListener(watcher);
        spinnerEstado.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            public void onItemSelected(android.widget.AdapterView<?> parent, View v, int pos, long id) { filtrar(); }
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarPedidos();
    }

    private void cargarPedidos() {
        PedidoService api = ApiClient.getClient().create(PedidoService.class);
        Call<List<Pedido>> call = api.getPedidos();
        call.enqueue(new Callback<List<Pedido>>() {
            @Override
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    pedidosOriginal = response.body();
                    adapter = new PedidosAdapter(pedidosOriginal);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Error al cargar pedidos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pedido>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filtrar() {
        if (pedidosOriginal == null) return;
        View view = getView();
        if (view == null) return;
        android.widget.EditText etBuscar = view.findViewById(R.id.etBuscarPedido);
        android.widget.EditText etPrecio = view.findViewById(R.id.etFiltroPrecio);
        android.widget.EditText etFecha = view.findViewById(R.id.etFiltroFecha);
        android.widget.Spinner spinnerEstado = view.findViewById(R.id.spinnerFiltroEstado);

        String query = etBuscar.getText().toString().toLowerCase();
        String precioStr = etPrecio.getText().toString();
        String fechaStr = etFecha.getText().toString();
        String estadoFiltro = spinnerEstado.getSelectedItem().toString();

        List<Pedido> filtrados = new java.util.ArrayList<>();
        for (Pedido p : pedidosOriginal) {
            boolean match = true;
            if (!query.isEmpty()) {
                boolean usuarioMatch = p.usuario != null && p.usuario.toLowerCase().contains(query);
                boolean libroMatch = p.libro != null && p.libro.titulo != null && p.libro.titulo.toLowerCase().contains(query);
                match &= (usuarioMatch || libroMatch);
            }
            if (!precioStr.isEmpty()) {
                try {
                    int precioMax = Integer.parseInt(precioStr);
                    match &= (p.libro != null && p.libro.stock != null && p.libro.stock <= precioMax);
                } catch (NumberFormatException e) {}
            }
            if (!fechaStr.isEmpty()) {
                match &= (p.fecha != null && p.fecha.startsWith(fechaStr));
            }
            if (!estadoFiltro.equals("Todos")) {
                match &= (p.estado != null && p.estado.equalsIgnoreCase(estadoFiltro));
            }
            if (match) filtrados.add(p);
        }
        adapter = new PedidosAdapter(filtrados);
        recyclerView.setAdapter(adapter);
    }
}
