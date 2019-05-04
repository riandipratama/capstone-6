package com.example.capstone.Customer;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.DB.Cart;
import com.example.capstone.DB.CartList;
import com.example.capstone.DB.Customer;
import com.example.capstone.DB.DatabaseHelper;
import com.example.capstone.DB.Order;
import com.example.capstone.DB.Payment;
import com.example.capstone.DB.Product;
import com.example.capstone.DB.ProductImages;
import com.example.capstone.DB.Review;
import com.example.capstone.DB.Vendor;
import com.example.capstone.R;
import com.example.capstone.Util.ProductDetailsImagesAdapter;
import com.example.capstone.Util.ReviewAdapter;
import com.example.capstone.Util.SessionManager;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextView tvName, tvVendor, tvHarga, tvQty, tvLokasi, tvDesc;
    private RecyclerView rvprodimg, rvreview;
   //private Button btnReview;

    private Product prod;
    private Vendor vendor;
    private DatabaseHelper helper;
    private Payment pay;

    private List<ProductImages> prodImgList;
    private String order_date = "";

    @Override
    protected void onStart() {
        super.onStart();

        runAsync();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        init();

        Intent i = getIntent();
        prod = helper.getProduct(i.getStringExtra("prod_id"));
        vendor = helper.getVendor(prod.getVendor_id());
        prodImgList = helper.getAllProductImages(prod.getId());

        new reviewAsync().execute(prod.getId());

        setTexts(prod,vendor);

        rvprodimg.setHasFixedSize(true);
        rvprodimg.setLayoutManager(new LinearLayoutManager(ProductDetailsActivity.this,LinearLayout.HORIZONTAL,false));
        rvprodimg.setAdapter(new ProductDetailsImagesAdapter(
                ProductDetailsActivity.this,
                prodImgList)
        );

//        btnReview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Customer cust = checkReviewRights(prod.getId());
//                if(cust != null) {
//                    Dialog addReview = new Dialog(ProductDetailsActivity.this);
//                    addReview.setContentView(R.layout.dialog_addreview);
//
//                    EditText etComment = addReview.findViewById(R.id.etComment);
//                    Button btnCancel = addReview.findViewById(R.id.btnCancel);
//                    Button btnAddReview = addReview.findViewById(R.id.btnAddReview);
//
//                    btnCancel.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            addReview.dismiss();
//                        }
//                    });
//
//                    btnAddReview.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Review rev = new Review(
//                                    prod.getId(),
//                                    cust.getId(),
//                                    pay.getId(),
//                                    etComment.getText().toString()
//                            );
//
//                            if(helper.addReview(rev)) {
//                                Toast.makeText(getApplicationContext(),"Berhasil tambah Review", Toast.LENGTH_SHORT).show();
//                                new reviewAsync().execute(prod.getId());
//                            } else {
//                                Toast.makeText(getApplicationContext(),"Gagal tambah Review", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                    addReview.show();
//                }
//            }
//        });
    }

    private Customer checkReviewRights(String id) {
        CartList cl = helper.getCartList(id,1);
        Order order = helper.getOrder(cl.getOrder_id(),2);
        pay = helper.getStatusPayment(order.getId(),"1");

        if(pay != null) {
            Customer cust = helper.getCustomer(order.getCust_id());
            return cust;
        }

        return null;
    }

    public void init() {
        tvName = findViewById(R.id.tvName);
        tvVendor = findViewById(R.id.tvVendor);
        tvHarga = findViewById(R.id.tvHarga);
        tvQty = findViewById(R.id.tvQty);
        tvLokasi = findViewById(R.id.tvLokasi);
        tvDesc = findViewById(R.id.tvDesc);
        rvprodimg = findViewById(R.id.rvprodimg);
        rvreview = findViewById(R.id.rvReview);
        //btnReview = findViewById(R.id.btnReview);

        helper = new DatabaseHelper(this);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_chat:
                    chatWhatsapp(vendor);
                    break;
                case R.id.navigation_schedule:
                    calendarDialog();
                    break;
                case R.id.navigation_checkout:
                    if(order_date.isEmpty()) {
                        Toast.makeText(getApplicationContext(),"Pilih Tanggal Terlebih Dahulu!",Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        toCart();
                    }
                    break;
            }
            return true;
        }
    };

    private void toCart() {
        SessionManager session = new SessionManager(this);

        String cart_id = "";

        if(!session.checkCartSession()) {
            Cart cart = new Cart(session.getUserDetails().get(SessionManager.KEY_ID));

            cart_id = String.valueOf(helper.addCart(cart));
            session.createCartSession(cart_id);
        } else {
            cart_id = session.getCartSession().get(SessionManager.KEY_CART);
        }

        if(!helper.checkCartListItem(cart_id,prod.getId())) {
            CartList cl = new CartList(
                    cart_id,
                    null,
                    prod.getId(),
                    "1",
                    prod.getPrice(),
                    order_date
            );

            if(helper.addCartList(cl)) {
                final AlertDialog.Builder successCart = new AlertDialog.Builder(this);
                successCart.setTitle("Produk Berhasil ditambahkan ke Cart!");
                successCart.setMessage("Lanjut Belanja?");
                successCart.setNegativeButton("Lihat Cart", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(ProductDetailsActivity.this, MainActivity.class);
                        i.putExtra("toCart",true);
                        startActivity(i);
                        finish();
                        dialog.dismiss();
                    }
                });
                successCart.setPositiveButton("Lanjut", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                successCart.show();
            }

        } else {
            Toast.makeText(getApplicationContext(),"Produk sudah ada di Cart!",Toast.LENGTH_SHORT)
                    .show();
        }

    }

    private void calendarDialog() {

        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                ProductDetailsActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DATE)
        );
        dpd.setMinDate(now);
        dpd.show(ProductDetailsActivity.this.getFragmentManager(),"DatePicker Dialog");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String[] booked = {"13/04/2019","17/04/2019","23/04/2019",};

        Date date = null;

        for (int i = 0; i < booked.length; i++) {
            try {
                date = sdf.parse(booked[i]);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            List<Calendar> dates = new ArrayList<>();
            dates.add(cal);
            Calendar [] disabledDays = dates.toArray(new Calendar[dates.size()]);
            dpd.setDisabledDays(disabledDays);
        }
    }


    private void chatWhatsapp(final Vendor ven) {
        AlertDialog.Builder wa_dialog = new AlertDialog.Builder(this);
        wa_dialog.setMessage("Chat Vendor via WhatsApp?");
        wa_dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String num = ven.getPhone();
                num = "62"+num.substring(1);

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http:wa.me/"+num)));
                dialog.cancel();
            }
        });
        wa_dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        wa_dialog.show();
    }

    public void setTexts(Product prod, Vendor vendor) {
        tvName.setText(prod.getName());
        tvVendor.setText(vendor.getName());
        tvHarga.setText(prod.getPrice());
        tvQty.setText(prod.getQty());
        tvLokasi.setText(
                getResources().getString(
                        R.string.lokasi_produk_detail,
                        vendor.getAddress(),vendor.getCity()));
        tvDesc.setText(prod.getDesc());
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        order_date = dayOfMonth + "/" + (monthOfYear+1) + "/" + year;

        Snackbar snack = Snackbar.make(findViewById(R.id.coorlay),
                "For : " + order_date + "\n(Select another date to change)", Snackbar.LENGTH_INDEFINITE);
        snack.setAction("GOT IT", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams)
                snack.getView().getLayoutParams();
        params.setMargins(0, 0, 0, 55);
        snack.getView().setLayoutParams(params);
        snack.show();
    }

    public void runAsync(){
        new reviewAsync().execute(prod.getId());
    }

    class reviewAsync extends AsyncTask<String, String, List<Review>> {

        @Override
        protected List<Review> doInBackground(String... strings) {
            return helper.getAllParticularReview(strings[0]);
        }

        @Override
        protected void onPostExecute(List<Review> reviews) {
            super.onPostExecute(reviews);
            Log.e("Review",String.valueOf(reviews.size()));
            if(!reviews.isEmpty()) {
                List<Customer> cList = new ArrayList<>();
                for(int i = 0; i < reviews.size(); i++) {
                    cList.add(helper.getCustomer(reviews.get(i).getCust_id()));
                }

                rvreview.setHasFixedSize(true);
                rvreview.setLayoutManager(new LinearLayoutManager(ProductDetailsActivity.this));
                rvreview.setAdapter(new ReviewAdapter(
                        ProductDetailsActivity.this,
                        reviews,
                        cList
                        ));
            }
        }
    }
}
