

package com.example.bibiliogo.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bibiliogo.R;
import com.example.bibiliogo.model.Libro;
import java.util.List;
import android.app.AlertDialog;
import android.widget.Toast;
import com.example.bibiliogo.network.ApiClient;
import com.example.bibiliogo.network.LibroService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibroUsuarioAdapter extends RecyclerView.Adapter<LibroUsuarioAdapter.LibroViewHolder> {
    private static final String[] ESTADOS_LIBRO = {"DISPONIBLE", "PRESTADO", "RESERVADO", "DADO_DE_BAJA"};
    private List<Libro> libros;
    private OnPedirClickListener pedirClickListener;
    private LibroService libroService;

    public interface OnPedirClickListener {
        void onPedirClick(Libro libro);
    }

    public void setOnPedirClickListener(OnPedirClickListener listener) {
        this.pedirClickListener = listener;
    }

    public LibroUsuarioAdapter(List<Libro> libros) {
        this.libros = libros;
        this.libroService = ApiClient.getClient().create(LibroService.class);
    }

    @NonNull
    @Override
    public LibroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_libro_usuario, parent, false);
        return new LibroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibroViewHolder holder, int position) {
        Libro libro = libros.get(position);
        holder.tvTitulo.setText(libro.getTitulo());
        holder.tvAutor.setText(libro.getAutor());
        holder.tvCategoria.setText(libro.getCategoria());
        holder.tvDescripcion.setText(libro.getDescripcion());
        holder.tvPrecio.setText("$" + libro.getPrecio());
        // Tag visual de estado
        holder.tagEstado.setText(libro.getEstado());
        int color;
        switch (libro.getEstado()) {
            case "DISPONIBLE":
                color = holder.itemView.getResources().getColor(android.R.color.holo_green_light);
                break;
            case "PRESTADO":
                color = holder.itemView.getResources().getColor(android.R.color.holo_orange_light);
                break;
            case "RESERVADO":
                color = holder.itemView.getResources().getColor(android.R.color.holo_blue_light);
                break;
            case "DADO_DE_BAJA":
                color = holder.itemView.getResources().getColor(android.R.color.holo_red_light);
                break;
            default:
                color = holder.itemView.getResources().getColor(android.R.color.darker_gray);
        }
        holder.tagEstado.setBackgroundResource(R.drawable.bg_estado_tag);
        holder.tagEstado.setBackgroundTintList(android.content.res.ColorStateList.valueOf(color));
        // Cambiar estado del libro al dejar presionado
        holder.itemView.setOnLongClickListener(v -> {
            mostrarDialogoEstadosLibro(holder.itemView.getContext(), libro, position);
            return true;
        });
        holder.btnPedir.setOnClickListener(v -> {
            if (pedirClickListener != null) {
                pedirClickListener.onPedirClick(libro);
            }
        });
        // Mostrar detalles y cancelar solo al hacer click en el tag de estado
        holder.tagEstado.setOnClickListener(v -> mostrarDetalleLibroDialog(holder.itemView.getContext(), libro, position));
    }

    private void mostrarDialogoEstadosLibro(android.content.Context context, Libro libro, int position) {
        android.widget.ArrayAdapter<String> adapterEstados = new android.widget.ArrayAdapter<>(context, android.R.layout.simple_list_item_1, ESTADOS_LIBRO);
        new android.app.AlertDialog.Builder(context)
                .setTitle("Cambiar estado del libro")
                .setAdapter(adapterEstados, (dialog, which) -> {
                    String nuevoEstado = ESTADOS_LIBRO[which];
                    libroService.cambiarEstado(libro.getIdLibro(), nuevoEstado).enqueue(new retrofit2.Callback<Libro>() {
                        @Override
                        public void onResponse(retrofit2.Call<Libro> call, retrofit2.Response<Libro> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                libro.setEstado(nuevoEstado);
                                notifyItemChanged(position);
                                android.widget.Toast.makeText(context, "Estado actualizado", android.widget.Toast.LENGTH_SHORT).show();
                            } else {
                                android.widget.Toast.makeText(context, "Error al actualizar estado", android.widget.Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(retrofit2.Call<Libro> call, Throwable t) {
                            android.widget.Toast.makeText(context, "Error de red al actualizar estado", android.widget.Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDetalleLibroDialog(android.content.Context context, Libro libro, int position) {
        StringBuilder detalle = new StringBuilder();
        detalle.append("Título: ").append(libro.getTitulo()).append("\n");
        detalle.append("Autor: ").append(libro.getAutor()).append("\n");
        detalle.append("Categoría: ").append(libro.getCategoria()).append("\n");
        detalle.append("Descripción: ").append(libro.getDescripcion()).append("\n");
        detalle.append("Precio: $").append(libro.getPrecio()).append("\n");
        detalle.append("Estado: ").append(libro.getEstado());
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Detalle del libro")
                .setMessage(detalle.toString());
        if (!"DISPONIBLE".equals(libro.getEstado())) {
            builder.setNegativeButton("Cancelar pedido", (d, w) -> {
                libroService.cambiarEstado(libro.getIdLibro(), "DISPONIBLE").enqueue(new Callback<Libro>() {
                    @Override
                    public void onResponse(Call<Libro> call, Response<Libro> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            libro.setEstado("DISPONIBLE");
                            notifyItemChanged(position);
                            Toast.makeText(context, "Pedido cancelado y libro disponible", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Error al cancelar pedido", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Libro> call, Throwable t) {
                        Toast.makeText(context, "Error de red al cancelar pedido", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
        builder.setPositiveButton("Cerrar", null).show();
    }

    @Override
    public int getItemCount() {
        return libros != null ? libros.size() : 0;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
        notifyDataSetChanged();
    }

    static class LibroViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvAutor, tvCategoria, tvDescripcion, tvPrecio, tagEstado;
        View btnPedir;
        LibroViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTituloUsuario);
            tvAutor = itemView.findViewById(R.id.tvAutorUsuario);
            tvCategoria = itemView.findViewById(R.id.tvCategoriaUsuario);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcionUsuario);
            tvPrecio = itemView.findViewById(R.id.tvPrecioUsuario);
            tagEstado = itemView.findViewById(R.id.tagEstadoLibroUsuario);
            btnPedir = itemView.findViewById(R.id.btnPedirLibro);
        }
    }
}
