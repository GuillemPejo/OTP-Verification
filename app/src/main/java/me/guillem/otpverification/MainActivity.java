package me.guillem.otpverification;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mAuth = FirebaseAuth.getInstance();

        username = findViewById(R.id.username);

        username.setText("Hello!\nYour UID is:  "+mAuth.getUid());



    }
}