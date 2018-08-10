package com.example.dajeffri.shoppinglist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.dajeffri.shoppinglist.R;

import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int FULL_LAYOUT_TYPE = 1;
    private static final int STORE_LAYOUT_TYPE = 2;

    private StoreViewModel mStoreViewModel;
    private RecyclerView mStoreRecyclerView;
    private StoreAdapter mStoreAdapter;
    private boolean displayingStores = false;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNavigationView = findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        mStoreRecyclerView = findViewById(R.id.stores_recycler);
        mStoreAdapter = new StoreAdapter(this);

        mStoreViewModel = ViewModelProviders.of(this).get(StoreViewModel.class);
        mStoreViewModel.getAllStores().observe(this, new Observer<List<Store>>() {
            @Override
            public void onChanged(@Nullable List<Store> stores) {
                mStoreAdapter.setStores(stores);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ItemEditActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        displayFullList();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (mStoreAdapter.getSingleStore()) {
            mStoreAdapter.setSingleStore(false);
            if (displayingStores) {
                displayStores();
            } else {
                displayFullList();
            }
        } else if (mNavigationView.getMenu().findItem(R.id.nav_stores).isChecked()) {
            displayFullList();
        } else{
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_stores:
                displayStores();
                break;
            case R.id.nav_full:
                displayFullList();
                break;
            case R.id.nav_send:
                sendEmail();
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mStoreViewModel.deleteItems();
    }

    private void displayStores() {
        displayingStores = true;
        mStoreAdapter.setLayoutType(STORE_LAYOUT_TYPE);
        updateViewModel();
        mStoreRecyclerView.setAdapter(mStoreAdapter);
        mStoreRecyclerView.setLayoutManager(new GridLayoutManager(this,  getResources().getInteger(R.integer.store_grid_span)));
    }

    private void displayFullList() {
        mStoreAdapter.setLayoutType(FULL_LAYOUT_TYPE);
        mNavigationView.getMenu().findItem(R.id.nav_full).setChecked(true);
        displayingStores = false;
        updateViewModel();
        mStoreRecyclerView.setAdapter(mStoreAdapter);
        mStoreRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void updateViewModel() {
        mStoreViewModel = ViewModelProviders.of(this).get(StoreViewModel.class);
        mStoreViewModel.getAllStores().observe(this, new Observer<List<Store>>() {
            @Override
            public void onChanged(@Nullable List<Store> stores) {
                mStoreAdapter.setStores(stores);
            }
        });
    }

    private void sendEmail() {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(getString(R.string.email_message_type));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_message_subject));
        String email = mStoreAdapter.getEmail();
        intent.putExtra(Intent.EXTRA_TEXT, email);
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.email_message_title)));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(MainActivity.this, getString(R.string.email_message_error), Toast.LENGTH_SHORT).show();
        }
    }
}
