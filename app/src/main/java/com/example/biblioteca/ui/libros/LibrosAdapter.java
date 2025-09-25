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
    private boolean modoAdmin = false;
    private OnLibroAdminClickListener adminListener;

    public interface OnPedidoClickListener {
        void onPedidoClick(Libro libro);
    }

    public interface OnLibroAdminClickListener {
        void onEditarClick(Libro libro);
        void onEliminarClick(Libro libro);
    }

    public LibrosAdapter(List<Libro> libros, OnPedidoClickListener listener) {
        this.libros = libros;
        this.listener = listener;
    }

    public void setModoAdmin(boolean admin, OnLibroAdminClickListener adminListener) {
        this.modoAdmin = admin;
        this.adminListener = adminListener;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LibroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (modoAdmin) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_libro_admin, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_libro, parent, false);
        }
        return new LibroViewHolder(view);
    }

    static class LibroViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvAutor, tvStock, tvPrecio;
        Button btnPedido, btnEditar, btnEliminar;
        public LibroViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvLibroTitulo);
            tvAutor = itemView.findViewById(R.id.tvLibroAutor);
            tvStock = itemView.findViewById(R.id.tvLibroStock);
            tvPrecio = itemView.findViewById(R.id.tvLibroPrecio);
            btnPedido = itemView.findViewById(R.id.btnHacerPedido);
            btnEditar = itemView.findViewById(R.id.btnEditarLibro);
            btnEliminar = itemView.findViewById(R.id.btnEliminarLibro);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull LibroViewHolder holder, int position) {
        Libro libro = libros.get(position);
        holder.tvTitulo.setText(libro.getTitulo());
        holder.tvAutor.setText(libro.getAutor());
        if (modoAdmin) {
            if (holder.tvPrecio != null) {
                holder.tvPrecio.setText("Precio: $" + libro.getStock());
            }
            if (holder.btnEditar != null && holder.btnEliminar != null) {
                holder.btnEditar.setVisibility(View.VISIBLE);
                holder.btnEliminar.setVisibility(View.VISIBLE);
                holder.btnEditar.setOnClickListener(v -> {
                    if (adminListener != null) adminListener.onEditarClick(libro);
                });
                holder.btnEliminar.setOnClickListener(v -> {
                    if (adminListener != null) adminListener.onEliminarClick(libro);
                });
            }
            if (holder.btnPedido != null) holder.btnPedido.setVisibility(View.GONE);
            if (holder.tvStock != null) holder.tvStock.setVisibility(View.GONE);
        } else {
            if (holder.tvStock != null) {
                holder.tvStock.setText("Precio: $" + libro.getStock());
                holder.tvStock.setVisibility(View.VISIBLE);
            }
            if (holder.btnPedido != null) {
                holder.btnPedido.setVisibility(View.VISIBLE);
                holder.btnPedido.setOnClickListener(v -> listener.onPedidoClick(libro));
            }
            if (holder.btnEditar != null) holder.btnEditar.setVisibility(View.GONE);
            if (holder.btnEliminar != null) holder.btnEliminar.setVisibility(View.GONE);
            if (holder.tvPrecio != null) holder.tvPrecio.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }
}
