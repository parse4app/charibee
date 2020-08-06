package com.example.service.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.service.R;
import com.example.service.fragments.DiscoverFragment;
import com.example.service.fragments.HomeFragment;
import com.example.service.fragments.MessagesFragment;
import com.example.service.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private FragmentManager fragmentManager;
    private BottomNavigationView bottomNavigationView;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;

    HomeFragment homeFragment = new HomeFragment();
    DiscoverFragment discoverFragment = new DiscoverFragment();
    MessagesFragment messagesFragment = new MessagesFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // top/side nav bar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        setupDrawerContent(nvDrawer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // fragment manager
        fragmentManager = getSupportFragmentManager();

        // bottom nav
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                setFragmentView(item.getItemId());
                return true;
            }
        });

        // default bottom nav selection
        bottomNavigationView.setSelectedItemId(R.id.menu_home);

        // remove discover view if admin
        if (ParseUser.getCurrentUser().get("role").equals("Organizer")) {
            setAdminView();
        }
    }

    // listener for when side/top toolbar is clicked
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });
    }

    // when sidebar menuItem is clicked
    public void selectDrawerItem(MenuItem menuItem) {
        mDrawer.closeDrawers();
        switch (menuItem.getItemId()) {
            case R.id.sidebar_logout_functionality:
                ParseUser.logOut();
                goWelcomeActivity();
                break;
            default:
                break;
        }
    }

    // items selected for sidebar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // sets view for admin account
    private void setAdminView() {
        bottomNavigationView.getMenu().removeItem(R.id.menu_discover);
    }

    // updates fragment view
    public void setFragmentView(int fragmentId) {
        Fragment fragment;
        switch (fragmentId) {
            case R.id.menu_home:
                fragment = homeFragment;
                break;
            case R.id.menu_discover:
                fragment = discoverFragment;
                break;
            case R.id.menu_messages:
                fragment = messagesFragment;
                break;
            case R.id.menu_profile:
                fragment = profileFragment;
                break;
            default:
                Log.e(TAG, "default case should not be hit");
                fragment = homeFragment;
        }

        fragmentManager.beginTransaction().replace(R.id.main_frame_layout, fragment).commit();
    }

    // go to welcome activity
    private void goWelcomeActivity() {
        Intent i = new Intent(MainActivity.this, WelcomeActivity.class);
        startActivity(i);
        this.finish();
    }

}