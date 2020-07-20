package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Locale;
//Importing access fine location
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity
{
    //Declaring FusedLocationProviderClient
     private FusedLocationProviderClient fusedLocationProviderClient;
     double lat,lon;
     boolean inside=false;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button=findViewById(R.id.button1);

        //Calling the function to request for the GPS Location permission
        requestPermission();
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        final TextView textView=findViewById(R.id.textView);

        while(ActivityCompat.checkSelfPermission(MainActivity.this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermission();
        }

        //Checking if clicked on allow
        if(ActivityCompat.checkSelfPermission(MainActivity.this,android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            //Gets the GPS Location
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();

                    //Storing the GPS Location
                    if (location != null) {
                        try {
                            inside = true;

                            Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            textView.setText(addresses.get(0).getLatitude() + "," + addresses.get(0).getLongitude());
                            lat = addresses.get(0).getLatitude();
                            lon = addresses.get(0).getLongitude();
                            updateLocationToFirestore(lat,lon);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            //if clicked on the option it moves to MapActivity with the data
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (inside) {
                        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                        intent.putExtra("lat", lat);
                        intent.putExtra("long", lon);

                        startActivity(intent);
                        finish();
                        inside = false;
                    }
                }
            });


        }
    }

    //updating the location to Firestore
    void updateLocationToFirestore(double lat,double lon)
    {
        //Initializing Firestore
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        CollectionReference collectionReference=firebaseFirestore.collection("users");
        String no = getIntent().getStringExtra("mobile");

        //Sending the location
        collectionReference.document(no).update("latitude",lat+"","longitude",lon+"")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Added Location", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //Method for requesting GPS permission
    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION}, 44);
    }
}
