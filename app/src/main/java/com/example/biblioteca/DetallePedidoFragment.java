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
        ((TextView)view.findViewById(R.id.tvDetalleNumero)).setText("Detalles del Pedido " + pedido.numero);
        ((TextView)view.findViewById(R.id.tvDetalleEstado)).setText(pedido.estado);
        Spinner spinner = view.findViewById(R.id.spinnerEstado);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.estados_pedido, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        // Simular selección actual
        spinner.setSelection(getEstadoIndex(pedido.estado));
        // Cliente
        ((TextView)view.findViewById(R.id.tvClienteNombre)).setText(pedido.usuario);
        ((TextView)view.findViewById(R.id.tvClienteEmail)).setText("maria.garcia@email.com");
        ((TextView)view.findViewById(R.id.tvClienteTelefono)).setText("+34 612 345 678");
        ((TextView)view.findViewById(R.id.tvClienteDireccion)).setText(pedido.direccion);
        ((TextView)view.findViewById(R.id.tvClienteNotas)).setText("Entregar en horario de mañana");
        // Pedido
        ((TextView)view.findViewById(R.id.tvPedidoFecha)).setText(pedido.fecha);
        ((TextView)view.findViewById(R.id.tvPedidoEntrega)).setText("2024-01-17");
        ((TextView)view.findViewById(R.id.tvPedidoTotal)).setText(pedido.total);
        return view;
    }
    private int getEstadoIndex(String estado) {
        String[] estados = {"Pendiente", "En Proceso", "En Camino", "Entregado", "Cancelado"};
        for (int i = 0; i < estados.length; i++) {
            if (estados[i].equalsIgnoreCase(estado)) return i;
        }
        return 0;
    }
}
