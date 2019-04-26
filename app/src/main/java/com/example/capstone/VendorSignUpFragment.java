package com.example.capstone;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.DB.DatabaseHelper;
import com.example.capstone.DB.Vendor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class VendorSignUpFragment extends Fragment {

    EditText email, pass, repass, name, address, city, phone;
    ImageView profilePreview;
    DatabaseHelper helper;
    private ProgressDialog progressBar;
    private Uri contentUri;


    public VendorSignUpFragment() {
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
        phone = rootView.findViewById(R.id.etPhone);
        profilePreview = rootView.findViewById(R.id.profilePreview);

        profilePreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

        Button signUp = rootView.findViewById(R.id.btnSignup);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    String Email = email.getText().toString();
                    String Pass = pass.getText().toString();

                    helper = new DatabaseHelper(getActivity());

                    if (!helper.isUserExists(Email)) {

                        new SignUpAsyncTask().execute();

                        Toast.makeText(getContext(),"Berhasil Daftar sebagai Vendor!",Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getActivity(), SignInActivity.class);
                        i.putExtra("email",Email);
                        i.putExtra("pass",Pass);
                        getActivity().startActivity(i);
                        getActivity().finish();
                    } else {
                        Toast.makeText(getContext(),"Pengguna sudah terdaftar!",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        TextView signup = rootView.findViewById(R.id.signin);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SignInActivity.class);
                getActivity().startActivity(i);
            }
        });

        return rootView;
    }

    class SignUpAsyncTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressBar = new ProgressDialog(getActivity());
            progressBar.setCancelable(true);
            progressBar.setMessage("Please Wait...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
                progressBar.dismiss();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            String[] imgpath = new String[1];

            if(contentUri == null) {
                contentUri = Uri.parse("android.resource://com.example.capstone/drawable/default_cust");
            }

            final StorageReference storeRef = FirebaseStorage.getInstance().getReference()
                    .child("vendor-profiles/" + UUID.randomUUID().toString());
            storeRef.putFile(contentUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Vendor vendor = new Vendor(
                                            email.getText().toString(),
                                            pass.getText().toString(),
                                            name.getText().toString(),
                                            address.getText().toString(),
                                            city.getText().toString(),
                                            phone.getText().toString(),
                                            uri.toString(),
                                            null
                                    );
                                    helper.addVendor(vendor);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Gagal Menambahkan Foto", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    });

            return null;
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
                                profilePreview.setImageResource(R.drawable.default_cust);
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data.getData() != null) {
            contentUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentUri);
                profilePreview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 2) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            profilePreview.setImageBitmap(bitmap);
        }
    }


}
