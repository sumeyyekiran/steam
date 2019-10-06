package com.example.steam.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.steam.Adapter.UserAdapter;
import com.example.steam.Models.User;
import com.example.steam.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    EditText mSearchField;
    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private DatabaseReference reference;
    private List<User> userList;
    private RecyclerView.LayoutManager layoutManager;

    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        reference= FirebaseDatabase.getInstance().getReference("Users");
        mSearchField=view.findViewById(R.id.search_field);


        userList=new ArrayList<>();
        recyclerView=view.findViewById(R.id.result_list);
        recyclerView.setHasFixedSize(true);

        readUsers();




        mSearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return view;
    }


    private void searchUsers(String s){
        Query query= FirebaseDatabase.getInstance().getReference("Users").orderByChild("username")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user =snapshot.getValue(User.class);
                    userList.add(user);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readUsers(){

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    User user=postSnapshot.getValue(User.class);
                    userList.add(user);
                }

                layoutManager=new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(layoutManager);
                adapter=new UserAdapter(getActivity(),userList);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}




