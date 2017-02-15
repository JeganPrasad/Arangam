package com.think42lab.arangam;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Callable;

import com.facebook.Profile;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;
//        arangam2016@gmail.com
//                Arangam16

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;
    private CallbackManager mFacebookCallbackManager;

    private String url = "http://173.255.238.139:3030/api/1.0/user";


    // Session Manager Class
    SessionManagement session1;

    private LoginButton btnFacebookSignIn;
    private TwitterLoginButton btnTwitterSignIn;

    FacebookSdk facebookSdk = new FacebookSdk();

    private ProfileTracker profileTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        mFacebookCallbackManager = CallbackManager.Factory.create();

        // The private key that follows should never be public
        // (consider this when deploying the application)
        TwitterAuthConfig authConfig = new TwitterAuthConfig(getString(R.string.fabric_app_id).toString(),
                getString(R.string.fabric_secrete_key).toString());
        Fabric.with(this, new TwitterCore(authConfig));
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        btnFacebookSignIn = (LoginButton) findViewById(R.id.btnFacebookSignIn);
        btnFacebookSignIn.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends", "user_mobile_phone"));

        btnTwitterSignIn = (TwitterLoginButton) findViewById(R.id.btnTwitterSignIn);

        setFacebookAuthentication(btnFacebookSignIn);
        setTwitterAuthentication(btnTwitterSignIn);


        session1 = new SessionManagement(getApplicationContext());
        //Toast.makeText(getApplicationContext(), "User Login Status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();


        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

//                displayMessage(currentProfile);
            }


        };

        getSupportActionBar().hide();


    }

    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
        } else if (TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE == requestCode) {
            btnTwitterSignIn.onActivityResult(requestCode, resultCode, data);
        } else {
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void setFacebookAuthentication(LoginButton button) {

        button.registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Profile profile = Profile.getCurrentProfile();

                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();

                // Facebook Email address
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                Log.e("LoginActivity Response ", response.toString());

                                try {
                                    String Fname = object.getString("name");

                                    String Femail = object.getString("email");
                                    String Fid = object.getString("id");
//                                    String Fmobile = object.getString("mobile_phone");

                                    Log.e(TAG, "onCompleted: Facebook details " + Fname + "   " + Femail + "    " + Fid);

                                    session1.createLoginSession(Fname, Femail, "", Fid);

                                    HashMap<String, String> params = new HashMap<String, String>();
                                    params.put("name", Fname);
                                    params.put("email", Femail);
                                    params.put("socialNetworkId", Fid);
                                    params.put("mobile", "");

                                  /*change
                                    new RestClientPost(new RequestCallBack() {
                                        @Override
                                        public void success(String response) {
                                            Toast.makeText(LoginActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                            Log.e(TAG, "success: " + response.toString());
                                        }

                                        @Override
                                        public void fail() {

                                        }
                                    }, params).execute(url);*/

                                    handleSignInResult(new Callable<Void>() {
                                        @Override
                                        public Void call() throws Exception {
                                            LoginManager.getInstance().logOut();
                                            return null;
                                        }
                                    });
                                    finish();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {
                handleSignInResult(null);
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(LoginActivity.class.getCanonicalName(), error.getMessage());
                handleSignInResult(null);
            }
        });
    }


    private void setTwitterAuthentication(TwitterLoginButton button) {

        button.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                login(result);

                handleSignInResult(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        TwitterCore.getInstance().logOut();
                        return null;
                    }
                });
                finish();
            }

            @Override
            public void failure(TwitterException e) {
                Log.d(LoginActivity.class.getCanonicalName(), e.getMessage());
                handleSignInResult(null);
            }
        });
    }


    //The login function accepting the result object
    public void login(Result<TwitterSession> result) {

        //Creating a twitter session with result's data
        TwitterSession session = result.data;


        //Getting the username from session
        final String username = session.getUserName();
        final long id = session.getUserId();

        Log.e(TAG, "onCurrentProfileChanged: Twitter Details " + session.getUserName() + "  " + session.getId() + " Password" + getSaltString());


        TwitterSession session3 = TwitterCore.getInstance().getSessionManager().getActiveSession();
        TwitterAuthClient authClient = new TwitterAuthClient();
        authClient.requestEmail(session3, new Callback<String>() {
            @Override
            public void success(Result<String> result) {
                // Do something with the result, which provides the email address
                Toast.makeText(LoginActivity.this, result.data, Toast.LENGTH_SHORT).show();

                Log.e(TAG, "success: " + result.response + "   " + result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
            }
        });

        //This code will fetch the profile image URL

        TwitterCore.getInstance().getApiClient(session).getAccountService()
                .verifyCredentials(true, true).enqueue(new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                String imageUrl = result.data.profileImageUrl;
                String email = result.data.email;
                String Name = result.data.name;
                long userid = result.data.id;
                String username = result.data.screenName;

                Log.e(TAG, "success: " + email + "   " + Name + "   " + userid + "   " + username);

                session1.createLoginSession(Name, email + "", "", userid + "");

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("name", Name);
                params.put("email", email);
                params.put("socialNetworkId", userid + "");
                params.put("mobile", "");

                /*change
                new RestClientPost(new RequestCallBack() {
                    @Override
                    public void success(String response) {
                        Toast.makeText(LoginActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void fail() {

                    }
                }, params).execute(url);*/

//                System.out.println(imageUrl);
//                System.out.println("EMAIL:" + email);
//                System.out.println("Name:" + Name);
//                System.out.println("ID:" + userid);
//                System.out.println("Username:" + username);
            }

            @Override
            public void failure(TwitterException exception) {

            }
        });

        /*Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(i);*/
        finish();

    }


    private void handleSignInResult(Callable<Void> logout) {

        if (logout == null) {
            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show();
        } else {
            try {
                logout.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            startActivity(getParentActivityIntent());
        }

    }
}
