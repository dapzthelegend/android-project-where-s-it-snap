package com.gamecodeschool.whereitssnap;

import android.Manifest;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.gamecodeschool.myapplication.BlankFragment;
import com.gamecodeschool.myapplication.ViewFragment;


public class MainActivity extends AppCompatActivity implements ActivityComs,BlankFragment.OpenCapture {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment;
    public ContactDbHelper contactDbHelper;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //action bar with the settings
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        //initialize the contactDbhelper
        contactDbHelper = new ContactDbHelper(getApplicationContext());
        BlankFragment blankFragment = new BlankFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer,blankFragment).commit();




        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView= findViewById(R.id.navigationView);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.menuCapture:
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        CaptureFragment captureFragment = new CaptureFragment();
                        if (findViewById(R.id.fragmentContainer) != null) {
                            if (Build.VERSION.SDK_INT >= 23) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 5);

                            }

                            if (savedInstanceState != null) {
                                return true;
                            }
                              fragment = captureFragment;
                            fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.fragmentContainer, captureFragment, null).commit();

                        }
                        return true;

                    case R.id.menuTags:
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        TagsFragment tagsFragment = new TagsFragment();
                        if (findViewById(R.id.fragmentContainer) != null) {

                            if (savedInstanceState != null) {
                                return true;
                            }

                            fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragmentContainer, tagsFragment, null).addToBackStack(null).commit();

                        }
                        return  true;


                    case R.id.menuTitle:
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        TitlesFragment titlesFragment = new TitlesFragment();
                        Bundle args = new Bundle();
                        args.putString("Tag","_NO_TAG");
                        titlesFragment.setArguments(args);
                        if (findViewById(R.id.fragmentContainer) != null) {

                            if (savedInstanceState != null) {
                                return true;
                            }

                            fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.fragmentContainer,titlesFragment, null).addToBackStack(null).commit();

                        }
                        return true;
                }

                return false;
            }
        });

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home:
                BlankFragment blankFragment = new BlankFragment();
               fragmentTransaction = getSupportFragmentManager().beginTransaction();
               fragmentTransaction.replace(R.id.fragmentContainer, blankFragment,null).addToBackStack(null).commit();

                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTitlesListItemSelected(int pos) {
        Bundle args = new Bundle();
        args.putInt("Position", pos);

        ViewFragment viewFragment = new ViewFragment();
        viewFragment.setArguments(args);

        if(viewFragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,viewFragment,"VIEW").addToBackStack(null).commit();

            Log.e("Title", "Displayed view fragment");
        }

        else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void onTagsListItemSelected(String tag) {
        Bundle args = new Bundle();
        args.putString("Tag", tag);

        TitlesFragment titlesFragment = new TitlesFragment();

        titlesFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, titlesFragment,"TAGS").addToBackStack(null).commit();

    }

    @Override
    public void open() {
        CaptureFragment captureFragment = new CaptureFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,captureFragment).commit();

    }
}
