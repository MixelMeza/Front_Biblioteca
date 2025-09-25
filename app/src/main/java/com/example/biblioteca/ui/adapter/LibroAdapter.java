package com.example.biblioteca.ui.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.biblioteca.R;
import com.example.biblioteca.ui.model.Libro;

import java.util.List;

public class LibroAdapter extends RecyclerView.Adapter<LibroAdapter.LibroViewHolder> {
    private boolean modoAdmin = false;

    public void setModoAdmin(boolean admin) {
        this.modoAdmin = admin;
        notifyDataSetChanged();
    }
    private List<Libro> libros;
    private OnLibroClickListener listener;

    public interface OnLibroClickListener {
        void onEditarClick(Libro libro);
        void onEliminarClick(Libro libro);
    }

    public LibroAdapter(List<Libro> libros) {
        this.libros = libros;
    }

    public void setOnLibroClickListener(OnLibroClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public LibroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (modoAdmin) {
            view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_libro_admin, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_libro, parent, false);
        }
        return new LibroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibroViewHolder holder, int position) {
        Libro libro = libros.get(position);
        holder.tvTitulo.setText(libro.getTitulo());
        holder.tvAutor.setText(libro.getAutor());
        if (modoAdmin) {
            TextView tvPrecio = holder.itemView.findViewById(R.id.tvLibroPrecio);
            if (tvPrecio != null) {
                tvPrecio.setText("Precio: $" + (libro.getStock() != null ? libro.getStock() : 0));
            }
            View btnEditar = holder.itemView.findViewById(R.id.btnEditarLibro);
            View btnEliminar = holder.itemView.findViewById(R.id.btnEliminarLibro);
            btnEditar.setOnClickListener(v -> {
                if (listener != null) listener.onEditarClick(libro);
            });
            btnEliminar.setOnClickListener(v -> {
                if (listener != null) listener.onEliminarClick(libro);
            });
        } else {
            holder.tvStock.setText("Precio: $" + (libro.getStock() != null ? libro.getStock() : 0));
            View btnHacerPedido = holder.itemView.findViewById(R.id.btnHacerPedido);
            btnHacerPedido.setOnClickListener(v -> {
                // Acción para hacer pedido
            });
        }
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }

    public static class LibroViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvAutor, tvStock;
        View btnEditar;

        public LibroViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvLibroTitulo);
            tvAutor = itemView.findViewById(R.id.tvLibroAutor);
            tvStock = itemView.findViewById(R.id.tvLibroStock);
            btnEditar = itemView.findViewById(R.id.btnEditarLibro);
        }
    }
}
