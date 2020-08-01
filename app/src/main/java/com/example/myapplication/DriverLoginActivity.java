package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class DriverLoginActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    CollectionReference collectionReference;
    Button button;
    EditText ed_mobile,ed_pass;
    private String mobile, pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);
        firebaseFirestore=FirebaseFirestore.getInstance();
        collectionReference=firebaseFirestore.collection("driver");
        ed_mobile=findViewById(R.id.et_mobile);
        ed_pass=findViewById(R.id.et_password);
        button=findViewById(R.id.btn_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mobile=ed_mobile.getText().toString().trim();
                pass=ed_pass.getText().toString().trim();

                collectionReference.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                boolean count=false;
                                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                                {
                                    String tempmobile=documentSnapshot.getString("mobile");
                                    String tempPass=documentSnapshot.getString("password");
                                    boolean authenticated=documentSnapshot.getBoolean("authenticated");

                                    if(tempmobile.equalsIgnoreCase(mobile)&&tempPass.equalsIgnoreCase(pass)) {
                                        if (authenticated) {
                                            Intent intent = new Intent(DriverLoginActivity.this, UpdateLocationActivity.class);
                                            startActivity(intent);
                                            finish();
                                            count = true;
                                            break;
                                        } else {
                                            Toast.makeText(DriverLoginActivity.this, "You are Yet to be Verified by our Team", Toast.LENGTH_SHORT).show();
                                            count = true;
                                            break;
                                        }
                                    }
                                    else if(tempmobile.equalsIgnoreCase(mobile)&&tempPass.equalsIgnoreCase(pass)==false)
                                    {
                                        Toast.makeText(DriverLoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                        count=true;
                                        break;
                                    }
                                }
                                if(count==false)
                                {
                                    Toast.makeText(DriverLoginActivity.this, "Please Register At First", Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(DriverLoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
    }
}