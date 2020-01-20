
package com.hazyaz.ctup.menu_item;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hazyaz.ctup.R;
import com.hazyaz.ctup.utils.users;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class AllUsersActivity extends AppCompatActivity {

    private RecyclerView mUsersList;
    private DatabaseReference mdatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        mdatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");

        mUsersList = (RecyclerView) findViewById(R.id.recyclerViewAllUsers);
        mUsersList.hasFixedSize();
        mUsersList.setLayoutManager(new LinearLayoutManager(this));

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<users, UsersViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<users, UsersViewHolder>
                        (users.class, R.layout.single_all_user, UsersViewHolder.class, mdatabaseReference) {
                    @Override
                    protected void populateViewHolder(UsersViewHolder viewHolder, users model, int position) {

                        viewHolder.setName(model.getName());
                        viewHolder.setImage(model.getThumbImage());
                        viewHolder.setUserStatus(model.getStatus());

                        final String user_id = getRef(position).getKey();

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(AllUsersActivity.this,UserProfileAcitivity.class);
                                intent.putExtra("user_id",user_id);
                                startActivity(intent);
                            }
                        });


                    }
                };

        mUsersList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

        }

        public void setName(String name12) {
            TextView mUserName = (TextView) mView.findViewById(R.id.user_name);
            mUserName.setText(name12);
        }

        public void setImage(String image) {
            CircleImageView mImageView = mView.findViewById(R.id.cirlce_user_all);
            Picasso.get().load(image).into(mImageView);

        }

        public void setUserStatus(String status12) {
            TextView mStatus =  (TextView)mView.findViewById(R.id.user_status);
            mStatus.setText(status12);
        }

    }


}