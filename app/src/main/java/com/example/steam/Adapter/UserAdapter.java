package com.example.steam.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.steam.Models.User;
import com.example.steam.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> userList;
    private Context context;
    FirebaseUser firebaseUser;

    public UserAdapter(Context context, List<User> users) {
        this.userList = users;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView userName;
        public TextView userDesc;
        public ImageView userPhoto;
        public Button btn_follow;

        public ViewHolder(View view) {
            super(view);

            userName = view.findViewById(R.id.name_text);
            userDesc = view.findViewById(R.id.status_text);
            userPhoto = view.findViewById(R.id.profile_image);
            btn_follow= view.findViewById(R.id.btn_follow);

        }

    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final User user = userList.get(position);
        viewHolder.btn_follow.setVisibility(View.VISIBLE);
        viewHolder.userName.setText(user.getUsername());
        viewHolder.userDesc.setText(user.getRole());


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }


}

