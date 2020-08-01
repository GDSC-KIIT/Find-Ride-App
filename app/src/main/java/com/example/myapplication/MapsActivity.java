package com.example.myapplication;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.directionhelpers.FetchURL;
import com.example.myapplication.directionhelpers.TaskLoadedCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DecimalFormat;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap mMap1;
    private Polyline polyline;
    private MarkerOptions m1,m2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Button backbutton=findViewById(R.id.backButton);
        Button callButton=findViewById(R.id.callButton);
        TextView textViewDistance=findViewById(R.id.distanceText);
        //TODO Accept the coordinates of driver

        Bundle b=getIntent().getExtras();
        double cusLat=b.getDouble("cuslat");
        double cuslong=b.getDouble("cuslong");
        double driverlat=b.getDouble("drilat");
        double driverlong=b.getDouble("drilon");

        m1 = new MarkerOptions().position(new LatLng(cusLat,cuslong)).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        m2 = new MarkerOptions().position(new LatLng(driverlat,driverlong)).title("Driver's Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //Distance

        Location location1=new Location("");
        location1.setLatitude(driverlat);
        location1.setLongitude(driverlong);

        Location location2=new Location("");
        location2.setLatitude(cusLat);
        location2.setLongitude(cuslong);

        double distance=location1.distanceTo(location2)/1000;
        DecimalFormat f = new DecimalFormat("##.00");
        if(Math.floor(distance)!=0)
        textViewDistance.setText("Distance is :-     "+f.format(distance)+ " Km");
        else
            textViewDistance.setText("Distance is :-    0"+f.format(distance)+ " Km");


        //Work of buttons


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FetchURL(MapsActivity.this).execute(getUrl(m1.getPosition(), m2.getPosition(), "driving"), "driving");

            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//todo get the phone number using intent
                Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:9748958563"));
                startActivity(intent);
            }
        });
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     *
     * @return
     */
    public BitmapDrawable writeOnDrawable(int drawableId, String text){

        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId).copy(Bitmap.Config.ARGB_8888, true);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTextSize(20);

        Canvas canvas = new Canvas(bm);
        canvas.drawText(text, 0, bm.getHeight()/2, paint);

        return new BitmapDrawable(bm);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add a marker1 in Latitude and Longitude and move the camera
        mMap1 = googleMap;
        Log.d("mylog", "Added Markers");
        mMap1.addMarker(m1);
        mMap1.addMarker(m2);

        //Animating the camera
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        builder.include(m1.getPosition());
        builder.include(m2.getPosition());

        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.20); // offset from edges of the map 10% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mMap1.animateCamera(cu);

    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (polyline != null)
            polyline.remove();
        polyline = mMap1.addPolyline((PolylineOptions) values[0]);
    }



}



