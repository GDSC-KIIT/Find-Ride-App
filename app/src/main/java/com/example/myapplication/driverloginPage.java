package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class driverloginPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverlogin_page);
    }
//Intent to move to the login screen of driver
    public void driverloginIntent(View view){
        Intent intent = new Intent(this, DriverLoginActivity.class);
        startActivity(intent);
    }

//Intent to move to the Registration screen of driver
    public void driverregisterIntent(View view){
        Intent intent = new Intent(this, DriverRegisterActivity.class);
        startActivity(intent);
    }
}