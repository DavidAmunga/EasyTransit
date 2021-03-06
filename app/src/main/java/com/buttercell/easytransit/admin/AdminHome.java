package com.buttercell.easytransit.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.buttercell.easytransit.Login;
import com.buttercell.easytransit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.paperdb.Paper;

public class AdminHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView mTitle;

    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user=FirebaseAuth.getInstance().getCurrentUser();

        mTitle = toolbar.findViewById(R.id.toolbar_title);


        displaySelectedScreen(R.id.nav_admin_trips);


        //        Switching fragments
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().get("fragment") != null) {
                switch (getIntent().getExtras().get("fragment").toString()) {
                    case "stationS":
                        displaySelectedScreen(R.id.nav_admin_stations);
                        break;
                    case "trips":
                        displaySelectedScreen(R.id.nav_admin_trips);
                        break;
                    case "ratings":
                        displaySelectedScreen(R.id.nav_admin_ratings);
                        break;
                }
            }

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headerView = navigationView.getHeaderView(0);

        TextView txtUserName = (TextView) headerView.findViewById(R.id.txtUsername);
        TextView txtEmail = (TextView) headerView.findViewById(R.id.txtEmail);
        setNavDetails(txtUserName,txtEmail);

    }
    private void setNavDetails(TextView txtUserName, TextView txtEmail) {

        String name = user.getDisplayName();
        String email = user.getEmail();

        txtUserName.setText(name);
        txtEmail.setText(email);

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.admin_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
//            case R.id.nav_admin_dashboard:
//
//                mTitle.setText("Dashboard");
//                fragment = new AdminDashboard();
//                break;
            case R.id.nav_admin_trips:
                mTitle.setText("Trips");
                fragment = new AdminTrips();
                break;
            case R.id.nav_admin_stations:
                mTitle.setText("Stations");
                fragment = new AdminStations();
                break;
            case R.id.nav_admin_ratings:
                mTitle.setText("Ratings");
                fragment = new AdminRatings();
                break;
            case R.id.nav_admin_about:
                mTitle.setText("About");
                fragment = new AdminAbout();
                break;
            case R.id.nav_admin_exit:
                FirebaseAuth.getInstance().signOut();
                Paper.book().destroy();
                startActivity(new Intent(getBaseContext(), Login.class));
                finish();
                break;
            default:
                fragment = new AdminDashboard();
                break;

        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame_admin, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
