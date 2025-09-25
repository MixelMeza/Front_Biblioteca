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
public class LibroAdapter  extends RecyclerView.Adapter<LibroAdapter.LibroViewHolder> {
    private List<Libro> libros;

    public LibroAdapter(List<Libro> libros) {
        this.libros = libros;
    }

    @NonNull
    @Override
    public LibroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_libro, parent, false); // usa item_libro.xml
        return new LibroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibroViewHolder holder, int position) {
        Libro libro = libros.get(position);
    holder.tvLibroTitulo.setText(libro.getTitulo());
    holder.tvLibroAutor.setText(libro.getAutor());
    holder.tvLibroStock.setText("Stock: " + libro.getStock());
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }

    public static class LibroViewHolder extends RecyclerView.ViewHolder {
    TextView tvLibroTitulo, tvLibroAutor, tvLibroStock;

        public LibroViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLibroTitulo = itemView.findViewById(R.id.tvLibroTitulo);
            tvLibroAutor = itemView.findViewById(R.id.tvLibroAutor);
            tvLibroStock = itemView.findViewById(R.id.tvLibroStock);
        }
    }
}
