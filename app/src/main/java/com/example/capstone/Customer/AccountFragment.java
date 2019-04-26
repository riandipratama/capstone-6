package com.example.capstone.Customer;

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
import android.service.voice.AlwaysOnHotwordDetector;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.DB.Cart;
import com.example.capstone.DB.CartList;
import com.example.capstone.DB.Customer;
import com.example.capstone.DB.DatabaseHelper;
import com.example.capstone.DB.Order;
import com.example.capstone.DB.Payment;
import com.example.capstone.R;
import com.example.capstone.Util.OrderAdapter;
import com.example.capstone.Util.SessionManager;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    private Uri contentUri;
    private ProgressDialog progressBar;

    private RecyclerView rvCustOrder;
    private RadioGroup rgOrder;
    private RadioButton rbPaid;
    private RadioButton rbUnpaid;

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
        rvCustOrder = rootView.findViewById(R.id.rvCustOrder);
        rgOrder = rootView.findViewById(R.id.rgCustOrder);
        rbPaid = rootView.findViewById(R.id.rbPaid);
        rbUnpaid = rootView.findViewById(R.id.rbUnpaid);

        user = session.getUserDetails();

        cust = helper.getCustomer(user.get(SessionManager.KEY_ID));
        userMail.setText(getString(R.string.user_fullname,cust.getFname(),cust.getLname()));

        Picasso.get().load(cust.getImage()).into(profileImg);

        editProfile.setOnClickListener(this);
        profileImg.setOnClickListener(this);

//        rbUnpaid.setChecked(true);
//        new rvUnpaidAsync().execute();

        rgOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.rbPaid :
                        new rvPaidAsync().execute();
                        break;
                    case R.id.rbUnpaid :
                        new rvUnpaidAsync().execute();
                        break;
                    default:
                        rbUnpaid.setChecked(true);
                        new rvUnpaidAsync().execute();
                        break;
                }
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    public void initClasses() {
        session = new SessionManager(getActivity());
        helper = new DatabaseHelper(getActivity());
    }

    public void runAsync(){
        new rvUnpaidAsync().execute();
        new rvPaidAsync().execute();
    }

    @Override
    public void onClick(View v) {
        if(v == editProfile) {
            startActivity(new Intent(getActivity(), EditCustActivity.class));
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
           contentUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentUri);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 2) {
            bitmap = (Bitmap) data.getExtras().get("data");
        }

        new UpdatePhotoAsync().execute();
    }

    class UpdatePhotoAsync extends AsyncTask<Void, Void, Void> {

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
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.dismiss();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if(contentUri == null) {
                contentUri = Uri.parse("android.resource://com.example.capstone/drawable/default_cust");
            }

            final StorageReference storeRef = FirebaseStorage.getInstance().getReference()
                    .child("cust-profiles");
            storeRef.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            if(helper.updateCustProfilePicture(uri.toString(),user.get(SessionManager.KEY_ID))) {
                               Picasso.get().load(uri).into(profileImg);
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Gagal Update Foto Profil", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
            });

            return null;
        }
    }

    class rvUnpaidAsync extends AsyncTask<Void, Void, List<Payment>> {

        @Override
        protected List<Payment> doInBackground(Void... voids) {
            List<Order> oList;
            List<Payment> payList = new ArrayList<>();

            oList = helper.getAllParticularOrders(session.getUserDetails().get(SessionManager.KEY_ID));

            for(int i = 0; i < oList.size(); i++) {
                if(oList.get(i).getId() != null) {
                    if(helper.getStatusPayment(oList.get(i).getId(),"0") != null) {
                        payList.add(helper.getStatusPayment(oList.get(i).getId(),"0"));
                    }

                }
            }

            return payList;
        }

        @Override
        protected void onPostExecute(List<Payment> payments) {
            super.onPostExecute(payments);
//            Log.e("PAYMENT SIZE",String.valueOf(payments.size()));
//            Log.e("PAYMENT SIZE",String.valueOf(payments.get(0).getOrder_id()));
//            Log.e("PAYMENT SIZE",String.valueOf(payments.get(0).getId()));
//            Log.e("PAYMENT SIZE",String.valueOf(payments.get(0).getStatus()));
//            Log.e("PAYMENT TOTAL",helper.getOrder(payments.get(0).getOrder_id(),2).getTotal_price());
            if(payments.size() > 0){

                List<String> totalPay = new ArrayList<>();
                for(int i = 0; i < payments.size(); i++) {
                    if(payments.get(i).getId() != null) {
                        totalPay.add(helper.getOrder(payments.get(i).getOrder_id(),2).getTotal_price());
                    }
                }
                rvCustOrder.setHasFixedSize(true);
                rvCustOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
                rvCustOrder.setAdapter(new OrderAdapter(
                        getActivity(),
                        payments,
                        totalPay,
                        AccountFragment.this,
                        OrderAdapter.UNPAIDCUST
                ));
            }
        }
    }

    class rvPaidAsync extends AsyncTask<Void, Void, List<Payment>> {

        @Override
        protected List<Payment> doInBackground(Void... voids) {
            List<Order> oList;
            List<Payment> payList = new ArrayList<>();

            oList = helper.getAllParticularOrders(session.getUserDetails().get(SessionManager.KEY_ID));

            for(int i = 0; i < oList.size(); i++) {
                if(oList.get(i).getId() != null) {
                    if(helper.getStatusPayment(oList.get(i).getId(),"1") != null) {
                        payList.add(helper.getStatusPayment(oList.get(i).getId(),"1"));
                    }
                }
            }

            return payList;
        }

        @Override
        protected void onPostExecute(List<Payment> payments) {
            super.onPostExecute(payments);
//            Log.e("PAYMENT SIZE PAID",String.valueOf(payments.size()));
//            Log.e("PAYMENT ORDER ID PAID",String.valueOf(payments.get(0).getOrder_id()));
//            Log.e("PAYMENT ID PAID",String.valueOf(payments.get(0).getId()));
//            Log.e("PAYMENT STATUS PAID",String.valueOf(payments.get(0).getStatus()));

            if(payments.size() > 0){
                List<String> totalPay = new ArrayList<>();
                for(int i = 0; i < payments.size(); i++) {
                    if(payments.get(i).getId() != null) {
                        totalPay.add(helper.getOrder(payments.get(i).getOrder_id(),2).getTotal_price());
                    }
                }
                rvCustOrder.setHasFixedSize(true);
                rvCustOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
                rvCustOrder.setAdapter(new OrderAdapter(
                        getActivity(),
                        payments,
                        totalPay,
                        AccountFragment.this,
                        OrderAdapter.PAIDCUST
                ));
            }
        }
    }
}
