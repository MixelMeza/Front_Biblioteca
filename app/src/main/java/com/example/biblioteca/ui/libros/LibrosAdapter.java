package com.example.biblioteca.ui.libros;
import com.example.biblioteca.R;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.biblioteca.ui.model.Libro;
import java.util.List;

public class LibrosAdapter extends RecyclerView.Adapter<LibrosAdapter.LibroViewHolder> {
    private List<Libro> libros;
    private OnPedidoClickListener listener;
    public interface OnPedidoClickListener {
        void onPedidoClick(Libro libro);
    }
    public LibrosAdapter(List<Libro> libros, OnPedidoClickListener listener) {
        this.libros = libros;
        this.listener = listener;
    }
    @NonNull
    @Override
    public LibroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_libro, parent, false);
        return new LibroViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull LibroViewHolder holder, int position) {
        Libro libro = libros.get(position);
    holder.tvTitulo.setText(libro.getTitulo());
    holder.tvAutor.setText(libro.getAutor());
    holder.tvStock.setText("Precio: $" + libro.getStock());
        holder.btnPedido.setOnClickListener(v -> listener.onPedidoClick(libro));
    }
    @Override
    public int getItemCount() {
        return libros.size();
    }
    static class LibroViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvAutor, tvStock;
        Button btnPedido;
        public LibroViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvLibroTitulo);
            tvAutor = itemView.findViewById(R.id.tvLibroAutor);
            tvStock = itemView.findViewById(R.id.tvLibroStock);
            btnPedido = itemView.findViewById(R.id.btnHacerPedido);
        }
    }
}
