package com.example.biblioteca;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.PedidoViewHolder> {
    private List<Pedido> pedidos;
    public PedidosAdapter(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido, parent, false);
        return new PedidoViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
    Pedido pedido = pedidos.get(position);
    holder.tvNumero.setText("PEDIDO #" + pedido.idPedido);
    holder.tvEstado.setText(pedido.estado);
    setEstadoColor(holder.tvEstado, pedido.estado);
    holder.tvUsuario.setText(pedido.usuario);
    holder.tvFecha.setText(pedido.fecha);
    holder.tvCantidad.setText(pedido.libro != null ? pedido.libro.titulo + " - " + pedido.libro.autor : "");
    holder.tvDireccion.setText(pedido.descripcion);
    holder.tvTotal.setText(pedido.libro != null && pedido.libro.stock != null ? "$" + pedido.libro.stock : "");
        View btnVerDetalles = holder.itemView.findViewById(R.id.btnVerDetalles);
        btnVerDetalles.setOnClickListener(v -> {
            androidx.fragment.app.FragmentActivity activity = (androidx.fragment.app.FragmentActivity) v.getContext();
            activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new DetallePedidoFragment(pedido))
                .addToBackStack(null)
                .commit();
        });
    }
    @Override
    public int getItemCount() {
        return pedidos.size();
    }
    static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumero, tvEstado, tvUsuario, tvFecha, tvCantidad, tvDireccion, tvTotal;
        View btnVerDetalles;
        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumero = itemView.findViewById(R.id.tvNumero);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            tvUsuario = itemView.findViewById(R.id.tvUsuario);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvTotal = itemView.findViewById(R.id.tvTotal);
            btnVerDetalles = itemView.findViewById(R.id.btnVerDetalles);
        }
    }

    private void setEstadoColor(TextView estadoTag, String estado) {
        int color;
        switch (estado.toUpperCase()) {
            case "PENDIENTE":
                color = android.graphics.Color.parseColor("#FFC107"); // Amarillo
                break;
            case "EN_PROCESO":
                color = android.graphics.Color.parseColor("#2196F3"); // Azul
                break;
            case "COMPLETADO":
                color = android.graphics.Color.parseColor("#4CAF50"); // Verde
                break;
            case "CANCELADO":
                color = android.graphics.Color.parseColor("#F44336"); // Rojo
                break;
            default:
                color = android.graphics.Color.parseColor("#BDBDBD"); // Gris
        }
        estadoTag.setBackgroundColor(color);
    }
}
