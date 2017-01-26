package com.frostox.doughnuts.activities;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.frostox.doughnuts.R;
import com.frostox.doughnuts.actionbar.ActionBarCallBack;
import com.frostox.doughnuts.entities.Recipe;
import com.frostox.doughnuts.fragments.IngredientsFragment;
import com.frostox.doughnuts.fragments.RecipeInfoFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RecipeWizardActivity extends AppCompatActivity {

    Toolbar toolbar;

    FirebaseDatabase database;
    DatabaseReference userData;
    private FirebaseAuth mAuth;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private DatabaseReference newRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_wizard);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userData = database.getReference("users").child(mAuth.getCurrentUser().getUid());
        newRecipe = userData.child("editable-recipe");

        toolbar = (Toolbar) findViewById(R.id.activity_recipe_wizard_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final FloatingActionButton fabPrev = (FloatingActionButton) findViewById(R.id.fab_prev);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_next);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mViewPager.getCurrentItem()){
                    case 0:
                        RecipeInfoFragment recipeInfoFragment = (RecipeInfoFragment) mSectionsPagerAdapter.getActiveFragment(mViewPager, 0);
                        if(recipeInfoFragment.next()){
                            Recipe recipe = recipeInfoFragment.getRecipe();
                            recipe.setKey(newRecipe.getKey());
                            recipe.setAuthorUUID(mAuth.getCurrentUser().getUid());
                            newRecipe.setValue(recipe);
                            mViewPager.setCurrentItem(1);
                            fabPrev.setVisibility(View.VISIBLE);
                            getSupportActionBar().setTitle("Ingredient");
                        }
                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                }
            }
        });

        fabPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mViewPager.getCurrentItem()){
                    case 0:
                        break;
                    case 1:
                        mViewPager.setCurrentItem(0);
                        fabPrev.setVisibility(View.GONE);
                        getSupportActionBar().setTitle(R.string.title_activity_recipe_wizard);
                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                }
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    toolbar.getMenu().clear();
                    toolbar.inflateMenu(R.menu.menu_ingredients);
                } else {
                    toolbar.getMenu().clear();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_recipe_wizard, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            if(position == 0){
                return RecipeInfoFragment.newInstance(position + 1);
            } else if(position == 1){
                return IngredientsFragment.newInstance(position + 1);
            } else return PlaceholderFragment.newInstance(position + 1);
        }

        public Fragment getActiveFragment(ViewPager container, int position) {
            String name = makeFragmentName(container.getId(), position);
            return  getSupportFragmentManager().findFragmentByTag(name);
        }

        private String makeFragmentName(int viewId, int index) {
            return "android:switcher:" + viewId + ":" + index;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Recipe Basic Info";
                case 1:
                    return "Ingredient";
                case 2:
                    return "Utensils";
                case 3:
                    return "Directions";
            }
            return null;
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_add_ingredient:
                IngredientsFragment recipeInfoFragment = (IngredientsFragment) mSectionsPagerAdapter.getActiveFragment(mViewPager, 1);
                recipeInfoFragment.add();


                break;
            case android.R.id.home:
                if(newRecipe != null)
                    newRecipe.removeValue();
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if(newRecipe != null)
            newRecipe.removeValue();
        super.onBackPressed();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(newRecipe != null)
            newRecipe.removeValue();
    }
}
