package com.example.capstone.Util;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.capstone.DB.Category;
import com.example.capstone.ProductList;
import com.example.capstone.R;

import java.util.List;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Category> cat_names;
    private Context context;
    Category cCategory;

    public CategoryAdapter(Context context, List<Category> cat_names) {
        this.cat_names = cat_names;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.cardview_category,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        cCategory = cat_names.get(i);
        viewHolder.catname.setText(cCategory.getCat_name());
    }

    @Override
    public int getItemCount() {
        return cat_names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView catname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catname = itemView.findViewById(R.id.cat_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Category cCategory = cat_names.get(getAdapterPosition());
            Intent i = new Intent(context, ProductList.class);
            i.putExtra("cat_id",cCategory.getId());
            i.putExtra("cat_name",cCategory.getCat_name());
            context.startActivity(i);
        }
    }
}
