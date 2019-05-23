package com.example.steam;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText email, parola;
    private Button register;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email=findViewById(R.id.email);
        parola=findViewById(R.id.parola);
        register=findViewById(R.id.register);
        getSupportActionBar().setTitle("Kayit ol");

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("users");//jsondaki users taginden dolayi
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().length()>0 & parola.getText().toString().length()>0){
                    registerControl();
                }else{
                    Toast.makeText(RegisterActivity.this, "Lutfen doldur", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerControl() {

        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),parola.getText().toString()).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    createNewUser(task.getResult().getUser().getUid(),email.getText().toString());
                    Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                    startActivity(intent);

                    Toast.makeText(RegisterActivity.this, "Kullanici eklendi", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(RegisterActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void createNewUser(String uid, String email) {

        User user=new User();
        user.setUuid(uid);
        user.setEmail(email);
        databaseReference.child(uid).setValue(user);

    }
}
