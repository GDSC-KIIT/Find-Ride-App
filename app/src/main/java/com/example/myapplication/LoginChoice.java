package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginChoice extends Activity {
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_loginchoice);
    }
    public void userloginIntent(View view){
        Intent intent = new Intent(this, userloginPage.class);
        startActivity(intent);
    }
    public void driverloginIntent(View view){
        Intent intent = new Intent(this, driverloginPage.class);
        startActivity(intent);
    }
}
