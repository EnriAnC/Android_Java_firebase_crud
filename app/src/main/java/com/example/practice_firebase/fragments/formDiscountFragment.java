package com.example.practice_firebase.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.practice_firebase.R;

public class formDiscountFragment extends Fragment {

    EditText editTextPrecio;
    EditText editTextDescuento;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_form_discount, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextPrecio = view.findViewById(R.id.inputNumberPrecio);
        editTextDescuento = view.findViewById(R.id.inputNumberDescuento);

        view.findViewById(R.id.btnCalcDiscount).setOnClickListener(view1 -> {
            float precio = Float.parseFloat(editTextPrecio.getText().toString());
            float descuento = Float.parseFloat(editTextDescuento.getText().toString());

            Number resultado = precio - precio * (descuento / 100);

            TextView textView = getActivity().findViewById(R.id.textViewResultDiscountForm);
            textView.setText(resultado.toString());
        });

    }
}