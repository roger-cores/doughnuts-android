package com.frostox.doughnuts.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.frostox.doughnuts.R;
import com.squareup.picasso.Picasso;

/**
 * Created by roger on 11/1/2016.
 */
public class CategoryViewHolder extends RecyclerView.ViewHolder {

    public TextView name;

    public ImageView image;

    public View container;

    public CategoryViewHolder(View itemView) {
        super(itemView);
        container = itemView;
        name = (TextView) itemView.findViewById(R.id.category_item_text);
        image = (ImageView) itemView.findViewById(R.id.category_item_image);
    }

}
