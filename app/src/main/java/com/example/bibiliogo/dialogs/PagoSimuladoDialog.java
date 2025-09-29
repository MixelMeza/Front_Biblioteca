package com.example.bibiliogo.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.example.bibiliogo.R;

public class PagoSimuladoDialog extends DialogFragment {
    public interface OnPagoExitosoListener {
        void onPagoExitoso();
    }
    private OnPagoExitosoListener listener;

    public void setOnPagoExitosoListener(OnPagoExitosoListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_pago_simulado, null);
        EditText etNumero = view.findViewById(R.id.etNumeroTarjeta);
        EditText etNombre = view.findViewById(R.id.etNombreTarjeta);
        EditText etVencimiento = view.findViewById(R.id.etVencimientoTarjeta);
        EditText etCvv = view.findViewById(R.id.etCvvTarjeta);
        Button btnPagar = view.findViewById(R.id.btnPagarSimulado);

        btnPagar.setOnClickListener(v -> {
            // Validación simple
            if (etNumero.getText().toString().isEmpty() || etNombre.getText().toString().isEmpty() ||
                etVencimiento.getText().toString().isEmpty() || etCvv.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(getContext(), "Pago realizado con éxito", Toast.LENGTH_SHORT).show();
            if (listener != null) listener.onPagoExitoso();
            dismiss();
        });

        return new AlertDialog.Builder(requireContext())
                .setTitle("Simular pago")
                .setView(view)
                .create();
    }
}
