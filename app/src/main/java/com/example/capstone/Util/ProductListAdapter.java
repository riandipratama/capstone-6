package com.example.capstone.Util;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.capstone.DB.Product;
import com.example.capstone.DB.ProductImages;
import com.example.capstone.DB.Vendor;
import com.example.capstone.Customer.ProductDetailsActivity;
import com.example.capstone.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {


    private Context context;
    private List<Product> prod;
    private List<ProductImages> prodImg;
    private List<Vendor> vendor;
    private Product cProd;
    private ProductImages cProdImg;
    private Vendor cVendor;

    public ProductListAdapter(Context context, List<Product> prod, List<ProductImages> prodImg, List<Vendor> vendor) {
        this.context = context;
        this.prod = prod;
        this.prodImg = prodImg;
        this.vendor = vendor;
    }

    @NonNull
    @Override
    public ProductListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_productlist,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListAdapter.ViewHolder viewHolder, int i) {
        cProd = prod.get(i);
        cVendor = vendor.get(i);
        cProdImg = prodImg.get(i);
        viewHolder.bindTo(cProd, cVendor, cProdImg);
    }

    public void updateList(List<Product> uProd, List<Vendor> uVendor, List<ProductImages> uProdImg) {
        prod = uProd;
        vendor = uVendor;
        prodImg = uProdImg;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.prod.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView prodImgCard;
        TextView prodName;
        TextView prodPrice;
        TextView prodCity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            prodImgCard = itemView.findViewById(R.id.prodImgCard);
            prodName = itemView.findViewById(R.id.prodName);
            prodPrice = itemView.findViewById(R.id.prodPrice);
            prodCity = itemView.findViewById(R.id.prodCity);

            itemView.setOnClickListener(this);
        }

        public void bindTo(Product cProd, Vendor cVendor, ProductImages cProdImg) {
            prodName.setText(cProd.getName());
            prodPrice.setText(cProd.getPrice());
            prodCity.setText(cVendor.getCity());
            Picasso.get().load(cProdImg.getImgPath()).into(prodImgCard);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(context, ProductDetailsActivity.class);
            i.putExtra("prod_id",prod.get(getAdapterPosition()).getId());
            context.startActivity(i);
        }
    }
}
