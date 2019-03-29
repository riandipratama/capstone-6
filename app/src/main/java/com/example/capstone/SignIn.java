package com.example.capstone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.DB.DatabaseHelper;

public class SignIn extends AppCompatActivity {

    TextView signup;
    EditText email, pass;
    Button signin;
    DatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        helper = new DatabaseHelper(SignIn.this);
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
                        if(helper.authenticateUser(Email,Pass)) {
                            Intent i = new Intent(SignIn.this,MainActivity.class);
                            i.putExtra("email",Email);
                            startActivity(i);
                        } else {
                            Toast.makeText(SignIn.this, "Email atau Password Salah", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignIn.this, "Pengguna Tidak Terdaftar", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignIn.this,Signup.class);
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
