package com.frostox.doughnuts.adapters;

import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frostox.doughnuts.R;
import com.frostox.doughnuts.entities.Category;
import com.frostox.doughnuts.viewholders.CategoryViewHolder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by roger on 11/1/2016.
 */
public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    LinkedHashMap<String, Category> categoryHashMap;
    File categoryImagesDirectory;
    Context context;

    public CategoryRecyclerAdapter(Context context, LinkedHashMap<String, Category> categoryHashMap) {
        this.categoryHashMap = categoryHashMap;
        this.context = context;
        this.categoryImagesDirectory = new File(context.getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()), "doughnuts/category_imgs");
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Category category = new ArrayList<>(categoryHashMap.values()).get(position);
        holder.name.setText(category.getName());
        File image = new File(categoryImagesDirectory, category.getKey() + ".jpg");
        if(image.exists())
            Picasso.with(context).load(image).into(holder.image);

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        if(categoryHashMap != null) return categoryHashMap.values().size();
        else return 0;
    }

    public LinkedHashMap<String, Category> getCategoryHashMap() {
        return categoryHashMap;
    }

    public void setCategoryHashMap(LinkedHashMap<String, Category> categoryHashMap) {
        this.categoryHashMap = categoryHashMap;
    }
}
