package com.example.biblioteca;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DetallePedidoFragment extends Fragment {
    private Pedido pedido;
    public DetallePedidoFragment(Pedido pedido) {
        this.pedido = pedido;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_pedido, container, false);
        ((TextView)view.findViewById(R.id.tvDetalleNumero)).setText("Detalles del Pedido #" + pedido.idPedido);
        TextView estadoTag = view.findViewById(R.id.tvDetalleEstado);
        estadoTag.setText(pedido.estado);
        setEstadoColor(estadoTag, pedido.estado);
        Spinner spinner = view.findViewById(R.id.spinnerEstado);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.estados_pedido, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(getEstadoIndex(pedido.estado));
        spinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View v, int pos, long id) {
                String nuevoEstado = parent.getItemAtPosition(pos).toString();
                if (!nuevoEstado.equalsIgnoreCase(pedido.estado)) {
                    com.example.biblioteca.ui.interfaces.PedidoService api = com.example.biblioteca.ui.utils.ApiClient.getClient().create(com.example.biblioteca.ui.interfaces.PedidoService.class);
                    // Actualizar el objeto pedido local y enviar al backend
                    Pedido pedidoActualizado = new Pedido();
                    pedidoActualizado.idPedido = pedido.idPedido;
                    pedidoActualizado.fecha = pedido.fecha;
                    pedidoActualizado.descripcion = pedido.descripcion;
                    pedidoActualizado.estado = nuevoEstado;
                    pedidoActualizado.usuario = pedido.usuario;
                    pedidoActualizado.libro = pedido.libro;
                    retrofit2.Call<Pedido> call = api.updatePedido(String.valueOf(pedido.idPedido), pedidoActualizado);
                    call.enqueue(new retrofit2.Callback<Pedido>() {
                        @Override
                        public void onResponse(retrofit2.Call<Pedido> call, retrofit2.Response<Pedido> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                pedido.estado = response.body().estado;
                                estadoTag.setText(pedido.estado);
                                setEstadoColor(estadoTag, pedido.estado);
                            }
                        }
                        @Override
                        // Manejar error
                        public void onFailure(retrofit2.Call<Pedido> call, Throwable t) {
                        }
                    });
                }
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
        // Cliente
        ((TextView)view.findViewById(R.id.tvClienteNombre)).setText(pedido.usuario);
        ((TextView)view.findViewById(R.id.tvClienteEmail)).setText(pedido.libro != null ? pedido.libro.autor + "@email.com" : "");
        ((TextView)view.findViewById(R.id.tvClienteTelefono)).setText("+34 612 345 678");
        ((TextView)view.findViewById(R.id.tvClienteDireccion)).setText(pedido.descripcion);
        ((TextView)view.findViewById(R.id.tvClienteNotas)).setText(pedido.libro != null ? pedido.libro.titulo : "");
        // Pedido
        ((TextView)view.findViewById(R.id.tvPedidoFecha)).setText(pedido.fecha);
        ((TextView)view.findViewById(R.id.tvPedidoEntrega)).setText("2024-01-17");
        ((TextView)view.findViewById(R.id.tvPedidoTotal)).setText(pedido.libro != null && pedido.libro.stock != null ? "$" + pedido.libro.stock : "");
        return view;
    }
    private int getEstadoIndex(String estado) {
        String[] estados = {"PENDIENTE", "EN_PROCESO", "COMPLETADO", "CANCELADO"};
        for (int i = 0; i < estados.length; i++) {
            if (estados[i].equalsIgnoreCase(estado)) return i;
        }
        return 0;
    }

    private void setEstadoColor(TextView estadoTag, String estado) {
        int color;
        switch (estado.toUpperCase()) {
            case "PENDIENTE":
                color = 0xFFFFC107; // Amarillo
                break;
            case "EN_PROCESO":
                color = 0xFF2196F3; // Azul
                break;
            case "COMPLETADO":
                color = 0xFF4CAF50; // Verde
                break;
            case "CANCELADO":
                color = 0xFFF44336; // Rojo
                break;
            default:
                color = 0xFFBDBDBD; // Gris
        }
        estadoTag.setBackgroundColor(color);
    }
}
