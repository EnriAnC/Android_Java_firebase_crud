package com.example.practice_firebase.fragments;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.practice_firebase.R;
import com.example.practice_firebase.models.Product;
import com.example.practice_firebase.utils.LoadImageTask;

import org.json.JSONException;

public class EditProductFragment extends ProductFormFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_product, container, false);

        inputName = view.findViewById(R.id.editTextName);
        inputPrice = view.findViewById(R.id.editTextPrice);
        inputCategory = view.findViewById(R.id.editTextCategory);

        Bundle args = getArguments(); // Obtén los argumentos pasados desde MyDialogFragment
        if (args != null) {
            product = args.getParcelable("product"); // Recupera el objeto Parcelable
            Log.d("onViewCreated", "onViewCreated: " + product.getKey().toString());
            if (product != null) {
                // Rellena los campos con los datos del producto
                inputName.setText(product.getName());
                inputPrice.setText(Double.toString(product.getPrice()));
                inputCategory.setText(product.getCategory());
                ImageView imageView = view.findViewById(R.id.imageViewProduct);
                new LoadImageTask(imageView).execute(product.getImage());
            }
        }

        Log.d("onCreateView PRODUCT", "onCreateView --- > " + product.getName());

        view.findViewById(R.id.uploadImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery() ;
            }
        });
        view.findViewById(R.id.buttonForm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    updateProduct(product.getKey());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        view.findViewById(R.id.buttonDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProduct(product.getKey());
            }
        });
        return view;
    }


}