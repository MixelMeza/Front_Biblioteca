package com.example.biblioteca.ui.pedidos.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.biblioteca.R;
import com.example.biblioteca.ui.model.Pedido;
import java.util.List;

public class PedidosAdapter extends RecyclerView.Adapter<PedidosAdapter.PedidoViewHolder> {

    private List<Pedido> pedidosList;

    public PedidosAdapter(List<Pedido> pedidosList) {
        this.pedidosList = pedidosList;
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        Pedido pedido = pedidosList.get(position);
        holder.bind(pedido);
    }

    @Override
    public int getItemCount() {
        return pedidosList.size();
    }

    public static class PedidoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitulo, tvEstado, tvFecha, tvIdPedido, tvDescripcion;

        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvIdPedido = itemView.findViewById(R.id.tvIdPedido);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
        }

        public void bind(Pedido pedido) {
            // Información básica del pedido
            tvIdPedido.setText("Pedido #" + pedido.getIdPedido());
            tvDescripcion.setText(pedido.getDescripcion());
            tvEstado.setText("Estado: " + pedido.getEstado());
            tvFecha.setText("Fecha: " + pedido.getFecha());

            // Información del libro (si está disponible)
            if (pedido.getLibro() != null) {
                tvTitulo.setText("Libro: " + pedido.getLibro().getTitulo());
            } else {
                tvTitulo.setText("Libro no disponible");
            }

            // Color del estado
            setEstadoColor(pedido.getEstado());
        }

        private void setEstadoColor(String estado) {
            int color = itemView.getContext().getColor(R.color.gray);

            if (estado != null) {
                switch (estado.toUpperCase()) {
                    case "COMPLETADO":
                        color = itemView.getContext().getColor(R.color.green);
                        break;
                    case "CANCELADO":
                        color = itemView.getContext().getColor(R.color.red);
                        break;
                    case "EN_PROCESO":
                        color = itemView.getContext().getColor(R.color.orange);
                        break;
                    case "PENDIENTE":
                        color = itemView.getContext().getColor(R.color.blue);
                        break;
                }
            }
            tvEstado.setBackgroundTintList(android.content.res.ColorStateList.valueOf(color));
        }

    }
}