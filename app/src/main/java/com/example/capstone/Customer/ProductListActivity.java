package com.example.capstone.Customer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.example.capstone.DB.DatabaseHelper;
import com.example.capstone.DB.Product;
import com.example.capstone.DB.ProductImages;
import com.example.capstone.DB.Vendor;
import com.example.capstone.R;
import com.example.capstone.Util.ProductListAdapter;
import com.example.capstone.Util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {

    RecyclerView rvlist;
    DatabaseHelper helper;
    TextView tvCat;
    EditText etFilter;
    List<Product> prodList, uProdList;
    List<ProductImages> prodImgList, uProdImgList;
    List<Vendor> vendorList, uVendorList;
    Toolbar toolbar;
    ProductListAdapter productListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        init();

        etFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                FilterSearch fs = new FilterSearch();
                fs.execute();
            }
        });

        rvlist.setHasFixedSize(true);
        rvlist.setLayoutManager(new GridLayoutManager(ProductListActivity.this,2));
        productListAdapter = new ProductListAdapter(
                ProductListActivity.this,
                prodList,
                prodImgList,
                vendorList
        );
        rvlist.setAdapter(productListAdapter);
    }

    public void init() {
        tvCat = findViewById(R.id.tvCat);
        etFilter = findViewById(R.id.etFilter);
        rvlist = findViewById(R.id.rv_products);
        helper = new DatabaseHelper(this);
        prodList = new ArrayList<>();
        prodImgList = new ArrayList<>();
        vendorList = new ArrayList<>();

        Intent i = getIntent();
        String id = i.getStringExtra("cat_id");
        tvCat.setText(i.getStringExtra("cat_name"));

        prodList = helper.getAllProducts("category",id);
        for (int x = 0; x < prodList.size(); x++) {
            prodImgList.add(helper.getProductImages(prodList.get(x).getId()));
            vendorList.add(helper.getVendor(prodList.get(x).getVendor_id()));
        }
    }

    private class FilterSearch extends AsyncTask<String, Integer, String>{

        @Override
        protected String doInBackground(String... strings) {
            filterSearch();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            productListAdapter.updateList(uProdList,uVendorList,uProdImgList);
        }
    }

    public void filterSearch() {
        uProdList = new ArrayList<>();
        uProdImgList = new ArrayList<>();
        uVendorList = new ArrayList<>();
        for(Product prod : prodList) {
            if(prod.getName().toLowerCase().contains(etFilter.getText().toString().toLowerCase())) {
                uProdList.add(prod);
                uVendorList.add(helper.getVendor(prod.getVendor_id()));
                uProdImgList.add(helper.getProductImages(prod.getId()));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuLogout:
                final AlertDialog.Builder backBuild = new AlertDialog.Builder(this);
                backBuild.setCancelable(false);
                backBuild.setTitle("Logout");
                backBuild.setIcon(android.R.drawable.ic_dialog_alert);
                backBuild.setMessage("Logout dari Akun Anda dan Keluar dari Aplikasi?");
                backBuild.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new SessionManager(ProductListActivity.this).logoutUser();
                        finish();
                    }
                });
                backBuild.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                backBuild.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
