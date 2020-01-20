package com.hazyaz.ctup.login;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;


import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;


import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hazyaz.ctup.MainActivity;
import com.hazyaz.ctup.R;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout m1DisplayName;
    private TextInputLayout m1Email;
    private TextInputLayout m1Password;
    private Toolbar mToolbar;
    private ProgressDialog mRegProgress;


    //Firebase Initialisization
    private FirebaseAuth mAuth;
    private DatabaseReference mFirebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mToolbar = (Toolbar) findViewById(R.id.app_bar_register);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create an Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mRegProgress = new ProgressDialog(this);


        mAuth = FirebaseAuth.getInstance();

        m1DisplayName = (TextInputLayout) findViewById(R.id.regName);
        m1Email = (TextInputLayout) findViewById(R.id.regEmail);
        m1Password = (TextInputLayout) findViewById(R.id.regPassword);


        Button mRegisterButton = (Button) findViewById(R.id.RegisterLoginInfoButton);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mDispName = m1DisplayName.getEditText().getText().toString();
                String mEmailn = m1Email.getEditText().getText().toString();
                String mPasswordn = m1Password.getEditText().getText().toString();


                if (!TextUtils.isEmpty(mDispName) || !TextUtils.isEmpty(mEmailn) || !TextUtils.isEmpty(mPasswordn)) {
                    mRegProgress.setTitle("Registering User");
                    mRegProgress.setMessage("please wait while we register you");
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();
                    RegisterUser(mDispName, mEmailn, mPasswordn);
                }


            }
        });


    }

    private void RegisterUser(final String mDisplayName, String mEmail, String mPassword) {

        mAuth.createUserWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if (task.isSuccessful()) {
                    mRegProgress.dismiss();
                    //getting current user uid
                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = current_user.getUid();

                    //creating json directorey for new user using its unique uid
                    mFirebaseDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uid);

                    // for storing key value pair of multiple data using hashmap
                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", mDisplayName);
                    userMap.put("status", "Hey there I am using CTUP");
                    userMap.put("image", "default");
                    userMap.put("thumb_image", "default");

                    //setting hashmap to detabase
                    mFirebaseDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {


                                //starting new activity ater saving user data and and registering the user
                                Intent mMainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                mMainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mMainIntent);
                                finish();


                            }
                        }
                    });

                } else {
                    mRegProgress.hide();
                    Toast.makeText(getApplicationContext(), "sometinsg ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
