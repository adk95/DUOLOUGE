package com.hazyaz.ctup.login;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hazyaz.ctup.MainActivity;
import com.hazyaz.ctup.R;

public class ExistingUserLogin extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private TextInputLayout mExistingUser;
    private TextInputLayout mExistingPassword;
    private Toolbar mToolbar1;
    private ProgressDialog mLoginProgress;
    private DatabaseReference mUserDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_user_login);

        mAuth = FirebaseAuth.getInstance();

        mUserDatabase = FirebaseDatabase.getInstance().getReference();
        mExistingUser = (TextInputLayout) findViewById(R.id.ExisEmail);
        mExistingPassword = (TextInputLayout) findViewById(R.id.ExisPass);
        Button mExistingUserLogin = (Button) findViewById(R.id.ExinstingUserLogin);

        mToolbar1 = findViewById(R.id.app_bar_login);
        setSupportActionBar(mToolbar1);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mLoginProgress = new ProgressDialog(this);


        mExistingUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mUserName = mExistingUser.getEditText().getText().toString();
                String mUserPassword = mExistingPassword.getEditText().getText().toString();


                if (!TextUtils.isEmpty(mUserName) || !TextUtils.isEmpty(mUserPassword)) {
                    mLoginProgress.setTitle("Logging User");
                    mLoginProgress.setMessage("Please wait while we login user");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();

                    loginUser(mUserName, mUserPassword);
                }


            }
        });

    }

    private void loginUser(String mUserName, String mUserPassword) {

        mAuth.signInWithEmailAndPassword(mUserName, mUserPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {


                    mLoginProgress.dismiss();

                    String mCurrentUser = mAuth.getCurrentUser().getUid();
                    String DeviceToken = FirebaseInstanceId.getInstance().getToken();


                    mUserDatabase.child(mCurrentUser).child("device_token").setValue(DeviceToken)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Intent intent = new Intent(ExistingUserLogin.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                } else {
                    mLoginProgress.hide();
                    String task_result = task.getException().getMessage();
                    Log.d("taggggggg","this is somea "+task_result);
                    Toast.makeText(ExistingUserLogin.this, "this is errow beta ", Toast.LENGTH_LONG).show();
                }


            }
        });


    }
}