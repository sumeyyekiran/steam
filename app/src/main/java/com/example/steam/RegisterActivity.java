package com.example.steam;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText regMail, pass1, pass2;
    private Button register;
    private FirebaseAuth firebaseAuth;
    private RadioButton hayran, sanatci;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regMail = findViewById(R.id.regMail);
        pass1 = findViewById(R.id.regPassword);
        register = findViewById(R.id.regBtn);

        hayran = findViewById(R.id.hayran);
        sanatci = findViewById(R.id.sanatci);


        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("user");

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (regMail.getText().toString().length() > 0 & pass1.getText().toString().length() > 0) {
                    registerControl();

                } else {
                    Toast.makeText(RegisterActivity.this, "Lutfen doldur", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void registerControl() {

        firebaseAuth.createUserWithEmailAndPassword(regMail.getText().toString(), pass1.getText().toString()).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if (task.isSuccessful()) {
                    createNewUser(task.getResult().getUser().getUid(), regMail.getText().toString(), true);
                }

                else if (TextUtils.isEmpty(regMail.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "email adresini giriniz.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void createNewUser(String uid, String email, Boolean role) {
        User user = new User();

        if(hayran.isChecked()){
             user.setUuid(uid);
             user.setEmail(email);
             user.setRole(role);
             databaseReference.child(uid).setValue(user);
             Toast.makeText(RegisterActivity.this, "Hayran eklendi", Toast.LENGTH_SHORT).show();
             Intent intent = new Intent(getApplicationContext(), MainActivity.class);
             startActivity(intent);
        }

        else{
            user.setUuid(uid);
            user.setEmail(email);
            user.setRole(role);
            databaseReference.child(uid).setValue(user);
            Toast.makeText(RegisterActivity.this, "Sanatçı eklendi", Toast.LENGTH_SHORT).show();
            Intent intent1= new Intent(getApplicationContext(),SanatciActivity.class);
            startActivity(intent1);
        }


    }

}



