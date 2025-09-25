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
        holder.tvNumero.setText(pedido.numero);
        holder.tvEstado.setText(pedido.estado);
        holder.tvUsuario.setText(pedido.usuario);
        holder.tvFecha.setText(pedido.fecha);
        holder.tvCantidad.setText(pedido.cantidad);
        holder.tvDireccion.setText(pedido.direccion);
        holder.tvTotal.setText(pedido.total);
    }
    @Override
    public int getItemCount() {
        return pedidos.size();
    }
    static class PedidoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumero, tvEstado, tvUsuario, tvFecha, tvCantidad, tvDireccion, tvTotal;
        public PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumero = itemView.findViewById(R.id.tvNumero);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            tvUsuario = itemView.findViewById(R.id.tvUsuario);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvTotal = itemView.findViewById(R.id.tvTotal);
        }
    }
}
