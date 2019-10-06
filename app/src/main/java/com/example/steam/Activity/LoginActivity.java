package com.example.steam.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.steam.Models.User;
import com.example.steam.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {


    private Button loginBttn;
    private TextView link_signup, forgetpass;
    private ProgressDialog pd;
    private EditText userEmail, userPassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private ImageView google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        View view= getWindow().getDecorView();
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT) {
            view.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        }

        loginBttn = findViewById(R.id.loginBtn);
        link_signup = findViewById(R.id.link_signup);
        userEmail = findViewById(R.id.mail);
        userPassword = findViewById(R.id.pass);
        google= findViewById(R.id.google_button);
        forgetpass=findViewById(R.id.forgetpass);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("user");

        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginActivity.this,ResetPassActivity.class);
                startActivity(intent);
            }
        });
        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginActivity.this,GoogleSignInActivity.class);
                startActivity(intent);
            }
        });

        loginBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd= new ProgressDialog(LoginActivity.this);
                pd.setMessage("Lütfen bekleyin...");
                pd.show();

                String str_email= userEmail.getText().toString();
                String str_pass= userPassword.getText().toString();

                if (TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_pass)){
                    Toast.makeText(LoginActivity.this,"Lütfen alanları doldurunuz", Toast.LENGTH_SHORT).show();
                }
                else {
                    firebaseAuth.signInWithEmailAndPassword(str_email,str_pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(task.getResult().getUser().getUid());
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        HashMap<String, Object> hashMap = (HashMap<String, Object>) dataSnapshot.getValue();
                                        String str = (String) hashMap.get("role");

                                        pd.dismiss();


                                        if(str.equals("sanatçı")){


                                            Intent intent= new Intent(LoginActivity.this,MainActivity.class);
                                            startActivity(intent);

                                        }else{

                                            Intent intent= new Intent(LoginActivity.this, FanActivity.class);
                                            startActivity(intent);
                                        }





                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });



                            } else {
                                pd.dismiss();
                                Toast.makeText(LoginActivity.this, "Böyle bir kullanıcı yok", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(LoginActivity.this, SplashScreen.class);
        startActivity(intent);
    }
}






