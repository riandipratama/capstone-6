package com.example.capstone.Util;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.capstone.DB.ProductImages;
import com.example.capstone.R;

import java.util.List;

public class ProductDetailsImagesAdapter extends RecyclerView.Adapter<ProductDetailsImagesAdapter.ViewHolder> {

    Context context;
    List<ProductImages> prodImgList;
    ProductImages prodImg;

    public ProductDetailsImagesAdapter(Context context, List<ProductImages> prodImgList) {
        this.context = context;
        this.prodImgList = prodImgList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.cardview_product_details,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        prodImg = prodImgList.get(i);
        viewHolder.imgList.setImageBitmap(BitmapFactory.decodeFile(prodImg.getImgPath()));
    }

    @Override
    public int getItemCount() {
        return prodImgList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgList = itemView.findViewById(R.id.prodImgCard);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
