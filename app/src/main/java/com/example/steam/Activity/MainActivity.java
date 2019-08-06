package com.example.steam.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.steam.Fragment.HomeFragment;
import com.example.steam.Fragment.SearchFragment;
import com.example.steam.ProfileFragment;
import com.example.steam.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    HomeFragment homeFragment= new HomeFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
                    return true;
                case R.id.navigation_search:
                    SearchFragment searchFragment=new SearchFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, searchFragment).commit();
                    return true;
                case R.id.navigation_profile:
                    ProfileFragment mainFragment=new ProfileFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,mainFragment).commit();
                    return true;

                case R.id.navigation_logout:

                        new AlertDialog.Builder(MainActivity.this)
                                .setMessage("Çıkış yapmak istediğinize emin misiniz?")
                                .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        firebaseAuth.signOut();
                                        finish();
                                    }
                                })
                                .setNegativeButton("Hayır", null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                        break;
            }
            return true;

            }



    };

}

