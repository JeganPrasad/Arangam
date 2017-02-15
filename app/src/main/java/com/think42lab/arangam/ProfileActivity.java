package com.think42lab.arangam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.twitter.sdk.android.core.SessionManager;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {
    SessionManagement session;

    // Button Logout
    Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        FacebookSdk.sdkInitialize(getApplicationContext());
        session = new SessionManagement(getApplicationContext());

        TextView Name = (TextView) findViewById(R.id.name);
        TextView Email = (TextView) findViewById(R.id.emailid);

        // Button logout
        btnLogout = (Button) findViewById(R.id.btnLogout);

        session.checkLogin();

        HashMap<String, String> user = session.getUserDetails();

        // name
        String name = user.get(session.KEY_NAME);

        // email
        String email = user.get(session.KEY_EMAIL);
        //String
        Log.d("ad", name);
        Log.d("ac", email);
        // displaying user data
        Name.setText(Html.fromHtml("Name: <b>" + name + "</b>"));
        Email.setText(Html.fromHtml("Email: <b>" + email + "</b>"));

        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Clear the session data
                // This will clear all session data and
                // redirect user to LoginActivity
                LoginManager.getInstance().logOut();
                session.logoutUser();
            }
        });

    }
}
