package com.example.hikebuddy;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Difficulty extends AppCompatActivity {
    private ArrayList<Hike> HikeData;
    private HikeAdapter Adapter;
    private RecyclerView recyclerView;
    private ArrayList<Hike> favHikes;
    private Context mContext;
    SearchView searchView;
    TextView textView;
    static boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbarHome);
        setSupportActionBar(toolbar);
        HikeData = new ArrayList<>();
        recyclerView = findViewById(R.id.rv_hike_list);
        Adapter = new HikeAdapter(this, HikeData);
        recyclerView.setAdapter(Adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        textView = findViewById(R.id.textView);
        mContext = this;
        initializeData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);
        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setIconifiedByDefault(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.trek_foreground);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                filter(query);
                return true;
            }
        });
        return true;
    }

    private void filter(String text) {
        ArrayList<Hike> filteredList = new ArrayList<>();

        for (Hike item : HikeData) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                recyclerView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
                filteredList.add(item);
                Adapter.filterList(filteredList);
            } else if (filteredList.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent detailIntent = new Intent(mContext, HomePage.class);
            mContext.startActivity(detailIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Initialize the Hike data from resources.
     */
    private void initializeData() {
        // Get the resources from the XML file.
        String[] HikeList = getResources()
                .getStringArray(R.array.diff_hike_names);
        String[] HikeInfo = getResources()
                .getStringArray(R.array.diff_hike_info);
        TypedArray HikeImageResources = getResources()
                .obtainTypedArray(R.array.diff_hike_images);
        int[] HikeDifficulty = getResources().getIntArray(R.array.diff_difficulty);
        String[] HikeGear = getResources().getStringArray(R.array.diff_gear);

        // Clear the existing data (to avoid duplication).
        //HikeData.clear();

        // Create the ArrayList of Hike objects with the titles and
        // information about each Hike
        for (int i = 0; i < HikeList.length; i++) {
            HikeData.add(new Hike(HikeList[i], HikeInfo[i],
                    HikeImageResources.getResourceId(i, 0), HikeDifficulty[i], HikeGear[i]));
        }

        //Get favorite hikes list
        SharedPreferences pref = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString("favs", null);
        Type type = new TypeToken<ArrayList<Hike>>() {
        }.getType();
        favHikes = gson.fromJson(json, type);

        //IF favhike list exists then compare to hike data and update its fav status
        if (favHikes != null && favHikes.size() > 0) {
            for (int i = 0; i < favHikes.size(); i++) {
                for (int j = 0; j < HikeData.size(); j++) {
                    if (favHikes.get(i).getTitle().equals(HikeData.get(j).getTitle())) {
                        HikeData.get(j).setFavStatus(true);
                    }
                }
            }
        }

        // Recycle the typed array.
        HikeImageResources.recycle();

        // Notify the adapter of the change.
        Adapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        running = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

}