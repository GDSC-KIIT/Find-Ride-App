package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class OTPActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);
        EditText ed1=findViewById(R.id.otpET1);
        EditText ed2=findViewById(R.id.otpET2);
        EditText ed3=findViewById(R.id.otpET3);
        EditText ed4=findViewById(R.id.otpET4);

        ed1.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText ed1=findViewById(R.id.otpET1);
                // TODO Auto-generated method stub
                if (ed1.getText().length() == 1)
                {
                    EditText ed2 = findViewById(R.id.otpET2);
                    ed2.requestFocus();
                }
                return false;
            }
        });

        ed2.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText ed2=findViewById(R.id.otpET2);
                // TODO Auto-generated method stub
                if (ed2.getText().length() == 1) {
                    EditText ed3 = findViewById(R.id.otpET3);
                    ed3.requestFocus();
                }
                return false;
            }
        });

        ed3.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText ed3=findViewById(R.id.otpET3);
                // TODO Auto-generated method stub
                if (ed3.getText().length() == 1) {
                    EditText ed4 = findViewById(R.id.otpET4);
                    ed4.requestFocus();
                }
                return false;
            }
        });

        ed4.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText ed4=findViewById(R.id.otpET4);
                // TODO Auto-generated method stub
                if (ed4.getText().length() == 1) {
                    Button button = findViewById(R.id.otp_submit);
                    button.requestFocus();
                }
                return false;
            }
        });
    }
}
