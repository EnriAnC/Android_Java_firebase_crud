package com.example.practice_firebase.fragments;

import androidx.fragment.app.DialogFragment;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.practice_firebase.R;
import com.example.practice_firebase.models.Product;


public class DialogEditProductFragment extends DialogFragment {

    public static DialogEditProductFragment newInstance(Product product) {
        DialogEditProductFragment fragment = new DialogEditProductFragment();
        Bundle args = new Bundle();
        args.putParcelable("product", product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_product, container, false);

        Bundle arguments = getArguments();
        if (arguments != null){
            Product product = arguments.getParcelable("product");
            Log.d("Dialog Edit Product Fragment", "PRODUCT: "+ product.getKey());
            EditProductFragment editProductFragment = (EditProductFragment) getChildFragmentManager().findFragmentById(R.id.fragment_container_view_edit_product);
            if (editProductFragment != null) {
                Bundle args = new Bundle();
                args.putParcelable("product", product);
                editProductFragment.setArguments(args);
            }
        }

        view.findViewById(R.id.buttonCloseDialog).setOnClickListener(view1 -> {
            dismiss();
        });

        return view;
    }
}
