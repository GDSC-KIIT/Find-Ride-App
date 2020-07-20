package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class UpdateLocationActivity extends AppCompatActivity
{

    //Initializing the variables
    Button button;
    private FusedLocationProviderClient fusedLocationProviderClient;
    double lat,lon;
    boolean inside;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_location);
        button = findViewById(R.id.updateButton);
        //When Clicked on update
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Requesting permission for gps
                requestPermission();
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(UpdateLocationActivity.this);

                while (ActivityCompat.checkSelfPermission(UpdateLocationActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermission();
                }

                //Checking if permission granted
                if (ActivityCompat.checkSelfPermission(UpdateLocationActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    //Getting the location coordinates
                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();

                            if (location != null) {
                                try
                                {
                                    inside = true;

                                    Geocoder geocoder = new Geocoder(UpdateLocationActivity.this, Locale.getDefault());
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    lat = addresses.get(0).getLatitude();
                                    lon = addresses.get(0).getLongitude();
                                    updateLocationToFirestore(lat, lon);
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                    Toast.makeText(UpdateLocationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

                }
            }
        });
    }

    //Updating the coordinates to Firestore
    void updateLocationToFirestore(double lat,double lon)
    {
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        CollectionReference collectionReference=firebaseFirestore.collection("driver");
        String no = getIntent().getStringExtra("mobile");
        System.out.println(no);
        collectionReference.document(no).update("latitude",lat+"","longitude",lon+"","available",true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UpdateLocationActivity.this, "Added Location", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UpdateLocationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //Method for requesting permission
    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION}, 44);
    }
}