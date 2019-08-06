package com.example.steam.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.steam.Models.User;
import com.example.steam.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText username, fullname,email, pass1;
    private Button register;
    private ProgressDialog pd;
    private FirebaseAuth firebaseAuth;
    private RadioButton hayran, sanatci;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username= findViewById(R.id.regusername);
        fullname= findViewById(R.id.regName);
        email = findViewById(R.id.regMail);
        pass1 = findViewById(R.id.regPassword);
        register = findViewById(R.id.regBtn);

        hayran = findViewById(R.id.hayran);
        sanatci = findViewById(R.id.sanatci);


        firebaseAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(RegisterActivity.this);
                pd.setMessage("Lütfen bekleyin");
                pd.show();

                String str_username = username.getText().toString();
                String str_fullname = fullname.getText().toString();
                String str_email = email.getText().toString();
                String str_pass = pass1.getText().toString();

                if (TextUtils.isEmpty(str_username) || TextUtils.isEmpty(str_fullname) || TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_pass)) {
                    Toast.makeText(RegisterActivity.this, "Tüm alanları lütfen doldurunuz", Toast.LENGTH_SHORT).show();
                }
                else if (str_pass.length()<6){
                    Toast.makeText(RegisterActivity.this,"Şifreniz 6 karakterden uzun olmalıdır",Toast.LENGTH_SHORT).show();
                }
                else {
                    register(str_fullname, str_username, str_email,str_pass, true);
                }
            }
        });


    }

    private void register(final String fullname , final String username, final String email, String pass1, Boolean role){
        firebaseAuth.createUserWithEmailAndPassword(email,pass1).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    String userid= firebaseUser.getUid();

                    if (hayran.isChecked()) {
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("id", userid);
                        hashMap.put("username", username.toLowerCase());
                        hashMap.put("fullname", fullname);
                        hashMap.put("email", email);
                        hashMap.put("role", "fan");

                        databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    pd.dismiss();
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

                                }
                            }
                        });
                    }
                    else {
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("id", userid);
                        hashMap.put("username", username.toLowerCase());
                        hashMap.put("fullname", fullname);
                        hashMap.put("role", "sanatçı");

                        databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    pd.dismiss();
                                    Intent intent = new Intent(RegisterActivity.this, SanatciActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

                                }
                            }
                        });
                    }
                }
                else{
                    pd.dismiss();
                    Toast.makeText(RegisterActivity.this,"Bu kullanıcı adına sahip birisi var",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
