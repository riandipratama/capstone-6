package com.example.capstone;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.DB.Customer;
import com.example.capstone.DB.DatabaseHelper;
import com.example.capstone.Util.SessionManager;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class AccountFragment extends Fragment implements View.OnClickListener {

    SessionManager session;
    ImageView editProfile;
    CircularImageView profileImg;
    Customer cust;
    TextView userMail;
    DatabaseHelper helper;
    Bitmap bitmap;
    HashMap<String, String> user;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        initClasses();

        userMail = rootView.findViewById(R.id.emailUser);
        profileImg = rootView.findViewById(R.id.profileImage);
        editProfile = rootView.findViewById(R.id.editProfile);

        user = session.getUserDetails();

        cust = helper.getCustomer(user.get(SessionManager.KEY_ID));
        userMail.setText(getString(R.string.user_fullname,cust.getFname(),cust.getLname()));

        profileImg.setImageBitmap(BitmapFactory.decodeFile(cust.getImage()));

        editProfile.setOnClickListener(this);
        profileImg.setOnClickListener(this);

        // Inflate the layout for this fragment
        return rootView;
    }

    public void initClasses() {
        session = new SessionManager(getActivity());
        helper = new DatabaseHelper(getActivity());
    }

    @Override
    public void onClick(View v) {
        if(v == editProfile) {
            startActivity(new Intent(getActivity(),EditCust.class));
        } else if (v == profileImg) {
            showPictureDialog();
        }
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(getActivity());
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {"Open Gallery", "Open Camera", "Delete Image"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                                break;
                            case 1:
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(cameraIntent, 2);
                                break;
                            case 2:
                                profileImg.setImageResource(R.drawable.default_cust);
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data.getData() != null) {
            Uri contentUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentUri);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 2) {
            bitmap = (Bitmap) data.getExtras().get("data");
        }

        String imgpath = "";
        File internalStorage = getActivity().getDir("CustProfile", Context.MODE_PRIVATE);
        File custProfilePath = new File(internalStorage, user.get(SessionManager.KEY_ID) + user.get(SessionManager.KEY_EMAIL) + ".png");
        imgpath = custProfilePath.toString();

        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(custProfilePath);
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.default_cust);
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            Log.i("DATABASE", "Problem updating picture", e);
            imgpath = "";
        }

        if(helper.updateCustProfilePicture(imgpath,user.get(SessionManager.KEY_ID))) {
            profileImg.setImageBitmap(BitmapFactory.decodeFile(helper.getCustomer(user.get("KEY_ID")).getImage()));
        }
    }
}
