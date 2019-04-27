package com.example.capstone.Vendor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.DB.Customer;
import com.example.capstone.DB.DatabaseHelper;
import com.example.capstone.DB.Vendor;
import com.example.capstone.R;
import com.example.capstone.Util.SessionManager;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AccountVendorFragment extends Fragment implements View.OnClickListener {

    SessionManager session;
    CircularImageView profileImg;
    Vendor vendor;
    TextView vendorName, vendorCity;
    DatabaseHelper helper;
    HashMap<String, String> user;
    TextView changePass,editProfile;
    TextInputLayout oldPass, newPass, reNewPass;
    EditText email, etOldPass, etNewPass, etReNewPass, name, address, city, phone;
    Button btnChangePass, btnEditProfile;

    int changepass = 0;
    int editprofile = 0;

    public AccountVendorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account_vendor, container, false);

        profileImg = rootView.findViewById(R.id.profileImage);
        vendorName = rootView.findViewById(R.id.nameVendor);
        vendorCity = rootView.findViewById(R.id.vendorCity);

        changePass = rootView.findViewById(R.id.changePass);
        oldPass = rootView.findViewById(R.id.OldPass);
        newPass = rootView.findViewById(R.id.Pass);
        reNewPass = rootView.findViewById(R.id.PassRepeat);
        etOldPass = rootView.findViewById(R.id.etOldPass);
        etNewPass = rootView.findViewById(R.id.etPass);
        etReNewPass = rootView.findViewById(R.id.etPassRepeat);
        btnChangePass = rootView.findViewById(R.id.btnChangePass);
        editProfile = rootView.findViewById(R.id.editProfile);
        email = rootView.findViewById(R.id.etEmail);
        name = rootView.findViewById(R.id.etName);
        address = rootView.findViewById(R.id.etAddress);
        city = rootView.findViewById(R.id.etCity);
        phone = rootView.findViewById(R.id.etPhone);
        btnEditProfile = rootView.findViewById(R.id.btnEditProfile);

        initClasses();
        initValues();

        user = session.getUserDetails();
        vendor = helper.getVendor(user.get(SessionManager.KEY_ID));

        Picasso.get().load(vendor.getImage()).into(profileImg);
        vendorName.setText(vendor.getName());
        vendorCity.setText(vendor.getCity());

        changePass.setOnClickListener(this);
        editProfile.setOnClickListener(this);

        btnChangePass.setOnClickListener(this);
        btnEditProfile.setOnClickListener(this);


        // Inflate the layout for this fragment
        return rootView;
    }

    public void initClasses() {
        session = new SessionManager(getActivity());
        helper = new DatabaseHelper(getActivity());
    }

    public void initValues() {
        Vendor vendor = helper.getVendor(session.getUserDetails().get(SessionManager.KEY_ID));
        email.setText(vendor.getEmail());
        name.setText(vendor.getName());
        address.setText(vendor.getAddress());
        city.setText(vendor.getCity());
        phone.setText(vendor.getPhone());
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
                    name.setVisibility(View.VISIBLE);
                    address.setVisibility(View.VISIBLE);
                    city.setVisibility(View.VISIBLE);
                    phone.setVisibility(View.VISIBLE);
                    btnEditProfile.setVisibility(View.VISIBLE);
                } else {
                    email.setVisibility(View.GONE);
                    name.setVisibility(View.GONE);
                    address.setVisibility(View.GONE);
                    city.setVisibility(View.GONE);
                    phone.setVisibility(View.GONE);
                    btnEditProfile.setVisibility(View.GONE);
                }
                return;
            case  R.id.btnChangePass :
                updatePassVendor();
                break;
            case R.id.btnEditProfile:
                updateProfile();
                break;
        }
    }

    private void updateProfile() {
        if(validate()){
            Vendor vendor = new Vendor(
                    email.getText().toString(),
                    name.getText().toString(),
                    address.getText().toString(),
                    city.getText().toString(),
                    phone.getText().toString());
            if(helper.updateVendorProfile(vendor,user.get(SessionManager.KEY_ID))) {
                session.createLoginSession(user.get("KEY_ID"),email.getText().toString(),1);
                Snackbar.make(getView(),"Profil berhasil diupdate",Snackbar.LENGTH_SHORT).show();
                editProfile.performClick();
            } else {
                Toast.makeText(getActivity(),"Gagal mengupdate profil", Toast.LENGTH_SHORT).show();
            }
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

    public boolean validate() {

        boolean valid = false;

        String Email = email.getText().toString();
        String Name = name.getText().toString();
        String Address = address.getText().toString();
        String City = city.getText().toString();

        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            valid = false;
            email.setError("Email Anda Tidak Valid");
        } else {
            valid = true;
            email.setError(null);
        }

        if(Name.isEmpty()) {
            valid = false;
            name.setError("Isi Nama Vendor/Perusahaan Anda");
        } else {
            valid = true;
            name.setError(null);
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

        return valid;
    }

    public void updatePassVendor(){
        if(validatePass()) {
            String[] result = helper.authenticateUser(user.get(SessionManager.KEY_EMAIL),etOldPass.getText().toString());
            if(result[1].equals(user.get(SessionManager.KEY_ID))){
                vendor.setPass(etReNewPass.getText().toString());
                if(helper.updateVendorPassword(vendor.getPass(),user.get("KEY_ID"))){
                    Snackbar.make(getView(),"Password berhasil diganti",Snackbar.LENGTH_SHORT).show();
                    changePass.performClick();
                } else {
                    Toast.makeText(getActivity(),"Gagal mengganti Password", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
