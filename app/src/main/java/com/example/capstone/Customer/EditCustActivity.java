package com.example.capstone.Customer;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.DB.Customer;
import com.example.capstone.DB.DatabaseHelper;
import com.example.capstone.R;
import com.example.capstone.Util.SessionManager;

import java.util.HashMap;

public class EditCustActivity extends AppCompatActivity implements View.OnClickListener {

    TextView changePass,editProfile;
    TextInputLayout oldPass, newPass, reNewPass;
    EditText email, etOldPass, etNewPass, etReNewPass, fname, lname, address, city, phone;
    Button btnChangePass, btnEditProfile;
    Toolbar toolbar;

    int changepass = 0;
    int editprofile = 0;
    HashMap<String, String> user;

    DatabaseHelper helper;
    SessionManager session;
    Customer cust;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_cust);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        initViews();
        initObjects();

        user = session.getUserDetails();
        cust = helper.getCustomer(user.get(SessionManager.KEY_ID));
        initValues(cust);

        changePass.setOnClickListener(this);
        editProfile.setOnClickListener(this);
    }

    public void initViews() {
        changePass = findViewById(R.id.changePass);
        oldPass = findViewById(R.id.OldPass);
        newPass = findViewById(R.id.Pass);
        reNewPass = findViewById(R.id.PassRepeat);
        etOldPass = findViewById(R.id.etOldPass);
        etNewPass = findViewById(R.id.etPass);
        etReNewPass = findViewById(R.id.etPassRepeat);
        btnChangePass = findViewById(R.id.btnChangePass);
        editProfile = findViewById(R.id.editProfile);
        email = findViewById(R.id.etEmail);
        fname = findViewById(R.id.etFName);
        lname = findViewById(R.id.etLName);
        address = findViewById(R.id.etAddress);
        city = findViewById(R.id.etCity);
        phone = findViewById(R.id.etPhone);
        btnEditProfile = findViewById(R.id.btnEditProfile);
    }

    public void initObjects() {
        helper = new DatabaseHelper(this);
        session = new SessionManager(this);
    }

    public void initValues(Customer cust) {
        email.setText(cust.getEmail());
        fname.setText(cust.getFname());
        lname.setText(cust.getLname());
        address.setText(cust.getAddress());
        city.setText(cust.getCity());
        phone.setText(cust.getPhone());

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.changePass:
                changepass++;
                if(changepass % 2 != 0) {
                    oldPass.setVisibility(View.VISIBLE);
                    newPass.setVisibility(View.VISIBLE);
                    reNewPass.setVisibility(View.VISIBLE);
                    btnChangePass.setVisibility(View.VISIBLE);
                } else {
                    oldPass.setVisibility(View.GONE);
                    newPass.setVisibility(View.GONE);
                    reNewPass.setVisibility(View.GONE);
                    btnChangePass.setVisibility(View.GONE);
                }
                return;
            case R.id.editProfile:
                editprofile++;
                if(editprofile % 2 != 0) {
                    email.setVisibility(View.VISIBLE);
                    fname.setVisibility(View.VISIBLE);
                    lname.setVisibility(View.VISIBLE);
                    address.setVisibility(View.VISIBLE);
                    city.setVisibility(View.VISIBLE);
                    phone.setVisibility(View.VISIBLE);
                    btnEditProfile.setVisibility(View.VISIBLE);
                } else {
                    email.setVisibility(View.GONE);
                    fname.setVisibility(View.GONE);
                    lname.setVisibility(View.GONE);
                    address.setVisibility(View.GONE);
                    city.setVisibility(View.GONE);
                    phone.setVisibility(View.GONE);
                    btnEditProfile.setVisibility(View.GONE);
                }
                return;
        }
    }

    public boolean validatePass(){
        boolean valid = false;

        String oldpass = etOldPass.getText().toString();
        String newpass = etNewPass.getText().toString();
        String renewpass = etReNewPass.getText().toString();

        if(oldpass.isEmpty()) {
            valid = false;
            etOldPass.setError("Silahkan isi password lama Anda");
        } else {
            valid = true;
            etOldPass.setError(null);
        }

        if(newpass.isEmpty()) {
            valid = false;
            etNewPass.setError("Silahkan Isi Password Baru");
        } else {
            if (newpass.length() < 8 ){
                valid = false;
                etNewPass.setError("Password Baru minimal 8 karakter");
            } else {
                valid = true;
                etNewPass.setError(null);
            }
        }

        if(!renewpass.equals(newpass)) {
            valid = false;
            etReNewPass.setError("Password tidak sama");
        } else {
            valid = true;
            etReNewPass.setError(null);
        }
        return valid;
    }

    public boolean validateProfile() {

        boolean valid = false;

        String Email = email.getText().toString();
        String FName = fname.getText().toString();
        String LName = lname.getText().toString();
        String Address = address.getText().toString();
        String City = city.getText().toString();
        String Phone = phone.getText().toString();

        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            valid = false;
            email.setError("Email Anda Tidak Valid");
        } else {
            valid = true;
            email.setError(null);
        }

        if(FName.isEmpty()) {
            valid = false;
            fname.setError("Isi Nama Depan Anda");
        } else {
            valid = true;
            fname.setError(null);
        }

        if(LName.isEmpty()) {
            valid = false;
            lname.setError("Isi Nama Belakang Anda");
        } else {
            valid = true;
            lname.setError(null);
        }

        if(Address.isEmpty()) {
            valid = false;
            address.setError("Isi Alamat Anda");
        } else {
            valid = true;
            address.setError(null);
        }

        if(City.isEmpty()) {
            valid = false;
            city.setError("Isi Kota Anda");
        } else {
            valid = true;
            city.setError(null);
        }

        if(Phone.isEmpty()) {
            valid = false;
            phone.setError("Isi Nomor Telepon Anda");
        } else {
            if (Phone.length() < 10 || Phone.length() > 13 ) {
                valid = false;
                phone.setError("No. Telp Tidak Valid");
            } else {
                valid = true;
                phone.setError(null);
            }
        }

        return valid;
    }

    //attached in xml to btnChangePass
    public void updatePass(View view) {
        if(validatePass()) {
            String[] result = helper.authenticateUser(user.get("KEY_EMAIL"),etOldPass.getText().toString());
            if(result[1].equals(user.get("KEY_ID"))){
                cust.setPass(etReNewPass.getText().toString());
                if(helper.updateCustPassword(cust.getPass(),user.get("KEY_ID"))){
                    Snackbar.make(view,"Password berhasil diganti",Snackbar.LENGTH_SHORT).show();
                    changePass.performClick();
                } else {
                    Toast.makeText(getApplicationContext(),"Gagal mengganti Password", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void updateProfile(View view) {
        if(validateProfile()){
            Customer cust = new Customer(
                    email.getText().toString(),
                    fname.getText().toString(),
                    lname.getText().toString(),
                    address.getText().toString(),
                    city.getText().toString(),
                    phone.getText().toString());
            if(helper.updateCustProfile(cust,user.get("KEY_ID"))) {
                session.createLoginSession(user.get("KEY_ID"),email.getText().toString(),1);
                Snackbar.make(view,"Profil berhasil diupdate",Snackbar.LENGTH_SHORT).show();
                editProfile.performClick();
            } else {
                Toast.makeText(getApplicationContext(),"Gagal mengupdate profil", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
