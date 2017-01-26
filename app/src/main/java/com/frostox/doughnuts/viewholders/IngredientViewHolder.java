package com.frostox.doughnuts.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.frostox.doughnuts.R;

import org.w3c.dom.Text;

/**
 * Created by roger on 11/1/2016.
 */
public class IngredientViewHolder extends RecyclerView.ViewHolder {

    CheckBox checkBox;

    TextView count, unit, name;

    ImageView delete;

    public IngredientViewHolder(View itemView) {
        super(itemView);

        count = (TextView) itemView.findViewById(R.id.ingredients_item_count);
        unit = (TextView) itemView.findViewById(R.id.ingredients_item_unit);
        name = (TextView) itemView.findViewById(R.id.ingredients_item_name);



    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public TextView getCount() {
        return count;
    }

    public void setCount(TextView count) {
        this.count = count;
    }

    public TextView getUnit() {
        return unit;
    }

    public void setUnit(TextView unit) {
        this.unit = unit;
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public ImageView getDelete() {
        return delete;
    }

    public void setDelete(ImageView delete) {
        this.delete = delete;
    }
}
