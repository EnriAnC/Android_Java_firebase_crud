package com.example.practice_firebase.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.practice_firebase.R;
import com.example.practice_firebase.models.Product;
import com.example.practice_firebase.utils.LoadImageTask;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;


public class ProductFormFragment extends Fragment {

    protected EditText inputName;
    protected EditText inputPrice;
    protected EditText inputCategory;
    protected Uri inputImage;
    protected Product product;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_form, container, false);

        inputName = view.findViewById(R.id.editTextName);
        inputPrice = view.findViewById(R.id.editTextPrice);
        inputCategory = view.findViewById(R.id.editTextCategory);

        view.findViewById(R.id.uploadImageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        view.findViewById(R.id.buttonForm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    submitNewProduct(null);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    protected void updateProduct(String productKey) throws JSONException {
        String name = inputName.getText().toString();
        double price = Double.parseDouble(inputPrice.getText().toString());
        String category = inputCategory.getText().toString();

        DatabaseReference productsReference = FirebaseDatabase.getInstance().getReference("products");

        if (productKey != null) {
            DatabaseReference productToUpdateRef = productsReference.child(productKey);

            // Actualiza los campos del producto específico
            productToUpdateRef.child("name").setValue(name);
            productToUpdateRef.child("category").setValue(category);
            productToUpdateRef.child("price").setValue(price);

            // Si deseas actualizar la imagen, agrega aquí la lógica

            closeParentDialog();
        }
    }

    protected void deleteProduct(String productKey) {
        // Referencia a la base de datos "products"
        DatabaseReference productsReference = FirebaseDatabase.getInstance().getReference("products");

        // Verifica que la clave del producto no sea nula
        if (productKey != null) {
            // Elimina el producto utilizando la clave
            productsReference.child(productKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Producto eliminado exitosamente
                        Log.d("Eliminar Producto", "Producto eliminado");
                        closeParentDialog(); // Cierra el diálogo o realiza cualquier acción necesaria
                    } else {
                        // Error al eliminar el producto
                        Log.e("Eliminar Producto", "Error al eliminar el producto");
                    }
                }
            });
        }
    }


    protected void submitNewProduct(String productName) throws JSONException {
        String name = inputName.getText().toString();
        double price = Double.parseDouble(inputPrice.getText().toString());
        String category = inputCategory.getText().toString();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("images/" + UUID.randomUUID().toString());
        imageRef.putFile(inputImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // La imagen se ha subido exitosamente
                        // Obtenemos la URL de descarga
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                Log.d("FormFragment", "URL de la imagen: " + imageUrl);


                                FirebaseDatabase dbReference = FirebaseDatabase.getInstance();
                                DatabaseReference productsReference  = dbReference.getReference("products");
                                Product product = new Product(name, price, category, imageUrl);
                                productsReference.push().setValue(product);

                                closeParentDialog();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle unsuccessful uploads
                        Log.e("FormFragment", "Error al subir la imagen", e);
                    }
                });;


    }

    private void closeParentDialog(){
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof DialogEditProductFragment || parentFragment instanceof DialogEditProductFragment) {
            DialogFragment dialogFragment = (DialogFragment) parentFragment;
            dialogFragment.dismiss();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    protected void openGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResultLauncher.launch(intent);
    }


    private final ActivityResultLauncher<Intent> startActivityForResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Intent data = result.getData();

                                Bitmap bitmap = getBitmapFromIntent(data);

                                // Aquí puedes manejar la imagen seleccionada
                                ImageView imageView = getView().findViewById(R.id.imageViewProduct);
                                imageView.setImageBitmap(bitmap);

                                inputImage = data.getData();
                            }
                        }
                    });

    private Bitmap getBitmapFromIntent(Intent data) {
        try {
            Uri selectedImageUri = data.getData();
            InputStream imageStream = getContext().getContentResolver().openInputStream(selectedImageUri);
            return BitmapFactory.decodeStream(imageStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Bitmap downloadImage(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}