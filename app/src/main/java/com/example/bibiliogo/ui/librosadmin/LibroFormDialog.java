package com.example.bibiliogo.ui.librosadmin;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.example.bibiliogo.R;

public class LibroFormDialog extends DialogFragment {
    private EditText etTitulo, etAutor, etCategoria, etCodigo, etPrecio, etDescripcion;
    private Spinner spinnerEstado;
    private Button btnGuardar;
    private OnLibroFormListener listener;
    private static final String[] ESTADOS_LIBRO = {"DISPONIBLE", "PRESTADO", "RESERVADO", "DADO_DE_BAJA"};

    public interface OnLibroFormListener {
        void onLibroGuardar(String titulo, String autor, String categoria, String codigo, int precio, String descripcion, String estado);
    }

    public void setOnLibroFormListener(OnLibroFormListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_form_libro, null);
        etTitulo = view.findViewById(R.id.etTitulo);
        etAutor = view.findViewById(R.id.etAutor);
        etCategoria = view.findViewById(R.id.etCategoria);
        etCodigo = view.findViewById(R.id.etCodigo);
        etPrecio = view.findViewById(R.id.etPrecio);
    etDescripcion = view.findViewById(R.id.etDescripcion);
    spinnerEstado = view.findViewById(R.id.spinnerEstado);
    btnGuardar = view.findViewById(R.id.btnGuardarLibro);

    // Set up spinner
    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, ESTADOS_LIBRO);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    spinnerEstado.setAdapter(adapter);

        // Prellenar si hay argumentos (para editar)
        Bundle args = getArguments();
        if (args != null) {
            etTitulo.setText(args.getString("titulo", ""));
            etAutor.setText(args.getString("autor", ""));
            etCategoria.setText(args.getString("categoria", ""));
            etCodigo.setText(args.getString("codigo", ""));
            etPrecio.setText(String.valueOf(args.getInt("precio", 0)));
            etDescripcion.setText(args.getString("descripcion", ""));
            String estado = args.getString("estado", "DISPONIBLE");
            int pos = 0;
            for (int i = 0; i < ESTADOS_LIBRO.length; i++) {
                if (ESTADOS_LIBRO[i].equalsIgnoreCase(estado)) {
                    pos = i;
                    break;
                }
            }
            spinnerEstado.setSelection(pos);
        } else {
            spinnerEstado.setSelection(0);
        }

        btnGuardar.setOnClickListener(v -> {
            if (listener != null) {
                String titulo = etTitulo.getText().toString();
                String autor = etAutor.getText().toString();
                String categoria = etCategoria.getText().toString();
                String codigo = etCodigo.getText().toString();
                int precio = 0;
                try { precio = Integer.parseInt(etPrecio.getText().toString()); } catch (Exception ignored) {}
                String descripcion = etDescripcion.getText().toString();
                String estado = (String) spinnerEstado.getSelectedItem();
                listener.onLibroGuardar(titulo, autor, categoria, codigo, precio, descripcion, estado);
                dismiss();
            }
        });

        return new AlertDialog.Builder(requireContext())
                .setView(view)
                .setTitle("Libro")
                .setNegativeButton("Cancelar", (d, w) -> dismiss())
                .create();
    }
}
