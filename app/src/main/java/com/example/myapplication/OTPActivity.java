package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity implements View.OnClickListener
{

    EditText otp,ed1,ed2,ed3,ed4,ed5,ed6;
    TextView phoneText;
    Button btn_verify;
    String no;
    private FirebaseAuth mAuth;
    private String mVerificationId;

    private void init(){
        phoneText = findViewById(R.id.phoneNumberText);
        ed1=findViewById(R.id.otpET1);
        ed2=findViewById(R.id.otpET2);
        ed3=findViewById(R.id.otpET3);
        ed4=findViewById(R.id.otpET4);
        ed5=findViewById(R.id.otpET5);
        ed6=findViewById(R.id.otpET6);
        btn_verify = findViewById(R.id.otp_submit);
        no = getIntent().getStringExtra("mobile");
        phoneText.setText("+91 " + no);
        sendVerificationCode(no);
        btn_verify.setOnClickListener(this);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);
        mAuth = FirebaseAuth.getInstance();
        init();


        ed1.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText ed1=findViewById(R.id.otpET1);
                // TODO Auto-generated method stub
                if(keyCode == KeyEvent.KEYCODE_DEL)
                {
                    ed1.requestFocus();
                }
                else if (ed1.getText().length() == 1)
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
                EditText ed1 = findViewById(R.id.otpET1);
                // TODO Auto-generated method stub
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    ed1.requestFocus();
                }
                else if (ed2.getText().length() == 1)
                {
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
                EditText ed2=findViewById(R.id.otpET2);
                // TODO Auto-generated method stub
                if(keyCode == KeyEvent.KEYCODE_DEL)
                {
                    ed2.requestFocus();
                }
                else if (ed3.getText().length() == 1)
                {
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
                EditText ed3=findViewById(R.id.otpET3);
                // TODO Auto-generated method stub
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    ed3.requestFocus();
                }
                else if (ed4.getText().length() == 1) {
                    EditText ed5 = findViewById(R.id.otpET5);
                    ed5.requestFocus();
                }
                return false;
            }
        });
        ed5.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText ed5=findViewById(R.id.otpET5);
                EditText ed4=findViewById(R.id.otpET4);
                // TODO Auto-generated method stub
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    ed4.requestFocus();
                }
                else if (ed5.getText().length() == 1) {
                    EditText ed6 = findViewById(R.id.otpET6);
                    ed6.requestFocus();
                }
                return false;
            }
        });

        ed6.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText ed6=findViewById(R.id.otpET6);
                EditText ed5=findViewById(R.id.otpET5);
                // TODO Auto-generated method stub
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    ed5.requestFocus();
                }
                else if (ed4.getText().length() == 1) {
                    Button button = findViewById(R.id.otp_submit);
                    button.requestFocus();
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == btn_verify.getId()){
            /// TODO KIRTIP CHANGE HERE
            String code = otp.getText().toString().trim();
            if (code.isEmpty() || code.length() < 6) {
                otp.setError("Enter valid code");
                otp.requestFocus();
                return;
            }
            //verifying the code entered manually
            verifyVerificationCode(code);
        }
    }

    private void sendVerificationCode(String no) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + no,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();
            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                otp.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }
        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Toast.makeText(OTPActivity.this, "Code Sent", Toast.LENGTH_SHORT).show();
            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };
    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(OTPActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //verification successful we will start the profile activity
                                    Toast.makeText(OTPActivity.this,"verified",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(OTPActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                        String message = "Invalid code entered...";
                                    }
                                }
                            }
                        }
                );
    }
}
