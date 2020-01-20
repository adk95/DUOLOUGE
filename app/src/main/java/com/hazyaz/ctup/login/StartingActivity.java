package com.hazyaz.ctup.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.hazyaz.ctup.R;

public class StartingActivity extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        Button button = (Button)findViewById(R.id.Signin_Starting);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivity = new Intent(StartingActivity.this,RegisterActivity.class);
                startActivity(registerActivity);
            }
        });

        Button button1 = (Button)findViewById(R.id.Signin );
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerActivity = new Intent(StartingActivity.this,ExistingUserLogin.class);
                startActivity(registerActivity);
            }
        });








    }
}
