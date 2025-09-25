package com.example.biblioteca.ui.libros;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.biblioteca.R;
import com.example.biblioteca.ui.model.Libro;
import java.util.List;

public class LibrosAdminFragment extends Fragment {
    private RecyclerView recyclerView;
    private LibrosAdapter adapter;
    private List<Libro> libros;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_libros_admin, container, false);
        recyclerView = view.findViewById(R.id.recyclerLibrosAdmin);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LibrosAdapter(libros, libro -> {});
        adapter.setModoAdmin(true, new LibrosAdapter.OnLibroAdminClickListener() {
            @Override
            public void onEditarClick(Libro libro) {
                // Aquí abres el fragment de edición
            }
            @Override
            public void onEliminarClick(Libro libro) {
                // Aquí ejecutas la lógica de eliminar
            }
        });
        recyclerView.setAdapter(adapter);
        return view;
    }

    // Puedes agregar un método para actualizar la lista de libros
    public void setLibros(List<Libro> libros) {
        this.libros = libros;
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }
}
