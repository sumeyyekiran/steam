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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    private Button loginBttn, registerBttn;
    private EditText userEmail, userPassword;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser(); // authenticated user

        if(firebaseUser != null){ // check user session

            Intent i = new Intent(MainActivity.this,HomeActivity.class);
            startActivity(i);
            finish();
        }

        loginBttn = findViewById(R.id.login);

        registerBttn = findViewById(R.id.register);
        userEmail = findViewById(R.id.email);
        userPassword = findViewById(R.id.parola);

        registerBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(userEmail.getText().toString().length()>0 && userPassword.getText().toString().length()>0){

                    loginControl();

                }else{

                    Toast.makeText(getApplicationContext(),"Lütfen ilgili alanları giriniz!",Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

    private void loginControl() {

        firebaseAuth.signInWithEmailAndPassword(userEmail.getText().toString(),userPassword.getText().toString()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){


                    Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                    intent.putExtra("email",userEmail.getText().toString());
                    startActivity(intent);
                    //Toast.makeText(getApplicationContext(),"GİRİŞ BAŞARILI",Toast.LENGTH_SHORT).show();

                    // giris yapılacak baska bir activity e yonlendirme
                }else {
                    Toast.makeText(getApplicationContext(),""+task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}
