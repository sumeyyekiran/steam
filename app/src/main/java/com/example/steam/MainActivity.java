package com.example.steam;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

                    return true;
                case R.id.navigation_search:
                    SearchFragment maFragment=new SearchFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.blank_layout,maFragment).commit();

                    return true;
                case R.id.navigation_profile:
                    ProfileFragment mainFragment=new ProfileFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.blank_layout,mainFragment).commit();


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

