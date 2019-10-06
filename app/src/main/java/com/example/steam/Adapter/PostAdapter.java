package com.example.steam.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.steam.Models.Post;
import com.example.steam.Models.User;
import com.example.steam.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public  class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    public Context mContext;
    public List<Post> mPost;

    private FirebaseUser firebaseUser;

    public PostAdapter(Context mContext, List<Post> mPost) {
        this.mContext = mContext;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.post_item, viewGroup,false);

        return new PostAdapter.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        Post post=mPost.get(i);

        Picasso.get().load(post.getPostimage()).into(viewHolder.post_image);


        publisherInfo(viewHolder.image_profile,viewHolder.username, post.getPublisher());
    }

    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_profile, post_image;
        public TextView username, play, like, comment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile);
            post_image = itemView.findViewById(R.id.post_image);
            play = itemView.findViewById(R.id.sayi1);
            like = itemView.findViewById(R.id.sayi2);
            comment = itemView.findViewById(R.id.sayi3);
            username = itemView.findViewById(R.id.username);

        }
    }

    private void publisherInfo(final ImageView image_profile, final TextView userName, String userid){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user= dataSnapshot.getValue(User.class);
                userName.setText(user.getFullname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}


