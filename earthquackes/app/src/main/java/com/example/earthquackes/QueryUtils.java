package com.example.earthquackes;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {
    private static final String LOG_TAG= QueryUtils.class.getSimpleName();

    private QueryUtils() {}

    public static List<Earthquake> extractQuackes(String url){

        //Create URL object
        URL quackeUrl =createUrl(url);

        String jsonResponse=null;
        try{
            //Make HTTP request to the URL object and receiving json response
            jsonResponse=  httpRequest(quackeUrl);
        }catch (IOException e){
            Log.e(LOG_TAG,"Error making http request: ",e);
        }

        //Get earthquakes list from json response
        return getQuackesList(jsonResponse);

    }

    private static URL createUrl(String stringUrl){
        URL url=null;
        try{
            url=new URL(stringUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG,"Error with creating url: ",e);
        }
        return url;
    }

    private static String httpRequest(URL url) throws IOException {
        String jsonResponse="";
        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection= null;
        InputStream inputStream=null;

        try{
            urlConnection= (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //If connection successful i.e. response code is 200
            //Read inputStream
            if(urlConnection.getResponseCode()==200){
                inputStream=urlConnection.getInputStream();
                jsonResponse=readFromStream(inputStream);
            }else{
                Log.e(LOG_TAG,"Error response code: "+urlConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(LOG_TAG,"Problem establishing connection: ",e);
        }finally {
            if(urlConnection!= null){
                urlConnection.disconnect();
            }

            if (inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output= new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader= new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader= new BufferedReader(inputStreamReader);
            String line=reader.readLine();
            while(line != null){
                output.append(line);
                line=reader.readLine();
            }
        }

        return output.toString();
    }

    private static List<Earthquake> getQuackesList(String json){
        List<Earthquake> quakes = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray features = jsonObject.getJSONArray("features");
            for (int i = 0; i < features.length(); i++) {
                quakes.add(new Earthquake(features.getJSONObject(i).getJSONObject("properties").getDouble("mag"), features.getJSONObject(i).getJSONObject("properties").getString("place"), features.getJSONObject(i).getJSONObject("properties").getLong("time"),features.getJSONObject(i).getJSONObject("properties").getString("url")));
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return quakes;
    }
}
