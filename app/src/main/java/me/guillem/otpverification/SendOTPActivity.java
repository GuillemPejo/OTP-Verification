package me.guillem.otpverification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendOTPActivity extends AppCompatActivity {

    private Spinner spinner;
    private EditText inputMobile;
    private Button buttonGetCode;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp);

        spinner = findViewById(R.id.spinnerCountries);
        spinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, CountryCodes.countryNames));
        inputMobile = findViewById(R.id.inputMobile);
        buttonGetCode = findViewById(R.id.buttonGetCode);
        progressBar = findViewById(R.id.progressBar);

        buttonGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = CountryCodes.countryAreaCodes[spinner.getSelectedItemPosition()];

                String number = inputMobile.getText().toString().trim();

                if (number.isEmpty() || number.length() < 9) {
                    inputMobile.setError(getString(R.string.valid_number_required));
                    inputMobile.requestFocus();
                    return;
                }

                String phoneNumber = "+" + code + number;

                progressBar.setVisibility(View.VISIBLE);
                buttonGetCode.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(SendOTPActivity.this, VerifyOTPActivity.class);
                intent.putExtra("phonenumber", phoneNumber);
                startActivity(intent);



            }
        });
    }
}