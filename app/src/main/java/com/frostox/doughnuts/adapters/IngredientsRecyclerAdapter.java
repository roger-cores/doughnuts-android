package com.frostox.doughnuts.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frostox.doughnuts.R;
import com.frostox.doughnuts.entities.Ingredient;
import com.frostox.doughnuts.viewholders.IngredientViewHolder;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by roger on 11/1/2016.
 */
public class IngredientsRecyclerAdapter extends RecyclerView.Adapter<IngredientViewHolder> {


    LinkedHashMap<String, Ingredient> ingredients;

    public IngredientsRecyclerAdapter(LinkedHashMap<String, Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients_item, null, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final IngredientViewHolder holder, final int position) {
        Ingredient ingredient = new ArrayList<>(ingredients.values()).get(position);

        holder.getCount().setText(ingredient.getCount() + "");
        holder.getUnit().setText(ingredient.getUnit());


    }



    @Override
    public int getItemCount() {
        if(ingredients != null){
            return ingredients.size();
        } else return 0;
    }

    public LinkedHashMap<String, Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(LinkedHashMap<String, Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
