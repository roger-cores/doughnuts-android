package com.frostox.doughnuts.fragments;


import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.frostox.doughnuts.R;
import com.frostox.doughnuts.adapters.CategoryRecyclerAdapter;
import com.frostox.doughnuts.entities.Category;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by roger on 11/1/2016.
 */
public class CategoriesFragment extends Fragment {


    Button back, current;
    RecyclerView recyclerView;

    StorageReference storageRef;
    StorageReference categoryImages;

    File appDirectory;
    File categoryImagesDirectory;

    CategoryRecyclerAdapter adapter;



    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public CategoriesFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static CategoriesFragment newInstance(int sectionNumber) {
        CategoriesFragment fragment = new CategoriesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_categories, container, false);
        back = (Button) rootView.findViewById(R.id.fragment_categories_back);
        current = (Button) rootView.findViewById(R.id.fragment_categories_current);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_categories_recycler_view);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference categoriesRef = database.getReference("categories");
        appDirectory =  new File(getActivity().getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()), "doughnuts");
        if(!appDirectory.exists()) appDirectory.mkdirs();
        categoryImagesDirectory = new File(appDirectory, "category_imgs");
        if(!categoryImagesDirectory.exists()) categoryImagesDirectory.mkdirs();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        categoryImages = storageRef.child("category_imgs");

        categoriesRef.addValueEventListener(new ValueEventListener() {

            LinkedHashMap<String, Category> categories = new LinkedHashMap<String, Category>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categories.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    categories.put(postSnapshot.getKey(), postSnapshot.getValue(Category.class));

                }

                adapter = new CategoryRecyclerAdapter(getContext(), categories);
                recyclerView.setAdapter(adapter);
                GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(layoutManager);
                categoriesRef.removeEventListener(this);


                for(Category category: categories.values()){
                    StorageReference imageStorageReference = categoryImages.child(category.getKey() + ".jpg");
                    File image = new File(categoryImagesDirectory, imageStorageReference.getName());
                    if(!image.exists())
                        imageStorageReference.getFile(image).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                // Local temp file has been created
                                Log.d(CategoriesFragment.class.getName(), "Download Done");
                                if(adapter != null) adapter.notifyDataSetChanged();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                                Log.d(CategoriesFragment.class.getName(), "Download Failed");
                            }
                        });
                }



                categoriesRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        Category category = dataSnapshot.getValue(Category.class);

                        adapter.getCategoryHashMap().put(dataSnapshot.getKey(), category);
                        adapter.notifyDataSetChanged();

                        StorageReference imageStorageReference = categoryImages.child(category.getKey() + ".jpg");
                        File image = new File(categoryImagesDirectory, imageStorageReference.getName());
                        if(!image.exists())
                            imageStorageReference.getFile(image).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    // Local temp file has been created
                                    Log.d(CategoriesFragment.class.getName(), "Download Done");
                                    if(adapter != null) adapter.notifyDataSetChanged();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                    Log.d(CategoriesFragment.class.getName(), "Download Failed");
                                }
                            });
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Category category = dataSnapshot.getValue(Category.class);

                        adapter.getCategoryHashMap().put(dataSnapshot.getKey(), dataSnapshot.getValue(Category.class));
                        adapter.notifyDataSetChanged();

                        StorageReference imageStorageReference = categoryImages.child(category.getKey() + ".jpg");
                        File image = new File(categoryImagesDirectory, imageStorageReference.getName());
                        if(!image.exists())
                            imageStorageReference.getFile(image).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    // Local temp file has been created
                                    Log.d(CategoriesFragment.class.getName(), "Download Done");
                                    if(adapter != null) adapter.notifyDataSetChanged();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                    Log.d(CategoriesFragment.class.getName(), "Download Failed");
                                }
                            });
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Category category = dataSnapshot.getValue(Category.class);
                        adapter.getCategoryHashMap().remove(dataSnapshot.getKey());
                        adapter.notifyDataSetChanged();
                        StorageReference imageStorageReference = categoryImages.child(category.getKey() + ".jpg");
                        File image = new File(categoryImagesDirectory, imageStorageReference.getName());
                        image.delete();
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(CategoriesFragment.class.getName(), "loadPost:onCancelled", databaseError.toException());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(CategoriesFragment.class.getName(), "loadPost:onCancelled", databaseError.toException());

            }
        });



        return rootView;
    }
}
