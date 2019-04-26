package com.example.capstone.Customer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.capstone.DB.DatabaseHelper;
import com.example.capstone.R;
import com.example.capstone.Util.CategoryAdapter;

public class HomeFragment extends Fragment {

    DatabaseHelper helper;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView rv_cat = rootView.findViewById(R.id.rv_cat);
        rv_cat.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rv_cat.setHasFixedSize(true);

        helper = new DatabaseHelper(getActivity());
        rv_cat.setAdapter(new CategoryAdapter(getActivity(), helper.getAllCategories()));


        // Inflate the layout for this fragment
        return rootView;
    }

}
