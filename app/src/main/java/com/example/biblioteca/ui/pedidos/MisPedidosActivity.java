package com.example.biblioteca.ui.pedidos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.biblioteca.R;
import com.example.biblioteca.ui.pedidos.adapters.PedidosAdapter;
import com.example.biblioteca.ui.interfaces.PedidoService;
import com.example.biblioteca.ui.model.Pedido;
import com.example.biblioteca.ui.utils.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisPedidosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PedidosAdapter adapter;
    private List<Pedido> pedidosList;
    private PedidoService pedidoService;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_pedidos);

        recyclerView = findViewById(R.id.recyclerViewPedidos);
        btnBack = findViewById(R.id.btnBack);

        pedidoService = ApiClient.getClient().create(PedidoService.class);

        initializeUI();
        cargarPedidos();

        btnBack.setOnClickListener(v -> finish());
    }

    private void initializeUI() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        pedidosList = new ArrayList<>();
        adapter = new PedidosAdapter(pedidosList);
        recyclerView.setAdapter(adapter);
    }

    private void cargarPedidos() {
        Call<List<Pedido>> call = pedidoService.getPedidos();
        call.enqueue(new Callback<List<Pedido>>() {
            @Override
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    pedidosList.clear();
                    pedidosList.addAll(response.body()); // ✅ Mostrar TODOS los pedidos
                    adapter.notifyDataSetChanged();

                    if (pedidosList.isEmpty()) {
                        Toast.makeText(MisPedidosActivity.this,
                                "No tienes pedidos realizados", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MisPedidosActivity.this,
                            "Error al cargar pedidos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pedido>> call, Throwable t) {
                Toast.makeText(MisPedidosActivity.this,
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
