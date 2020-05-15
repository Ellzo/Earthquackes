package com.example.earthquackes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;

public class EarthquackeLoader extends AsyncTaskLoader<List<Earthquake>> {
    private String url;
    private Context context;

    public EarthquackeLoader(@NonNull Context context,String url) {
        super(context);
        this.context=context;
        this.url=url;
    }

    @Nullable
    @Override
    public List<Earthquake> loadInBackground() {
        if(url == null){
            return null;
        }

        return QueryUtils.extractQuackes(url);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}

