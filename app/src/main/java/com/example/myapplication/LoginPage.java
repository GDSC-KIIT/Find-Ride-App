package com.example.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class LoginPage extends Activity {
    EditText nameEditText,phoneEditText;

    private void initViews(){
        nameEditText = findViewById(R.id.edttextname);
        phoneEditText = findViewById(R.id.edttextnumber);
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);
        initViews();
    }
}
