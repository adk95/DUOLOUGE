package com.hazyaz.ctup;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hazyaz.ctup.adapters.CustomPagerAdapter;
import com.hazyaz.ctup.login.StartingActivity;
import com.hazyaz.ctup.menu_item.AllUsersActivity;
import com.hazyaz.ctup.menu_item.Setting_activity;


public class MainActivity extends AppCompatActivity {
    TabLayout mTabLayout;
    CustomPagerAdapter mCustomPagerAdapter;
    ViewPager mViewPager;
    int RC_SIGN_IN = 0;
    //Fire base variable declaration
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private DatabaseReference mUserRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();


        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle("CTUP");


        if (mAuth.getCurrentUser() != null) {


            mUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());

        }


        mViewPager = findViewById(R.id.view_pager_main);
        mCustomPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mCustomPagerAdapter);


        mTabLayout = findViewById(R.id.tab_bar_main);
        mTabLayout.setupWithViewPager(mViewPager);


        // Fire base Class initialising of obejct


    }


    @Override
    public void onStart() {
        super.onStart();



        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
// if user is not signed in send it to login screen
            Intent mStartIntent = new Intent(MainActivity.this, StartingActivity.class);
            startActivity(mStartIntent);
            finish();
        } else {
            mUserRef.child("online").setValue("true");
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        // mUserRef.child("online").setValue("false");
        //   mUserRef.child("last_seen").setValue(ServerValue.TIMESTAMP);


    }

    // menu item for signout procress
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // menu item for signout procress
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.log_out_from_main:
                FirebaseAuth.getInstance().signOut();
                sendToStart();
                return true;


            case R.id.status_setting:
                Intent SettingIntent = new Intent(MainActivity.this, Setting_activity.class);
                startActivity(SettingIntent);


            case R.id.all_users:
                Intent allUsersIntent = new Intent(MainActivity.this, AllUsersActivity.class);
                startActivity(allUsersIntent);


            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // menu item for signout procress


    void sendToStart() {
        Intent intent = new Intent(MainActivity.this, StartingActivity.class);
        startActivity(intent);
        finish();
    }


}
