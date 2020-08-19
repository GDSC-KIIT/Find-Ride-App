package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class LoginChoice extends Activity {
    FirebaseAuth firebaseAuth;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_loginchoice);
        firebaseAuth=FirebaseAuth.getInstance();
    }

    //Directs to the user login page
    public void userloginIntent(View view) {
        Intent intent = new Intent(this, userloginPage.class);
        startActivity(intent);
    }

    //directs to the driver login or register page
    public void driverloginIntent(View view) {
        Intent intent = new Intent(this, driverloginPage.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}