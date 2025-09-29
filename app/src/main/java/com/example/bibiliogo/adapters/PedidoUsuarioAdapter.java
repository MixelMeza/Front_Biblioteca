package com.example.bibiliogo.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bibiliogo.R;
import com.example.bibiliogo.model.Pedido;
import com.example.bibiliogo.network.ApiClient;
import com.example.bibiliogo.network.PedidoService;
import com.example.bibiliogo.network.LibroService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.bibiliogo.dialogs.PedidoDetalleDialog;
import com.example.bibiliogo.dialogs.PagoSimuladoDialog;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PedidoUsuarioAdapter extends RecyclerView.Adapter<PedidoUsuarioAdapter.PedidoViewHolder> {
    private List<Pedido> pedidos;
    private PedidoService pedidoService;
    private LibroService libroService;

    public PedidoUsuarioAdapter(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    this.pedidoService = ApiClient.getClient().create(PedidoService.class);
    this.libroService = ApiClient.getClient().create(LibroService.class);
    }

    @NonNull
    @Override
    public PedidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido_usuario, parent, false);
        return new PedidoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PedidoViewHolder holder, int position) {
        Pedido pedido = pedidos.get(position);
        holder.tvTitulo.setText(pedido.getLibro() != null ? pedido.getLibro().getTitulo() : "");

        // Mostrar fecha tipo "Hoy", "Ayer" o la fecha
        String fechaStr = pedido.getFecha();
        if (fechaStr != null && !fechaStr.isEmpty()) {
            String fechaFormateada = formatearFecha(fechaStr);
            holder.tvFecha.setText(fechaFormateada);
            holder.tvFecha.setVisibility(View.VISIBLE);
        } else {
            holder.tvFecha.setVisibility(View.GONE);
        }

        // Mostrar precio
        if (pedido.getLibro() != null && pedido.getLibro().getPrecio() != null) {
            holder.tvPrecio.setText("S/ " + pedido.getLibro().getPrecio());
            holder.tvPrecio.setVisibility(View.VISIBLE);
        } else {
            holder.tvPrecio.setVisibility(View.GONE);
        }

        // Tag de estado visual
        String estado = pedido.getEstado();
        holder.tagEstado.setText(estado);
        int color;
        switch (estado) {
            case "PENDIENTE":
                color = holder.itemView.getResources().getColor(android.R.color.holo_orange_dark);
                break;
            case "CANCELADO":
                color = holder.itemView.getResources().getColor(android.R.color.holo_red_dark);
                break;
            case "EN_PROCESO":
                color = holder.itemView.getResources().getColor(android.R.color.holo_blue_dark);
                break;
            case "COMPLETADO":
            case "ENTREGADO":
                color = holder.itemView.getResources().getColor(android.R.color.holo_green_dark);
                break;
            default:
                color = holder.itemView.getResources().getColor(android.R.color.darker_gray);
        }
        holder.tagEstado.setBackgroundResource(R.drawable.bg_estado_tag);
        holder.tagEstado.setBackgroundTintList(android.content.res.ColorStateList.valueOf(color));

        // Botón de pago solo si está pendiente
        if ("PENDIENTE".equals(estado)) {
            holder.btnPagar.setVisibility(View.VISIBLE);
            holder.btnPagar.setOnClickListener(v -> {
                FragmentActivity activity = (FragmentActivity) v.getContext();
                PagoSimuladoDialog pagoDialog = new PagoSimuladoDialog();
                pagoDialog.setOnPagoExitosoListener(() -> {
                    // Al pagar, primero cambiar estado del pedido a EN_PROCESO y libro a PRESTADO
                    pedidoService.cambiarEstado(pedido.getIdPedido(), "EN_PROCESO").enqueue(new Callback<Pedido>() {
                        @Override
                        public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                pedido.setEstado("EN_PROCESO");
                                notifyItemChanged(position);
                                Toast.makeText(activity, "Pago realizado. Pedido en proceso", Toast.LENGTH_SHORT).show();
                                // Cambiar libro a PRESTADO
                                if (pedido.getLibro() != null && pedido.getLibro().getIdLibro() != null) {
                                    libroService.cambiarEstado(pedido.getLibro().getIdLibro(), "PRESTADO").enqueue(new Callback<com.example.bibiliogo.model.Libro>() {
                                        @Override
                                        public void onResponse(Call<com.example.bibiliogo.model.Libro> call, Response<com.example.bibiliogo.model.Libro> response) {}
                                        @Override
                                        public void onFailure(Call<com.example.bibiliogo.model.Libro> call, Throwable t) {}
                                    });
                                }
                            } else {
                                Toast.makeText(activity, "Error al actualizar estado", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Pedido> call, Throwable t) {
                            Toast.makeText(activity, "Error de red al actualizar estado", Toast.LENGTH_SHORT).show();
                        }
                    });
                });
                pagoDialog.show(activity.getSupportFragmentManager(), "pagoSimulado");
            });
        } else {
            holder.btnPagar.setVisibility(View.GONE);
        }

        // Long click para mostrar detalle
        holder.itemView.setOnLongClickListener(v -> {
            FragmentActivity activity = (FragmentActivity) v.getContext();
            PedidoDetalleDialog dialog = PedidoDetalleDialog.newInstance(pedido);
            dialog.setOnCancelarPedidoListener(new PedidoDetalleDialog.OnCancelarPedidoListener() {
                @Override
                public void onCancelarPedido(Pedido pedidoCancelado) {
                    // Llamar endpoint para cambiar estado a CANCELADO
                    pedidoService.cambiarEstado(pedidoCancelado.getId(), "CANCELADO").enqueue(new Callback<Pedido>() {
                        @Override
                        public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                pedidoCancelado.setEstado("CANCELADO");
                                notifyItemChanged(position);
                                Toast.makeText(activity, "Pedido cancelado", Toast.LENGTH_SHORT).show();
                                // Cambiar libro a DISPONIBLE
                                if (pedidoCancelado.getLibro() != null && pedidoCancelado.getLibro().getIdLibro() != null) {
                                    libroService.cambiarEstado(pedidoCancelado.getLibro().getIdLibro(), "DISPONIBLE").enqueue(new Callback<com.example.bibiliogo.model.Libro>() {
                                        @Override
                                        public void onResponse(Call<com.example.bibiliogo.model.Libro> call, Response<com.example.bibiliogo.model.Libro> response) {}
                                        @Override
                                        public void onFailure(Call<com.example.bibiliogo.model.Libro> call, Throwable t) {}
                                    });
                                }
                            } else {
                                String errorMsg = "Error al cancelar pedido";
                                try {
                                    if (response.errorBody() != null) {
                                        errorMsg += ": " + response.errorBody().string();
                                    }
                                } catch (Exception e) { /* ignore */ }
                                Log.e("PedidoUsuarioAdapter", errorMsg);
                                Toast.makeText(activity, errorMsg, Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Pedido> call, Throwable t) {
                            Toast.makeText(activity, "Error de red al cancelar pedido", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            dialog.show(activity.getSupportFragmentManager(), "detallePedido");
            dialog.setCancelable(true);
            return true;
        });
    }

    private String formatearFecha(String fechaStr) {
        // Suponiendo formato yyyy-MM-dd
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date fecha = sdf.parse(fechaStr);
            // Normalizar ambas fechas a medianoche para comparar solo día (zona local)
            java.util.Calendar calHoy = java.util.Calendar.getInstance();
            calHoy.set(java.util.Calendar.HOUR_OF_DAY, 0);
            calHoy.set(java.util.Calendar.MINUTE, 0);
            calHoy.set(java.util.Calendar.SECOND, 0);
            calHoy.set(java.util.Calendar.MILLISECOND, 0);
            java.util.Calendar calFecha = java.util.Calendar.getInstance();
            calFecha.setTime(fecha);
            calFecha.set(java.util.Calendar.HOUR_OF_DAY, 0);
            calFecha.set(java.util.Calendar.MINUTE, 0);
            calFecha.set(java.util.Calendar.SECOND, 0);
            calFecha.set(java.util.Calendar.MILLISECOND, 0);
            // Ajuste: sumar 1 día a la fecha si hay desfase por zona horaria UTC
            if (calFecha.getTimeInMillis() < calHoy.getTimeInMillis() && Math.abs(calHoy.getTimeInMillis() - calFecha.getTimeInMillis()) < 1000 * 60 * 60 * 24) {
                calFecha.add(java.util.Calendar.DAY_OF_MONTH, 1);
            }
            long diff = calHoy.getTimeInMillis() - calFecha.getTimeInMillis();
            long dias = diff / (1000 * 60 * 60 * 24);
            if (dias == 0) return "Hoy";
            if (dias == 1) return "Ayer";
            return fechaStr;
        } catch (ParseException e) {
            return fechaStr;
        }
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
        TextView tvTitulo, tvFecha, tvPrecio, tagEstado;
        android.widget.Button btnPagar;
        PedidoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTituloPedidoUsuario);
            tvFecha = itemView.findViewById(R.id.tvFechaPedidoUsuario);
            tvPrecio = itemView.findViewById(R.id.tvPrecioLibroPedidoUsuario);
            tagEstado = itemView.findViewById(R.id.tagEstadoPedidoUsuario);
            btnPagar = itemView.findViewById(R.id.btnPagarPedidoUsuario);
        }
    }
}
