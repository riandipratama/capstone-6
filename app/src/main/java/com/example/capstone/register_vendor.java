package com.example.capstone;


import android.content.Intent;
import android.os.Bundle;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class register_vendor extends Fragment {

    EditText email, pass, repass, name, address, city;
    DatabaseHelper helper;


    public register_vendor() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_register_vendor, container, false);

        email = rootView.findViewById(R.id.etEmail);
        pass = rootView.findViewById(R.id.etPass);
        repass = rootView.findViewById(R.id.etPassRepeat);
        name = rootView.findViewById(R.id.etName);
        address = rootView.findViewById(R.id.etAddress);
        city = rootView.findViewById(R.id.etCity);

        Button signUp = rootView.findViewById(R.id.btnSignup);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewUser();
            }
        });

        TextView signup = rootView.findViewById(R.id.signin);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),SignIn.class);
                getActivity().startActivity(i);
            }
        });

        return rootView;
    }

    public void addNewUser() {
        if(validate()) {
            String Email = email.getText().toString();
            String Pass = pass.getText().toString();
            String Name = name.getText().toString();
            String Address = address.getText().toString();
            String City = city.getText().toString();

            helper = new DatabaseHelper(getActivity());

            if (!helper.isUserExists(Email)) {
                Vendor vendor = new Vendor();
                vendor.setEmail(Email);
                vendor.setPass(Pass);
                vendor.setName(Name);
                vendor.setAddress(Address);
                vendor.setCity(City);

                helper.addVendor(vendor);

                Toast.makeText(getContext(),"Berhasil Daftar sebagai Vendor!",Toast.LENGTH_LONG).show();
                Intent i = new Intent(getActivity(),SignIn.class);
                i.putExtra("email",Email);
                i.putExtra("pass",Pass);
                getActivity().startActivity(i);
            } else {
                Toast.makeText(getContext(),"Pengguna sudah terdaftar!",Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean validate() {

        boolean valid = false;

        String Email = email.getText().toString();
        String Pass = pass.getText().toString();
        String Repass = repass.getText().toString();
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

        if(Pass.isEmpty()) {
            valid = false;
            pass.setError("Silahkan Isi Password");
        } else {
            if (Pass.length() < 8 ){
                valid = false;
                pass.setError("Password minimal 8 karakter");
            } else {
                valid = true;
                pass.setError(null);
            }
        }

        if(!Repass.equals(Pass)) {
            valid = false;
            repass.setError("Password tidak sama");
        } else {
            valid = true;
            repass.setError(null);
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

}
