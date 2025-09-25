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
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_libro_card, parent, false);
        return new LibroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibroViewHolder holder, int position) {
        Libro libro = libros.get(position);

        // Asignar datos
        holder.tvTitulo.setText(libro.getTitulo());
        holder.tvAutor.setText(libro.getAutor());
        holder.tvStock.setText("Stock: " + (libro.getStock() != null ? libro.getStock() : 0));

        // Eventos de botones
        holder.btnEditar.setOnClickListener(v -> {
            if (listener != null) listener.onEditarClick(libro);
        });
        holder.btnEliminar.setOnClickListener(v -> {
            if (listener != null) listener.onEliminarClick(libro);
        });
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }

    public static class LibroViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvAutor, tvStock;
        View btnEditar, btnEliminar;

        public LibroViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvLibroTitulo);
            tvAutor = itemView.findViewById(R.id.tvLibroAutor);
            tvStock = itemView.findViewById(R.id.tvLibroStock);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}
