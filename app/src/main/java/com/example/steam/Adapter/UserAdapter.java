package com.example.steam.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.steam.Models.User;
import com.example.steam.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<User> mUser;
    private FirebaseUser firebaseUser;



    public UserAdapter(Context mContext, ArrayList<User> mUser){
        this.mContext= mContext;
        this.mUser= mUser;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(mContext).inflate(R.layout.user_item, viewGroup, false);
        return new UserAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final User user= mUser.get(i);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();


        viewHolder.btn_follow.setVisibility(View.VISIBLE);
        viewHolder.username.setText(user.getUsername());
        viewHolder.fullname.setText(user.getUsername());

        isFollowing(user.getUuid(), viewHolder.btn_follow);

        if(user.getUuid().equals((firebaseUser.getUid()))){
            viewHolder.btn_follow.setVisibility(View.GONE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor= mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                editor.putString("profileid", user.getUuid());
                editor.apply();
            }
        });


    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username, fullname;
        public Button btn_follow;

      public ViewHolder(@NonNull View itemView) {
          super(itemView);

          username= itemView.findViewById(R.id.username);
          fullname=itemView.findViewById(R.id.fullname);
          btn_follow=itemView.findViewById(R.id.btn_follow);

      }
  }

  private void isFollowing(final String userid, final Button button){
      DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following");
      reference.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              if (dataSnapshot.child(userid).exists()){
                  button.setText("following");
              }else{
                  button.setText("follow");
              }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });

  }

}
