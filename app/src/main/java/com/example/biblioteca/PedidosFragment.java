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
import java.util.ArrayList;
import java.util.List;

public class PedidosFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pedidos, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rvPedidos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Pedido> pedidos = getFakePedidos();
        PedidosAdapter adapter = new PedidosAdapter(pedidos);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private List<Pedido> getFakePedidos() {
        List<Pedido> pedidos = new ArrayList<>();
        pedidos.add(new Pedido("ORD-001", "Entregado", "María García López", "2024-01-15", "2 artículos", "Calle Mayor 123, 28001 Madrid", "€45.50"));
        pedidos.add(new Pedido("ORD-002", "En Proceso", "Juan Pérez Martín", "2024-01-14", "1 artículo", "Avenida de la Paz 45, 28002 Madrid", "€120.00"));
        return pedidos;
    }
}
