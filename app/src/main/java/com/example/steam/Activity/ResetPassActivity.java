package com.example.steam.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.steam.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassActivity extends AppCompatActivity {

    EditText send_email;
    Button btn_reset;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        send_email=findViewById(R.id.send_email);
        btn_reset= findViewById(R.id.btn_reset);

        firebaseAuth = FirebaseAuth.getInstance();

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=send_email.getText().toString();
                if (email.equals("")){
                    Toast.makeText(ResetPassActivity.this,"Boş bırakılamaz",Toast.LENGTH_SHORT).show();
                }else{
                        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(ResetPassActivity.this,"Lütfen email adresinizi kontrol edin",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ResetPassActivity.this,LoginActivity.class));
                                }
                                else{
                                    String error= task.getException().getMessage();
                                    Toast.makeText(ResetPassActivity.this,error,Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                }

            }
        });

    }
}
