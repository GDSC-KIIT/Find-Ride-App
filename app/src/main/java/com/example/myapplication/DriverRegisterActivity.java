package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Driver;

public class DriverRegisterActivity extends AppCompatActivity {

    //Declaration
    EditText name,address,age,chasis_no,password,mobile,class_of_vehicle,engine_no;
    String sname,saddress,sage,schasis_no,spassword,smobile,sclass_of_vehicle,sengine_no;
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