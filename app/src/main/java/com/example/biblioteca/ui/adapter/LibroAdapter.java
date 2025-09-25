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
        holder.tvTitulo.setText(libro.getTitulo());
        holder.tvAutor.setText(libro.getAutor());
        holder.tvEstado.setText(libro.getEstado());
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }

    public static class LibroViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvAutor, tvEstado;

        public LibroViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvAutor = itemView.findViewById(R.id.tvAutor);
            tvEstado = itemView.findViewById(R.id.tvEstado);
        }
    }
}
