package com.example.capstone.Vendor;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.capstone.Customer.AccountFragment;
import com.example.capstone.DB.CartList;
import com.example.capstone.DB.Customer;
import com.example.capstone.DB.DatabaseHelper;
import com.example.capstone.DB.Order;
import com.example.capstone.DB.Payment;
import com.example.capstone.DB.Product;
import com.example.capstone.R;
import com.example.capstone.Util.OrderAdapter;
import com.example.capstone.Util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    private RecyclerView rvCustOrder;
    private RadioGroup rgOrder;
    private SessionManager session;
    private DatabaseHelper helper;

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order, container, false);

        initClasses();

        rvCustOrder = rootView.findViewById(R.id.rvCustOrder);
        rgOrder = rootView.findViewById(R.id.rgCustOrder);

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

    class rvUnpaidAsync extends AsyncTask<Void, Void, List<Payment>> {

        @Override
        protected List<Payment> doInBackground(Void... voids) {
            List<Product> pList;
            List<CartList> cList = new ArrayList<>();
            List<Order> oList = new ArrayList<>();
            List<Payment> payList = new ArrayList<>();

            pList = helper.getAllProducts("vendor",session.getUserDetails().get(SessionManager.KEY_ID));

            Log.e("PLIST",pList.get(0).getId());

            for(int i = 0; i < pList.size(); i++) {
                if(pList.get(i).getId() != null) {
                    cList.add(helper.getCartList(pList.get(i).getId()));
                }
            }

            Log.e("CLIST",cList.get(0).getOrder_id());

            for(int i = 0; i < cList.size(); i++) {
                oList.add(helper.getOrder(cList.get(i).getOrder_id(),2));
            }

            //Log.e("OLIST",oList.get(0).getId());

            for(int i = 0; i < oList.size(); i++) {
                if(oList.get(i).getId() != null) {
                    if(helper.getStatusPayment(oList.get(i).getId(),"0") != null) {
                        payList.add(helper.getStatusPayment(oList.get(i).getId(),"0"));
                    }
                }
            }

            //Log.e("PAYLIST",payList.get(0).getId());

            return payList;
        }

        @Override
        protected void onPostExecute(List<Payment> payments) {
            super.onPostExecute(payments);
//            Log.e("PAYMENT SIZE",String.valueOf(payments.size()));
//            Log.e("PAYMENT ORDER ID",String.valueOf(payments.get(0).getOrder_id()));
//            Log.e("PAYMENT ID",String.valueOf(payments.get(0).getId()));
//            Log.e("PAYMENT STATUS",String.valueOf(payments.get(0).getStatus()));

            if(payments.size() > 0){
                Order order;
                List<String> totalPay = new ArrayList<>();
                List<Customer> custList = new ArrayList<>();

                for(int i = 0; i < payments.size(); i++) {
                    if(payments.get(i).getId() != null) {
                        order = helper.getOrder(payments.get(i).getOrder_id(),2);
                        totalPay.add(order.getTotal_price());
                        custList.add(helper.getCustomer(order.getCust_id()));
                    }
                }
                rvCustOrder.setHasFixedSize(true);
                rvCustOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
                rvCustOrder.setAdapter(new OrderAdapter(
                        getActivity(),
                        payments,
                        totalPay,
                        custList,
                        OrderAdapter.UNPAIDVENDOR
                ));
            }
        }
    }

    class rvPaidAsync extends AsyncTask<Void, Void, List<Payment>> {

        @Override
        protected List<Payment> doInBackground(Void... voids) {
            List<Product> pList;
            List<CartList> cList = new ArrayList<>();
            List<Order> oList = new ArrayList<>();
            List<Payment> payList = new ArrayList<>();

            pList = helper.getAllProducts("vendor",session.getUserDetails().get(SessionManager.KEY_ID));

            Log.e("PLIST",pList.get(0).getId());

            for(int i = 0; i < pList.size(); i++) {
                if(pList.get(i).getId() != null) {
                    cList.add(helper.getCartList(pList.get(i).getId()));
                }
            }

            CartList cart = helper.getCartList(pList.get(0).getId());
            Log.e("CARTLIST",cart.getOrder_id());

            Log.e("CLIST",cList.get(0).getOrder_id());

            for(int i = 0; i < cList.size(); i++) {
                oList.add(helper.getOrder(cList.get(i).getOrder_id(),2));
            }

            //Log.e("OLIST",oList.get(0).getId());

            for(int i = 0; i < oList.size(); i++) {
                if(oList.get(i).getId() != null) {
                    if(helper.getStatusPayment(oList.get(i).getId(),"1") != null) {
                        payList.add(helper.getStatusPayment(oList.get(i).getId(),"1"));
                    }
                }
            }

            //Log.e("PAYLIST",payList.get(0).getId());

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
                Order order;
                List<String> totalPay = new ArrayList<>();
                List<Customer> custList = new ArrayList<>();

                for(int i = 0; i < payments.size(); i++) {
                    if(payments.get(i).getId() != null) {
                        order = helper.getOrder(payments.get(i).getOrder_id(),2);
                        totalPay.add(order.getTotal_price());
                        custList.add(helper.getCustomer(order.getCust_id()));
                    }
                }

                rvCustOrder.setHasFixedSize(true);
                rvCustOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
                rvCustOrder.setAdapter(new OrderAdapter(
                        getActivity(),
                        payments,
                        totalPay,
                        custList,
                        OrderAdapter.PAIDVENDOR
                ));
            }
        }
    }

}
