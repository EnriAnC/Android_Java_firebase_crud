package com.example.practice_firebase.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.practice_firebase.R;
import com.example.practice_firebase.models.Product;
import com.example.practice_firebase.utils.LoadImageTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;
    private List<Product> productList;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener; // Define un oyente personalizado
    private Bitmap imageBitmap;


    public ProductAdapter(Context context, List<Product> productList, LayoutInflater layoutInflater) {
        this.context = context;
        this.productList = productList;
        this.layoutInflater = layoutInflater;
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    // Permite establecer el oyente
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_list_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.productNameTextView.setText("Nombre: " + product.getName());
        holder.productPriceTextView.setText("$ " + Double.toString(product.getPrice()));
        holder.productCategoryTextView.setText("Categoria: " + product.getCategory());
        AsyncTask asyncTask = new LoadImageTask(holder.productAvatarImageView);
        asyncTask.execute(new String[]{product.getImage()});
        holder.openEditFormDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Notifica al oyente cuando se hace clic en un elemento
                if (onItemClickListener != null && product != null) {
                    onItemClickListener.onItemClick(product);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTextView;
        TextView productPriceTextView;
        TextView productCategoryTextView;
        Button openEditFormDialogButton;
        ImageView productAvatarImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.textViewNameItem);
            productPriceTextView = itemView.findViewById(R.id.textViewPriceItem);
            productCategoryTextView = itemView.findViewById(R.id.textViewCategoryItem);
            productAvatarImageView = itemView.findViewById(R.id.imageViewItem);
            openEditFormDialogButton = itemView.findViewById(R.id.buttonEditarItem);
            // Initialize other views as needed
        }
    }


}
