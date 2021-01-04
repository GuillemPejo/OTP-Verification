package me.guillem.otpverification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static me.guillem.otpverification.R.drawable.background_number;

public class VerifyOTPActivity extends AppCompatActivity {

    private String verificationId;
    private Button buttonVerify;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private EditText inputCode1, inputCode2, inputCode3, inputCode4, inputCode5, inputCode6;
    TextView resendCode;
    private Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_send_otp);

        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbar);
        buttonVerify = findViewById(R.id.buttonVerify);


        String phonenumber = getIntent().getStringExtra("phonenumber");
        sendVerificationCode(phonenumber);

        TextView textMobile = findViewById(R.id.textMobile);
        textMobile.setText(phonenumber);

        inputCode1 = findViewById(R.id.inputCode1);
        inputCode2 = findViewById(R.id.inputCode2);
        inputCode3 = findViewById(R.id.inputCode3);
        inputCode4 = findViewById(R.id.inputCode4);
        inputCode5 = findViewById(R.id.inputCode5);
        inputCode6 = findViewById(R.id.inputCode6);

        resendCode = findViewById(R.id.textResendCode);

        setupOTPInputs();

        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code1 = inputCode1.getText().toString().trim();
                String code2 = inputCode2.getText().toString().trim();
                String code3 = inputCode3.getText().toString().trim();
                String code4 = inputCode4.getText().toString().trim();
                String code5 = inputCode5.getText().toString().trim();
                String code6 = inputCode6.getText().toString().trim();

                String code = code1+code2+code3+code4+code5+code6;

                if (code.isEmpty() || code.length() < 6) {

                    Toast.makeText(VerifyOTPActivity.this, "Enter code...", Toast.LENGTH_SHORT).show();
                    inputCode1.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });
    }

    private void setupOTPInputs(){
        inputCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    inputCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    inputCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    inputCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    inputCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        inputCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()){
                    inputCode6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Intent intent = new Intent(VerifyOTPActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);

                        } else {
                            Toast.makeText(VerifyOTPActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.verifyPhoneNumber(
                PhoneAuthOptions
                        .newBuilder(FirebaseAuth.getInstance())
                        .setActivity(this)
                        .setPhoneNumber(number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setCallbacks(mCallBack)
                        .build());

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();

            System.out.println("THE NUMBER CODE IS_ " +code);
            if (code != null) {

                int number = code.charAt(0);
                System.out.println("THE FIRST NUMBER IS_ " +number);


                //Toast.makeText(VerifyOTPActivity.this, code, Toast.LENGTH_SHORT).show();
                //resendCode.setText(code);
                inputCode1.setText(number);
              /*  inputCode1.setBackground(ContextCompat.getDrawable(c, background_number));
                inputCode2.setText(code.charAt(1));
                inputCode3.setText(code.charAt(2));
                inputCode4.setText(code.charAt(3));
                inputCode5.setText(code.charAt(4));
                inputCode6.setText(code.charAt(5));*/
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyOTPActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

}