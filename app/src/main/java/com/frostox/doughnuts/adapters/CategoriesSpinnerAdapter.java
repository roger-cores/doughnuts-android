package com.frostox.doughnuts.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.frostox.doughnuts.R;
import com.frostox.doughnuts.entities.Category;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by roger on 11/1/2016.
 */
public class CategoriesSpinnerAdapter extends ArrayAdapter<Category> {

    LinkedHashMap<String, Category> categories;



    public CategoriesSpinnerAdapter(Context context, int resource, LinkedHashMap<String, Category> categories) {
        super(context, resource);
        this.categories = categories;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_dropdown_item, null, false);
        }

        ((TextView) convertView).setText(new ArrayList<>(categories.values()).get(position).getName());

        return convertView;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_dropdown_item, null, false);
        }

        ((TextView) convertView).setText(new ArrayList<>(categories.values()).get(position).getName());

        return convertView;
    }

    @Override
    public Category getItem(int position) {
        if(categories != null && categories.size() > position){
            return new ArrayList<>(categories.values()).get(position);
        } else return null;
    }

    @Override
    public int getCount() {
        if(categories != null) return categories.size();
        else return 0;
    }

    public LinkedHashMap<String, Category> getCategories() {
        return categories;
    }

    public void setCategories(LinkedHashMap<String, Category> categories) {
        this.categories = categories;
    }
}
