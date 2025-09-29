package com.example.bibiliogo.admin;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.view.Gravity;
import android.graphics.drawable.GradientDrawable;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bibiliogo.R;
import com.example.bibiliogo.model.Pedido;
import com.example.bibiliogo.network.ApiClient;
import com.example.bibiliogo.network.PedidoService;
import com.example.bibiliogo.network.LibroService;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PedidosAdminAdapter extends RecyclerView.Adapter<PedidosAdminAdapter.PedidoViewHolder> {
    private List<Pedido> pedidos;
    private PedidoService pedidoService;
    private static final String[] ESTADOS = {"PENDIENTE", "EN PROCESO", "COMPLETADO"}; // Omitir CANCELADO
    private static final String[] ESTADOS_LIBRO = {"DISPONIBLE", "PRESTADO", "RESERVADO", "DADO_DE_BAJA"};

    private LibroService libroService;
    public PedidosAdminAdapter(List<Pedido> pedidos) {
        this.pedidos = pedidos;
        this.pedidoService = ApiClient.getClient().create(PedidoService.class);
        this.libroService = ApiClient.getClient().create(LibroService.class);
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido_admin, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        Pedido pedido = pedidos.get(position);
        holder.tvTitulo.setText(pedido.getLibro() != null ? pedido.getLibro().getTitulo() : "");
        holder.tvNombre.setText("Nombre: " + pedido.getNombre());
        // Tag visual para estado
        holder.tagEstado.setText(pedido.getEstado());
        int color;
        switch (pedido.getEstado()) {
            case "PENDIENTE":
                color = holder.itemView.getResources().getColor(android.R.color.holo_orange_dark);
                break;
            case "CANCELADO":
                color = holder.itemView.getResources().getColor(android.R.color.holo_red_dark);
                break;
            case "EN PROCESO":
                color = holder.itemView.getResources().getColor(android.R.color.holo_blue_dark);
                break;
            case "COMPLETADO":
                color = holder.itemView.getResources().getColor(android.R.color.holo_green_dark);
                break;
            default:
                color = holder.itemView.getResources().getColor(android.R.color.darker_gray);
        }
        holder.tagEstado.setBackgroundResource(R.drawable.bg_estado_tag);
        holder.tagEstado.setBackgroundTintList(android.content.res.ColorStateList.valueOf(color));
        // Detalle al hacer click
        holder.itemView.setOnClickListener(v -> mostrarDetalleDialog(holder.itemView.getContext(), pedido, position));
    // ...existing code...
    // (No cambiar estado de libro aquí, solo mostrar detalles y cambiar estado del pedido)
        // Botón cambiar estado (ocultar si está cancelado)
        if ("CANCELADO".equals(pedido.getEstado())) {
            holder.btnCambiarEstado.setVisibility(View.GONE);
        } else {
            holder.btnCambiarEstado.setVisibility(View.VISIBLE);
            holder.btnCambiarEstado.setOnClickListener(v -> mostrarDialogoEstados(holder.itemView.getContext(), pedido, position));
        }
    }

    // ...existing code...

    private void mostrarDetalleDialog(Context context, Pedido pedido, int position) {
        StringBuilder detalle = new StringBuilder();
        detalle.append("Nombre: ").append(pedido.getNombre()).append("\n");
        detalle.append("Dirección: ").append(pedido.getDireccion()).append("\n");
        detalle.append("Teléfono: ").append(pedido.getTelefono()).append("\n");
        detalle.append("Descripción: ").append(pedido.getDescripcion()).append("\n");
        detalle.append("Libro: ").append(pedido.getLibro() != null ? pedido.getLibro().getTitulo() : "").append("\n");
        detalle.append("Estado: ").append(pedido.getEstado());
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Detalle del pedido")
                .setMessage(detalle.toString());
        if (!"CANCELADO".equals(pedido.getEstado())) {
            builder.setNegativeButton("Cancelar pedido", (d, w) -> {
                pedidoService.cambiarEstado(pedido.getId(), "CANCELADO").enqueue(new Callback<Pedido>() {
                    @Override
                    public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            pedido.setEstado("CANCELADO");
                            notifyItemChanged(position);
                            Toast.makeText(context, "Pedido cancelado", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Error al cancelar pedido", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Pedido> call, Throwable t) {
                        Toast.makeText(context, "Error de red al cancelar pedido", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
        builder.setPositiveButton("Cerrar", null).show();
    }

    private void mostrarDialogoEstados(Context context, Pedido pedido, int position) {
        ArrayAdapter<String> adapterEstados = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, ESTADOS);
        new AlertDialog.Builder(context)
                .setTitle("Cambiar estado")
                .setAdapter(adapterEstados, (dialog, which) -> {
                    String nuevoEstado = ESTADOS[which];
                    pedidoService.cambiarEstado(pedido.getId(), nuevoEstado).enqueue(new Callback<Pedido>() {
                        @Override
                        public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                pedido.setEstado(nuevoEstado);
                                notifyItemChanged(position);
                                Toast.makeText(context, "Estado actualizado", Toast.LENGTH_SHORT).show();
                                // Cambiar estado del libro según el estado del pedido
                                if (pedido.getLibro() != null && pedido.getLibro().getIdLibro() != null) {
                                    String estadoLibro = null;
                                    if ("COMPLETADO".equals(nuevoEstado)) {
                                        estadoLibro = "DISPONIBLE";
                                    } else if ("EN PROCESO".equals(nuevoEstado)) {
                                        estadoLibro = "PRESTADO";
                                    } else if ("PENDIENTE".equals(nuevoEstado)) {
                                        estadoLibro = "RESERVADO";
                                    }
                                    if (estadoLibro != null) {
                                        libroService.cambiarEstado(pedido.getLibro().getIdLibro(), estadoLibro).enqueue(new Callback<com.example.bibiliogo.model.Libro>() {
                                            @Override
                                            public void onResponse(Call<com.example.bibiliogo.model.Libro> call, Response<com.example.bibiliogo.model.Libro> response) {}
                                            @Override
                                            public void onFailure(Call<com.example.bibiliogo.model.Libro> call, Throwable t) {}
                                        });
                                    }
                                }
                            } else {
                                Toast.makeText(context, "Error al actualizar estado", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Pedido> call, Throwable t) {
                            Toast.makeText(context, "Error de red al actualizar estado", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return pedidos != null ? pedidos.size() : 0;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
        notifyDataSetChanged();
    }

    static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvNombre, tagEstado;
        Button btnCambiarEstado;
        PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTituloPedidoAdmin);
            tvNombre = itemView.findViewById(R.id.tvNombrePedidoAdmin);
            tagEstado = itemView.findViewById(R.id.tagEstadoPedidoAdmin);
            btnCambiarEstado = itemView.findViewById(R.id.btnCambiarEstadoPedidoAdmin);
        }
    }
}
