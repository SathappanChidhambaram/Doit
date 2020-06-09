package com.doit.doitappfin.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.doit.doitappfin.MainActivity;
import com.doit.doitappfin.R;
import com.doit.doitappfin.ui.MainWorkActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GoogleLoginActivity extends AppCompatActivity {
    GoogleSignInClient mGoogleSignInClient;
    public static final int RC_SIGN_IN=10;
    private FirebaseAuth mAuth;
    private EditText Enum;
    String mail="";
    String  A="";
    private Button login;
    RequestQueue requestQueue;
    GoogleSignInAccount acct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login);

        mAuth = FirebaseAuth.getInstance();
        TextView t=findViewById(R.id.textView6);
        login=findViewById(R.id.button2);
        Enum=findViewById(R.id.editText2);



        acct = GoogleSignIn.getLastSignedInAccount(this);

        if(acct!= null) {

            mail = acct.getEmail();
        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(Enum.getText().toString().length()!=10 || TextUtils.isEmpty(Enum.getText().toString()))
                    Enum.setError("Invalid Number");
                else {

                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("LoginData");
                    rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (!snapshot.getValue().toString().contains(Enum.getText().toString()) ) {

                                //    FirebaseDatabase.getInstance().getReference().child("LoginData").child(mail).setValue("empty");

                                if(connectedToNetwork()){
                                    Intent intent = new Intent(GoogleLoginActivity.this, Registration.class);
                                    intent.putExtra("number", Enum.getText().toString());
                                    intent.putExtra("mail", "");
                                    startActivity(intent);                                }
                                else{ NoInternetAlertDialog(); }

                            }
                            else
                            {
                                if(connectedToNetwork()){
                                    SharedPreferences sp = getApplicationContext().getSharedPreferences("com.doitAppfin.PRIVATEDATA", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("number", Enum.getText().toString());
                                    editor.apply();
                                    finish();

                                    Intent i=(new Intent(GoogleLoginActivity.this, MainWorkActivity.class));
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);                                }
                                else{ NoInternetAlertDialog(); }


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }


            }
        });


        SignInButton b=findViewById(R.id.button);
// Configure Google Sign In
        GoogleSignInOptions gsop = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gsop);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


        FirebaseMessaging.getInstance().subscribeToTopic("test").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });

    }


    public  void sendNotification(){



        requestQueue = Volley.newRequestQueue(this);
        if(mAuth.getCurrentUser()!=null){
            A = "teacher";
        }
        else{
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            A =  acct.getDisplayName();
        }
        JSONObject json = new JSONObject();
        try{
            json.put("to","/topics/test");
            JSONObject details = new JSONObject();
            details.put("title","Test");
            details.put("body","this is the body");
            json.put("notification",details);
            JsonObjectRequest requester = new JsonObjectRequest(com.android.volley.Request.Method.POST, "https://fcm.googleapis.com/fcm/send", json, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String>  header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AIzaSyDi9drhOkWLGG6MIAhI6BuHBkJiS-3TLOk");
                    return header;
                }

            };
            requestQueue.add(requester);
        }
        catch (JSONException e ){

        }
    }




    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.

        if(connectedToNetwork()){
            volley();
        }else{ NoInternetAlertDialog(); }    }

    private void volley() {
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null) {                                  //startActivity(new Intent(GoogleLoginActivity.this, MainWorkActivity.class));
            if (currentUser.getEmail() != null)
            System.out.println(mail);
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("LoginData");
            rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    int t = 0;
                    for(DataSnapshot data: snapshot.getChildren()){
                        if(data.getKey().equals(mail.replace(".","_"))){

                            Intent i = (new Intent(GoogleLoginActivity.this, MainWorkActivity.class));
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(i);
                            t = 1;
                        }
                    }
                    if(t==0) {
                        Intent i = (new Intent(GoogleLoginActivity.this, Registration.class));
                        i.putExtra("mail", mail);
                        i.putExtra("number", "");
                        startActivity(i);
                    }






//                    if (!snapshot.hasChild(mail)) {
//
//                        //    FirebaseDatabase.getInstance().getReference().child("LoginData").child(mail).setValue("empty");
//
//                        if(connectedToNetwork()){
//                            Intent i = (new Intent(GoogleLoginActivity.this, Registration.class));
//                            i.putExtra("mail", currentUser.getEmail());
//                            i.putExtra("number", "");
//                            startActivity(i);                        }
//                        else{ NoInternetAlertDialog(); }
//
//                    } else {
//
//                        if(connectedToNetwork()){
//                            Intent i = (new Intent(GoogleLoginActivity.this, MainWorkActivity.class));
//                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
//                            startActivity(i);                        }
//                        else{ NoInternetAlertDialog(); }
//
//
//                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            //finish();
            //startActivity(new Intent(GoogleLoginActivity.this, MainWorkActivity.class));

        }
        }

    private void signIn() {
        System.out.println("su--log");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("cess--log");
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
               // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
                // ...
            }
        }
    }



    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());

        final AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            final FirebaseUser user = mAuth.getCurrentUser();

                            System.out.println("sucess--log");
                            if(user!=null)
                            {
                                // finish();
                                //startActivity(new Intent(GoogleLoginActivity.this, MainWorkActivity.class));
                                if(acct.getEmail()!=null)
                                    mail=acct.getEmail().replace(".","_");

                                System.out.println(mail);
                                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("LoginData");
                                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {
                                        if (!snapshot.hasChild(mail)) {

                                            //   FirebaseDatabase.getInstance().getReference().child("LoginData").child(mail).setValue("empty");
                                            if(connectedToNetwork()){
                                                Intent i=(new Intent(GoogleLoginActivity.this, Registration.class));
                                                i.putExtra("mail",acct.getEmail());
                                                i.putExtra("number","");
                                                startActivity(i);
                                            }
                                            else{ NoInternetAlertDialog(); }

                                        }
                                        else
                                        {
                                            if(connectedToNetwork()){
                                                Intent i=(new Intent(GoogleLoginActivity.this, MainWorkActivity.class));
                                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                                startActivity(i);                                            }
                                            else{ NoInternetAlertDialog(); }



                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                            else
                            {

                            }
                        } else {

                            System.out.println("fail--log");
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            //           Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //     updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @SuppressWarnings("MissingPermission")
    public boolean connectedToNetwork() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null) {
            return activeNetworkInfo.isConnected();
        }

        return false;

    }


    public void NoInternetAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You are not connected to the internet. ");
        builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(connectedToNetwork()){
                    volley();
                }else{ NoInternetAlertDialog(); }
            }
        });
        builder.setNegativeButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent openSettings = new Intent();
                openSettings.setAction(Settings.ACTION_WIRELESS_SETTINGS);
                openSettings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(openSettings);
            }
        });
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


}