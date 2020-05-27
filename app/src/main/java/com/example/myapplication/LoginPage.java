package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginPage extends Activity implements View.OnClickListener {
    EditText nameEditText,phoneEditText;
    Button loginButton;
    String mobile,name;

    private void initViews(){
        nameEditText = findViewById(R.id.edttextname);
        phoneEditText = findViewById(R.id.edttextnumber);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(this);
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);
        initViews();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == loginButton.getId()){
            mobile = phoneEditText.getText().toString().trim();
            name = nameEditText.getText().toString().trim();
            if (mobile.isEmpty() || mobile.length() < 10 || name.isEmpty() || name.length()<3) {
                Toast.makeText(this, "Please Check the above FIelds", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(LoginPage.this, OTPActivity.class);
            intent.putExtra("mobile", mobile);
            startActivity(intent);
            finish();
        }
    }

}
