package com.frostox.doughnuts.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.frostox.doughnuts.R;
import com.frostox.doughnuts.adapters.CategoriesSpinnerAdapter;
import com.frostox.doughnuts.entities.Category;
import com.frostox.doughnuts.entities.Recipe;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by roger on 11/1/2016.
 */
public class RecipeInfoFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private static final Integer PICK_IMAGE_FROM_GALLERY = 0, PICK_IMAGE_FROM_CAMERA = 1, CROP_IMAGE = 2;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;

    FirebaseDatabase database;
    DatabaseReference userData;
    DatabaseReference userRecipes;

    private FirebaseAuth mAuth;

    private CategoriesSpinnerAdapter adapter;

    FloatingActionButton addImage;
    ImageView avatar;
    EditText name;
    Spinner categoriySpinner;
    String selectedCategory = null;
    private File appDirectory;
    private File tempImage;
    private Uri resultUri;

    public RecipeInfoFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static RecipeInfoFragment newInstance(int sectionNumber) {
        RecipeInfoFragment fragment = new RecipeInfoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_info, null, false);


        name = (EditText) rootView.findViewById(R.id.fragment_recipe_info_name);

        categoriySpinner = (Spinner) rootView.findViewById(R.id.fragment_recipe_info_category_spinner);

        categoriySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategory = ((Category) adapterView.getItemAtPosition(i)).getKey();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference categoriesRef = database.getReference("categories");

        categoriesRef.addValueEventListener(new ValueEventListener() {

            LinkedHashMap<String, Category> categories = new LinkedHashMap<String, Category>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categories.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    categories.put(postSnapshot.getKey(), postSnapshot.getValue(Category.class));
                }

                adapter = new CategoriesSpinnerAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, categories);
                categoriySpinner.setAdapter(adapter);
                categoriesRef.removeEventListener(this);

                categoriesRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        Category category = dataSnapshot.getValue(Category.class);

                        adapter.getCategories().put(dataSnapshot.getKey(), dataSnapshot.getValue(Category.class));
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Category category = dataSnapshot.getValue(Category.class);

                        adapter.getCategories().put(dataSnapshot.getKey(), dataSnapshot.getValue(Category.class));
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        Category category = dataSnapshot.getValue(Category.class);
                        adapter.getCategories().remove(dataSnapshot.getKey());
                        adapter.notifyDataSetChanged();

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

            }
        });


        appDirectory =  new File(getActivity().getExternalFilesDir(Environment.getDataDirectory().getAbsolutePath()), "doughnuts");
        appDirectory.mkdirs();
        tempImage = new File(appDirectory, "tempImageFile");
        try {
            tempImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


        addImage = (FloatingActionButton) rootView.findViewById(R.id.fab_select_image);
        avatar = (ImageView) rootView.findViewById(R.id.fragment_recipe_info_avatar);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(getContext())
                        .setTitle("What's it gonna be?")
                        .setMessage("Would you like to take a pic now or just pick from gallery?")
                        .setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                                            MY_PERMISSIONS_REQUEST_CAMERA);

                                } else {

                                    Uri outputUri = Uri.fromFile(tempImage);



                                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    takePicture.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
                                    startActivityForResult(takePicture, PICK_IMAGE_FROM_CAMERA);
                                }




                            }
                        }).setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto , PICK_IMAGE_FROM_GALLERY);
                            }
                        }).setCancelable(false)
                        .show();

            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_FROM_GALLERY && resultCode == Activity.RESULT_OK && data != null){
            CropImage.activity(data.getData())
                    .setAspectRatio(1,1)
                    .start(getContext(), this);
        } else if(requestCode == PICK_IMAGE_FROM_CAMERA && resultCode == Activity.RESULT_OK && data != null){
                CropImage.activity(Uri.parse(tempImage.toURI().toString()))
                        .setAspectRatio(1,1)
                        .start(getContext(), this);
//              Picasso.with(getContext()).load(Uri.fromFile(tempImage)).into(avatar);
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                resultUri = result.getUri();
                Picasso.with(getContext()).load(resultUri).into(avatar);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(getContext(), R.string.contingency_error, Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Uri outputUri = Uri.fromFile(tempImage);

                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    takePicture.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
                    startActivityForResult(takePicture, PICK_IMAGE_FROM_CAMERA);


                } else {

                    //nothing can be done
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public boolean next(){

        String name = this.name.getText().toString();

        if(name.equals("")){
            this.name.setError("What do we call it?");
            return false;
        } else if(name.contains(" ")){
            this.name.setError("I don't like these spaces, remove them please!");
            return false;
        } else return true;
    }

    public Recipe getRecipe(){
        Recipe recipe = new Recipe();
        recipe.setName(name.getText().toString());
        selectedCategory = ((Category) categoriySpinner.getSelectedItem()).getKey();
        recipe.setCategory(selectedCategory);
        return recipe;
    }

    public Uri getImageUri(){
        return resultUri;
    }
}
