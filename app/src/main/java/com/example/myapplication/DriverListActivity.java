package com.example.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.adapters.ListAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class DriverListActivity extends AppCompatActivity
{
    //Initialization
    FirebaseFirestore firebaseFirestore;
    CollectionReference collectionReference;
    private FusedLocationProviderClient fusedLocationProviderClient;
    public double lat;
    public double lon;
    boolean inside=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        //Initial Work
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverview);
        firebaseFirestore=FirebaseFirestore.getInstance();
        collectionReference=firebaseFirestore.collection("driver");

        //Calling the function to request for the GPS Location permission
        requestPermission();
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);

        while(ActivityCompat.checkSelfPermission(DriverListActivity.this,android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermission();
        }


        //Checking if clicked on allow
        if(ActivityCompat.checkSelfPermission(DriverListActivity.this,android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            //Gets the GPS Location
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();

                    //Storing the GPS Location
                    if (location != null) {
                        try {
                            inside = true;
                            Geocoder geocoder = new Geocoder(DriverListActivity.this, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            lat = addresses.get(0).getLatitude();
                            lon = addresses.get(0).getLongitude();
                            updateLocationToFirestore(lat, lon);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(DriverListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }


        //Getting the details from firebase
        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        //Arraylist to store the DriverList objects to be displayed to customer
                        ArrayList<DriverList> driverLists=new ArrayList<>(20);

                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                        {
                            //Getting the name, coordinates and phone number of the Driver
                            String tempName=documentSnapshot.getString("name");
                            String tempLat=documentSnapshot.getString("latitude");
                            String tempLon=documentSnapshot.getString("longitude");
                            String phno=documentSnapshot.getString("mobile");
                            double distance=distance(Double.parseDouble(tempLat),lat,Double.parseDouble(tempLon),lon);
                            boolean authenticated=documentSnapshot.getBoolean("authenticated");

                            //Filtering out if the person is authenticated and if he is closer to the customer (3 km radius)
                            if(authenticated&&distance<=3)
                              driverLists.add(new DriverList(tempName,String.format("%.2f", distance)+" km",Double.parseDouble(tempLat),Double.parseDouble(tempLon),phno));
                        }

                        //Sorting the Arraylist according to proximity of the customer
                        for(int i=0;i<driverLists.size();i++)
                        {
                            for(int j=0;j<driverLists.size()-1-i;j++)
                            {
                                if((Double.parseDouble(driverLists.get(j).getDis().substring(0,driverLists.get(j).getDis().indexOf(' ')))) > (Double.parseDouble(driverLists.get(j+1).getDis().substring(0,driverLists.get(j+1).getDis().indexOf(' ')))))
                                Collections.swap(driverLists,j+1,j+0);
                            }
                        }

                        //ListAdapter class called and the details are displayed there
                        RecyclerView recyclerView  = (RecyclerView)findViewById(R.id.rec);
                        ListAdapter adapter = new ListAdapter(driverLists,lat,lon);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(DriverListActivity.this));
                        recyclerView.setAdapter(adapter);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DriverListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



    }






    // Used Haversine formula to calculate the distance between 2 points
    public static double distance(double lat1, double lat2, double lon1, double lon2)
    {

        // Converting degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Used Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers
        double r = 6371;

        // calculate the result
        return(c * r);
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
                        Toast.makeText(DriverListActivity.this, "Added Location", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DriverListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //Method for requesting GPS permission
    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION}, 44);
    }
}
