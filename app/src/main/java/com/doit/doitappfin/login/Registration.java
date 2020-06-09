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
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.doit.doitappfin.R;
import com.doit.doitappfin.utils.UserDetails;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registration extends AppCompatActivity {

    EditText Ename,Ephone,Email;
    Button submit;
    String mail ;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    GoogleSignInClient mGoogleInCilients;

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mGoogleInCilients = GoogleSignIn.getClient(this,gso);

        Ename=findViewById(R.id.entername);
        Ephone=findViewById(R.id.enterphone);
        Email=findViewById(R.id.entermail);
        submit=findViewById(R.id.submit);


        if(connectedToNetwork()){

            volley();
        }else{ NoInternetAlertDialog(); }


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connectedToNetwork()){
                    getdata();

                    if(notempty())
                    {

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
DatabaseReference dbpay=database.getReference().child("Payment").child(UserDetails.semail.replace(".","_"));
                        HashMap<String,String> hashMap=new HashMap<>();


                        dbpay.child("Training").setValue("empty");
                        dbpay.child("Certification").setValue("empty");

                        SharedPreferences sp = getApplicationContext().getSharedPreferences("com.doitAppfin.PRIVATEDATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("number", UserDetails.sphone);
                        editor.apply();

                       Intent i = new Intent(Registration.this, otp.class);
                        i.putExtra("number",UserDetails.sphone);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }

                }else{ NoInternetAlertDialog();
                }



            }
        });


    }

    private boolean notempty() {

        if(TextUtils.isEmpty(UserDetails.semail) )
            return false;
        if(!Patterns.EMAIL_ADDRESS.matcher(UserDetails.semail).matches()){
            Email.setError("Enter a valid mail");
            return false;}

        if(TextUtils.isEmpty(UserDetails.sname) || UserDetails.sname.length()<3) {
          Ename.setError("Enter a valid name");
            return false;
        } if(TextUtils.isEmpty(UserDetails.sphone) || UserDetails.sphone.length()!=10) {
         Ephone.setError("Enter a valid mobile number");
            return false;
        }


        return true;
    }

    private void getdata() {

        UserDetails.sname=Ename.getText().toString();
        UserDetails.sphone=Ephone.getText().toString();
        UserDetails.semail=Email.getText().toString();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(mAuth!=null){
            mAuth.signOut();

            mGoogleInCilients.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // ...
                        }
                    });



            finish();
            startActivity(new Intent(this, GoogleLoginActivity.class));
        }

    }


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

    private void volley() {
        if(mAuth.getCurrentUser()!= null){

            String number = getIntent().getExtras().getString("mail");
            Email.setText(number);

        }

        else {
            String number = getIntent().getExtras().getString("number");

            Ephone.setText(number);
        }



    }
}
