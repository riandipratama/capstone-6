package com.example.capstone;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.DB.Customer;
import com.example.capstone.DB.DatabaseHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustSignUpFragment extends Fragment {

    EditText email, pass, repass, fname, lname, address, city, phone;
    ImageView profilePreview;
    DatabaseHelper helper;
    private ProgressDialog progressBar;
    private Uri contentUri;

    public CustSignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_register_cust, container, false);

        email = rootView.findViewById(R.id.etEmail);
        pass = rootView.findViewById(R.id.etPass);
        repass = rootView.findViewById(R.id.etPassRepeat);
        fname = rootView.findViewById(R.id.etFName);
        lname = rootView.findViewById(R.id.etLName);
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

                        new SignUpAsync().execute();

                        Toast.makeText(getContext(),"Berhasil Daftar sebagai Customer!",Toast.LENGTH_LONG).show();
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

        TextView signin = rootView.findViewById(R.id.signin);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), SignInActivity.class);
                getActivity().startActivity(i);
            }
        });

        return rootView;
    }

    class SignUpAsync extends AsyncTask<Void, Void, Boolean> {

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

            if(contentUri == null) {
                contentUri = Uri.parse("android.resource://com.example.capstone/drawable/default_cust");
            }

            final StorageReference storeRef = FirebaseStorage.getInstance().getReference()
                    .child("cust-profiles/" + UUID.randomUUID().toString());
            storeRef.putFile(contentUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Customer cust = new Customer();
                                    cust.setEmail(email.getText().toString());
                                    cust.setPass(pass.getText().toString());
                                    cust.setFname(fname.getText().toString());
                                    cust.setLname(lname.getText().toString());
                                    cust.setAddress(address.getText().toString());
                                    cust.setCity(city.getText().toString());
                                    cust.setImage(uri.toString());
                                    cust.setPhone(phone.getText().toString());

                                    helper.addCust(cust);
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
