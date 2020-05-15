package com.example.earthquackes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.icu.text.DecimalFormat;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

public class QuackesAdapter extends RecyclerView.Adapter<QuackesAdapter.QuackeViewHolder> {
    private ArrayList<Earthquake>earthquakes;
    private LayoutInflater mInflayer;
    private Context context;

    public QuackesAdapter(Activity context, List<Earthquake> earthquackes){
        mInflayer= LayoutInflater.from(context);
        this.context=context;
        if(earthquackes != null) {
            this.earthquakes = (ArrayList) earthquackes;
        }

    }

    @NonNull
    @Override
    public QuackesAdapter.QuackeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView= mInflayer.inflate(R.layout.earthquake_layout,parent,false);
        return new QuackeViewHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuackeViewHolder holder, int position) {
        final Earthquake earthquake=earthquakes.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri= Uri.parse(earthquake.getUrl());
                Intent intent= new Intent(Intent.ACTION_VIEW,uri);
                if(intent.resolveActivity(context.getPackageManager())!= null){
                    context.startActivity(intent);
                }
            }
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            if (earthquake.getMagnitude()<10){
                holder.getMagnitude().setText(new DecimalFormat("0.0").format(earthquake.getMagnitude()));
            }else{
                holder.getMagnitude().setText(new DecimalFormat("0").format(earthquake.getMagnitude()));
            }
        }else{
            holder.getMagnitude().setText(Double.toString(earthquake.getMagnitude()));
        }

        if(earthquake.getPlace().contains("km")){
            holder.getDistance().setText(earthquake.getPlace().substring(0,earthquake.getPlace().indexOf('f')+1));
            holder.getPlace().setText(earthquake.getPlace().substring(earthquake.getPlace().indexOf('f')+2).toUpperCase());
        }else{
            holder.getDistance().setText(R.string.near);
            holder.getPlace().setText(earthquake.getPlace().toUpperCase());
        }

        /*
        // We Can use this code too:

         if (originalLocation.contains(LOCATION_SEPARATOR)) {
    String[] parts = originalLocation.split(LOCATION_SEPARATOR);
    locationOffset = parts[0] + LOCATION_SEPARATOR;
    primaryLocation = parts[1];
 } else {
    locationOffset = getContext().getString(R.string.near_the);
    primaryLocation = originalLocation;
 }
         */

        SimpleDateFormat dateFormat= new SimpleDateFormat("MMM dd, yyyy");
        holder.getDate().setText(dateFormat.format(new Date(earthquake.getDate())));
        SimpleDateFormat timeFormat= new SimpleDateFormat("HH:mm a");
        holder.getTime().setText(timeFormat.format(new Date(earthquake.getDate())));

        GradientDrawable magnitudeCircle= (GradientDrawable) holder.getMagnitude().getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(earthquake.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

    }

    private int getMagnitudeColor(Double mag){
        switch ((int) Math.round(mag)){
            case 0:
            case 1:
                return ContextCompat.getColor(context, R.color.magnitude1);
            case 2:
                return ContextCompat.getColor(context, R.color.magnitude2);
            case 3:
                return ContextCompat.getColor(context, R.color.magnitude3);
            case 4:
                return ContextCompat.getColor(context, R.color.magnitude4);
            case 5:
                return ContextCompat.getColor(context, R.color.magnitude5);
            case 6:
                return ContextCompat.getColor(context, R.color.magnitude6);
            case 7:
                return ContextCompat.getColor(context, R.color.magnitude7);
            case 8:
                return ContextCompat.getColor(context, R.color.magnitude8);
            case 9:
                return ContextCompat.getColor(context, R.color.magnitude9);
            default:
                return ContextCompat.getColor(context, R.color.magnitude10plus);

        }
    }

    @Override
    public int getItemCount() {
        return earthquakes.size();
    }

    public void clear(){
        earthquakes.clear();
        mInflayer=null;
        context=null;
    }


    class QuackeViewHolder extends RecyclerView.ViewHolder{
        private TextView magnitude;
        private TextView distance;
        private TextView place;
        private TextView date;
        private TextView time;

        public QuackeViewHolder(@NonNull View itemView) {
            super(itemView);
            magnitude= itemView.findViewById(R.id.magnitude);
            distance= itemView.findViewById(R.id.distance);
            place= itemView.findViewById(R.id.place);
            date= itemView.findViewById(R.id.date);
            time= itemView.findViewById(R.id.time);
        }


        public TextView getDate() {
            return date;
        }

        public TextView getMagnitude() {
            return magnitude;
        }

        public TextView getPlace() {
            return place;
        }

        public TextView getTime() {
            return time;
        }

        public TextView getDistance(){
            return distance;
        }
    }
}
