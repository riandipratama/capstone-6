package com.example.capstone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.capstone.DB.DatabaseHelper;
import com.example.capstone.DB.Product;
import com.example.capstone.DB.ProductImages;
import com.example.capstone.DB.Vendor;
import com.example.capstone.Util.ProductDetailsImagesAdapter;

public class ProductDetails extends AppCompatActivity {

    TextView tvName, tvVendor, tvHarga, tvQty, tvLokasi, tvDesc;
    Button btnAddCart, btnCheckout;
    RecyclerView rvprodimg;
    Product prod;
    Vendor vendor;
    DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        init();

        Intent i = getIntent();
        prod = helper.getProduct(i.getStringExtra("prod_id"));
        vendor = helper.getVendor(prod.getVendor_id());

        setTexts(prod,vendor);

        rvprodimg.setHasFixedSize(true);
        rvprodimg.setLayoutManager(new LinearLayoutManager(ProductDetails.this,LinearLayout.HORIZONTAL,false));
        rvprodimg.setAdapter(new ProductDetailsImagesAdapter(
                ProductDetails.this,
                helper.getAllProductImages(prod.getId())
        ));


    }

    public void init() {
        tvName = findViewById(R.id.tvName);
        tvVendor = findViewById(R.id.tvVendor);
        tvHarga = findViewById(R.id.tvHarga);
        tvQty = findViewById(R.id.tvQty);
        tvLokasi = findViewById(R.id.tvLokasi);
        tvDesc = findViewById(R.id.tvDesc);
        btnAddCart = findViewById(R.id.btnAddCart);
        btnCheckout = findViewById(R.id.btnCheckout);
        rvprodimg = findViewById(R.id.rvprodimg);

        helper = new DatabaseHelper(this);
    }

    public void setTexts(Product prod, Vendor vendor) {
        tvName.setText(prod.getName());
        tvVendor.setText(vendor.getName());
        tvHarga.setText(prod.getPrice());
        tvQty.setText(prod.getQty());
        tvLokasi.setText(
                getResources().getString(
                        R.string.lokasi_produk_detail,
                        vendor.getAddress(),vendor.getCity()));
        tvDesc.setText(prod.getDesc());
    }
}
