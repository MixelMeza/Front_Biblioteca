package com.example.bibiliogo.ui.pedidos;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.example.bibiliogo.R;

public class PedidoFormDialog extends DialogFragment {
    private String tituloLibro;
    private OnPedidoFormListener listener;

    public interface OnPedidoFormListener {
        void onPedidoGuardar(String nombre, String direccion, String telefono, String descripcion);
    }

    public void setTituloLibro(String titulo) {
        this.tituloLibro = titulo;
    }

    public void setOnPedidoFormListener(OnPedidoFormListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_form_pedido, null);
        TextView tvTitulo = view.findViewById(R.id.tvTituloPedido);
    EditText etNombre = view.findViewById(R.id.etNombre);
    EditText etDescripcion = view.findViewById(R.id.etDescripcion);
        EditText etDireccion = view.findViewById(R.id.etDireccion);
        EditText etTelefono = view.findViewById(R.id.etTelefono);
        tvTitulo.setText(tituloLibro);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(view)
                .setTitle("Nuevo Pedido")
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Confirmar pedido", null)
                .create();

        dialog.setOnShowListener(d -> {
            Button btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btn.setOnClickListener(v -> {
                String nombre = etNombre.getText().toString();
                String descripcion = etDescripcion.getText().toString();
                String direccion = etDireccion.getText().toString();
                String telefono = etTelefono.getText().toString();
                if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(direccion) || TextUtils.isEmpty(telefono)) {
                    etNombre.setError(TextUtils.isEmpty(nombre) ? "Obligatorio" : null);
                    etDireccion.setError(TextUtils.isEmpty(direccion) ? "Obligatorio" : null);
                    etTelefono.setError(TextUtils.isEmpty(telefono) ? "Obligatorio" : null);
                    return;
                }
                if (listener != null) listener.onPedidoGuardar(nombre, direccion, telefono, descripcion);
                dismiss();
            });
        });
        return dialog;
    }
}
