    // Ejemplo: construir la descripción al crear el pedido
    // String descripcion =
    //     "Direccion:" + direccion + "\n" +
    //     "Telefono:" + telefono + "\n" +
    //     "Notas:" + notas + "\n" +
    //     "MetodoPago:" + metodoPago + "\n" +
    //     "EstadoPago:" + estadoPago + "\n" +
    //     "Prioridad:" + prioridad;
    // pedido.descripcion = descripcion;
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
    private Runnable onPedidoChanged;
    public DetallePedidoFragment(Pedido pedido) {
        this.pedido = pedido;
    }
    public DetallePedidoFragment(Pedido pedido, Runnable onPedidoChanged) {
        this.pedido = pedido;
        this.onPedidoChanged = onPedidoChanged;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_pedido, container, false);
        // Extraer datos de descripcion
        String direccion = "", telefono = "", notas = "", metodoPago = "", estadoPago = "", prioridad = "";
        if (pedido.descripcion != null) {
            String[] partes = pedido.descripcion.split("\n");
            for (String parte : partes) {
                if (parte.startsWith("Direccion:")) direccion = parte.replace("Direccion:", "").trim();
                else if (parte.startsWith("Telefono:")) telefono = parte.replace("Telefono:", "").trim();
                else if (parte.startsWith("Notas:")) notas = parte.replace("Notas:", "").trim();
                else if (parte.startsWith("MetodoPago:")) metodoPago = parte.replace("MetodoPago:", "").trim();
                else if (parte.startsWith("EstadoPago:")) estadoPago = parte.replace("EstadoPago:", "").trim();
                else if (parte.startsWith("Prioridad:")) prioridad = parte.replace("Prioridad:", "").trim();
            }
        }
        // Datos adicionales
        ((TextView)view.findViewById(R.id.tvExtraDireccion)).setText("Dirección: " + direccion);
        ((TextView)view.findViewById(R.id.tvExtraTelefono)).setText("Teléfono: " + telefono);
        ((TextView)view.findViewById(R.id.tvExtraNotas)).setText("Notas: " + notas);
        ((TextView)view.findViewById(R.id.tvExtraMetodoPago)).setText("Método de Pago: " + metodoPago);
        ((TextView)view.findViewById(R.id.tvExtraEstadoPago)).setText("Estado de Pago: " + estadoPago);
        ((TextView)view.findViewById(R.id.tvExtraPrioridad)).setText("Prioridad: " + prioridad);

        // Información del libro
        if (pedido.libro != null) {
            ((TextView)view.findViewById(R.id.tvLibroTitulo)).setText("Título: " + (pedido.libro.titulo != null ? pedido.libro.titulo : ""));
            ((TextView)view.findViewById(R.id.tvLibroAutor)).setText("Autor: " + (pedido.libro.autor != null ? pedido.libro.autor : ""));
            ((TextView)view.findViewById(R.id.tvLibroCategoria)).setText("Categoría: " + (pedido.libro.categoria != null ? pedido.libro.categoria : ""));
            ((TextView)view.findViewById(R.id.tvLibroCodigo)).setText("Código: " + (pedido.libro.codigo != null ? pedido.libro.codigo : ""));
            ((TextView)view.findViewById(R.id.tvLibroPrecio)).setText("Precio: $" + (pedido.libro.stock != null ? pedido.libro.stock : "0"));
            ((TextView)view.findViewById(R.id.tvPedidoTotal)).setText("Precio: $" + (pedido.libro.stock != null ? pedido.libro.stock : "0"));
        }

        // Información del cliente
        ((TextView)view.findViewById(R.id.tvClienteNombre)).setText(pedido.usuario);
        ((TextView)view.findViewById(R.id.tvClienteEmail)).setText(pedido.libro != null ? pedido.libro.autor  : "");
        ((TextView)view.findViewById(R.id.tvClienteTelefono)).setText(telefono);
        ((TextView)view.findViewById(R.id.tvClienteDireccion)).setText(direccion);

        // Información del pedido (fecha y entrega)
        ((TextView)view.findViewById(R.id.tvPedidoFecha)).setText("Fecha: " + (pedido.fecha != null ? pedido.fecha : "-"));
        String fechaEntrega = "-";
        if (pedido.estado != null && pedido.estado.equalsIgnoreCase("EN_PROCESO") && pedido.fecha != null) {
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
                java.util.Date fechaPedido = sdf.parse(pedido.fecha);
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(fechaPedido);
                cal.add(java.util.Calendar.DAY_OF_MONTH, 1);
                fechaEntrega = sdf.format(cal.getTime());
            } catch (Exception e) {
                fechaEntrega = "-";
            }
        }
        ((TextView)view.findViewById(R.id.tvPedidoEntrega)).setText("Entrega Estimada: " + fechaEntrega);

        // Estado y spinner
        ((TextView)view.findViewById(R.id.tvDetalleNumero)).setText("Detalles del Pedido #" + pedido.idPedido);
        TextView estadoTag = view.findViewById(R.id.tvDetalleEstado);
        estadoTag.setText(pedido.estado);
        setEstadoColor(estadoTag, pedido.estado);
        Spinner spinner = view.findViewById(R.id.spinnerEstado);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.estados_pedido, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(getEstadoIndex(pedido.estado));

        // Obtener usuario actual
        android.content.SharedPreferences prefs = requireActivity().getSharedPreferences("session", android.content.Context.MODE_PRIVATE);
        String usuarioActual = prefs.getString("usuario", "");

        // Si el usuario es el dueño, solo puede cancelar
        if (pedido.usuario != null && pedido.usuario.equalsIgnoreCase(usuarioActual)) {
            spinner.setEnabled(false);
            // Botón cancelar
            View btnCancelar = view.findViewById(R.id.btnCancelarPedido);
            btnCancelar.setVisibility(View.VISIBLE);
            btnCancelar.setOnClickListener(v -> {
                android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(requireContext())
                    .setTitle("Cancelar Pedido")
                    .setMessage("¿Estás seguro de que quieres cancelar este pedido?")
                    .setPositiveButton("Sí", (dialogInterface, which) -> {
                        com.example.biblioteca.ui.interfaces.PedidoService api = com.example.biblioteca.ui.utils.ApiClient.getClient().create(com.example.biblioteca.ui.interfaces.PedidoService.class);
                        Pedido pedidoCancelado = new Pedido();
                        pedidoCancelado.idPedido = pedido.idPedido;
                        pedidoCancelado.fecha = pedido.fecha;
                        pedidoCancelado.descripcion = pedido.descripcion;
                        pedidoCancelado.estado = "CANCELADO";
                        pedidoCancelado.usuario = pedido.usuario;
                        pedidoCancelado.libro = pedido.libro;
                        retrofit2.Call<Pedido> call = api.updatePedido(String.valueOf(pedido.idPedido), pedidoCancelado);
                        call.enqueue(new retrofit2.Callback<Pedido>() {
                            @Override
                            public void onResponse(retrofit2.Call<Pedido> call, retrofit2.Response<Pedido> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    pedido.estado = response.body().estado;
                                    estadoTag.setText(pedido.estado);
                                    setEstadoColor(estadoTag, pedido.estado);
                                    android.widget.Toast.makeText(requireContext(), "Pedido cancelado exitosamente", android.widget.Toast.LENGTH_SHORT).show();
                                    requireActivity().getSupportFragmentManager().popBackStack();
                                    if (onPedidoChanged != null) onPedidoChanged.run();
                                } else {
                                    android.widget.Toast.makeText(requireContext(), "Error al cancelar el pedido", android.widget.Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(retrofit2.Call<Pedido> call, Throwable t) {
                                android.widget.Toast.makeText(requireContext(), "Error de conexión", android.widget.Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("No", null)
                    .create();
                dialog.setOnShowListener(d -> {
                    dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(0xFF388E3C); // Verde
                    dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(0xFFF44336); // Rojo
                });
                dialog.show();
            });
        } else {
            // Si es admin, puede editar el estado
            spinner.setEnabled(true);
            spinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(android.widget.AdapterView<?> parent, View v, int pos, long id) {
                    String nuevoEstado = parent.getItemAtPosition(pos).toString();
                    if (!nuevoEstado.equalsIgnoreCase(pedido.estado)) {
                        android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(requireContext())
                            .setTitle("Editar Estado")
                            .setMessage("¿Confirmas el cambio de estado a '" + nuevoEstado + "'?")
                            .setPositiveButton("Sí", (dialogInterface, which) -> {
                                com.example.biblioteca.ui.interfaces.PedidoService api = com.example.biblioteca.ui.utils.ApiClient.getClient().create(com.example.biblioteca.ui.interfaces.PedidoService.class);
                                Pedido pedidoActualizado = new Pedido();
                                pedidoActualizado.idPedido = pedido.idPedido;
                                pedidoActualizado.fecha = pedido.fecha;
                                pedidoActualizado.descripcion = pedido.descripcion;
                                pedidoActualizado.estado = nuevoEstado;
                                pedidoActualizado.usuario = pedido.usuario;
                                pedidoActualizado.libro = pedido.libro;
                                // Si el estado es EN_PROCESO, calcular fecha estimada (1 día después)
                                String fechaEntrega = "-";
                                if (nuevoEstado.equalsIgnoreCase("EN_PROCESO") && pedido.fecha != null) {
                                    try {
                                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
                                        java.util.Date fechaPedido = sdf.parse(pedido.fecha);
                                        java.util.Calendar cal = java.util.Calendar.getInstance();
                                        cal.setTime(fechaPedido);
                                        cal.add(java.util.Calendar.DAY_OF_MONTH, 1);
                                        fechaEntrega = sdf.format(cal.getTime());
                                    } catch (Exception e) {
                                        fechaEntrega = "-";
                                    }
                                }
                                // Guardar la fecha estimada en el campo descripcion si quieres persistirla
                                if (!fechaEntrega.equals("-")) {
                                    pedidoActualizado.descripcion = pedido.descripcion + "\nEntrega estimada: " + fechaEntrega;
                                }
                                retrofit2.Call<Pedido> call = api.updatePedido(String.valueOf(pedido.idPedido), pedidoActualizado);
                                call.enqueue(new retrofit2.Callback<Pedido>() {
                                    @Override
                                    public void onResponse(retrofit2.Call<Pedido> call, retrofit2.Response<Pedido> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            pedido.estado = response.body().estado;
                                            estadoTag.setText(pedido.estado);
                                            setEstadoColor(estadoTag, pedido.estado);
                                            android.widget.Toast.makeText(requireContext(), "Estado actualizado exitosamente", android.widget.Toast.LENGTH_SHORT).show();
                                            requireActivity().getSupportFragmentManager().popBackStack();
                                            if (onPedidoChanged != null) onPedidoChanged.run();
                                        } else {
                                            android.widget.Toast.makeText(requireContext(), "Error al actualizar el estado", android.widget.Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    @Override
                                    public void onFailure(retrofit2.Call<Pedido> call, Throwable t) {
                                        android.widget.Toast.makeText(requireContext(), "Error de conexión", android.widget.Toast.LENGTH_SHORT).show();
                                    }
                                });
                            })
                            .setNegativeButton("No", null)
                            .create();
                        dialog.setOnShowListener(d -> {
                            dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(0xFF388E3C); // Verde
                            dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(0xFFF44336); // Rojo
                        });
                        dialog.show();
                    }
                }
                @Override
                public void onNothingSelected(android.widget.AdapterView<?> parent) {}
            });
            View btnCancelar = view.findViewById(R.id.btnCancelarPedido);
            btnCancelar.setVisibility(View.GONE);
        }
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
