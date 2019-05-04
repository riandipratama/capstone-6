package com.example.capstone.Customer;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstone.DB.CartList;
import com.example.capstone.DB.DatabaseHelper;
import com.example.capstone.DB.Order;
import com.example.capstone.DB.Payment;
import com.example.capstone.R;
import com.example.capstone.Util.CartAdapter;
import com.example.capstone.Util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment implements View.OnClickListener {

    private TextView tvTotal;
    private TextView tvEmpty;
    private Button btnOrder;
    private RecyclerView rvCart;
    private Spinner spinPayMethod;
    private ScrollView scCart;

    private DatabaseHelper helper;
    private SessionManager session;
    private String payMethod;
    private List<CartList> cList = new ArrayList<>();

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);

        tvTotal = rootView.findViewById(R.id.tvTotal);
        tvEmpty = rootView.findViewById(R.id.tvEmptyCart);
        rvCart = rootView.findViewById(R.id.rvCart);
        btnOrder = rootView.findViewById(R.id.btnOrder);
        spinPayMethod = rootView.findViewById(R.id.spinPayMethod);
        scCart = rootView.findViewById(R.id.scCart);

        helper = new DatabaseHelper(getActivity());
        session = new SessionManager(getActivity());

        runGetDataAsync();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(),R.array.spinPayMethod, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinPayMethod.setAdapter(adapter);
        spinPayMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                payMethod = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnOrder.setOnClickListener(this);

        // Inflate the layout for this fragment
        return rootView;
    }


    public void runGetDataAsync() {
        new getData().execute();
    }

    void initRecyclerView(RecyclerView rv, List<CartList> cList) {
        this.cList = cList;
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(new CartAdapter(getActivity(),cList,CartFragment.this));
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnOrder:
                Order order = new Order(
                        session.getUserDetails().get(SessionManager.KEY_ID),
                        cList.get(0).getCart_id(),
                        tvTotal.getText().toString().substring(2)
                );

                String order_id = helper.addOrder(order);
                Log.e("orderid",order_id);

                Payment pay = new Payment(
                        order_id,
                        payMethod,
                        "0"
                );

                String pay_id = helper.addPayment(pay);

                helper.updateCartListOrderId(cList.get(0).getCart_id(),order_id);
                helper.deleteCart(cList.get(0).getCart_id());
                session.deleteCartSession();

                showPayDialog(order_id,pay_id,payMethod,tvTotal.getText().toString());
                break;
        }
    }

    private void showPayDialog(String order_id, String pay_id, String payMethod, String total) {
        Dialog payDialog = new Dialog(getActivity());
        payDialog.setContentView(R.layout.dialog_payment);

        TextView tvOrderID = payDialog.findViewById(R.id.tvOrderIDPaid);
        TextView tvPaymentID = payDialog.findViewById(R.id.tvCustName);
        TextView tvPaymentMethod = payDialog.findViewById(R.id.tvPaymentMethod);
        TextView tvTotalPay = payDialog.findViewById(R.id.tvTotal);
        Button btnBack = payDialog.findViewById(R.id.btnBack);
        Button btnPay = payDialog.findViewById(R.id.btnPay);

        tvOrderID.setText(getActivity().getResources().getString(R.string.order_id_payment,order_id));
        tvPaymentID.setText(getActivity().getResources().getString(R.string.payment_id_payment,pay_id));
        tvPaymentMethod.setText(getActivity().getResources().getString(R.string.pay_method_payment,payMethod));
        tvTotalPay.setText(getActivity().getResources().getString(R.string.total_payment,total));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runGetDataAsync();
                payDialog.dismiss();
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.updatePayStatus(pay_id,"1");
                Toast.makeText(getActivity(), "Pembayaran Berhasil", Toast.LENGTH_SHORT).show();
                runGetDataAsync();
                payDialog.dismiss();

                cList.clear();
                initRecyclerView(rvCart,cList);
            }
        });
        payDialog.show();
    }

    class getData extends AsyncTask<Boolean, Boolean, List<CartList>> {

        @Override
        protected List<CartList> doInBackground(Boolean... booleans) {
            List<CartList> cList = new ArrayList<>();
            String cart_id = helper.getCart(session.getUserDetails().get(SessionManager.KEY_ID)).getId();
            if(cart_id != null) {
                cList = helper.getAllParticularCartList(cart_id,1);
                return cList;
            } else {
                return cList;
            }

        }

        @Override
        protected void onPostExecute(List<CartList> cListTemp) {
            super.onPostExecute(cListTemp);
            if(!cListTemp.isEmpty()) {
                initRecyclerView(rvCart,cListTemp);
                initTotal(cListTemp);
                tvEmpty.setVisibility(View.GONE);
                scCart.setVisibility(View.VISIBLE);
            } else {
                scCart.setVisibility(View.GONE);
                tvEmpty.setVisibility(View.VISIBLE);
            }
        }
    }

    public void updateTotal(String total) {
        tvTotal.setText(getActivity().getResources().getString(R.string.rp_symbol,total));
    }

    private void initTotal(List<CartList> cList) {

        int total = 0;

        for(int i = 0; i < cList.size(); i++) {
            String price = helper.getProduct(cList.get(i).getProd_id()).getPrice();
            Log.e("PRICE",
                    cList.get(i).getProd_id() + " - "
            + cList.get(i).getCart_id());
            total += Integer.parseInt(price);
        }

        tvTotal.setText(getActivity().getResources().getString(
                R.string.rp_symbol,String.valueOf(total))
        );
    }

}
