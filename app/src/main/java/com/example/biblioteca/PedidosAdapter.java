package com.example.biblioteca;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.PedidoViewHolder> {
    public interface OnPedidosRefreshListener {
        void onPedidosRefresh();
    }

    private OnPedidosRefreshListener refreshListener;

    public void setOnPedidosRefreshListener(OnPedidosRefreshListener listener) {
        this.refreshListener = listener;
    }
    private List<Pedido> pedidos;
    private boolean modoAdmin = false;

    public void setModoAdmin(boolean admin) {
        this.modoAdmin = admin;
        notifyDataSetChanged();
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
        notifyDataSetChanged();
    }
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
    if (modoAdmin) {
        holder.tvNumero.setText("PEDIDO #" + pedido.idPedido);
    } else {
        holder.tvNumero.setText("PEDIDO #" + (position + 1));
    }
    holder.tvEstado.setText(pedido.estado);
    setEstadoColor(holder.tvEstado, pedido.estado);
    holder.tvUsuario.setText(pedido.usuario);
    holder.tvFecha.setText(pedido.fecha);
    holder.tvCantidad.setText(pedido.libro != null ? pedido.libro.titulo + " - " + pedido.libro.autor : "");
    holder.tvDireccion.setText(pedido.descripcion);
    holder.tvTotal.setText(pedido.libro != null && pedido.libro.stock != null ? "Stock: " + pedido.libro.stock : "");

    // Cambiar color de fondo de la tarjeta según estado
    int cardColor;
    switch (pedido.estado != null ? pedido.estado.toUpperCase() : "") {
        case "PENDIENTE":
            cardColor = android.graphics.Color.parseColor("#FFF8E1"); // Amarillo claro
            break;
        case "EN_PROCESO":
            cardColor = android.graphics.Color.parseColor("#E3F2FD"); // Azul claro
            break;
        case "COMPLETADO":
            cardColor = android.graphics.Color.parseColor("#E8F5E9"); // Verde claro
            break;
        case "CANCELADO":
            cardColor = android.graphics.Color.parseColor("#FFEBEE"); // Rojo claro
            break;
        default:
            cardColor = android.graphics.Color.parseColor("#F5F5F5"); // Gris claro
    }
    if (holder.itemView instanceof androidx.cardview.widget.CardView) {
        ((androidx.cardview.widget.CardView) holder.itemView).setCardBackgroundColor(cardColor);
    }

    View btnVerDetalles = holder.itemView.findViewById(R.id.btnVerDetalles);
    btnVerDetalles.setOnClickListener(v -> {
        androidx.fragment.app.FragmentActivity activity = (androidx.fragment.app.FragmentActivity) v.getContext();
        activity.getSupportFragmentManager()
            .beginTransaction()
            .replace(android.R.id.content, new DetallePedidoFragment(pedido, () -> {
                // Usar el callback para refrescar la lista
                if (refreshListener != null) refreshListener.onPedidosRefresh();
            }))
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
