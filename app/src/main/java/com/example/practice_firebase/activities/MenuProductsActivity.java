package com.example.practice_firebase.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.practice_firebase.R;

public class MenuProductsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_products);

        findViewById(R.id.buttonStartViewNewProduct).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuProductsActivity.this, NewProduct.class));
            }
        });
        findViewById(R.id.buttonStartViewListProducts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuProductsActivity.this, ListProductActivity.class));
            }
        });
        findViewById(R.id.buttonStartViewTabs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuProductsActivity.this, TabsActivity.class));
            }
        });
    }
}