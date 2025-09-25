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
    EditText etDireccion = view.findViewById(R.id.etDireccion);
    EditText etTelefono = view.findViewById(R.id.etTelefono);
    EditText etNotas = view.findViewById(R.id.etNotas);
    EditText etMetodoPago = view.findViewById(R.id.etMetodoPago);
    EditText etEstadoPago = view.findViewById(R.id.etEstadoPago);
    EditText etPrioridad = view.findViewById(R.id.etPrioridad);

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
            String direccion = etDireccion.getText().toString();
            String telefono = etTelefono.getText().toString();
            String notas = etNotas.getText().toString();
            String metodoPago = etMetodoPago.getText().toString();
            String estadoPago = etEstadoPago.getText().toString();
            String prioridad = etPrioridad.getText().toString();
            String estado =  "PENDIENTE";
            Pedido pedido = new Pedido();
            pedido.libro = new Pedido.Libro();
            // Obtener el id del libro si se recibió por argumentos
            if (args != null && args.getSerializable("libro") != null) {
                com.example.biblioteca.ui.model.Libro libroArg = (com.example.biblioteca.ui.model.Libro) args.getSerializable("libro");
                if (libroArg != null) {
                    pedido.libro.idLibro = libroArg.getIdLibro();
                }
            }
            // Construir la descripción con todos los campos
            String descripcion =
                "Direccion:" + direccion + "\n" +
                "Telefono:" + telefono + "\n" +
                "Notas:" + notas + "\n" +
                "MetodoPago:" + metodoPago + "\n" +
                "EstadoPago:" + estadoPago + "\n" +
                "Prioridad:" + prioridad;
            pedido.descripcion = descripcion;
            pedido.estado = estado;
            pedido.usuario = obtenerUsuarioActual();
            // Registrar fecha actual
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
            String fechaActual = sdf.format(new java.util.Date());
            pedido.fecha = fechaActual;
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
