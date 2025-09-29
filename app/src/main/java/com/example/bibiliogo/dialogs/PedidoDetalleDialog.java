package com.example.bibiliogo.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.example.bibiliogo.R;
import com.example.bibiliogo.model.Pedido;
import com.example.bibiliogo.model.Libro;

public class PedidoDetalleDialog extends DialogFragment {
    private static final String ARG_PEDIDO = "pedido";
    private Pedido pedido;
    private OnCancelarPedidoListener listener;

    public interface OnCancelarPedidoListener {
        void onCancelarPedido(Pedido pedido);
    }

    public void setOnCancelarPedidoListener(OnCancelarPedidoListener listener) {
        this.listener = listener;
    }

    public static PedidoDetalleDialog newInstance(Pedido pedido) {
        PedidoDetalleDialog dialog = new PedidoDetalleDialog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PEDIDO, pedido);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            pedido = (Pedido) getArguments().getSerializable(ARG_PEDIDO);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_detalle_pedido, null);

        // Datos del pedido
        ((TextView) view.findViewById(R.id.tvNombreDetalle)).setText(pedido.getNombre());
        ((TextView) view.findViewById(R.id.tvDireccionDetalle)).setText(pedido.getDireccion());
        ((TextView) view.findViewById(R.id.tvTelefonoDetalle)).setText(pedido.getTelefono());
        ((TextView) view.findViewById(R.id.tvDescripcionDetalle)).setText(pedido.getDescripcion());
        ((TextView) view.findViewById(R.id.tvEstadoDetalle)).setText(pedido.getEstado());
        ((TextView) view.findViewById(R.id.tvFechaDetalle)).setText(pedido.getFecha());

        // Datos del libro
        Libro libro = pedido.getLibro();
        if (libro != null) {
            ((TextView) view.findViewById(R.id.tvTituloLibroDetalle)).setText(libro.getTitulo());
            ((TextView) view.findViewById(R.id.tvAutorLibroDetalle)).setText(libro.getAutor());
            // Si tu modelo Libro no tiene getEditorial(), comenta o elimina la siguiente línea:
            // ((TextView) view.findViewById(R.id.tvEditorialLibroDetalle)).setText(libro.getEditorial());
            // Aquí podrías cargar una imagen si tienes url o recurso
        }

        Button btnCancelar = view.findViewById(R.id.btnCancelarPedido);
        btnCancelar.setOnClickListener(v -> {
            if (listener != null) listener.onCancelarPedido(pedido);
            dismiss();
        });

        builder.setView(view);
        return builder.create();
    }
}
