package com.example.earthquackes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.xml.sax.helpers.XMLReaderAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Earthquake>> {
    private static final String REQUEST_URL=
            "https://earthquake.usgs.gov/fdsnws/event/1/query";
    private final Activity activity=this;
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        empty = findViewById(R.id.empty_view);

        getSupportLoaderManager().initLoader(EARTHQUAKE_LOADER_ID,null,this);


    }

    @NonNull
    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, @Nullable Bundle args) {

        ConnectivityManager conMgr= (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        if(conMgr.getActiveNetworkInfo() == null || !conMgr.getActiveNetworkInfo().isConnected()){
            findViewById(R.id.progress_bar).setVisibility(View.GONE);

            empty.setText(R.string.connection);
            empty.setVisibility(View.VISIBLE);

        }

        SharedPreferences sharedPrefs= PreferenceManager.getDefaultSharedPreferences(this);

        String minMagnitude= sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );


        Uri baseUri= Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder=baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format","geojson");
        uriBuilder.appendQueryParameter("limit","10");
        uriBuilder.appendQueryParameter("minmag",minMagnitude);
        uriBuilder.appendQueryParameter("orderby",orderBy);




        return new EarthquackeLoader(this,uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Earthquake>> loader, List<Earthquake> data) {


        findViewById(R.id.progress_bar).setVisibility(View.GONE);


        // Find a reference to the {@link ListView} in the layout
        RecyclerView recyclerView= findViewById(R.id.recycler);

        if(data == null || data.isEmpty()){
            findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
            return;
        }

        recyclerView.setVisibility(View.VISIBLE);


        // Create a new {@link ArrayAdapter} of earthquakes
        QuackesAdapter adapter = new QuackesAdapter(activity, data);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Earthquake>> loader) {}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== R.id.settings){
            startActivity(new Intent(this,SettingsAct.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}