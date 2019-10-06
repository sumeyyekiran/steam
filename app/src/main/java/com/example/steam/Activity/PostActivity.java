package com.example.steam.Activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.steam.Models.Post;
import com.example.steam.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class PostActivity extends AppCompatActivity {
    Uri imageUri;
    String myUri="";
    StorageTask uploadTask;
    StorageReference storageReference;

    ImageView close, image_added;
    TextView post;
    EditText desc;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        close= findViewById(R.id.close);
        image_added= findViewById(R.id.image_add);
        post= findViewById(R.id.post);
        desc= findViewById(R.id.description);

        storageReference= FirebaseStorage.getInstance().getReference("posts");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostActivity.this, MainActivity.class));
                finish();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        CropImage.activity().setAspectRatio(1,1).start(PostActivity.this);

    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver =getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
       final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Posting");
        pd.show();

        if (imageUri != null) {
          final StorageReference filereference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));

            uploadTask =filereference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isComplete()){
                        throw task.getException();
                    }
                    return filereference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri= task.getResult();
                        myUri=downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference( "Posts");

                        String postid= reference.push().getKey();
                        HashMap<String,Object> hashMap= new HashMap<>();
                        hashMap.put("postid",postid);
                        hashMap.put("postimage",myUri);
                        hashMap.put("description",desc.getText().toString());
                        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());

                        reference.child(postid).setValue(hashMap);
                        pd.dismiss();

                        startActivity(new Intent(PostActivity.this, MainActivity.class));
                        finish();

                    }else {
                        Toast.makeText(PostActivity.this,"Failed",Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });



        }else {
            Toast.makeText(this,"No Image Selected",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK){
            CropImage.ActivityResult result= CropImage.getActivityResult(data);
            imageUri =result.getUri();

            image_added.setImageURI(imageUri);
        }else{
            Toast.makeText(this,"Yanlış gitti",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PostActivity.this,MainActivity.class));
            finish();
        }
    }
}
