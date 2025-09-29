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
import com.example.bibiliogo.adapters.PedidoUsuarioAdapter;
import com.example.bibiliogo.model.Pedido;
import com.example.bibiliogo.network.ApiClient;
import com.example.bibiliogo.network.PedidoService;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisPedidosFragment extends Fragment {
    private RecyclerView rvPedidosUsuario;
    private PedidoUsuarioAdapter adapter;
    private PedidoService pedidoService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mis_pedidos_usuario, container, false);
        rvPedidosUsuario = view.findViewById(R.id.rvPedidosUsuario);
        rvPedidosUsuario.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PedidoUsuarioAdapter(new ArrayList<>());
        rvPedidosUsuario.setAdapter(adapter);

        pedidoService = ApiClient.getClient().create(PedidoService.class);
        cargarPedidosUsuario();

        return view;
    }

    private void cargarPedidosUsuario() {
        String usuario = requireActivity().getSharedPreferences("user_prefs", 0).getString("username", "");
        pedidoService.getPedidos().enqueue(new Callback<List<Pedido>>() {
            @Override
            public void onResponse(Call<List<Pedido>> call, Response<List<Pedido>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Pedido> pedidosUsuario = new ArrayList<>();
                    for (Pedido p : response.body()) {
                        if (usuario.equals(p.getUsuario())) {
                            pedidosUsuario.add(p);
                        }
                    }
                    adapter.setPedidos(pedidosUsuario);
                }
            }

            @Override
            public void onFailure(Call<List<Pedido>> call, Throwable t) {
                // Manejar error (puedes mostrar un Toast si quieres)
            }
        });
    }
}
