package com.example.practice_firebase.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.practice_firebase.R;
import com.example.practice_firebase.adapters.ProductAdapter;
import com.example.practice_firebase.models.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class ProductListFragment extends Fragment {

    private List<Product> productList;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;


    public ProductListFragment(){
        super(R.layout.fragment_product_list);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseReference = FirebaseDatabase.getInstance().getReference("products");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerViewListProducts);
        productList = new ArrayList<>();
        setupRecyclerView();

    }

    private void setupRecyclerView() {
        // Configura tu RecyclerView aquí (configura adaptador, administrador de diseño, etc.)
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        ProductAdapter productAdapter = new ProductAdapter(getContext(), productList, LayoutInflater.from(getContext()));
        recyclerView.setAdapter(productAdapter);
        // Agrega un ValueEventListener para escuchar cambios en los productos
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    String name = productSnapshot.child("name").getValue(String.class);
                    String category = productSnapshot.child("category").getValue(String.class);
                    double price = productSnapshot.child("price").getValue(Double.class);
                    String image = productSnapshot.child("image").getValue(String.class);

                    Product product = new Product(name, price, category, image);
                    product.setKey(productSnapshot.getKey());
                    productList.add(product);
                }

                // Notifica al adaptador de RecyclerView que los datos han cambiado
                if (recyclerView.getAdapter() != null) {
                    recyclerView.getAdapter().notifyDataSetChanged();
                } else {
                    productAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ProductListFragment", "Error al leer datos", error.toException());

            }

        };

        databaseReference.addValueEventListener(valueEventListener);

        productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                Log.d("Product list fragment", "onItemClick: "+product.getName());
                DialogEditProductFragment dialogEditProductFragment = DialogEditProductFragment.newInstance(product);
                dialogEditProductFragment.show(getParentFragmentManager(), "DialogEditProductFragment");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Asegúrate de eliminar el ValueEventListener al destruir el fragmento
        if (databaseReference != null && valueEventListener != null) {
            databaseReference.removeEventListener(valueEventListener);
        }
    }


}