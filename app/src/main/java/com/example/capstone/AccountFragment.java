package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.capstone.Util.SessionManager;

import java.util.HashMap;

public class AccountFragment extends Fragment {

    SessionManager session;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        session = new SessionManager(getActivity());
        TextView userMail = rootView.findViewById(R.id.emailUser);

        HashMap<String, String> user = session.getUserDetails();

        String email = user.get(SessionManager.KEY_EMAIL);

        userMail.setText(Html.fromHtml(email));

        TextView logout = rootView.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

}
