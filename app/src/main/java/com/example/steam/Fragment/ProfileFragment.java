package com.example.steam;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;

import com.example.steam.Fragment.SettingsFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    private TextView userName, kullanici;
    private ImageView selectedImageView, settings;
    FirebaseStorage firebaseStorage;
    FirebaseAuth firebaseAuth;
    StorageReference storageReference;
    Button duzenle, update;
    ProgressDialog progressDialog;
    Uri uri;

    private static final int GALLERY_INTENT = 2;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==121 && resultCode==RESULT_OK && data!=null && data.getData()!=null){

            uri=data.getData();
            Picasso.get().load(data.getData()).fit().centerCrop().into(selectedImageView);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.item1:
                SettingsFragment mainFragment = new SettingsFragment();
                //getFragmentManager().beginTransaction().replace(R.id.blank_layout, mainFragment).commit();
                return true;
        }

        return false;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userName = view.findViewById(R.id.username);
        kullanici = view.findViewById(R.id.kullanici);
        settings = view.findViewById(R.id.settings);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = firebaseStorage.getReference();
        userName.setText(user.getEmail());
        duzenle = view.findViewById(R.id.duzenle);
        update = view.findViewById(R.id.guncelle);
        selectedImageView = view.findViewById(R.id.userPhoto);



        duzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");//her resim dosyasi olur png-jpg-...
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Resim seciniz"), 121);
            }
        });
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Yükleniyor...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StorageReference storageRef = firebaseStorage.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid());
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                progressDialog.dismiss();
                Picasso.get().load(uri).fit().centerCrop().into(selectedImageView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                progressDialog.dismiss();
                Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uri != null) {

                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setMessage("Yükleniyor...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    StorageReference storageRef = firebaseStorage.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid());
                    storageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Fotoğraf başarılı bir şekilde kaydedildi.", Toast.LENGTH_SHORT).show();
                            selectedImageView.setImageBitmap(null);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            progressDialog.dismiss();//donmemesi icin
                            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

                }


            }
        });



        return view;

    }



}