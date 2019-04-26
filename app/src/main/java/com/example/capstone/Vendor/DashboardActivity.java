package com.example.capstone.Vendor;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.capstone.R;
import com.example.capstone.Util.SessionManager;

public class DashboardActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new SessionManager(this);
        session.checkLogin(2);

        loadFragment(new DashboardFragment());

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_dashboard:
                    fragment = new DashboardFragment();
                    break;
                case R.id.navigation_order:
                    fragment = new OrderFragment();
                    break;
                case R.id.navigation_account:
                    fragment = new AccountVendorFragment();
                    break;
                default:
                    fragment = new DashboardFragment();
            }
            loadFragment(fragment);
            return true;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.rootLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuLogout:
                final AlertDialog.Builder backBuild = new AlertDialog.Builder(this);
                backBuild.setCancelable(false);
                backBuild.setTitle("Logout");
                backBuild.setIcon(android.R.drawable.ic_dialog_alert);
                backBuild.setMessage("Logout dari Akun Anda dan Keluar dari Aplikasi?");
                backBuild.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        session.logoutUser();
                        finish();
                    }
                });
                backBuild.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                backBuild.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
