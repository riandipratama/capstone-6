package com.example.capstone;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.Customer.MainActivity;
import com.example.capstone.DB.DatabaseHelper;
import com.example.capstone.Util.SessionManager;
import com.example.capstone.Vendor.DashboardActivity;

public class SignInActivity extends AppCompatActivity {

    TextView signup;
    EditText email, pass;
    Button signin;
    DatabaseHelper helper;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        helper = new DatabaseHelper(SignInActivity.this);
        session = new SessionManager(getApplicationContext());

        if(session.isLoggedIn(1)) {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        } else if (session.isLoggedIn(2)) {
            startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
            finish();
        }

        initReferences();

        Intent in = getIntent();
        if(in.hasExtra("email")) {
            email.setText(in.getStringExtra("email"));
            pass.setText(in.getStringExtra("pass"));
        }

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {

                    String Email = email.getText().toString();
                    String Pass = pass.getText().toString();

                    if(helper.isUserExists(Email)) {
                        String[] result;
                        result = helper.authenticateUser(Email,Pass);

                        if(result[0].equals("isCustomer")) {
                            session.createLoginSession(result[1],Email,1);
                            startActivity(new Intent(SignInActivity.this, MainActivity.class));
                            finish();
                        } else if(result[0].equals("isVendor")) {
                            session.createLoginSession(result[1],Email,2);
                            startActivity(new Intent(SignInActivity.this, DashboardActivity.class));
                            finish();
                        }

                    } else {
                        Toast.makeText(SignInActivity.this, "Pengguna Tidak Terdaftar", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
    }

    public void initReferences() {
        email = findViewById(R.id.etEmail);
        pass = findViewById(R.id.etPass);
        signin = findViewById(R.id.btnLogin);
        signup = findViewById(R.id.signup);
    }

    public boolean validate() {
        boolean valid = false;

        String Email = email.getText().toString();
        String Pass = pass.getText().toString();

        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            valid = false;
            email.setError("Email Anda Tidak Valid");
        } else {
            valid = true;
            email.setError(null);
        }

        if(Pass.isEmpty()) {
            valid = false;
            pass.setError("Silahkan Isi Password");
        } else {
            valid = true;
            pass.setError(null);
        }

        return valid;
    }
}
