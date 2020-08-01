package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.sql.Driver;
import java.util.UUID;

/// TODO make Ui bettr
/// TODO check whether the person is already registered and direct him to login page
/// TODO validity of the chasis ans engine number
/// TODO Make a separate page which says Your information will be validated by us soon
public class DriverRegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Declaration
    EditText name,address,age,conf_pass,password,mobile,engine_no;
    String sname,saddress,sage,sconf_pass,spassword,smobile,sclass_of_vehicle,sengine_no;
    ImageView im_photo,im_vehicle;
    Spinner class_of_vehicle;
    public Uri imageURIVehicle, imageURIName;

    boolean activePhoto,activeVehicle,selectedPhoto,selectedVehicle;
    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register);
        //Getting all the ids
        name=findViewById(R.id.et_name);
        address=findViewById(R.id.et_address);
        age=findViewById(R.id.et_age);
        conf_pass=findViewById(R.id.et_conf_pass);
        password=findViewById(R.id.et_password);
        mobile=findViewById(R.id.et_mobile);
        class_of_vehicle=findViewById(R.id.et_class_of_vehicle);
        engine_no=findViewById(R.id.et_engine_no);
        im_photo=findViewById(R.id.photo);
        im_vehicle=findViewById(R.id.vehicle);
        selectedPhoto=selectedVehicle=false;
        mStorageRef = FirebaseStorage.getInstance().getReference();

        activePhoto=activeVehicle=false;
        im_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPhoto();
                activePhoto=true;
            }
        });

        im_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPhoto();
                activeVehicle=true;
            }
        });

        String veh[]={"Class Of Vehicle (TAP HERE)","Auto","Rickshaw","Toto", "Taxi","Chota Hati","Van","Magic Car"};
        ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,veh);
        class_of_vehicle.setAdapter(arrayAdapter);
        class_of_vehicle.setOnItemSelectedListener(this);
    }


    //Intent to launch the storage
    private void uploadPhoto() {
        Intent takePictureIntent = new Intent();
        takePictureIntent.setType("image/");
        takePictureIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(takePictureIntent,1);
    }

    //Displaying the photo to the ImageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            if(activePhoto) {
                imageURIName=data.getData();
                activePhoto = false;
                im_photo.setImageURI(imageURIName);
                selectedPhoto=true;
            }
            else if(activeVehicle) {
                imageURIVehicle=data.getData();
                activeVehicle=false;
                im_vehicle.setImageURI(imageURIVehicle);
                selectedVehicle=true;
            }
        }
    }


    //Uploads the image to the Firestore specified location with the image name given in the parameter
    private void uploadToFirebase(String name, Uri imageURI)
    {
        StorageReference riversRef = mStorageRef.child("image/"+(mobile.getText().toString())+"/"+name );

        //imageURI stores the URI of the image to be uploaded
        riversRef.putFile(imageURI)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(DriverRegisterActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(DriverRegisterActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //Adding the data provided in the form to firestore
    public void driver_registration(View view)
    {
        //getting all the data from EditText
        sname=name.getText().toString().trim();
        saddress=address.getText().toString().trim();
        sage=age.getText().toString().trim();
        sconf_pass=conf_pass.getText().toString().trim();
        spassword=password.getText().toString().trim();
        smobile=mobile.getText().toString().trim();
        sengine_no=engine_no.getText().toString().trim();
        if (smobile.isEmpty() ||saddress.isEmpty()||sage.isEmpty()||sconf_pass.isEmpty()||spassword.isEmpty()||smobile.isEmpty()||sclass_of_vehicle.isEmpty()||sengine_no.isEmpty()||smobile.length() != 10 || sname.isEmpty() || sname.length() < 3|| Integer.parseInt(sage)<18 ||Integer.parseInt(sage)>90) {
            Toast.makeText(this, "Please Check the above Fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if(spassword.equals(sconf_pass)==false)
        {
            Toast.makeText(this, "Password Doesn't Match", Toast.LENGTH_SHORT).show();
            return;
        }
        if(sclass_of_vehicle.equalsIgnoreCase("Class Of Vehicle (TAP HERE)"))
        {
            Toast.makeText(this, "Select Appropriate Class of Vehicle", Toast.LENGTH_SHORT).show();
            return;
        }
        if(selectedVehicle==false||selectedPhoto==false)
        {
            Toast.makeText(this, "Select Appropriate photo", Toast.LENGTH_SHORT).show();
            return;
        }

        //Firestore initializations
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        CollectionReference collectionReference=firebaseFirestore.collection("driver");

        //Creating the Drivers Object
        Drivers driver=new Drivers(sname,saddress,sage,sconf_pass,spassword,smobile,sclass_of_vehicle,sengine_no);

        //Uploading the data to Firestore with success and failure listener
        collectionReference.document(smobile).set(driver)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DriverRegisterActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(DriverRegisterActivity.this,UpdateLocationActivity.class);
                        intent.putExtra("mobile",smobile);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DriverRegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //Uploading the pictures to Firestore
        uploadToFirebase("name",imageURIName);
        uploadToFirebase("vehicle",imageURIVehicle);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
    {
        sclass_of_vehicle=class_of_vehicle.getSelectedItem().toString().trim();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
