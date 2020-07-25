package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class OTPActivity extends AppCompatActivity implements View.OnClickListener
{

    //Initialization of Variables
    EditText ed1,ed2,ed3,ed4,ed5,ed6;
    TextView phoneText;
    Button btn_verify;
    String no,name;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    FirebaseFirestore firebaseFirestore;
    boolean pressed;

    //init
    private void init(){
        phoneText = findViewById(R.id.phoneNumberText);
        ed1=findViewById(R.id.otpET1);
        ed2=findViewById(R.id.otpET2);
        ed3=findViewById(R.id.otpET3);
        ed4=findViewById(R.id.otpET4);
        ed5=findViewById(R.id.otpET5);
        ed6=findViewById(R.id.otpET6);
        btn_verify = findViewById(R.id.otp_submit);

        firebaseFirestore = FirebaseFirestore.getInstance();

        no = getIntent().getStringExtra("mobile");
        name = getIntent().getStringExtra("name");
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





        pressed=false;
        //The 6 editText in OTP communicating with each other
       ed1.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
           {
           }

           @Override
           public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(ed1.getText().toString().trim().length()==1)
                        ed2.requestFocus();
           }

           @Override
           public void afterTextChanged(Editable editable) {

           }
       });

        ed2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(ed2.getText().toString().trim().length()==1)
                    ed3.requestFocus();
                if(ed2.getText().toString().trim().length()==0)
                    ed1.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ed3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(ed3.getText().toString().trim().length()==1)
                    ed4.requestFocus();
                if(ed3.getText().toString().trim().length()==0)
                    ed2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ed4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(ed4.getText().toString().trim().length()==1)
                    ed5.requestFocus();
                if(ed4.getText().toString().trim().length()==0)
                    ed3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ed5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(ed5.getText().toString().trim().length()==1)
                    ed6.requestFocus();
                if(ed5.getText().toString().trim().length()==0)
                    ed4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ed6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(ed6.getText().toString().trim().length()==1)
                    btn_verify.requestFocus();
                if(ed6.getText().toString().trim().length()==0)
                    ed5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    //When Clicked on submit
    @Override
    public void onClick(View v) {
        if(v.getId() == btn_verify.getId())
        {
            //Getting the ids
            ed1=findViewById(R.id.otpET1);
            ed2=findViewById(R.id.otpET2);
            ed3=findViewById(R.id.otpET3);
            ed4=findViewById(R.id.otpET4);
            ed5=findViewById(R.id.otpET5);
            ed6=findViewById(R.id.otpET6);
            String code = ""+ed1.getText().toString().trim()+ed2.getText().toString().trim()+ed3.getText().toString().trim()+ed4.getText().toString().trim()+ed5.getText().toString().trim()+ed6.getText().toString().trim();

            //Checking if the code is invalid
            if (code.isEmpty() || code.length() < 6)
            {
                Toast.makeText(this, "Enter Valid Code", Toast.LENGTH_SHORT).show();
                return;
            }

            /// Putting name and phone number in database
            CollectionReference customerdb=firebaseFirestore.collection("users");
            Customers customers=new Customers(name,no);
            customerdb.document(no)
                    .set(customers)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(OTPActivity.this, "Added your name", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(OTPActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });

            //verifying the code entered manually
            verifyVerificationCode(code);
        }
    }

    //other methods8
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
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();
            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null)
            {
                ed1=findViewById(R.id.otpET1);
                ed2=findViewById(R.id.otpET2);
                ed3=findViewById(R.id.otpET3);
                ed4=findViewById(R.id.otpET4);
                ed5=findViewById(R.id.otpET5);
                ed6=findViewById(R.id.otpET6);
                ed1.setText(code.charAt(0)+"");
                ed2.setText(code.charAt(1)+"");
                ed3.setText(code.charAt(2)+"");
                ed4.setText(code.charAt(3)+"");
                ed5.setText(code.charAt(4)+"");
                ed6.setText(code.charAt(5)+"");

                //verifying the code
                verifyVerificationCode(code);
            }
        }
        @Override
        public void onVerificationFailed(FirebaseException e)
        {
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
                                    intent.putExtra("mobile",no);
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
