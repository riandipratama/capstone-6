package com.example.capstone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.capstone.DB.DatabaseHelper;
import com.example.capstone.DB.Vendor;
import com.example.capstone.Util.SessionManager;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.HashMap;

public class AccountVendorFragment extends Fragment {

    SessionManager session;
    CircularImageView profileImg;
    Vendor vendor;
    TextView vendorName, vendorCity;
    DatabaseHelper helper;
    HashMap<String, String> user;

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

        initClasses();
        user = session.getUserDetails();
        vendor = helper.getVendor(user.get(SessionManager.KEY_ID));

        profileImg.setImageBitmap(BitmapFactory.decodeFile(vendor.getImage()));
        vendorName.setText(vendor.getName());
        vendorCity.setText(vendor.getCity());


        // Inflate the layout for this fragment
        return rootView;
    }

    public void initClasses() {
        session = new SessionManager(getActivity());
        helper = new DatabaseHelper(getActivity());
    }

}
