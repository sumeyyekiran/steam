package com.example.steam.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.steam.Fragment.HomeFragment;
import com.example.steam.Fragment.NotificationFragment;
import com.example.steam.Fragment.ProfileFragment;
import com.example.steam.Fragment.SearchFragment;
import com.example.steam.R;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    Fragment selectedFragment= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment= new HomeFragment();
                    break;
                case R.id.navigation_search:
                    selectedFragment= new SearchFragment();
                    break;

                case R.id.navigation_notification:

                    selectedFragment= new NotificationFragment();
                    break;
                case R.id.navigation_profile:
                    SharedPreferences.Editor editor= getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileId",FirebaseAuth.getInstance().getCurrentUser().getUid());
                    selectedFragment= new ProfileFragment();
                    break;

                case R.id.navigation_logout:

                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage("Çıkış yapmak istediğinize emin misiniz?")
                            .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    FirebaseAuth.getInstance().signOut();
                                    Intent loginActivity = new Intent(getApplicationContext(),LoginActivity.class);
                                    startActivity(loginActivity);
                                    finish();
                                }
                            })
                            .setNegativeButton("Hayır", null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    break;
            }

            if (selectedFragment!=null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
            }
            return true;

        }



    };

}
