package com.example.bibiliogo.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bibiliogo.R;
import com.example.bibiliogo.model.Pedido;
import com.example.bibiliogo.network.ApiClient;
import com.example.bibiliogo.network.PedidoService;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PedidosAdminFragment extends Fragment {
    private RecyclerView rvPedidosAdmin;
    private PedidosAdminAdapter adapter;
    private PedidoService pedidoService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pedidos_admin, container, false);
        rvPedidosAdmin = view.findViewById(R.id.rvPedidosAdmin);
        rvPedidosAdmin.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PedidosAdminAdapter(new ArrayList<>());
        rvPedidosAdmin.setAdapter(adapter);
        pedidoService = ApiClient.getClient().create(PedidoService.class);
        cargarPedidos();
        return view;
    }

    private void cargarPedidos() {
        pedidoService.getPedidos().enqueue(new Callback<List<Pedido>>() {
            @Override
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setPedidos(response.body());
                } else {
                    Toast.makeText(getContext(), "Error al cargar pedidos", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Pedido>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de red al cargar pedidos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
