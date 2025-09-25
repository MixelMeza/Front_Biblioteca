package com.example.biblioteca.ui.pedidos;
import com.example.biblioteca.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.biblioteca.Pedido;
import com.example.biblioteca.ui.interfaces.PedidoService;
import com.example.biblioteca.ui.utils.ApiClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearPedidoFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crear_pedido, container, false);
        Button btnEnviar = view.findViewById(R.id.btnEnviarPedido);
        EditText etLibro = view.findViewById(R.id.etLibro);
        EditText etDescripcion = view.findViewById(R.id.etDescripcion);
        Spinner spinnerEstado = view.findViewById(R.id.spinnerEstadoCrear);

        // Si se recibe un libro por argumentos, rellenar el campo automáticamente
        Bundle args = getArguments();
        if (args != null && args.getSerializable("libro") != null) {
            com.example.biblioteca.ui.model.Libro libroArg = (com.example.biblioteca.ui.model.Libro) args.getSerializable("libro");
            if (libroArg != null) {
                etLibro.setText(libroArg.getTitulo());
            }
        }

        btnEnviar.setOnClickListener(v -> {
            String libro = etLibro.getText().toString();
            String descripcion = etDescripcion.getText().toString();
            Object estadoObj = spinnerEstado.getSelectedItem();
            String estado = estadoObj != null ? estadoObj.toString() : "PENDIENTE";
            Pedido pedido = new Pedido();
            pedido.libro = new Pedido.Libro();
            // Obtener el id del libro si se recibió por argumentos
            if (args != null && args.getSerializable("libro") != null) {
                com.example.biblioteca.ui.model.Libro libroArg = (com.example.biblioteca.ui.model.Libro) args.getSerializable("libro");
                if (libroArg != null) {
                    pedido.libro.idLibro = libroArg.getIdLibro();
                }
            }
            pedido.descripcion = descripcion;
            pedido.estado = estado;
            pedido.usuario = obtenerUsuarioActual();
            PedidoService api = ApiClient.getClient().create(PedidoService.class);
            Call<Pedido> call = api.createPedido(pedido);
            call.enqueue(new Callback<Pedido>() {
                @Override
                public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                    if (response.isSuccessful()) {
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                }
                @Override
                public void onFailure(Call<Pedido> call, Throwable t) {}
            });
        });
        return view;
    }

    private String obtenerUsuarioActual() {
        android.content.SharedPreferences prefs = requireActivity().getSharedPreferences("session", android.content.Context.MODE_PRIVATE);
        return prefs.getString("usuario", "");
    }
}
