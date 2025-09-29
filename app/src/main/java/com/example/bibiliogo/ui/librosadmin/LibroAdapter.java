package com.example.bibiliogo.ui.librosadmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bibiliogo.R;
import com.example.bibiliogo.model.Libro;
import java.util.List;

public class LibroAdapter extends RecyclerView.Adapter<LibroAdapter.LibroViewHolder> {
    private List<Libro> libros;
    private OnLibroActionListener listener;

    public interface OnLibroActionListener {
        void onEdit(Libro libro, int position);
        void onDelete(Libro libro, int position);
    }

    public LibroAdapter(List<Libro> libros, OnLibroActionListener listener) {
        this.libros = libros;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LibroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_libro_card, parent, false);
        return new LibroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibroViewHolder holder, int position) {
        Libro libro = libros.get(position);
        holder.tvTitulo.setText(libro.getTitulo());
        holder.tvAutor.setText("Autor: " + libro.getAutor());
        holder.tvCategoria.setText("Categoría: " + libro.getCategoria());
        holder.tvPrecio.setText("Precio: $" + libro.getPrecio());
        // Aquí puedes agregar listeners para editar/eliminar si usas swipe
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
        notifyDataSetChanged();
    }

    public Libro getLibroAt(int position) {
        return libros.get(position);
    }

    static class LibroViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvAutor, tvCategoria, tvPrecio;
        LibroViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvAutor = itemView.findViewById(R.id.tvAutor);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
        }
    }
}
