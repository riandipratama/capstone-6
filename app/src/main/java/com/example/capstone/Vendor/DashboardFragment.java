package com.example.capstone.Vendor;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.capstone.DB.Category;
import com.example.capstone.DB.DatabaseHelper;
import com.example.capstone.DB.Product;
import com.example.capstone.DB.ProductImages;
import com.example.capstone.DB.Vendor;
import com.example.capstone.R;
import com.example.capstone.Util.DashboardProductAdapter;
import com.example.capstone.Util.ProductListAdapter;
import com.example.capstone.Util.SessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class DashboardFragment extends Fragment implements View.OnClickListener {


    private ImageView prodPrev1, prodPrev2, prodPrev3, prodPrev4, prodPrev5;
    private EditText namaPrd, qtyProd, hargaProd, descProd;
    private ProgressDialog progressBar;

    private List<Product> prodList = new ArrayList<>();
    private List<Bitmap> bitmap = new ArrayList<>();
    private List<Uri> contentUri = new ArrayList<>();
    private int maxPic = 0;

    DashboardProductAdapter dashboardAdapter;
    SessionManager session;
    DatabaseHelper helper;
    Product prod;
    Dialog dialog_add_product;


    private String selected;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        helper = new DatabaseHelper(getActivity());
        session = new SessionManager(getActivity());

        prodList = helper.getAllProducts("vendor", session.getUserDetails().get(SessionManager.KEY_ID));
        List<ProductImages> prodImgList = new ArrayList<>();
        for (int x = 0; x < prodList.size(); x++) {
            prodImgList.add(helper.getProductImages(prodList.get(x).getId()));
        }

        RecyclerView rvprodvendor = rootView.findViewById(R.id.rv_prod_vendor);
        rvprodvendor.setHasFixedSize(true);
        rvprodvendor.setLayoutManager(new LinearLayoutManager(getActivity()));

        dashboardAdapter = new DashboardProductAdapter(
                getActivity(),
                prodList,
                prodImgList
        );
        rvprodvendor.setAdapter(dashboardAdapter);

        Spinner saringProduk = rootView.findViewById(R.id.saringProduk);
        FloatingActionButton addProduct = rootView.findViewById(R.id.floatingActionButton);
        addProduct.setOnClickListener(this);

        spinnerAdapter(saringProduk, getResources().getStringArray(R.array.spinDashboard));
        saringProduk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floatingActionButton:
                showDialog();
                return;
        }
    }

    public void showDialog() {
        dialog_add_product = new Dialog(getActivity(), R.style.Dialog);
        dialog_add_product.setContentView(R.layout.dialog_add_product);
        dialog_add_product.setTitle("Tambah Produk");

        namaPrd = dialog_add_product.findViewById(R.id.namaProd);
        qtyProd = dialog_add_product.findViewById(R.id.qtyProd);
        hargaProd = dialog_add_product.findViewById(R.id.hargaProd);
        descProd = dialog_add_product.findViewById(R.id.descProd);
        Button btnPilihFoto = dialog_add_product.findViewById(R.id.btnPilihFoto);
        Button btnCancel = dialog_add_product.findViewById(R.id.btnCancelAdd);
        Button btnAddProd = dialog_add_product.findViewById(R.id.btnAddProd);

        String[] catNames = new String[helper.getAllCategories().size()];
        final String[] catIds = new String[helper.getAllCategories().size()];
        Category cat;
        for (int i = 0; i < helper.getAllCategories().size(); i++) {
            cat = helper.getAllCategories().get(i);
            catNames[i] = cat.getCat_name();
            catIds[i] = cat.getId();
        }


        Spinner spinKategori = dialog_add_product.findViewById(R.id.spinKategori);
        spinnerAdapter(spinKategori, catNames);
        spinKategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected = catIds[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        prodPrev1 = dialog_add_product.findViewById(R.id.prodPrev1);
        prodPrev2 = dialog_add_product.findViewById(R.id.prodPrev2);
        prodPrev3 = dialog_add_product.findViewById(R.id.prodPrev3);
        prodPrev4 = dialog_add_product.findViewById(R.id.prodPrev4);
        prodPrev5 = dialog_add_product.findViewById(R.id.prodPrev5);


        btnPilihFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });

        btnAddProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddProductAsync().execute();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_add_product.cancel();
            }
        });

        dialog_add_product.show();
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
                                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                                break;
                            case 1:
                                if (maxPic < 5) {
                                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(cameraIntent, 2);
                                    maxPic++;
                                }

                                break;
                            case 2:
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView prevs[] = {prodPrev1, prodPrev2, prodPrev3, prodPrev3, prodPrev4, prodPrev5};
        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                contentUri.add(data.getData());
                try {
                    bitmap.add(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentUri.get(0)));
                    prevs[0].setImageBitmap(bitmap.get(0));
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }

            } else if (data.getClipData() != null) {
                ClipData mClipData = data.getClipData();
                for (int i = 0; i < mClipData.getItemCount(); i++) {
                    ClipData.Item item = mClipData.getItemAt(i);
                    contentUri.add(item.getUri());
                    try {
                        bitmap.add(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentUri.get(i)));
                        prevs[i].setImageBitmap(bitmap.get(i));
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                    }
                } //end for
            }

        } else if (requestCode == 2) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            prevs[maxPic].setImageBitmap(bitmap);
        }
    }

    public void addProductImages(long id, String uri) {

        ProductImages prodImg = new ProductImages();
        prodImg.setId(String.valueOf(id));
        helper.addProductImages(prodImg,uri);
        dashboardAdapter.notifyDataSetChanged();
    }

    public void spinnerAdapter(Spinner spinner, String[] resources) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item,
                resources);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    class AddProductAsync extends AsyncTask<Void, Void, Void> {

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
            prodList.add(prod);
            dialog_add_product.cancel();
            dashboardAdapter.notifyDataSetChanged();
            progressBar.dismiss();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            prod = new Product(
                    namaPrd.getText().toString(),
                    selected,
                    qtyProd.getText().toString(),
                    hargaProd.getText().toString(),
                    descProd.getText().toString(),
                    new SessionManager(getActivity()).getUserDetails().get(SessionManager.KEY_ID)
            );

            long id = helper.addProduct(prod);

            for(int i = 0; i < contentUri.size(); i++) {
                StorageReference storeRef = FirebaseStorage.getInstance().getReference()
                        .child("product-images/" + id + "-" + i);
                storeRef.putFile(contentUri.get(i))
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        addProductImages(id,uri.toString());
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            return null;
        }
    }

}
