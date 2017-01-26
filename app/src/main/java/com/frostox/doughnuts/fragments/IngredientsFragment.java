package com.frostox.doughnuts.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.frostox.doughnuts.R;
import com.frostox.doughnuts.adapters.IngredientsRecyclerAdapter;
import com.frostox.doughnuts.entities.Ingredient;

import java.util.LinkedHashMap;

/**
 * Created by roger on 11/1/2016.
 */
public class IngredientsFragment extends Fragment {


    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private RecyclerView recyclerView;
    private IngredientsRecyclerAdapter adapter;

    public IngredientsFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static IngredientsFragment newInstance(int sectionNumber) {
        IngredientsFragment fragment = new IngredientsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_ingredient_recycler_view);
        adapter = new IngredientsRecyclerAdapter(new LinkedHashMap<String, Ingredient>());
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        return rootView;
    }

    public void add(){


    }


}
