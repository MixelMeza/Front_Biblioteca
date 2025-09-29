package com.example.bibiliogo.ui.librosadmin;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.example.bibiliogo.R;
import com.example.bibiliogo.model.Libro;
import com.example.bibiliogo.network.ApiClient;
import com.example.bibiliogo.network.LibroService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibrosAdminFragment extends Fragment {
    private RecyclerView recyclerView;
    private LibroAdapter adapter;
    private List<Libro> libros = new ArrayList<>();
    private LibroService libroService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_libros_admin, container, false);
        recyclerView = view.findViewById(R.id.recyclerLibros);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LibroAdapter(libros, null);
        recyclerView.setAdapter(adapter);

        // Swipe para editar y eliminar con animación e iconos
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Libro libro = adapter.getLibroAt(position);
                if (direction == ItemTouchHelper.RIGHT) {
                    mostrarDialogoEditarLibro(libro, position);
                } else if (direction == ItemTouchHelper.LEFT) {
                    confirmarEliminarLibro(libro, position);
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                   float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                View itemView = viewHolder.itemView;
                Paint paint = new Paint();
                int iconMargin = (itemView.getHeight() - 64) / 2; // icon size 64px
                float tilt = 0.15f; // inclinación

                if (dX > 0) { // Swipe derecha (editar)
                    paint.setColor(0xFFECCD37); // azul
                    c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom(), paint);
                    // Icono editar
                    Drawable icon = getResources().getDrawable(android.R.drawable.ic_menu_edit, null);
                    int iconTop = itemView.getTop() + iconMargin;
                    int iconLeft = itemView.getLeft() + iconMargin;
                    int iconRight = iconLeft + 64;
                    int iconBottom = iconTop + 64;
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                    icon.draw(c);
                    // Inclinación
                    itemView.setRotation(-tilt * Math.min(dX / itemView.getWidth(), 1));
                } else if (dX < 0) { // Swipe izquierda (eliminar)
                    paint.setColor(0xFFD32F2F); // rojo
                    c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom(), paint);
                    // Icono eliminar
                    Drawable icon = getResources().getDrawable(android.R.drawable.ic_menu_delete, null);
                    int iconTop = itemView.getTop() + iconMargin;
                    int iconRight = itemView.getRight() - iconMargin;
                    int iconLeft = iconRight - 64;
                    int iconBottom = iconTop + 64;
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                    icon.draw(c);
                    // Inclinación
                    itemView.setRotation(tilt * Math.min(-dX / itemView.getWidth(), 1));
                } else {
                    itemView.setRotation(0f);
                }
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setRotation(0f);
            }
        };
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        libroService = ApiClient.getClient().create(LibroService.class);
        cargarLibros();


        return view;
    }

    private void mostrarDialogoEditarLibro(Libro libro, int position) {
        LibroFormDialog dialog = new LibroFormDialog();
        dialog.setOnLibroFormListener((titulo, autor, categoria, codigo, precio, descripcion, estado) -> {
            libro.setTitulo(titulo);
            libro.setAutor(autor);
            libro.setCategoria(categoria);
            libro.setCodigo(codigo);
            libro.setPrecio(precio);
            libro.setDescripcion(descripcion);
            libro.setEstado(estado);
            libroService.updateLibro(libro.getIdLibro(), libro).enqueue(new Callback<Libro>() {
                @Override
                public void onResponse(Call<Libro> call, Response<Libro> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        libros.set(position, response.body());
                        adapter.notifyItemChanged(position);
                        Toast.makeText(getContext(), "Libro editado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Error al editar libro", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Libro> call, Throwable t) {
                    Toast.makeText(getContext(), "Error de red", Toast.LENGTH_SHORT).show();
                }
            });
        });
        // Prellenar datos
        Bundle args = new Bundle();
        args.putString("titulo", libro.getTitulo());
        args.putString("autor", libro.getAutor());
        args.putString("categoria", libro.getCategoria());
        args.putString("codigo", libro.getCodigo());
        args.putInt("precio", libro.getPrecio() != null ? libro.getPrecio() : 0);
        args.putString("descripcion", libro.getDescripcion());
        args.putString("estado", libro.getEstado() != null ? libro.getEstado() : "DISPONIBLE");
        dialog.setArguments(args);
        dialog.show(getParentFragmentManager(), "editarLibro");
        adapter.notifyItemChanged(position); // Para que la tarjeta vuelva a su lugar si se cancela
    }

    private void confirmarEliminarLibro(Libro libro, int position) {
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Eliminar libro")
                .setMessage("¿Seguro que deseas eliminar este libro?")
                .setPositiveButton("Eliminar", (d, w) -> eliminarLibro(libro, position))
                .setNegativeButton("Cancelar", (d, w) -> {
                    adapter.notifyItemChanged(position);
                })
                .setOnCancelListener(dialog -> adapter.notifyItemChanged(position))
                .show();
    }

    private void eliminarLibro(Libro libro, int position) {
        libroService.deleteLibro(libro.getIdLibro()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    libros.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(getContext(), "Libro eliminado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error al eliminar libro", Toast.LENGTH_SHORT).show();
                    adapter.notifyItemChanged(position);
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error de red", Toast.LENGTH_SHORT).show();
                adapter.notifyItemChanged(position);
            }
        });
    }

    private void cargarLibros() {
        libroService.getLibros().enqueue(new Callback<List<Libro>>() {
            @Override
            public void onResponse(Call<List<Libro>> call, Response<List<Libro>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    libros.clear();
                    libros.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Error al cargar libros", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Libro>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    private void mostrarDialogoCrearLibro() {
        LibroFormDialog dialog = new LibroFormDialog();
        dialog.setOnLibroFormListener((titulo, autor, categoria, codigo, precio, descripcion, estado) -> {
            Libro libro = new Libro();
            libro.setTitulo(titulo);
            libro.setAutor(autor);
            libro.setCategoria(categoria);
            libro.setCodigo(codigo);
            libro.setPrecio(precio);
            libro.setDescripcion(descripcion);
            libro.setEstado(estado);
            crearLibro(libro);
        });
        dialog.show(getParentFragmentManager(), "crearLibro");
    }

    private void crearLibro(Libro libro) {
        libroService.createLibro(libro).enqueue(new Callback<Libro>() {
            @Override
            public void onResponse(Call<Libro> call, Response<Libro> response) {
                if (response.isSuccessful() && response.body() != null) {
                    libros.add(response.body());
                    adapter.notifyItemInserted(libros.size() - 1);
                    Toast.makeText(getContext(), "Libro creado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error al crear libro", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Libro> call, Throwable t) {
                Toast.makeText(getContext(), "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ...existing code...
}
