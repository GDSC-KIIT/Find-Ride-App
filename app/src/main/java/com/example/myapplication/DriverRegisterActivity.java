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
import android.widget.EditText;
import android.widget.ImageView;
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
/// TODO after clicking on register the file will be uploaded not before
/// TODO check whether the person is already registered and direct him to login page
/// TODO validity of the chasis ans engine number
/// TODO validity of the phone number
/// TODO Age should be more than 18
/// TODO check if all the fields are filled or not, open dialog box or snackbar if not filled
/// TODO Make a separate page which says Your information will be validated by us soon
public class DriverRegisterActivity extends AppCompatActivity {

    //Declaration
    EditText name,address,age,chasis_no,password,mobile,class_of_vehicle,engine_no;
    String sname,saddress,sage,schasis_no,spassword,smobile,sclass_of_vehicle,sengine_no;
    ImageView im_photo,im_vehicle;
    public Uri imageURI;
    boolean activePhoto,activeVehicle;
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
        chasis_no=findViewById(R.id.et_chasis_no);
        password=findViewById(R.id.et_password);
        mobile=findViewById(R.id.et_mobile);
        class_of_vehicle=findViewById(R.id.et_class_of_vehicle);
        engine_no=findViewById(R.id.et_engine_no);
        im_photo=findViewById(R.id.photo);
        im_vehicle=findViewById(R.id.vehicle);

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
            imageURI=data.getData();
            if(activePhoto) {
                activePhoto = false;
                im_photo.setImageURI(imageURI);
                uploadToFirebase("name");
            }
            else if(activeVehicle) {
                activeVehicle=false;
                im_vehicle.setImageURI(imageURI);
                uploadToFirebase("vehicle");
            }
        }
    }


    //Uploads the image to the specified location with the image name given in the parameter
    private void uploadToFirebase(String name)
    {
        StorageReference riversRef = mStorageRef.child("image/"+(mobile.getText().toString())+"/"+name );

        //imageURI stores the URI of the image to be uploaded
        riversRef.putFile(imageURI    )
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
        schasis_no=chasis_no.getText().toString().trim();
        spassword=password.getText().toString().trim();
        smobile=mobile.getText().toString().trim();
        sclass_of_vehicle=class_of_vehicle.getText().toString().trim();
        sengine_no=engine_no.getText().toString().trim();

        //Firestore initializations
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        CollectionReference collectionReference=firebaseFirestore.collection("driver");

        //Creating the Drivers Object
        Drivers driver=new Drivers(sname,saddress,sage,schasis_no,spassword,smobile,sclass_of_vehicle,sengine_no);

        //putting the data with success and failure listener
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
    }
}