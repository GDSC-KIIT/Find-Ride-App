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

        //Initial work
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Button backbutton=findViewById(R.id.backButton);
        Button callButton=findViewById(R.id.callButton);
        TextView textViewDistance=findViewById(R.id.distanceText);

        //Accepting the coordinates of driver and customer along with phone number of the customer requested
        Bundle b=getIntent().getExtras();
        double cusLat=b.getDouble("cuslat");
        double cuslong=b.getDouble("cuslong");
        double driverlat=b.getDouble("drilat");
        double driverlong=b.getDouble("drilon");
        final String phno=b.getString("phno");

        //Adding Markers
        m1 = new MarkerOptions().position(new LatLng(cusLat,cuslong)).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        m2 = new MarkerOptions().position(new LatLng(driverlat,driverlong)).title("Driver's Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //Distance between the 2 markers
        //Fetching Location 1
        Location location1=new Location("");
        location1.setLatitude(driverlat);
        location1.setLongitude(driverlong);

        //Fetching Location 2
        Location location2=new Location("");
        location2.setLatitude(cusLat);
        location2.setLongitude(cuslong);

        //Finds the distance between the two location
        double distance=location1.distanceTo(location2)/1000;
        DecimalFormat f = new DecimalFormat("##.00");
        if(Math.floor(distance)!=0)
        textViewDistance.setText("Distance is :-     "+f.format(distance)+ " Km");
        else
            textViewDistance.setText("Distance is :-    0"+f.format(distance)+ " Km");


        //The BACK button
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MapsActivity.this,DriverListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //The CALL Button
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phno));
                startActivity(intent);
            }
        });
    }

    //Colour and other image of the marker change
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

    //When the map is ready
    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add a marker1 and marker 2 in the map
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

    //generating the url of the given map
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


    //Adding polyline
    @Override
    public void onTaskDone(Object... values) {
        if (polyline != null)
            polyline.remove();
        polyline = mMap1.addPolyline((PolylineOptions) values[0]);
    }
}



